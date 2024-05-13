package QuizApp.validator;

import QuizApp.annotation.ValidOptions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Set;

public class OptionsKeysValidator implements ConstraintValidator<ValidOptions, Map<String, String>> {


    @Override
    public boolean isValid(Map<String, String> options, ConstraintValidatorContext context) {
        final Set<String> VALID_KEYS = Set.of("A", "B", "C", "D");
        final int MAX_LENGTH = 30;

        if (options.size() != VALID_KEYS.size()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Options must exactly contain keys 'A', 'B', 'C', 'D'.")
                    .addConstraintViolation();
            return false;
        }

        for (String key : VALID_KEYS) {
            String value = options.get(key);
            if (value == null || value.trim().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Option '" + key + "' must not be empty.")
                        .addConstraintViolation();
                return false;
            }
            if (value.length() > MAX_LENGTH) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Option '" + key + "' must not exceed " + MAX_LENGTH + " characters.")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}

