package android.dima.com.trivia;

import android.content.Intent;
import android.dima.com.trivia.model.Answer;
import android.dima.com.trivia.model.Answers;
import android.dima.com.trivia.model.MusicTrivia;
import android.dima.com.trivia.model.Question;
import android.dima.com.trivia.worker.MusicWorker;
import android.os.Bundle;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutionException;

public class QuizActivity extends AppCompatActivity {

    private MusicTrivia musicTrivia;
    private TextView questionNumber, questionText;
    private CheckBox answer1, answer2, answer3, answer4;
    private Button nextButton;
    private ListIterator<Question> questionListIterator;
    private Question currentQuestion;
    private List<Answers> actualAnswers = new ArrayList<>();
    private int wrongAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        this.questionNumber = (TextView) findViewById(R.id.questionNumber);
        this.questionText = (TextView) findViewById(R.id.questionText);
        this.nextButton = (Button) findViewById(R.id.next);
        this.answer1 = (CheckBox) findViewById(R.id.answer1);
        this.answer2 = (CheckBox) findViewById(R.id.answer2);
        this.answer3 = (CheckBox) findViewById(R.id.answer3);
        this.answer4 = (CheckBox) findViewById(R.id.answer4);

        try {
            musicTrivia = new MusicWorker().execute(getString(R.string.music_trivia_address)).get();
            if (musicTrivia == null) {
                Toast.makeText(this, "Loading trivia questions from static json due to network error", Toast.LENGTH_SHORT).show();
                musicTrivia = loadFromJson(R.raw.questions);
                Collections.shuffle(musicTrivia.questions);
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }

        questionListIterator = musicTrivia.questions.listIterator();
        currentQuestion = questionListIterator.next();
        setQuizOptions(currentQuestion);
    }

    /**
     * Load the Music Trivia content from a static json file
     * This method is intended to replace performRequest() in case of a network failure or other technical difficulty
     */
    private MusicTrivia loadFromJson(@RawRes int id) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final InputStream inputStream = getResources().openRawResource(id);

        return objectMapper.readValue(inputStream, MusicTrivia.class);
    }

    private void setQuizOptions(Question question) {
        resetCheckboxState();
        questionText.setText(question.getText());

        switch (question.answers.answer.size()) {
            case 1:
                new CheckBoxSize1(question);
                break;
            case 2:
                new CheckBoxSize2(question);
                break;
            case 3:
                new CheckBoxSize3(question);
                break;
            case 4:
                new CheckBoxSize4(question);
                break;
        }

        this.questionNumber.setText("Question Number " + questionListIterator.nextIndex() + "/" + musicTrivia.questions.size());
    }

    public void nextQuestion(View view) throws InterruptedException {
        getCurrentAnswers();
        if (questionListIterator.nextIndex() == 9) {
            nextButton.setText(R.string.view_results);
        }
        if (questionListIterator.nextIndex() >= 10) {
            final Intent resultActivity = new Intent(this, ResultsActivity.class);
            resultActivity.putExtra("wrongAnswers", wrongAnswers);
            startActivity(resultActivity);
            return;
        }
        this.questionNumber.setText("Question Number " + questionListIterator.nextIndex() + "/" + musicTrivia.questions.size());
        currentQuestion = questionListIterator.next();
        setQuizOptions(currentQuestion);
    }

    public void getCurrentAnswers() throws InterruptedException {
        final List<Answer> answers = new ArrayList<>(currentQuestion.answers.answer);
        saveAnswer(answers);
        actualAnswers.add(new Answers(answers));
        if (!currentQuestion.answers.answer.equals(answers)) {
            Toast.makeText(this, "Wrong answer!", Toast.LENGTH_SHORT).show();
            wrongAnswers++;
        }
    }

    public void saveAnswer(List<Answer> answers) {
        ListIterator<Answer> answerListIterator = answers.listIterator();
        answerListIterator.next();
        answerListIterator.set(new Answer(answer1.getText().toString(), answer1.isChecked()));
        answerListIterator.next();

        answerListIterator.set(new Answer(answer2.getText().toString(), this.answer2.isChecked()));
        if (!answerListIterator.hasNext())
            return;

        answerListIterator.next();
        answerListIterator.set(new Answer(answer3.getText().toString(), this.answer3.isChecked()));

        if (!answerListIterator.hasNext())
            return;
        answerListIterator.next();
        answerListIterator.set(new Answer(answer4.getText().toString(), this.answer4.isChecked()));

    }

    public void resetCheckboxState() {
        this.answer2.setVisibility(View.GONE);
        this.answer2.setVisibility(View.GONE);
        this.answer3.setVisibility(View.GONE);
        this.answer4.setVisibility(View.GONE);

        this.answer1.setChecked(false);
        this.answer2.setChecked(false);
        this.answer3.setChecked(false);
        this.answer4.setChecked(false);
    }

    private class CheckBoxSize1 {
        CheckBoxSize1(Question question) {
            answer1.setText(question.answers.answer.get(0).value);
            answer1.setVisibility(View.VISIBLE);
        }
    }

    private class CheckBoxSize2 extends CheckBoxSize1 {
        CheckBoxSize2(Question question) {
            super(question);
            answer2.setText(question.answers.answer.get(1).value);
            answer2.setVisibility(View.VISIBLE);
        }
    }

    private class CheckBoxSize3 extends CheckBoxSize2 {

        CheckBoxSize3(Question question) {
            super(question);
            answer3.setText(question.answers.answer.get(2).value);
            answer3.setVisibility(View.VISIBLE);
        }

    }

    private class CheckBoxSize4 extends CheckBoxSize3 {
        CheckBoxSize4(Question question) {
            super(question);
            answer4.setText(question.answers.answer.get(3).value);
            answer4.setVisibility(View.VISIBLE);
        }
    }

}
