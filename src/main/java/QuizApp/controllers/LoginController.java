package QuizApp.controllers;

import QuizApp.model.jwt.JWTRequest;
import QuizApp.model.jwt.RefreshTokenResponse;
import QuizApp.services.jwt.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<RefreshTokenResponse> login(@Valid @RequestBody JWTRequest request) {
        return ResponseEntity.ok(loginService.authenticate(request));
    }
}