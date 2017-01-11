package android.dima.com.trivia.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "a"
})
public class Answers {
    @JsonProperty("a")
    public List<Answer> answer = new ArrayList<>();

    public Answers() {
    }

    public Answers(List<Answer> answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answers answers = (Answers) o;

        return answer.equals(answers.answer);
    }

    @Override
    public int hashCode() {
        return answer.hashCode();
    }
}