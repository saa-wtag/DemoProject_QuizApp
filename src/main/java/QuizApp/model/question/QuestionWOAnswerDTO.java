package QuizApp.model.question;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@JsonPropertyOrder({"quesId", "quesTitle", "options"})
public class QuestionWOAnswerDTO {
    private int quesId;
    private String quesTitle;
    private Map<String, String> options;
}
