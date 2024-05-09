package QuizApp.model.quiz;

import QuizApp.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"quizId","score"})
public interface QuizzesAndScoresView {
    int getQuizId();
    long getQuizScore();
}
