package QuizApp.services.question;


import QuizApp.model.question.*;
import QuizApp.quizObjectMapper.QuizObjectMapper;
import QuizApp.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static QuizApp.quizObjectMapper.QuizObjectMapper.convertToQuestionViewDTO;


@Service
@Transactional
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;


    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public QuestionViewDTO createQuestion(QuestionInput questionInput) {
        Question question = QuizObjectMapper.convertQuestionInputToModel(questionInput);
        questionRepository.save(question);
        return convertToQuestionViewDTO(question);
    }

    @Override
    public QuestionViewDTO updateQuestionDetails(int questionId, QuestionUpdate questionUpdate) {

        Question question = QuizObjectMapper.convertQuestionUpdateToModel(questionUpdate);

        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("Question not found with ID: " + questionId));

        if (question.getQuesTitle() != null && !question.getQuesTitle().trim().isEmpty()) {
            existingQuestion.setQuesTitle(question.getQuesTitle());
        }
        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            if (!shouldUpdateOptions(question.getOptions())) {
                throw new IllegalArgumentException("Four of the options must be uniquely labeled with 'A', 'B', 'C', or 'D'");
            }
            existingQuestion.setOptions(question.getOptions());
        }
        if (question.getAnswer() != null && !question.getAnswer().isEmpty()) {
            existingQuestion.setAnswer(question.getAnswer());
        }
        questionRepository.save(existingQuestion);
        return convertToQuestionViewDTO(existingQuestion);
    }

    private boolean shouldUpdateOptions(Map<String, String> updatedOptions) {
        return updatedOptions.size() == 4 &&
                updatedOptions.containsKey("A") &&
                updatedOptions.containsKey("B") &&
                updatedOptions.containsKey("C") &&
                updatedOptions.containsKey("D");
    }

    @Override
    public QuestionViewDTO getQuestionById(int questionId) {
        Question questionView = questionRepository.findQuestionByQuesId(questionId);
        if (questionView == null) {
            throw new NoSuchElementException("Question not found with ID: " + questionId);
        }
       return convertToQuestionViewDTO(questionView);
    }

//    @Override
//    public void deleteQuestion(int questionId) {
//        Question question = questionRepository.findQuestionByQuesId(questionId);
//        if (question == null) {
//            throw new NoSuchElementException("Question not found with ID: " + questionId);
//        }
//        questionRepository.delete(question);
//    }

    @Override
    public List<Question> getRandomQuestionsForQuiz() {
        return questionRepository.findRandomQuestions();
    }
}
