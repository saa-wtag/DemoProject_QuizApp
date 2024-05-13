package QuizApp.controllers;

import QuizApp.services.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LogoutController {
    private final JwtService jwtService;


    public LogoutController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String token) {
        int headerBearerStringSize = 7;
        String jwt = token.substring(headerBearerStringSize);
        jwtService.blacklistToken(jwt);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout Successfully done!!");

        return ResponseEntity.ok(response);
    }
}