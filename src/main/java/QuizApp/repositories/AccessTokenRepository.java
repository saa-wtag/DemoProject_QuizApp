package QuizApp.repositories;

import QuizApp.model.jwt.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    @Query("SELECT at.id FROM AccessToken at WHERE at.token = :token")
    Long findIdByToken(@Param("token") String token);
}
