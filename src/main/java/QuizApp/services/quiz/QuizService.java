package QuizApp.services.quiz;

import QuizApp.model.quiz.Quiz;
import QuizApp.model.quiz.QuizView;
import QuizApp.model.quiz.QuizzesAndScoresView;
import QuizApp.model.quiz.ResultView;

import java.util.List;

public interface QuizService {
    QuizView createQuiz(int userId);
    QuizView getQuiz(int quizId);
    List<QuizzesAndScoresView> listQuizzesForUser(int userId);

    ResultView submitAnswers(int quizId, List<String> answerIds);

    void deleteQuiz(int quizId);
}
