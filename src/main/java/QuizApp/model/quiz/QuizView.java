package QuizApp.model.quiz;


import QuizApp.model.question.QuestionWithoutAnswerView;
import QuizApp.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"quizId","ifAttempted","userName","score","questions"})
public interface QuizView {
    int getQuizId();
    boolean getIfAttempted();
    long getScore();
    List<QuestionWithoutAnswerView> getQuestions();

    default String getUserName() {
        User user = getUser();
        return user.getUsername();
    }
    @JsonIgnore
    User getUser();
}
