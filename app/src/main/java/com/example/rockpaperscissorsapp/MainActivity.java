package com.example.rockpaperscissorsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;

import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String USERNAME_MESSAGE = "com.example.rockpaperscissorsapp.USERNAME";
    private final String USERNAME_INPUT = "Username input";

    private EditText usernameInput;
    private String usernameInputString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInput = findViewById(R.id.usernameTextView);
    }



    public void goToGame(View view) {

        usernameInputString = usernameInput.getText().toString();

        Intent intent = new Intent(this, GameWindow.class);
        intent.putExtra(USERNAME_MESSAGE, usernameInputString);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        usernameInputString = usernameInput.getText().toString();
        outState.putString(USERNAME_INPUT, usernameInputString);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        usernameInputString = savedInstanceState.getString(USERNAME_INPUT);
        usernameInput.setText(usernameInputString);
    }
}
