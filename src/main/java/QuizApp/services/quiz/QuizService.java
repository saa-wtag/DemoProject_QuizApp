package QuizApp.services.quiz;

import QuizApp.model.quiz.*;

import java.util.List;

public interface QuizService {
    Quiz createQuiz();
    Quiz getQuiz(int quizId);
    List<UserQuizDTO> listQuizzesForUser(int userId);

    ResultDTO submitAnswers(int quizId, List<String> answerIds);

    void deleteQuiz(int quizId);
}
