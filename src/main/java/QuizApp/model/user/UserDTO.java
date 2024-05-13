package QuizApp.model.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"userId", "userName", "userEmail", "role", "quizzes"})
public class UserDTO {
    private int userId;
    private String userName;
    private String userEmail;
    private User.UserRole role;
}
