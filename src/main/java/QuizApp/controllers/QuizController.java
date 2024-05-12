package QuizApp.controllers;

import javax.validation.Valid;

import QuizApp.model.quiz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import QuizApp.services.quiz.QuizService;


@RestController
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;


    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/")
    public ResponseEntity<QuizViewDTO> createQuiz() {
        QuizViewDTO quizView = quizService.createQuiz();
        return new ResponseEntity<>(quizView, HttpStatus.CREATED);
    }

    @PatchMapping("/{quizId}")
    public ResponseEntity<ResultViewDTO> submitAnswers(@PathVariable int quizId, @Valid @RequestBody AnswerInput answers) {
        ResultViewDTO resultView = quizService.submitAnswers(quizId, answers.getAnswerIds());
        return ResponseEntity.ok(resultView);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizViewDTO> getQuiz(@PathVariable int quizId) {
        QuizViewDTO quizView = quizService.getQuiz(quizId);
        return ResponseEntity.ok(quizView);
    }


    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable int quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }
}
