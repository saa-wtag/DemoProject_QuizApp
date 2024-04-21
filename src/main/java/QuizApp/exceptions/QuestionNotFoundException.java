package QuizApp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionNotFoundException extends RuntimeException{
    public QuestionNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
