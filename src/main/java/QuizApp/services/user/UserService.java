package QuizApp.services.user;

import QuizApp.model.user.User;
import QuizApp.model.user.UserUpdate;
import QuizApp.model.user.UserViewDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserViewDTO registerUser(User user);
    UserViewDTO updateUserDetails(String token, int userId, UserUpdate user);
    UserViewDTO getUser(int userId);
    void deleteUser(int userId);
    User loadUserByUsername(String userName);
}
