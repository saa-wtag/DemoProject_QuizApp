package QuizApp.model.quiz;

import QuizApp.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"quizId","ifAttempted","userName","score"})
public interface QuizzesAndScoresView {
    int getQuizId();
    boolean getIfAttempted();
    long getQuizScore();


    default String getUserName() {
        User user = getUser();
        return user.getUsername();
    }
    @JsonIgnore
    User getUser();
}
