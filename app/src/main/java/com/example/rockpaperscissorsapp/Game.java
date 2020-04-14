package com.example.rockpaperscissorsapp;

public class Game {
    private Player player1;
    private Player player2;
    private int scorePlayer1;
    private int scorePlayer2;
    private int[] movesPlayer1;
    private int[] movesPlayer2;

    public static final int ROCK = 1;
    public static final int SCISSORS = 2;
    public static final int PAPER = 3;
    public static final int DRAW = 0;
    public static final int PLAYER1_WINS = 1;
    public static final int PLAYER2_WINS = 2;
    public static final int INVALID_INPUT = -1;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
        movesPlayer1 = new int[5];
        movesPlayer2 = new int[5];

    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getScorePlayer1() {
        return scorePlayer1;
    }

    public int getScorePlayer2() {
        return scorePlayer2;
    }

    public void increaseScorePlayer1() {
        scorePlayer1++;
    }

    public void increaseScorePlayer2() {
        scorePlayer2++;
    }

    public void addChoice(int player, int choice, int round) {

        if(round >= movesPlayer1.length) {
            int[] temp = new int[movesPlayer1.length * 2];
            System.arraycopy(movesPlayer1, 0, temp, 0, movesPlayer1.length);
            movesPlayer1 = temp;
        }
        if(round >= movesPlayer2.length) {
            int[] temp = new int[movesPlayer2.length * 2];
            System.arraycopy(movesPlayer2, 0, temp, 0, movesPlayer2.length);
            movesPlayer2 = temp;
        }
        if (player == 1) {
            movesPlayer1[round] = choice;
        } else if (player == 2) {
            movesPlayer2[round] = choice;
        }

    }

    public int compareChoices(int choice1, int choice2) {

        int result;

        if ((choice2 != ROCK) && (choice2 != PAPER) && (choice2 !=SCISSORS)) {
            result = INVALID_INPUT;
        } else if (choice1 == choice2) {
            result = DRAW;
        } else switch (choice1) {
            case ROCK:
                result = choice2 == SCISSORS ?  PLAYER1_WINS :  PLAYER2_WINS;
                break;
            case SCISSORS:
                result = choice2 == PAPER ?  PLAYER1_WINS :  PLAYER2_WINS;
                break;
            case PAPER:
                result = choice2 == ROCK ?  PLAYER1_WINS :  PLAYER2_WINS;
                break;
            default:
                result = INVALID_INPUT;
        }
        return result;
    }
}