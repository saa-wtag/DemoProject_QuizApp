package QuizApp.services.user;


import QuizApp.exceptions.*;
import QuizApp.model.user.User;
import QuizApp.model.user.UserUpdate;
import QuizApp.model.user.UserViewDTO;
import QuizApp.repositories.UserRepository;
import QuizApp.services.jwt.JwtService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;
import static QuizApp.quizObjectMapper.QuizObjectMapper.convertToUserViewDTO;


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
    public UserViewDTO registerUser(User user) {

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

        return convertToUserViewDTO(user);
    }

    @Override
    @PreAuthorize("#userId == authentication.principal.userId")
    public UserViewDTO updateUserDetails(String token,int userId, UserUpdate userToUpdate) {
        User existingUser = userRepository.findByUserId(userId);

        if (userToUpdate.getUserName() != null) {
            if (!userToUpdate.getUserName().equals(existingUser.getUsername()) &&
                    userRepository.existsByUserName(userToUpdate.getUserName())) {
                throw new IllegalStateException("Username is already in use");
            }
            existingUser.setUserName(userToUpdate.getUserName());
        }

        if (userToUpdate.getUserEmail() != null) {
            if (!userToUpdate.getUserEmail().equals(existingUser.getUserEmail()) &&
                    userRepository.existsByUserEmail(userToUpdate.getUserEmail())) {
                throw new IllegalStateException("Email is already in use");
            }
            existingUser.setUserEmail(userToUpdate.getUserEmail());
        }

        if (userToUpdate.getUserPassword() != null && !userToUpdate.getUserPassword().isEmpty()) {
            existingUser.setUserPassword(passwordEncoder.encode(userToUpdate.getUserPassword()));
            jwtService.blacklistToken(token);
            SecurityContextHolder.clearContext();
        }

        userRepository.save(existingUser);

        return convertToUserViewDTO(existingUser);
    }

    @Override
    public UserViewDTO getUser(int userId) {

        User user = userRepository.findByUserId(userId);
        if(Objects.isNull(user))
            throw new UserNotFoundException("There is no such user!");
        return convertToUserViewDTO(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public void deleteUser(int userId) {
        User user = userRepository.findByUserId(userId);
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
