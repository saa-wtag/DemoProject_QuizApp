package QuizApp.services.question;

import QuizApp.model.question.Question;
import QuizApp.model.question.QuestionInput;
import QuizApp.model.question.QuestionUpdate;
import QuizApp.model.question.QuestionView;

import java.util.List;

public interface QuestionService {
    QuestionView createQuestion(QuestionInput questionInput);
    QuestionView updateQuestionDetails(int questionId, QuestionUpdate questionUpdate);
    QuestionView getQuestionById(int questionId);
    void deleteQuestion(int questionId);
    List<Question> getRandomQuestionsForQuiz();
}
