package QuizApp.services.quiz;

import QuizApp.model.quiz.Quiz;
import QuizApp.repositories.QuizRepository;
import org.springframework.stereotype.Component;


@Component
public class QuizSecurity {

    private final QuizRepository quizRepository;

    public QuizSecurity(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public boolean isUserQuizOwner(int quizId, int userId) {
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        return quiz != null && quiz.getUser().getUserId() == userId;
    }
}

