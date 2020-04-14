package com.example.rockpaperscissorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameWindow extends AppCompatActivity {

    public static final String RESULT_MESSAGE = "com.example.rockpaperscissorsapp.RESULTS";
    public static final String WINNER_MESSAGE = "com.example.rockpaperscissorsapp.WINNER";
    private final String MOVES_PLAYER1 = "Moves player 1";
    private final String MOVES_PLAYER2 = "Moves player 2";
    private final String ROUND_WINNERS = "Winners";

    private TextView lastView1 = null;
    private TextView lastView2 = null;
    private TextView resultTextView = null;
    private List<TextView> movesPlayer1;
    private List<TextView> movesPlayer2;
    private ArrayList<Integer> winners;

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

        movesPlayer1 = new ArrayList<>();
        movesPlayer2 = new ArrayList<>();
        winners = new ArrayList<>();

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

        movesPlayer1.add(textView);
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
            winners.add(1);
        } else if (roundWinner == Game.PLAYER2_WINS) {
            game.increaseScorePlayer2();
            lastView2.setTextColor(Color.rgb(54, 108, 76));
            lastView2.setTypeface(null, Typeface.BOLD);
            winners.add(2);
        } else {
            winners.add(0);
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

        movesPlayer2.add(textView);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        String[] movesPlayer1Strings = new String[movesPlayer1.size()];
        String[] movesPlayer2Strings = new String[movesPlayer2.size()];

        for (int i = 0; i < movesPlayer1.size(); i++) {
            movesPlayer1Strings[i] = movesPlayer1.get(i).getText().toString();
        }
        for (int i = 0; i < movesPlayer2.size(); i++) {
            movesPlayer2Strings[i] = movesPlayer2.get(i).getText().toString();
        }

        outState.putStringArray(MOVES_PLAYER1, movesPlayer1Strings);
        outState.putStringArray(MOVES_PLAYER2, movesPlayer2Strings);
        outState.putIntegerArrayList(ROUND_WINNERS, winners);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ConstraintLayout cl = findViewById(R.id.currentGameConstraintLayout);
        setContentView(cl);

        String[] movesPlayer1Strings = savedInstanceState.getStringArray(MOVES_PLAYER1);
        String[] movesPlayer2Strings = savedInstanceState.getStringArray(MOVES_PLAYER2);
        winners = savedInstanceState.getIntegerArrayList(ROUND_WINNERS);

        if (movesPlayer1Strings != null) {
            for (int i = 0; i < movesPlayer1Strings.length; i++) {
                TextView textView = new TextView(this);
                textView.setId(View.generateViewId());
                textView.setText(movesPlayer1Strings[i]);
                if (winners.get(i) == Game.PLAYER1_WINS) {
                    game.increaseScorePlayer1();
                    textView.setTextColor(Color.rgb(54, 108, 76));
                    textView.setTypeface(null, Typeface.BOLD);
                }

                movesPlayer1.add(textView);
                cl.addView(textView);

                int id = textView.getId();

                ConstraintSet cs = new ConstraintSet();
                cs.clone(cl);

                cs.connect(id, ConstraintSet.TOP, lastView1.getId(), ConstraintSet.BOTTOM, 10);
                cs.connect(id, ConstraintSet.START, lastView1.getId(), ConstraintSet.START, ConstraintSet.MATCH_CONSTRAINT);

                cs.applyTo(cl);

                lastView1 = textView;
            }
        }

        if (movesPlayer2Strings != null) {
            for (int i = 0; i < movesPlayer2Strings.length; i++) {
                TextView textView = new TextView(this);
                textView.setId(View.generateViewId());
                textView.setText(movesPlayer2Strings[i]);
                if (winners.get(i) == Game.PLAYER2_WINS) {
                    game.increaseScorePlayer2();
                    textView.setTextColor(Color.rgb(54, 108, 76));
                    textView.setTypeface(null, Typeface.BOLD);
                }

                movesPlayer2.add(textView);
                cl.addView(textView);

                int id = textView.getId();

                ConstraintSet cs = new ConstraintSet();
                cs.clone(cl);

                cs.connect(id, ConstraintSet.TOP, lastView2.getId(), ConstraintSet.BOTTOM, 10);
                cs.connect(id, ConstraintSet.START, lastView2.getId(), ConstraintSet.START, ConstraintSet.MATCH_CONSTRAINT);

                cs.applyTo(cl);

                lastView2 = textView;
            }
        }

        String resultText = game.getScorePlayer1() + " - " + game.getScorePlayer2();
        resultTextView.setText(resultText);
    }
}

