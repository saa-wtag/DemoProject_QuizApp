package QuizApp.controllers;

import javax.validation.Valid;

import QuizApp.model.quiz.*;
import QuizApp.quizObjectMapper.QuizObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import QuizApp.services.quiz.QuizService;


@RestController
@RequestMapping("/quizzes")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/")
    public ResponseEntity<QuizDTO> createQuiz() {
        QuizDTO quizView = QuizObjectMapper.convertToQuizViewDTO(quizService.createQuiz());
        return new ResponseEntity<>(quizView, HttpStatus.CREATED);
    }

    @PatchMapping("/{quizId}")
    public ResponseEntity<ResultDTO> submitAnswers(@PathVariable int quizId, @Valid @RequestBody AnswerInput answers) {
        ResultDTO resultView = (quizService.submitAnswers(quizId, answers.getAnswerIds()));
        return ResponseEntity.ok(resultView);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable int quizId) {
        QuizDTO quizView = QuizObjectMapper.convertToQuizViewDTO(quizService.getQuiz(quizId));
        return ResponseEntity.ok(quizView);
    }


    @DeleteMapping("/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable int quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.noContent().build();
    }
}
