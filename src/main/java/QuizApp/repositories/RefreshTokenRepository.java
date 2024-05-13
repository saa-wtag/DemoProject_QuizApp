package QuizApp.repositories;

import QuizApp.model.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT rt.token FROM RefreshToken rt WHERE rt.id = :accessTokenId")
    String findTokenByAccessTokenId(@Param("accessTokenId") Long accessTokenId);
}
