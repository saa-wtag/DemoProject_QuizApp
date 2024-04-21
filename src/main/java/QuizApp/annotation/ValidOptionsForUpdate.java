package QuizApp.annotation;



import QuizApp.validator.OptionsKeysValidatorUpdate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = OptionsKeysValidatorUpdate.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidOptionsForUpdate {

    String message() default "Each option must be uniquely labeled with 'A', 'B', 'C', or 'D' and must not be empty, having 1 to 30 characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean applyValidation() default true; // New attribute to control validation
}

