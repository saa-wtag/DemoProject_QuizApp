package QuizApp.services.jwt;

import QuizApp.model.jwt.JWTRequest;
import QuizApp.model.jwt.RefreshTokenResponse;
import QuizApp.services.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, UserService userService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public RefreshTokenResponse authenticate(JWTRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword());

        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Bad credentials");
        }

        UserDetails userDetails = userService.loadUserByUsername(request.getUserName());

        String accessToken = this.jwtService.generateAccessToken(userDetails);
        String refreshToken = this.jwtService.generateRefreshToken(userDetails);

        return RefreshTokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}