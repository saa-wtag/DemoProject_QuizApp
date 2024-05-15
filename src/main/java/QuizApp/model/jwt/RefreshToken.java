package QuizApp.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "expiration", nullable = false)
    private Date expiration;

    public RefreshToken(String token, String username, Date expiration) {
        this.token = token;
        this.username = username;
        this.expiration = expiration;
    }
}