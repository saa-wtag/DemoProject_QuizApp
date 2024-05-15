package QuizApp.services.user;

import QuizApp.model.user.User;
import QuizApp.model.user.UserUpdate;
import QuizApp.model.user.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User registerUser(User user);
    User updateUserDetails(String token, int userId, UserUpdate user);
    User getUser(int userId);
    void deleteUser(int userId, String token);
    User loadUserByUsername(String userName);
}
