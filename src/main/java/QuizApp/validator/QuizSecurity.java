package QuizApp.validator;

import QuizApp.model.quiz.Quiz;
import QuizApp.services.quiz.QuizService;
import org.springframework.stereotype.Component;


@Component
public class QuizSecurity {

    private final QuizService quizService;

    public QuizSecurity(QuizService quizService) {
        this.quizService = quizService;
    }

    public boolean hasPermission(int quizId, int userId) {
        Quiz quiz = quizService.getQuiz(quizId);
        return quiz != null && quiz.getUser().getUserId() == userId;
    }
}

