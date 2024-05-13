package QuizApp.services.jwt;

import QuizApp.exceptions.UnauthorizedException;
import QuizApp.model.jwt.JWTRequest;
import QuizApp.model.jwt.TokenResponse;
import QuizApp.services.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;


    public LoginService(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public TokenResponse authenticate(JWTRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword());

        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Bad credentials");
        }

        UserDetails userDetails = userService.loadUserByUsername(request.getUserName());

        String accessToken = this.jwtService.generateAccessToken(userDetails);
        String refreshToken = this.jwtService.generateRefreshToken(userDetails);

        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}