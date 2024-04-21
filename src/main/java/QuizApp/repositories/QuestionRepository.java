package QuizApp.repositories;

import QuizApp.model.question.Question;
import QuizApp.model.question.QuestionView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = "SELECT * FROM tbl_question ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Question> findRandomQuestions();

    QuestionView findQuestionViewByQuesId(int id);

    Question findQuestionByQuesId(int id);
}
