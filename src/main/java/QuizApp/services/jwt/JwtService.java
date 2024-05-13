package QuizApp.services.jwt;

import QuizApp.model.jwt.AccessToken;
import QuizApp.model.jwt.InvalidatedToken;
import QuizApp.model.jwt.RefreshToken;
import QuizApp.repositories.AccessTokenRepository;
import QuizApp.repositories.InvalidatedTokenRepository;
import QuizApp.repositories.RefreshTokenRepository;
import QuizApp.util.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;


@Service
public class JwtService {

    @Autowired
    private AccessTokenRepository accessTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${jwt.validity.refresh.token}")
    public long JWT_REFRESH_TOKEN_VALIDITY;
    @Value("${jwt.validity.access.token}")
    public long JWT_ACCESS_TOKEN_VALIDITY;
    @Value("${jwt.secret.refresh.token}")
    private String refreshTokenSecret;
    @Value("${jwt.secret.access.token}")
    private String accessTokenSecret;

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
        String secretToken = tokenType.equals(TokenType.ACCESS) ? accessTokenSecret : refreshTokenSecret;

        return Jwts.parserBuilder()
                .setSigningKey(secretToken)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token, TokenType tokenType) {
        final Date expiration = getExpirationDateFromToken(token, tokenType);
        return expiration.before(new Date());
    }

    public String generateAccessToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        String token = doGenerateToken(claims, userDetails.getUsername(), JWT_ACCESS_TOKEN_VALIDITY, accessTokenSecret);
        saveAccessTokenToDatabase(token, userDetails.getUsername(), new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY * 1000));
        return token;
    }

    public String generateRefreshToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        String token = doGenerateToken(claims, userDetails.getUsername(), JWT_REFRESH_TOKEN_VALIDITY, refreshTokenSecret);
        saveRefreshTokenToDatabase(token, userDetails.getUsername(), new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY * 1000));
        return token;
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

    @Transactional
    public void blacklistToken(String token) {
        Long accessTokenId = accessTokenRepository.findIdByToken(token);
        String refreshToken = refreshTokenRepository.findTokenByAccessTokenId(accessTokenId);

        invalidatedTokenRepository.save(new InvalidatedToken(token));
        invalidatedTokenRepository.save(new InvalidatedToken(refreshToken));
    }

    @Transactional(readOnly = true)
    public boolean isTokenInBlacklist(String token) {
        return invalidatedTokenRepository.existsByToken(token);
    }

    @Transactional
    public void saveAccessTokenToDatabase(String token, String username, Date expiration) {
        accessTokenRepository.save(new AccessToken(token, username, expiration));
    }

    @Transactional
    public void saveRefreshTokenToDatabase(String token, String username, Date expiration) {
        refreshTokenRepository.save(new RefreshToken(token, username, expiration));
    }

}