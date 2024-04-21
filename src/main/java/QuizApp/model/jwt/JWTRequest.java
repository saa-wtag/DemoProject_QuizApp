package QuizApp.model.jwt;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class JWTRequest {
    @NotBlank
    private String userName;
    @NotBlank
    private String password;
}
