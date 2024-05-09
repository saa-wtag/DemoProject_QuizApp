package QuizApp.services.user;


import QuizApp.exceptions.AccessDeniedException;
import QuizApp.exceptions.UnauthorizedException;
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

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserView registerUser(User user) {

        User existingUser = userRepository.findByUsernameOrEmail(user.getUsername(),user.getUserEmail());

        if (existingUser != null) {
            if (existingUser.getUsername().equals(user.getUsername()))
                throw new UserAlreadyExistsException("Username is already registered");
            else if (existingUser.getUserEmail().equals(user.getUserEmail()))
                throw new UserAlreadyExistsException("Email is already registered");
        }

        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        user.setRole(User.UserRole.USER);
        userRepository.save(user);
        return userRepository.findUserViewByUserId(user.getUserId());
    }

    @Override
    public UserView updateUserDetails(String token,int userId, UserUpdate userToUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();
        int currentUserId = currentUser.getUserId();
        if(currentUserId != userId)
            throw new UnauthorizedException("You do not have permission to update other's profile");

        User user = QuizObjectMapper.convertUserUpdateToModel(userToUpdate);
        User existingUser = userRepository.findByUserId(currentUserId);

        boolean logoutNeeded = false;

        if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
            if (userRepository.existsByUserName(user.getUsername()))
                throw new IllegalStateException("Username is already in use");
            existingUser.setUserName(user.getUsername());
            logoutNeeded = true;
        }

        if (userToUpdate.getUserEmail() != null && !userToUpdate.getUserEmail().equals(existingUser.getUserEmail())) {
            if (userRepository.existsByUserEmail(userToUpdate.getUserEmail()))
                throw new IllegalStateException("Email is already in use");
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
        if(Objects.isNull(user))
            throw new NoSuchElementException("There is no such user!");
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isSelf = authentication.getName().equals(userRepository.findById(userId).get().getUsername());

        if (!isAdmin && !isSelf)
            throw new AccessDeniedException("Access denied: Only administrators or the user themselve can delete this account.");

        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user == null)
            throw new UsernameNotFoundException("User not found with username: " + userName);

        return user;
    }
}
