package QuizApp.model.question;

import QuizApp.annotation.ValidOptions;
import QuizApp.annotation.ValidOptionsForUpdate;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;


@Getter
@Setter
public class QuestionUpdate {

    @Size(min = 1, message = "Question title cannot be empty!")
    private String quesTitle;

    @ValidOptionsForUpdate
    private Map<String, String> options;

    @Pattern(regexp = "[ABCD]", message = "Answer must be one of 'A', 'B', 'C', or 'D'")
    private String answer;
}

