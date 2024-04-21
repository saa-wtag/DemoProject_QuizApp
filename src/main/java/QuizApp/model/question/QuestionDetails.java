package QuizApp.model.question;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonPropertyOrder({"quesTitle","options","givenAnswer","answer"})
public class QuestionDetails {
    private String quesTitle;
    private Map<String, String> options;
    private String givenAnswer;
    private String answer;
}
