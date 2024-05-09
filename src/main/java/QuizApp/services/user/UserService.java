package QuizApp.services.user;

import QuizApp.model.user.User;
import QuizApp.model.user.UserUpdate;
import QuizApp.model.user.UserView;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserView registerUser(User user);
    UserView updateUserDetails(String token, int userId, UserUpdate user);
    UserView getUser(int userId);
    void deleteUser(int userId);
    User loadUserByUsername(String userName);
}
