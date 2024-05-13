package QuizApp.controllers;

import QuizApp.model.jwt.JWTRequest;
import QuizApp.model.jwt.TokenResponse;
import QuizApp.services.jwt.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {
    private final LoginService loginService;


    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody JWTRequest request) {

            TokenResponse response = loginService.authenticate(request);
            return ResponseEntity.ok(response);
    }
}