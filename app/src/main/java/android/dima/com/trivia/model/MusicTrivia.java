package android.dima.com.trivia.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "question",
        "level",
        "difficulty",
        "size"
})
public class MusicTrivia {
    @JsonProperty("question")
    public List<Question> questions = new ArrayList<>();
    @JsonProperty("level")
    public Integer level;
    @JsonProperty("difficulty")
    public Integer difficulty;
    @JsonProperty("size")
    public Integer size;
}