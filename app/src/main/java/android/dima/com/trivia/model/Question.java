package android.dima.com.trivia.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "text",
        "answers",
        "selectable"
})
public class Question {

    @JsonProperty("answers")
    public Answers answers;
    @JsonProperty("selectable")
    public Integer selectable;
    @JsonProperty("text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
//        "text" : "<font color='grey'>Which two tracks appear on the album</font><br>'Master Of Reality'<font color='grey'><br>by<br></font>Black Sabbath"
        this.text = eliminateHtmlTags(text);
    }

    /**
     * Eliminate the extra HTML tags included in the questions' text...
     *
     * @param text
     * @return
     */
    private String eliminateHtmlTags(String text) {
        final String processedString = text.replaceAll("<[^>]*>", " ");
        return processedString.trim();
    }

    @Override
    public String toString() {
        return "Question title: " + this.getText() + " : " + this.answers;
    }
}
