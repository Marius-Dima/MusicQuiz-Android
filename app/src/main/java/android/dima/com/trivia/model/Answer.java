package android.dima.com.trivia.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "value",
        "correct"
})
public class Answer {
    @JsonProperty("value")
    public String value;
    @JsonProperty("correct")
    public Boolean correct;

    public Answer() {
    }

    public Answer(String value, Boolean correct) {
        this.value = value;
        this.correct = correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        return value.equals(answer.value) && correct.equals(answer.correct);
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + correct.hashCode();
        return result;
    }
}