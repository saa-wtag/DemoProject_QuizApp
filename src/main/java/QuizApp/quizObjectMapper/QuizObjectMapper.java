package QuizApp.quizObjectMapper;

import QuizApp.model.question.Question;
import QuizApp.model.question.QuestionInput;
import QuizApp.model.question.QuestionUpdate;
import QuizApp.model.user.User;
import QuizApp.model.user.UserInput;
import QuizApp.model.user.UserUpdate;

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
}
