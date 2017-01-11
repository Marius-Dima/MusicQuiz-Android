package android.dima.com.trivia;

import android.content.Intent;
import android.dima.com.trivia.model.Answer;
import android.dima.com.trivia.model.Answers;
import android.dima.com.trivia.model.MusicTrivia;
import android.dima.com.trivia.model.Question;
import android.dima.com.trivia.worker.MusicWorker;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
    private List<CheckBox> checkBoxes = new ArrayList<>();
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
            questionListIterator = musicTrivia.questions.listIterator();
            currentQuestion = questionListIterator.next();
            iterateQuiz(currentQuestion);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use Inheritence
     *
     * @param question
     */
    private void iterateQuiz(final Question question) {
        questionText.setText(question.getText());
        answer1.setText(question.answers.answer.get(0).value);

        if (question.answers.answer.size() == 1) {
            answer2.setVisibility(View.GONE);
            answer3.setVisibility(View.GONE);
            answer4.setVisibility(View.GONE);
        } else if (question.answers.answer.size() == 2) {
            answer2.setText(question.answers.answer.get(1).value);
            answer3.setVisibility(View.GONE);
            answer4.setVisibility(View.GONE);
        } else if (question.answers.answer.size() == 3) {
            answer2.setText(question.answers.answer.get(1).value);
            answer3.setText(question.answers.answer.get(2).value);
            answer4.setVisibility(View.GONE);
        } else {
            answer2.setText(question.answers.answer.get(1).value);
            answer3.setText(question.answers.answer.get(2).value);
            answer4.setText(question.answers.answer.get(3).value);
        }
        this.questionNumber.setText("Question Number " + questionListIterator.nextIndex());
    }

    public void nextQuestion(View view) {
        getCurrentAnswers();
        resetCheckboxState();
        if (questionListIterator.nextIndex() == 9) {
            nextButton.setText(R.string.view_results);
        }
        if (questionListIterator.nextIndex() >= 10) {
            ResultsActivity.wrongAnswers = wrongAnswers;
            startActivity(new Intent(this, ResultsActivity.class));
            return;
        }
        this.questionNumber.setText("Question Number " + questionListIterator.nextIndex());
        currentQuestion = questionListIterator.next();
        iterateQuiz(currentQuestion);
    }

    public void getCurrentAnswers() {
        final List<Answer> answers = new ArrayList<>(currentQuestion.answers.answer);
        saveAnswer(answers);
        actualAnswers.add(new Answers(answers));
        if (!currentQuestion.answers.answer.equals(answers)) {
            Toast.makeText(this, "That was a wrong answer!", Toast.LENGTH_SHORT);
            wrongAnswers++;
            setCorrectAnswer(currentQuestion.answers.answer);
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
        this.answer3.setVisibility(View.GONE);
        this.answer4.setVisibility(View.GONE);

        this.answer1.setChecked(false);
        this.answer2.setChecked(false);
        this.answer3.setChecked(false);
        this.answer4.setChecked(false);
    }

    void setCorrectAnswer(List<Answer> answers) {


    }

    class CheckboxSize2 {
        private CheckBox checkbox = answer2;

        public CheckboxSize2(Question question) {
            this.checkbox.setVisibility(View.VISIBLE);
            initCheckbox(question);
        }

        public void initCheckbox(Question question) {
            this.checkbox.setChecked(question.answers.answer.get(1).correct);
            this.checkbox.setText(question.answers.answer.get(1).value);
        }
    }

    class CheckboxSize3 extends CheckboxSize2 {
        private CheckBox checkbox = answer3;

        public CheckboxSize3(Question question) {
            super(question);
            initCheckbox(question);
        }

        public void initCheckbox(Question question) {
            this.checkbox.setChecked(question.answers.answer.get(2).correct);
            this.checkbox.setText(question.answers.answer.get(2).value);
        }
    }

    class CheckboxSize4 extends CheckboxSize3 {
        private CheckBox checkbox = answer4;

        public CheckboxSize4(Question question) {
            super(question);
        }

        public void initCheckbox(Question question) {
            this.checkbox.setChecked(question.answers.answer.get(3).correct);
            this.checkbox.setText(question.answers.answer.get(3).value);
        }
    }

}
