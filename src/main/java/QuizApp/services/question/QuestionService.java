package QuizApp.services.question;

import QuizApp.model.question.*;

import java.util.List;

public interface QuestionService {
    Question createQuestion(QuestionInput questionInput);
    Question updateQuestionDetails(int questionId, QuestionUpdate questionUpdate);
    Question getQuestionById(int questionId);
//    void deleteQuestion(int questionId);
    List<Question> getRandomQuestionsForQuiz();
}
