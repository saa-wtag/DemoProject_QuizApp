package QuizApp.model.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Getter
@Setter
public class UserUpdate {

    @Size(min = 3, message = "Given Name is too short")
    private String userName;

    @Email
    private String userEmail;

    @Size(min = 3, message = "Given password is too short")
    private String userPassword;

}
