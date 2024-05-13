package QuizApp.services.quiz;


import QuizApp.exceptions.AccessDeniedException;
import QuizApp.exceptions.BadRequestException;
import QuizApp.exceptions.ObjectNotFoundException;
import QuizApp.exceptions.QuizNotFoundException;
import QuizApp.model.question.Question;
import QuizApp.model.question.QuestionDetails;
import QuizApp.model.quiz.*;
import QuizApp.model.user.User;
import QuizApp.repositories.QuizRepository;
import QuizApp.repositories.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import QuizApp.services.question.QuestionService;


import java.util.ArrayList;
import java.util.List;

import static QuizApp.quizObjectMapper.QuizObjectMapper.convertToQuizViewDTO;
import static QuizApp.quizObjectMapper.QuizObjectMapper.convertToResultViewDTO;


@Service
public class QuizServiceImpl implements QuizService{
    private final QuestionService questionService;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;


    public QuizServiceImpl(QuestionService questionService, QuizRepository quizRepository, UserRepository userRepository) {
        this.questionService = questionService;
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public Quiz createQuiz() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Question> questions = questionService.getRandomQuestionsForQuiz();

        Quiz quiz = new Quiz();
        quiz.setUser(user);
        quiz.setQuestions(questions);
        quizRepository.save(quiz);

        return quiz;
    }


    @Override
    @Transactional(readOnly = true)
    public Quiz getQuiz(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if(quiz==null)
            throw new ObjectNotFoundException("Quiz not found with ID: " + quizId);

        return quiz;
    }


    @Override
    @Transactional(readOnly = true)
    public  List<UserQuizDTO>  listQuizzesForUser(int userId) {
        List<Object[]> results = quizRepository.findUserAndQuizzes(userId);

        if (results.isEmpty()) {
            throw new ObjectNotFoundException("User not found or has no quizzes: " + userId);
        }
        List<UserQuizDTO> userQuizDTOS = new ArrayList<>();
        for (Object[] result : results) {
            int quizId = (int) result[0];
            long quizScore = (long) result[1];
            userQuizDTOS.add(new UserQuizDTO(quizId, quizScore));
        }
        return userQuizDTOS;
    }

    @Override
    @Transactional
    @PreAuthorize("@quizSecurity.isUserQuizOwner(#quizId, principal.userId)")
    public ResultDTO submitAnswers(int quizId, List<String> answerIds) {
        Quiz quiz = quizRepository.findQuizByQuizId(quizId);
        if (quiz.isIfAttempted()) {
            throw new BadRequestException("This quiz has already been attempted. You cannot submit answers again.");
        }

        long score = 0;
        quiz.setIfAttempted(true);
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
        return convertToResultViewDTO(quiz, questionDetailsList);
    }


    @Override
    @Transactional
    public void deleteQuiz(int quizId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByUserName(userDetails.getUsername());

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new QuizNotFoundException("Quiz not found"));
        boolean isOwner = quiz.getUser().getUserId() == currentUser.getUserId();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Access denied: You are not authorized to delete this quiz.");
        }
        quizRepository.deleteById(quizId);
    }
}
