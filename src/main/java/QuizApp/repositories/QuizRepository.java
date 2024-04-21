package QuizApp.repositories;

import QuizApp.model.quiz.Quiz;
import QuizApp.model.quiz.QuizView;
import QuizApp.model.quiz.QuizzesAndScoresView;
import QuizApp.model.quiz.ResultView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
//    List<Quiz> findByUserUserId(int userId);

    QuizView findQuizViewByQuizId(int quizId);

    Quiz findQuizByQuizId(int quizId);

    @Query("SELECT q.quizId AS quizId, q.score AS quizScore FROM Quiz q WHERE q.quizOwner = :userId")
    List<QuizzesAndScoresView> findAllByUserId(@Param("userId") int userId);

    ResultView findResultViewByQuizId(int quizId);
    int deleteQuizByQuizId(int quizId);
}
