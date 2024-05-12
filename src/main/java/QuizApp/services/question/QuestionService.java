package QuizApp.services.question;

import QuizApp.model.question.*;

import java.util.List;

public interface QuestionService {
    QuestionViewDTO createQuestion(QuestionInput questionInput);
    QuestionViewDTO updateQuestionDetails(int questionId, QuestionUpdate questionUpdate);
    QuestionViewDTO getQuestionById(int questionId);
//    void deleteQuestion(int questionId);
    List<Question> getRandomQuestionsForQuiz();
}
