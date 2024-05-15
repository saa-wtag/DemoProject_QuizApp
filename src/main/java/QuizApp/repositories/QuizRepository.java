package QuizApp.repositories;

import QuizApp.model.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

    @Query(value = "SELECT q.quizId AS quizId, q.score AS quizScore FROM Quiz q WHERE q.user.userId = :userId")
    List<Object[]> findUserAndQuizzes(@Param("userId") int userId);

    @Modifying
    @Query(value = "DELETE FROM tbl_quiz WHERE quiz_id = :quizId; DELETE FROM quiz_questions WHERE quiz_id = :quizId", nativeQuery = true)
    void deleteQuizAndRelatedData(int quizId);
}
