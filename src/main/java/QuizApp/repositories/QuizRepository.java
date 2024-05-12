package QuizApp.repositories;

import QuizApp.model.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    Quiz findQuizByQuizId(int quizId);

    @Query(value = "SELECT q.quizId AS quizId, q.score AS quizScore FROM Quiz q WHERE q.user.userId = :userId")
    List<Object[]> findUserAndQuizzes(@Param("userId") int userId);

}
