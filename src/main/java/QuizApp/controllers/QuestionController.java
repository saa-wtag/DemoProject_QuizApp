package QuizApp.controllers;

import javax.validation.Valid;

import QuizApp.model.question.*;
import QuizApp.quizObjectMapper.QuizObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import QuizApp.services.question.QuestionService;

import static QuizApp.quizObjectMapper.QuizObjectMapper.convertToQuestionViewDTO;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    private final QuestionService questionService;


    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionInput questionInput) {
        QuestionDTO question = QuizObjectMapper.convertToQuestionViewDTO(questionService.createQuestion(questionInput));
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }



    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestionDetails(@PathVariable int questionId, @Valid @RequestBody QuestionUpdate questionUpdate) {

        QuestionDTO updatedQuestionView = QuizObjectMapper.convertToQuestionViewDTO(questionService.updateQuestionDetails(questionId, questionUpdate));
        return ResponseEntity.ok(updatedQuestionView);
    }


    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable int questionId) {
        QuestionDTO questionView = QuizObjectMapper.convertToQuestionViewDTO(questionService.getQuestionById(questionId));
        return ResponseEntity.ok(questionView);
    }


//    @DeleteMapping("/{questionId}")
//    public ResponseEntity<Void> deleteQuestion(@PathVariable int questionId) {
//        questionService.deleteQuestion(questionId);
//        return ResponseEntity.noContent().build();
//
//    }
}
