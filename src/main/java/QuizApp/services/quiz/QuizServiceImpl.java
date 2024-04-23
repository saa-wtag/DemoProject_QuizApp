package QuizApp.services.quiz;


import QuizApp.exceptions.AccessDeniedException;
import QuizApp.exceptions.BadRequestException;
import QuizApp.exceptions.ObjectNotFoundException;
import QuizApp.model.question.Question;
import QuizApp.model.question.QuestionDetails;
import QuizApp.model.quiz.Quiz;
import QuizApp.model.quiz.QuizView;
import QuizApp.model.quiz.QuizzesAndScoresView;
import QuizApp.model.quiz.ResultView;
import QuizApp.model.user.User;
import QuizApp.repositories.QuizRepository;
import QuizApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import QuizApp.services.question.QuestionService;


import java.util.ArrayList;
import java.util.List;



@Service
public class QuizServiceImpl implements QuizService{
    private final QuestionService questionService;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuizServiceImpl(QuestionService questionService, QuizRepository quizRepository, UserRepository userRepository) {

        this.questionService = questionService;
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    public boolean isUserQuizOwner(int quizId, int userId) {
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        return quiz != null && quiz.getUser().getUserId() == userId;
    }



    @Override
    public QuizView createQuiz() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new AccessDeniedException("User is not authenticated");
        }

        User user = (User) authentication.getPrincipal();
        List<Question> questions = questionService.getRandomQuestionsForQuiz();

        Quiz quiz = new Quiz();
        quiz.setUser(user);
        quiz.setQuestions(questions);
        quizRepository.save(quiz);

        return quizRepository.findQuizViewByQuizId(quiz.getQuizId());
    }


    @Override
    public QuizView getQuiz(int quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user==null) {
            throw new AccessDeniedException("You are not logged in");
        }

        QuizView quizView = quizRepository.findQuizViewByQuizId(quizId);
        if(quizView==null)
        {
            throw new ObjectNotFoundException("Quiz not found with ID: " + quizId);
        }
        return quizView;
    }

    @Override
    public  List< QuizzesAndScoresView>  listQuizzesForUser(int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user==null)
            throw new AccessDeniedException("You are not logged in");
        if(userRepository.findByUserId(userId)==null)
            throw new ObjectNotFoundException("User not found with ID: " + userId);

        return quizRepository.findAllByUserId(userId);
    }

    @Override
    public ResultView submitAnswers(int quizId, List<String> answerIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        int currentUserId = user.getUserId();

        if (!isUserQuizOwner(quizId, currentUserId)) {
            throw new AccessDeniedException("Access is denied: User does not own this quiz.");
        }

        Quiz quiz = quizRepository.findQuizByQuizId(quizId);
        if (quiz.isIfAttempted()) {
            throw new BadRequestException("This quiz has already been attempted. You cannot submit answers again.");
        }
        else
            quiz.setIfAttempted(true);

        long score = 0;

        List<QuestionDetails> questionDetailsList = new ArrayList<>();

        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question question = quiz.getQuestions().get(i);
            String submittedAnswer = answerIds.get(i);

            if (question.getAnswer().equals(submittedAnswer)) {
                score++;
            }

            QuestionDetails questionDetails = new QuestionDetails();
            questionDetails.setQuesTitle(question.getQuesTitle());
            questionDetails.setOptions(question.getOptions());
            questionDetails.setAnswer(question.getAnswer());
            questionDetails.setGivenAnswer(submittedAnswer);

            questionDetailsList.add(questionDetails);
        }

        quiz.setScore(score);
        quizRepository.save(quiz);

        long finalScore = score;
        return new ResultView() {
            @Override
            public int getQuizId() {
                return quizId;
            }

            @Override
            public boolean getIfAttempted() {
                return true;
            }

            @Override
            public long getScore() {
                return finalScore;
            }

            @Override
            public List<QuestionDetails> getQuestions() {
                return questionDetailsList;
            }

            @Override
            public String getUserName() {
                return ResultView.super.getUserName();
            }

            @Override
            public User getUser() {
                return user;
            }
        };
    }


    @Override
    @Transactional
    public void deleteQuiz(int quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new AccessDeniedException("User is not authenticated.");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUserName(userDetails.getUsername());

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        boolean isOwner = quiz.getUser().getUserId() == currentUser.getUserId(); // Corrected to use 'user'

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Access denied: You are not authorized to delete this quiz.");
        }

        quizRepository.deleteById(quizId); // Use the correct delete method
        System.out.println("Deleted quiz with ID: " + quizId);
    }

}
