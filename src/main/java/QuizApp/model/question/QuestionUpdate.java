package QuizApp.model.question;

import QuizApp.annotation.ValidOptionsUpdate;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;


@Getter
@Setter
public class QuestionUpdate {

    @Size(min = 8,max = 80, message = "Question length must be in between 8 to 80 characters.")
    private String quesTitle;

    @ValidOptionsUpdate
    private Map<String, String> options;

    @Pattern(regexp = "[ABCD]", message = "Answer must be one of 'A', 'B', 'C', or 'D'")
    private String answer;
}

