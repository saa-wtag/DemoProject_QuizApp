package QuizApp.model.quiz;

import QuizApp.model.question.QuestionDetails;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JsonPropertyOrder({"quizId","ifAttempted","userName","score","questions"})
public class ResultViewDTO {
    private int quizId;
    private boolean ifAttempted;
    private long score;
    private List<QuestionDetails> questions;
    private String userName;
}
