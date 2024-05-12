package QuizApp.model.quiz;

import QuizApp.model.question.QuestionWOAnswerDTO;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@JsonPropertyOrder({"quizId","ifAttempted","userName","score","questions"})
public class QuizViewDTO {
    private int quizId;
    private boolean ifAttempted;
    private long score;
    private List<QuestionWOAnswerDTO> questions;
    private String userName;


}
