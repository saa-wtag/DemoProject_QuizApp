package QuizApp.model.quiz;


import QuizApp.model.question.QuestionWithoutAnswerView;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"quizId","ifAttempted","score","questions"})
public interface QuizView {
    int getQuizId();
    boolean getIfAttempted();
    long getScore();
    List<QuestionWithoutAnswerView> getQuestions();
}
