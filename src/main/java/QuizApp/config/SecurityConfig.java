package QuizApp.config;

import QuizApp.config.jwt.JwtAuthenticationEntryPoint;
import QuizApp.config.jwt.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

    private final JwtAuthenticationEntryPoint point;
    private final JwtAuthenticationFilter filter ;

    @Autowired
    @Lazy
    public SecurityConfig(JwtAuthenticationEntryPoint point, JwtAuthenticationFilter filter) {
        this.point = point;
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/login").permitAll()
                .antMatchers(HttpMethod.POST,"/logout").hasAnyRole("ADMIN", "USER")

                .antMatchers(HttpMethod.POST, "/users/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/users/*").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.DELETE, "/users/*").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.GET, "/users/*").hasAnyRole("ADMIN", "USER")


                .antMatchers(HttpMethod.POST, "/questions").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/questions/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/questions/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/questions/**").hasRole("ADMIN")


                .antMatchers(HttpMethod.POST, "/quizzes/**").hasRole("USER")
                .antMatchers(HttpMethod.PATCH, "/quizzes/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE,"/quizzes/*").hasAnyRole("ADMIN", "USER")
                .antMatchers(HttpMethod.GET, "/quizzes/*").hasAnyRole("ADMIN", "USER")

                .antMatchers(HttpMethod.GET, "/refresh-token").hasAnyRole("ADMIN", "USER")


                .anyRequest().authenticated()
                .and()
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
                    response.setContentType("application/json");
                    String jsonPayload = "{\"error\": \"Access Denied: You do not have permission to access this resource.\"}";
                    response.getOutputStream().println(jsonPayload);
                })
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable();
        http.formLogin().disable();
        http.httpBasic().disable();

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        return mapper;
    }
}
