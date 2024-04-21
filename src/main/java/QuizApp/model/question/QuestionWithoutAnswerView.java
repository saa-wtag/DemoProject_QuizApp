package QuizApp.model.question;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

@JsonPropertyOrder({"quesId", "quesTitle", "options"})
public interface QuestionWithoutAnswerView {
    int getQuesId();
    String getQuesTitle();
    Map<String, String> getOptions();
}
