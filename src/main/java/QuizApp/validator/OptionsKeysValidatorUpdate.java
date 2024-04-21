package QuizApp.validator;

import QuizApp.annotation.ValidOptions;
import QuizApp.annotation.ValidOptionsForUpdate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Set;

public class OptionsKeysValidatorUpdate implements ConstraintValidator<ValidOptionsForUpdate, Map<String, String>> {

    private static final Set<String> VALID_KEYS = Set.of("A", "B", "C", "D");
    private static final int MAX_LENGTH = 30;


    @Override
    public void initialize(ValidOptionsForUpdate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Map<String, String> options, ConstraintValidatorContext context) {
        if (options == null || options.isEmpty()) {
            return true;
        }

        for (String key : VALID_KEYS) {
            String value = options.get(key);
            if (value != null && value.trim().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Option '" + key + "' must not be empty.")
                        .addConstraintViolation();
                return false;
            }
            if (value != null && value.length() > MAX_LENGTH) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Option '" + key + "' must not exceed " + MAX_LENGTH + " characters.")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }



}


