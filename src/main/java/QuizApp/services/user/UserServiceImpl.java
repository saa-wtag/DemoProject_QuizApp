package QuizApp.services.user;


import QuizApp.exceptions.AccessDeniedException;
import QuizApp.exceptions.UserAlreadyExistsException;
import QuizApp.model.user.User;
import QuizApp.model.user.UserUpdate;
import QuizApp.model.user.UserView;
import QuizApp.quizObjectMapper.QuizObjectMapper;
import QuizApp.repositories.UserRepository;
import QuizApp.exceptions.ObjectNotFoundException;
import QuizApp.services.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Override
    public UserView registerUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Logged-in users are not allowed to create new accounts.");
        }

        User existingUserByUsername = userRepository.findByUserName(user.getUsername());
        User existingUserByEmail = userRepository.findByUserEmail(user.getUserEmail());

        if (existingUserByUsername != null) {
            throw new UserAlreadyExistsException("Username is already registered");
        }

        if (existingUserByEmail != null) {
            throw new UserAlreadyExistsException("Email is already registered");
        }

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        user.setRole(User.UserRole.USER);
        userRepository.save(user);
        return userRepository.findUserViewByUserId(user.getUserId());
    }

    @Override
    @Transactional
    public UserView updateUserDetails(String token, UserUpdate userToUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new AccessDeniedException("User is not authenticated");
        }

        User currentUser = (User) authentication.getPrincipal();
        int currentUserId = currentUser.getUserId();
        User user = QuizObjectMapper.convertUserUpdateToModel(userToUpdate);

        User existingUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        boolean logoutNeeded = false;


        if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUserName(user.getUsername())) {
                throw new IllegalStateException("Username is already in use");
            }
            existingUser.setUserName(user.getUsername());
            logoutNeeded = true;
        }

        if (userToUpdate.getUserEmail() != null && !userToUpdate.getUserEmail().equals(existingUser.getUserEmail())) {
            if (userRepository.existsByUserEmail(userToUpdate.getUserEmail())) {
                throw new IllegalStateException("Email is already in use");
            }
            existingUser.setUserEmail(userToUpdate.getUserEmail());
        }

        if (userToUpdate.getUserPassword() != null && !userToUpdate.getUserPassword().isEmpty()) {
            existingUser.setUserPassword(passwordEncoder.encode(userToUpdate.getUserPassword()));
            logoutNeeded = true;
        }

        if (logoutNeeded) {
            jwtService.blacklistToken(token);
            SecurityContextHolder.clearContext();
        }
        userRepository.save(existingUser);
        return userRepository.findUserViewByUserId(existingUser.getUserId());
    }

    @Override
    public UserView getUser(int userId) {

        UserView user = userRepository.findUserViewByUserId(userId);
        if(Objects.isNull(user)){
            throw new NoSuchElementException("There is no such user!");
        }
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("Access denied: Only administrators can delete users.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user == null)
            throw new UsernameNotFoundException("User not found with username: " + userName);

        return user;
    }

}
