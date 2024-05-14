package QuizApp.config.jwt;

import QuizApp.services.jwt.JwtService;
import QuizApp.util.TokenType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtHelper;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtHelper, UserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        TokenType secret = TokenType.ACCESS;

        if(request.getRequestURI().equals("/refresh-token")) {
            secret = TokenType.REFRESH;
        }

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.substring(7);
            try {
                username = jwtHelper.getUsernameFromToken(token, secret);
                logger.info("{} is authenticated", username);
            } catch (ExpiredJwtException e) {
                logger.info("Given JWT token is expired: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                logger.info("Invalid Token: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("An error occurred while parsing JWT token", e);
            }
        } else {
            logger.info("Authentication header not present");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Check if the token is invalidated
            if (jwtHelper.isTokenInBlacklist(token)) {
                logger.info("Token is invalidated, authentication failed");
            } else if (jwtHelper.validateToken(token, userDetails, secret)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentication successful");
            } else {
                logger.info("Token validation failed");
            }
        }

        filterChain.doFilter(request, response);
    }
}