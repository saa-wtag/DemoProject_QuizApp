package QuizApp.controllers;

import QuizApp.model.jwt.TokenResponse;
import QuizApp.services.jwt.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;


    public RefreshTokenController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken() {
        return ResponseEntity.ok(refreshTokenService.createTokenFromRefreshToken());
    }
}