package QuizApp.repositories;

import QuizApp.model.user.User;
import QuizApp.model.user.UserView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
    User findByUserEmail(String email);
    User findByUserId(int userId);
    UserView findUserViewByUserId(int userId);
    boolean existsByUserName(String userName);
    boolean existsByUserEmail(String userEmail);

}
