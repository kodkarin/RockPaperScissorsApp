package com.example.rockpaperscissorsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class GameWindow extends AppCompatActivity {

    public static final String RESULT_MESSAGE = "com.example.rockpaperscissorsapp.RESULTS";
    public static final String WINNER_MESSAGE = "com.example.rockpaperscissorsapp.WINNER";

    private TextView lastView1 = null;
    private TextView lastView2 = null;
    private TextView resultTextView = null;

    private Game game = null;
    private int round = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_window);

        Intent intent = getIntent();

        String username = intent.getStringExtra(MainActivity.USERNAME_MESSAGE);

        Player player1 = new Player(username, 1);
        Player cpuPlayer = new Player("CPU", 2);
        game = new Game(player1, cpuPlayer);

        resultTextView = findViewById(R.id.resultTextView);

        TextView player1TextView = findViewById(R.id.textViewPlayer1);
        TextView player2TextView = findViewById(R.id.textViewPlayer2);
        player1TextView.setText(player1.getUserName());
        player2TextView.setText(cpuPlayer.getUserName());

        lastView1 = player1TextView;
        lastView2 = player2TextView;

    }

    public void makeMove (View view) {

        int choice = 0;
        int buttonId = view.getId();
        String text = "";

        if (buttonId == R.id.rockButton) {
            text = "Rock";
            choice = Game.ROCK;
        } else if (buttonId == R.id.paperButton) {
            text = "Paper";
            choice = Game.PAPER;
        } else if (buttonId == R.id.scissorsButton) {
            text = "Scissors";
            choice = Game.SCISSORS;
        }

        ConstraintLayout cl = findViewById(R.id.currentGameConstraintLayout);
        setContentView(cl);

        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setText(text);

        cl.addView(textView);

        int id = textView.getId();

        ConstraintSet cs = new ConstraintSet();
        cs.clone(cl);

        cs.connect(id, ConstraintSet.TOP, lastView1.getId(), ConstraintSet.BOTTOM, 10);
        cs.connect(id, ConstraintSet.START, lastView1.getId(), ConstraintSet.START, ConstraintSet.MATCH_CONSTRAINT);

        cs.applyTo(cl);

        lastView1 = textView;

        int cpuChoice = makeMoveForCPU();

        game.addChoice(1, choice, round);
        game.addChoice(2, cpuChoice, round);

        int roundWinner = game.compareChoices(choice, cpuChoice);
        if (roundWinner == Game.PLAYER1_WINS) {
            game.increaseScorePlayer1();
            lastView1.setTextColor(Color.rgb(54, 108, 76));
            lastView1.setTypeface(null, Typeface.BOLD);
        } else if (roundWinner == Game.PLAYER2_WINS) {
            game.increaseScorePlayer2();
            lastView2.setTextColor(Color.rgb(54, 108, 76));
            lastView2.setTypeface(null, Typeface.BOLD);
        }

        round++;

        String resultText = game.getScorePlayer1() + " - " + game.getScorePlayer2();
        resultTextView.setText(resultText);

        if(game.getScorePlayer1() > 2 ) {
            goToWinner(true);
        } else if(game.getScorePlayer2() > 2) {
            goToWinner(false);
        }

        cl.invalidate();
    }

    private int makeMoveForCPU() {

        Random random = new Random();
        int cpuChoice = random.nextInt(3) + 1;
        String text = "";

        switch (cpuChoice) {
            case Game.ROCK:
                text = "Rock";
                break;
            case Game.PAPER:
                text = "Paper";
                break;
            case Game.SCISSORS:
                text = "Scissors";
                break;
        }

        ConstraintLayout cl = findViewById(R.id.currentGameConstraintLayout);
        setContentView(cl);

        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setText(text);

        cl.addView(textView);

        int id = textView.getId();
        ConstraintSet cs = new ConstraintSet();
        cs.clone(cl);

        cs.connect(id, ConstraintSet.TOP, lastView2.getId(), ConstraintSet.BOTTOM, 10);
        cs.connect(id, ConstraintSet.START, lastView2.getId(), ConstraintSet.START, ConstraintSet.MATCH_CONSTRAINT);

        cs.applyTo(cl);

        lastView2 = textView;

        return cpuChoice;
    }

    private void goToWinner(boolean isWinner) {
        Intent intent = new Intent(this, WinnerWindow.class);
        intent.putExtra(RESULT_MESSAGE, resultTextView.getText().toString());
        intent.putExtra(WINNER_MESSAGE, isWinner);
        intent.putExtra(MainActivity.USERNAME_MESSAGE, game.getPlayer1().getUserName());
        startActivity(intent);
    }
}

