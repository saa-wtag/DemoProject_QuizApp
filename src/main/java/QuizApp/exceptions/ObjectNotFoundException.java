package QuizApp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
