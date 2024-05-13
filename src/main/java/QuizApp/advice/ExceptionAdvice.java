package QuizApp.advice;

import QuizApp.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }



    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", ex.getMessage()));
    }


    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<?> handleQuestionNotFoundException(QuestionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(QuizNotFoundException.class)
    public ResponseEntity<?> handleQuizNotFoundException(QuizNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }


    @ExceptionHandler(InvalidAnswerException.class)
    public ResponseEntity<?> handleInvalidAnswerException(InvalidAnswerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
