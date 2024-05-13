package QuizApp.quizObjectMapper;

import QuizApp.model.question.*;
import QuizApp.model.quiz.Quiz;
import QuizApp.model.quiz.QuizDTO;
import QuizApp.model.quiz.ResultDTO;
import QuizApp.model.user.User;
import QuizApp.model.user.UserInput;
import QuizApp.model.user.UserUpdate;
import QuizApp.model.user.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class QuizObjectMapper {
    public static User convertUserInputToModel(UserInput userInput){
        User modelUser = new User();
        modelUser.setUserName(userInput.getUserName());
        modelUser.setUserEmail(userInput.getUserEmail());
        modelUser.setUserPassword(userInput.getUserPassword());
        return modelUser;
    }

    public static Question convertQuestionInputToModel(QuestionInput questionInput){
        Question modelQuestion = new Question();
        modelQuestion.setQuesTitle(questionInput.getQuesTitle());
        modelQuestion.setOptions(questionInput.getOptions());
        modelQuestion.setAnswer(questionInput.getAnswer());
        return modelQuestion;
    }
    public static Question convertQuestionUpdateToModel(QuestionUpdate questionUpdate){
        Question modelQuestion = new Question();
        modelQuestion.setQuesTitle(questionUpdate.getQuesTitle());
        modelQuestion.setOptions(questionUpdate.getOptions());
        modelQuestion.setAnswer(questionUpdate.getAnswer());
        return modelQuestion;
    }

    public static UserDTO convertToUserViewDTO(User user){
        UserDTO modelUserDTO = new UserDTO();
        modelUserDTO.setUserId(user.getUserId());
        modelUserDTO.setUserName(user.getUsername());
        modelUserDTO.setUserEmail(user.getUserEmail());
        modelUserDTO.setRole(user.getRole());
        return modelUserDTO;
    }

    public static QuizDTO convertToQuizViewDTO(Quiz quiz){
        QuizDTO modelQuizDTO = new QuizDTO();
        modelQuizDTO.setQuizId(quiz.getQuizId());
        modelQuizDTO.setIfAttempted(quiz.isIfAttempted());
        modelQuizDTO.setScore(quiz.getScore());
        modelQuizDTO.setQuestions(convertToQuestionWOAnswerDTOList(quiz.getQuestions()));
        modelQuizDTO.setUserName(quiz.getUser().getUsername());
        return modelQuizDTO;
    }


    public static QuestionWOAnswerDTO convertToQuestionWOAnswerDTO(Question question){
        QuestionWOAnswerDTO modelQuestionWOAnswerDTO = new QuestionWOAnswerDTO();
        modelQuestionWOAnswerDTO.setQuesId(question.getQuesId());
        modelQuestionWOAnswerDTO.setQuesTitle(question.getQuesTitle());
        modelQuestionWOAnswerDTO.setOptions(question.getOptions());
        return modelQuestionWOAnswerDTO;
    }

    public static List<QuestionWOAnswerDTO> convertToQuestionWOAnswerDTOList(List<Question> questions) {
        return questions.stream()
                .map(QuizObjectMapper::convertToQuestionWOAnswerDTO)
                .collect(Collectors.toList());
    }

    public static ResultDTO convertToResultViewDTO(Quiz quiz, List<QuestionDetails> questionDetailsList) {
        ResultDTO modelResultDTO = new ResultDTO();
        modelResultDTO.setQuizId(quiz.getQuizId());
        modelResultDTO.setIfAttempted(quiz.isIfAttempted());
        modelResultDTO.setScore(quiz.getScore());
        modelResultDTO.setQuestions(questionDetailsList);
        modelResultDTO.setUserName(quiz.getUser().getUsername());
        return modelResultDTO;
    }

    public static QuestionDTO convertToQuestionViewDTO(Question question) {
        QuestionDTO modelQuestionDTO = new QuestionDTO();
        modelQuestionDTO.setQuesId(question.getQuesId());
        modelQuestionDTO.setQuesTitle(question.getQuesTitle());
        modelQuestionDTO.setOptions(question.getOptions());
        modelQuestionDTO.setAnswer(question.getAnswer());
        return modelQuestionDTO;
    }

}
