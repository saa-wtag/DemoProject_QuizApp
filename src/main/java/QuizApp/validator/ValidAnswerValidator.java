package QuizApp.validator;

import QuizApp.annotation.EachValidAnswer;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ValidAnswerValidator implements ConstraintValidator<EachValidAnswer, List<String>> {
    private static final List<String> VALID_ANSWERS = Arrays.asList("A", "B", "C", "D");

    @Override
    public void initialize(EachValidAnswer constraintAnnotation) {
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        if (values == null) {
            return true; // Null case is handled by @NotNull
        }
        return values.stream().allMatch(VALID_ANSWERS::contains);
    }
}

