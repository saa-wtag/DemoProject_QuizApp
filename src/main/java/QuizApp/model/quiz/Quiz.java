package QuizApp.model.quiz;

import QuizApp.model.question.Question;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import QuizApp.model.user.User;

import java.util.List;


@Entity
@Getter
@Setter
@Table(name="tbl_quiz")
@JsonPropertyOrder({ "quizId", "questions","score" })
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int quizId;
    private int quizOwner;
    private long score;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "quiz_questions",
            joinColumns = @JoinColumn(name = "quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions;
}