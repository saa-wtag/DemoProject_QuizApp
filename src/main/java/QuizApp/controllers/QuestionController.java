package QuizApp.controllers;

import javax.validation.Valid;

import QuizApp.model.question.*;
import QuizApp.repositories.QuestionRepository;
import QuizApp.services.quiz.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import QuizApp.quizObjectMapper.QuizObjectMapper;
import QuizApp.services.question.QuestionService;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<QuestionViewDTO> createQuestion(@Valid @RequestBody QuestionInput questionInput) {
        QuestionViewDTO question = questionService.createQuestion(questionInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }



    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionViewDTO> updateQuestionDetails(@PathVariable int questionId, @Valid @RequestBody QuestionUpdate questionUpdate) {

        QuestionViewDTO updatedQuestionView = questionService.updateQuestionDetails(questionId, questionUpdate);
        return ResponseEntity.ok(updatedQuestionView);
    }


    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionViewDTO> getQuestion(@PathVariable int questionId) {
        QuestionViewDTO questionView = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(questionView);
    }


//    @DeleteMapping("/{questionId}")
//    public ResponseEntity<Void> deleteQuestion(@PathVariable int questionId) {
//        questionService.deleteQuestion(questionId);
//        return ResponseEntity.noContent().build();
//
//    }
}
