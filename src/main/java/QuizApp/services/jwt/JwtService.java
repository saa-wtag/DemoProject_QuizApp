package QuizApp.services.jwt;

import QuizApp.util.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.validity.refresh.token}")
    public long JWT_REFRESH_TOKEN_VALIDITY;
    @Value("${jwt.validity.access.token}")
    public long JWT_ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.secret.refresh.token}")
    private String refreshTokenSecret;
    @Value("${jwt.secret.access.token}")
    private String accessTokenSecret;

    private final Set<String> invalidatedTokens = new HashSet<>();

    public String getUsernameFromToken(String token, TokenType tokenType) {
        return getClaimFromToken(token, Claims::getSubject, tokenType);
    }

    public Date getExpirationDateFromToken(String token, TokenType tokenType) {
        return getClaimFromToken(token, Claims::getExpiration, tokenType);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver, TokenType tokenType) {
        final Claims claims = getAllClaimsFromToken(token, tokenType);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token, TokenType tokenType) {
        if(tokenType.equals(TokenType.ACCESS)){
            return Jwts
                    .parserBuilder()
                    .setSigningKey(accessTokenSecret.toString())
                    .build()
                    .parseClaimsJws(token).getBody();
        }
        else {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(refreshTokenSecret.toString())
                    .build()
                    .parseClaimsJws(token).getBody();
        }
    }

    private Boolean isTokenExpired(String token, TokenType tokenType) {
        final Date expiration = getExpirationDateFromToken(token, tokenType);
        return expiration.before(new Date());
    }

    public String generateAccessToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), JWT_ACCESS_TOKEN_VALIDITY, accessTokenSecret);
    }

    public String generateRefreshToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(), JWT_REFRESH_TOKEN_VALIDITY, refreshTokenSecret);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, long validity, String tokenSecret) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity * 1000))
                .signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails, TokenType tokenType) {
        final String username = getUsernameFromToken(token, tokenType);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, tokenType));
    }

    public void blacklistToken(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInBlacklist(String token) {
        return !invalidatedTokens.contains(token);
    }

}