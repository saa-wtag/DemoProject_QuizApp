package QuizApp.repositories;

import QuizApp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);
    User findByUserEmail(String email);
    User findByUserId(int userId);
    boolean existsByUserName(String userName);

    boolean existsByUserEmail(String userEmail);

    @Query("SELECT u FROM User u WHERE u.userName = :username OR u.userEmail = :email")
    User findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

}
