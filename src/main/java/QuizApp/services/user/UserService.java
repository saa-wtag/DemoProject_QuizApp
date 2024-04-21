package QuizApp.services.user;

import QuizApp.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User registerUser(User user);
    User registerAdmin(User user);
    User updateUserDetails(int userId, User user, String token);
    User getUser(int userId);
    void deleteUser(int userId);
    User loadUserByUsername(String userName);
}
