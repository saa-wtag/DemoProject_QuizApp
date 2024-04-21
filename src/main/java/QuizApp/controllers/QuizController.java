package QuizApp.controllers;

import javax.validation.Valid;

import QuizApp.exceptions.InvalidAnswerException;
import QuizApp.exceptions.QuizNotFoundException;
import QuizApp.exceptions.UserNotFoundException;
import QuizApp.model.quiz.*;
import QuizApp.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import QuizApp.services.quiz.QuizService;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;


    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<QuizView> createQuiz(@PathVariable int userId) {
        QuizView quizView = quizService.createQuiz(userId);
        return new ResponseEntity<>(quizView, HttpStatus.CREATED);
    }

    @PatchMapping("/submit/{quizId}")
    public ResponseEntity<ResultView> submitAnswers(@PathVariable int quizId, @Valid @RequestBody AnswerInput answers) {
        ResultView resultView = quizService.submitAnswers(quizId, answers.getAnswerIds());
        return ResponseEntity.ok(resultView);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizView> getQuiz(@PathVariable int quizId) {
        QuizView quizView = quizService.getQuiz(quizId);
        return ResponseEntity.ok(quizView);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuizzesAndScoresView>> listQuizzesForUser(@PathVariable int userId) {
        List<QuizzesAndScoresView> quizzes = quizService.listQuizzesForUser(userId);
        return ResponseEntity.ok(quizzes);
    }


    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable int quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }
}
