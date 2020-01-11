package com.example.randomquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomquiz.controller.AppController;
import com.example.randomquiz.data.AnswerListAsyncResponse;
import com.example.randomquiz.data.QuestionBank;
import com.example.randomquiz.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView txtQuestion;
    private TextView txtCounter;

    private Button btnTrue;
    private Button btnFalse;

    private ImageButton btnNext;
    private ImageButton btnPrev;

    private int currentQuestionIndex = 0;

    private  List<Question> questionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNext = findViewById(R.id.btnForward);
        btnPrev = findViewById(R.id.btnPrevious);

        btnTrue = findViewById(R.id.btnTrue);
        btnFalse = findViewById(R.id.btnFalse);

        txtCounter = findViewById(R.id.txtCounter);
        txtQuestion = findViewById(R.id.txtQuestions);

        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnTrue.setOnClickListener(this);
        btnFalse.setOnClickListener(this);

        questionsList = new QuestionBank().getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {

                txtQuestion.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                txtCounter.setText(currentQuestionIndex + "/" + questionsList.size());
            }
        });

        //Log.d("MAin", "onCreate: " + questionsList);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnPrevious:
                if(currentQuestionIndex > 0)
                {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionsList.size();
                    updteQuestion();
                }
                break;
            case R.id.btnForward:

                currentQuestionIndex = (currentQuestionIndex + 1) % questionsList.size();
                updteQuestion();
                break;
            case R.id.btnTrue:
                checkAnswer(true);
                updteQuestion();
                break;
            case R.id.btnFalse:
                checkAnswer(false);
                updteQuestion();
                break;

        }
    }

    private void checkAnswer(boolean userChoice) {
        boolean answerisTrue = questionsList.get(currentQuestionIndex).isAnswerTrue();
        int toastID = 0;

        if(userChoice == answerisTrue)
        {
            fadeView();
            toastID = R.string.correct;

        }
        else
        {
            shakeAnimation();
            toastID = R.string.Wrong;
        }

        Toast.makeText(MainActivity.this, toastID, Toast.LENGTH_SHORT).show();
    }

    private void updteQuestion() {

        String question = questionsList.get(currentQuestionIndex).getAnswer();
        txtQuestion.setText(question);

        txtCounter.setText(currentQuestionIndex + "/" + questionsList.size());
    }

    private void shakeAnimation(){
        Animation shake  = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);

        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void fadeView(){
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
