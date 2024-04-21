package QuizApp.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@Getter
@Setter
public class BadRequestException extends RuntimeException {
    private BindingResult bindingResult;

    public BadRequestException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}

