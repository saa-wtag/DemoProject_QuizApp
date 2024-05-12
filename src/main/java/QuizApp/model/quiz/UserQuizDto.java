package QuizApp.model.quiz;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"quizId","score"})
public class UserQuizDto {
    private int quizId;
    private long quizScore;

    public UserQuizDto(int quizId, long quizScore) {
    }
}
