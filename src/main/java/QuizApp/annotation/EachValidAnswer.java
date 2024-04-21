package QuizApp.annotation;

import QuizApp.validator.ValidAnswerValidator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ValidAnswerValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface EachValidAnswer {
    String message() default "Invalid answer";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
