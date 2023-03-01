package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

//TODO find out how to pause app when user exits
//TODO add notifications that notify the user every once in a while to make some math

public class MainActivity extends AppCompatActivity {
    ArrayList <Integer> answers = new ArrayList<Integer>();
    int locationOfGoodResult;
    int score = 0;
    int totalQuestions = 0;
    int highScore;
    boolean inmultire;
    SharedPreferences sharedPreferences;

    Button startButton;
    Button playAgain;
    Button settings;

    TextView timerTextView;
    TextView sumTextView;
    TextView scoreTextView;
    TextView resultTextView;
    TextView congratsTextView;
    TextView highScoreTextView;

    Button button0;
    Button button1;
    Button button2;
    Button button3;

    public void setButtonInvisible(){

        timerTextView.setVisibility(View.INVISIBLE);
        sumTextView.setVisibility(View.INVISIBLE);
        scoreTextView.setVisibility(View.INVISIBLE);

        button0.setVisibility(View.INVISIBLE);
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
    }

    public void setButtonVisible(){

        timerTextView.setVisibility(View.VISIBLE);
        sumTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);

        button0.setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);

    }

    public void start(View view){
        sharedPreferences = this.getSharedPreferences("com.example.braintrainer", Context.MODE_PRIVATE);
        inmultire = sharedPreferences.getBoolean("rezultat",SettingsActivity.inmultire);

        startButton.setVisibility(View.INVISIBLE);
        settings = findViewById(R.id.button);
        settings.setVisibility(View.INVISIBLE);

        setButtonVisible();

        startTimer();
        newQuestion();

        Log.i("inmultireInStart",String.valueOf(inmultire));
    }

    public void playAgain(View view){
        inmultire = sharedPreferences.getBoolean("rezultat",SettingsActivity.inmultire);

        startTimer();

        setButtonVisible();

        score = 0;
        totalQuestions = 0;

        playAgain.setVisibility(View.INVISIBLE);
        settings.setVisibility(View.INVISIBLE);

        congratsTextView.setVisibility(View.INVISIBLE);
        highScoreTextView.setVisibility(View.INVISIBLE);

        scoreTextView.setText("0/0");

        newQuestion();
    }

    MediaPlayer tick_tock;
    MediaPlayer timer_end;

    public void startTimer () {
        tick_tock.setLooping(true);
        new CountDownTimer(30000,1000){

            @Override
            public void onTick(long l) {
                timerTextView.setText((int)l/1000+"s");
                int s = (int) (l/1000);

                if(s <= 5)
                    tick_tock.start();
            }

            @Override
            public void onFinish() {
                tick_tock.setLooping(false);
                tick_tock.pause();

                playAgain.setVisibility(View.VISIBLE);
                settings.setVisibility(View.VISIBLE);
                resultTextView.setVisibility(View.INVISIBLE);

                if (highScore < score)
                    highScore = score;

                setButtonInvisible();

                congratsTextView.setText("Congrats !\n" + "You got " + String.valueOf(score) + " questions right !");
                congratsTextView.setVisibility(View.VISIBLE);

                highScoreTextView.setVisibility(View.VISIBLE);
                highScoreTextView.setText("Your high score is: " + String.valueOf(highScore) + " !");

                timer_end.start();
            }
        }.start();
    }

    public void chooseAnswer(View view){
        resultTextView.setVisibility(View.VISIBLE);
       if(String.valueOf(locationOfGoodResult).equals(view.getTag().toString())){
           resultTextView.setText("Correct !");
           score++;
       }else {
           resultTextView.setText("Wrong ! :(");
       }
        totalQuestions++;
        scoreTextView.setText(String.valueOf(score)+"/"+String.valueOf(totalQuestions));

        newQuestion();
    }

    public void newQuestion() {
            Log.i("inmultire", String.valueOf(inmultire));

            answers.clear();

            Random random = new Random();

            int a = random.nextInt(101);
            int b = random.nextInt(101);
            int goodResult;
            int wrongAnswer;

            if (!inmultire) {

                goodResult = a + b;
                sumTextView.setText(String.valueOf(a) + " + " + String.valueOf(b));
            }else{

                goodResult = a*b;
                sumTextView.setText(String.valueOf(a) + " * " + String.valueOf(b));
            }

            locationOfGoodResult = random.nextInt(4);

            for (int i = 0; i < 4; i++) {
                if (i == locationOfGoodResult) {
                    answers.add(goodResult);
                } else {

                    if(!inmultire) {
                        wrongAnswer = random.nextInt(202);

                        while (wrongAnswer == goodResult) {
                            wrongAnswer = random.nextInt(202);
                        }
                    }else {
                        wrongAnswer = random.nextInt(10001);

                        while (wrongAnswer == goodResult) {
                            wrongAnswer = random.nextInt(10001);
                        }
                    }

                    answers.add(wrongAnswer);
                }
            }
            button0.setText(String.valueOf(answers.get(0)));
            button1.setText(String.valueOf(answers.get(1)));
            button2.setText(String.valueOf(answers.get(2)));
            button3.setText(String.valueOf(answers.get(3)));
    }

    public void settingsActivity(View view){
        Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);

        sharedPreferences = this.getSharedPreferences("com.example.braintrainer", Context.MODE_PRIVATE);
        inmultire = sharedPreferences.getBoolean("rezultat",SettingsActivity.inmultire);

        intent.putExtra("tipEx",inmultire);
        startActivity(intent);

        Log.i("inmultireDupaSettings",String.valueOf(inmultire));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tick_tock = MediaPlayer.create(this,R.raw.tick_tock);
        timer_end = MediaPlayer.create(this,R.raw.alarm_clock);

        startButton = findViewById(R.id.startButton);
        startButton.setVisibility(View.VISIBLE);

        playAgain = findViewById(R.id.playAgain);

        timerTextView = findViewById(R.id.timerTextView);
        sumTextView = findViewById(R.id.sumTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        resultTextView = findViewById(R.id.resultTextView);
        congratsTextView = findViewById(R.id.congratsTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
    }
}