package com.example.rockpaperscissorsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class WinnerWindow extends AppCompatActivity {


    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_window);

        TextView headerWinnerWindow = findViewById(R.id.headerWinnerWindow);
        GifImageView gifImageView = findViewById(R.id.gifImageView);
        TextView resultTextView = findViewById(R.id.resultsWinnerWindow);

        Intent intent = getIntent();
        boolean isWinner = intent.getBooleanExtra(GameWindow.WINNER_MESSAGE, false);
        if(!isWinner) {
            headerWinnerWindow.setText(R.string.loser_message);
            gifImageView.setImageResource(R.drawable.losergif);
        }

        String resultText = intent.getStringExtra(GameWindow.RESULT_MESSAGE);
        resultTextView.setText(resultText);

        username = intent.getStringExtra(MainActivity.USERNAME_MESSAGE);
    }

    public void playAgain(View view) {
        Intent intent = new Intent(this, GameWindow.class);
        intent.putExtra(MainActivity.USERNAME_MESSAGE, username);
        startActivity(intent);
    }

    public void changePlayer(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

