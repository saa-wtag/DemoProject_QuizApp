package QuizApp.model.quiz;

import QuizApp.model.question.QuestionDetails;
import QuizApp.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"quizId","ifAttempted","userName","score","questions"})
public interface ResultView {
    int getQuizId();
    boolean getIfAttempted();
    long getScore();
    List<QuestionDetails> getQuestions();

    default String getUserName() {
        User user = getUser();
        return user.getUsername();
    }
    @JsonIgnore
    User getUser();
}
