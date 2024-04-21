package QuizApp.model.question;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

@JsonPropertyOrder({"quesId","quesTitle", "options","answer"})
public interface QuestionView {
    int getQuesId();
    String getQuesTitle();
    Map<String, String> getOptions();
    String getAnswer();
}
