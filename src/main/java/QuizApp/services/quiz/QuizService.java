package QuizApp.services.quiz;

import QuizApp.model.quiz.*;

import java.util.List;

public interface QuizService {
    QuizViewDTO createQuiz();
    QuizViewDTO getQuiz(int quizId);
    List<UserQuizDto> listQuizzesForUser(int userId);

    ResultViewDTO submitAnswers(int quizId, List<String> answerIds);

    void deleteQuiz(int quizId);
}
