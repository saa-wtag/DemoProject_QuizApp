package QuizApp.model.user;

import QuizApp.model.quiz.Quiz;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonFilter;

import java.util.List;

@JsonFilter("userViewFilter")
@JsonPropertyOrder({"userId", "userName", "userEmail", "role", "quizzes"})
public interface UserView {
    int getUserId();
    String getUserName();
    String getUserEmail();
    User.UserRole getRole();
    List<Quiz> getQuizzes();
}
