package QuizApp.model.quiz;

import QuizApp.annotation.EachValidAnswer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class AnswerInput {

    @Size(min = 5, max = 5, message = "Exactly 5 answers must be provided")
    @EachValidAnswer(message = "Each answer must be one of A, B, C, or D")
    private List<String> answerIds;

}
