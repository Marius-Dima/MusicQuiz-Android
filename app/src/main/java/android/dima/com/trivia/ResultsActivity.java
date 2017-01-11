package android.dima.com.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    public static int wrongAnswers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        final TextView resultsArea = (TextView) findViewById(R.id.resultsArea);
        resultsArea.setText("Your score " + (10 - wrongAnswers) * 10 + "%");
    }

    public void restartQuiz(View view) {
        wrongAnswers = 0;
        final Intent quizActivity = new Intent(this, QuizActivity.class);
        startActivity(quizActivity);
    }


}
