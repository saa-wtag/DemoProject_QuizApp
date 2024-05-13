package QuizApp.services.question;


import QuizApp.model.question.*;
import QuizApp.quizObjectMapper.QuizObjectMapper;
import QuizApp.repositories.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Transactional
    public Question createQuestion(QuestionInput questionInput) {
        Question question = QuizObjectMapper.convertQuestionInputToModel(questionInput);
        questionRepository.save(question);
        return question;
    }


    @Override
    @Transactional
    public Question updateQuestionDetails(int questionId, QuestionUpdate questionUpdate) {

        Question question = QuizObjectMapper.convertQuestionUpdateToModel(questionUpdate);

        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException("Question not found with ID: " + questionId));

        if (question.getQuesTitle() != null && !question.getQuesTitle().trim().isEmpty()) {
            existingQuestion.setQuesTitle(question.getQuesTitle());
        }
        if (question.getOptions() != null && !question.getOptions().isEmpty()) {

            existingQuestion.setOptions(question.getOptions());
        }
        if (question.getAnswer() != null && !question.getAnswer().isEmpty()) {
            existingQuestion.setAnswer(question.getAnswer());
        }
        questionRepository.save(existingQuestion);
        return existingQuestion;
    }


    @Override
    @Transactional(readOnly = true)
    public Question getQuestionById(int questionId) {
        Question questionView = questionRepository.findQuestionByQuesId(questionId);
        if (questionView == null) {
            throw new NoSuchElementException("Question not found with ID: " + questionId);
        }
       return questionView;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Question> getRandomQuestionsForQuiz() {
        return questionRepository.findRandomQuestions();
    }



//    @Override
//    public void deleteQuestion(int questionId) {
//        Question question = questionRepository.findQuestionByQuesId(questionId);
//        if (question == null) {
//            throw new NoSuchElementException("Question not found with ID: " + questionId);
//        }
//        questionRepository.delete(question);
//    }


}
