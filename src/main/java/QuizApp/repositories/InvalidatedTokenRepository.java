package QuizApp.repositories;

import QuizApp.model.jwt.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Long> {

    boolean existsByToken(String token);
}
