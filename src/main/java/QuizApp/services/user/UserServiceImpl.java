package QuizApp.services.user;


import QuizApp.exceptions.AccessDeniedException;
import QuizApp.exceptions.UserAlreadyExistsException;
import QuizApp.model.user.User;
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
    public User registerUser(User user) {
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

        return userRepository.save(user);
    }

    @Override
    public User registerAdmin(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Access denied: Only administrators can perform this action.");
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
        user.setRole(User.UserRole.ADMIN);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserDetails(int userId, User user, String token) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();
        User authenticatedUser = userRepository.findByUserId(currentUser.getUserId());

        if (authenticatedUser.getUserId() != userId) {
            throw new AccessDeniedException("Access denied: Users can only update their own details.");
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        boolean logoutNeeded = false;

        if (user.getUsername() != null && !user.getUsername().isEmpty() && !user.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUserName(user.getUsername())) {
                throw new IllegalStateException("Username is already in use");
            }
            existingUser.setUserName(user.getUsername());
            logoutNeeded = true;
        }

        if (user.getUserEmail() != null && !user.getUserEmail().isEmpty() && !user.getUserEmail().equals(existingUser.getUserEmail())) {
            if (userRepository.existsByUserEmail(user.getUserEmail())) {
                throw new IllegalStateException("Email is already in use");
            }
            existingUser.setUserEmail(user.getUserEmail());
        }

        if (user.getUserPassword() != null && !user.getUserPassword().isEmpty()) {
            existingUser.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
            logoutNeeded=true;
        }

        if (logoutNeeded) {
            jwtService.blacklistToken(token);
            SecurityContextHolder.clearContext();
        }

        return userRepository.save(existingUser);
    }

    @Override
    public User getUser(int userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        if(Objects.isNull(user)){
            throw new NoSuchElementException("There is no such user!");
        }

        user.getQuizzes().size();  // Initialize the collection to avoid LazyInitializationException
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
