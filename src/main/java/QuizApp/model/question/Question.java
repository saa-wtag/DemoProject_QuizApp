package QuizApp.model.question;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Entity
@Table(name="tbl_question")
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int quesId;

    @Column(nullable = false)
    private String quesTitle;

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @MapKeyColumn(name = "option_key")
    @Column(name = "option_value")
    private Map<String, String> options;

    private String answer;
}
