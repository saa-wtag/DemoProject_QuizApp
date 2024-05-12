package QuizApp.quizObjectMapper;

import QuizApp.model.question.*;
import QuizApp.model.quiz.Quiz;
import QuizApp.model.quiz.QuizViewDTO;
import QuizApp.model.quiz.ResultViewDTO;
import QuizApp.model.user.User;
import QuizApp.model.user.UserInput;
import QuizApp.model.user.UserUpdate;
import QuizApp.model.user.UserViewDTO;

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

    public static User convertUserUpdateToModel(UserUpdate userUpdate){
        User modelUser = new User();
        modelUser.setUserName(userUpdate.getUserName());
        modelUser.setUserEmail(userUpdate.getUserEmail());
        modelUser.setUserPassword(userUpdate.getUserPassword());
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

    public static UserViewDTO convertToUserViewDTO(User user){
        UserViewDTO modelUserViewDTO = new UserViewDTO();
        modelUserViewDTO.setUserId(user.getUserId());
        modelUserViewDTO.setUserName(user.getUsername());
        modelUserViewDTO.setUserEmail(user.getUserEmail());
        modelUserViewDTO.setRole(user.getRole());
        return modelUserViewDTO;
    }

    public static QuizViewDTO convertToQuizViewDTO(Quiz quiz){
        QuizViewDTO modelQuizViewDTO = new QuizViewDTO();
        modelQuizViewDTO.setQuizId(quiz.getQuizId());
        modelQuizViewDTO.setIfAttempted(quiz.isIfAttempted());
        modelQuizViewDTO.setScore(quiz.getScore());
        modelQuizViewDTO.setQuestions(convertToQuestionWOAnswerDTOList(quiz.getQuestions()));
        modelQuizViewDTO.setUserName(quiz.getUser().getUsername());
        return modelQuizViewDTO;
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

    public static ResultViewDTO convertToResultViewDTO(Quiz quiz, List<QuestionDetails> questionDetailsList) {
        ResultViewDTO modelResultViewDTO = new ResultViewDTO();
        modelResultViewDTO.setQuizId(quiz.getQuizId());
        modelResultViewDTO.setIfAttempted(quiz.isIfAttempted());
        modelResultViewDTO.setScore(quiz.getScore());
        modelResultViewDTO.setQuestions(questionDetailsList);
        modelResultViewDTO.setUserName(quiz.getUser().getUsername());
        return modelResultViewDTO;
    }

    public static QuestionViewDTO convertToQuestionViewDTO(Question question) {
        QuestionViewDTO modelQuestionViewDTO = new QuestionViewDTO();
        modelQuestionViewDTO.setQuesId(question.getQuesId());
        modelQuestionViewDTO.setQuesTitle(question.getQuesTitle());
        modelQuestionViewDTO.setOptions(question.getOptions());
        modelQuestionViewDTO.setAnswer(question.getAnswer());
        return modelQuestionViewDTO;
    }

}
