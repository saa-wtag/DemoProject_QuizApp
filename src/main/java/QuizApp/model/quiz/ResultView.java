package QuizApp.model.quiz;

import QuizApp.model.question.QuestionDetails;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"quizId","ifAttempted","score","questions"})
public interface ResultView {
    int getQuizId();
    boolean getIfAttempted();
    long getScore();
    List<QuestionDetails> getQuestions();
}
