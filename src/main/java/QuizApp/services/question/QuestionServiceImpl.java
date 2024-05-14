package QuizApp.services.question;


import QuizApp.exceptions.QuizNotFoundException;
import QuizApp.model.question.*;
import QuizApp.quizObjectMapper.QuizObjectMapper;
import QuizApp.repositories.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;


    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    @Override
    @Transactional
    public Question createQuestion(QuestionInput questionInput) {
        Question question = QuizObjectMapper.convertQuestionInputToEntity(questionInput);
        questionRepository.save(question);
        return question;
    }


    @Override
    @Transactional
    public Question updateQuestionDetails(int questionId, QuestionUpdate questionUpdate) {

        Question existingQuestion = getQuestionById(questionId);

        if (questionUpdate.getQuesTitle() != null && !questionUpdate.getQuesTitle().trim().isEmpty()) {
            existingQuestion.setQuesTitle(questionUpdate.getQuesTitle());
        }
        if (questionUpdate.getOptions() != null && !questionUpdate.getOptions().isEmpty()) {

            existingQuestion.setOptions(questionUpdate.getOptions());
        }
        if (questionUpdate.getAnswer() != null && !questionUpdate.getAnswer().isEmpty()) {
            existingQuestion.setAnswer(questionUpdate.getAnswer());
        }
        questionRepository.save(existingQuestion);
        return existingQuestion;
    }


    @Override
    @Transactional(readOnly = true)
    public Question getQuestionById(int questionId) {
      return questionRepository.findById(questionId)
                .orElseThrow(() -> new QuizNotFoundException("Question not found with ID: " + questionId));
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
