package QuizApp.services.user;


import QuizApp.exceptions.*;
import QuizApp.model.jwt.AccessToken;
import QuizApp.model.user.User;
import QuizApp.model.user.UserUpdate;
import QuizApp.repositories.AccessTokenRepository;
import QuizApp.repositories.UserRepository;
import QuizApp.services.jwt.JwtService;

import QuizApp.util.TokenType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccessTokenRepository accessTokenRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AccessTokenRepository accessTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    @Transactional
    public User registerUser(User user) {

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

        return user;
    }

    @Override
    @Transactional
    @PreAuthorize("#userId == authentication.principal.userId")
    public User updateUserDetails(String token, int userId, UserUpdate userToUpdate) {
        User existingUser = getUser(userId);

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
        return existingUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("There is no such user!"));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    public void deleteUser(int userId, String token) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = getUser(userId);

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")))
                jwtService.blacklistToken(token);
        else {
            AccessToken accessToken = accessTokenRepository.findByUserName(user.getUsername());
            if (accessToken != null) {
                jwtService.blacklistToken(accessToken.getToken());
            }
        }
        userRepository.delete(user);
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
