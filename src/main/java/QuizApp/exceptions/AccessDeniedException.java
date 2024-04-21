package QuizApp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessDeniedException extends org.springframework.security.access.AccessDeniedException {
    public AccessDeniedException(String msg) {
        super(msg);
    }
}
