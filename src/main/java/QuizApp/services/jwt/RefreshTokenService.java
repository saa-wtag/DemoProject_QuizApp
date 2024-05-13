package QuizApp.services.jwt;

import QuizApp.model.jwt.TokenResponse;
import QuizApp.model.user.User;
import QuizApp.services.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class RefreshTokenService {
    private final JwtService jwtService;
    private final UserService userService;


    public RefreshTokenService(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public TokenResponse createTokenFromRefreshToken(){
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.loadUserByUsername(principal.getName());
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return TokenResponse
                .builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
