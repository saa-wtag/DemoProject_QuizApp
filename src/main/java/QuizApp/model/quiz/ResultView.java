package QuizApp.model.quiz;

import QuizApp.model.question.QuestionDetails;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"quizId","score","questions"})
public interface ResultView {
    int getQuizId();
    long getScore();
    List<QuestionDetails> getQuestions();
}
