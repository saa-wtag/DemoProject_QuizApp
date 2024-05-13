package QuizApp.validator;

import QuizApp.annotation.EachValidAnswer;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ValidAnswerValidator implements ConstraintValidator<EachValidAnswer, List<String>> {

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        final List<String> VALID_ANSWERS = Arrays.asList("A", "B", "C", "D");
        return values.stream().allMatch(VALID_ANSWERS::contains);
    }
}

