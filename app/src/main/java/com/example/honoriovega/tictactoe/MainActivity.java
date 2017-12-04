package com.example.honoriovega.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Model grid and UI grid
    private Button[][] mGridButton = new Button[3][3];
    private char[][] mGrid = new char[3][3];

    private Button mPlayAgainButton;

    // Users
    private String mPlayerSymbol = "X";
    private String mComputerSymbol = "0";

    private boolean mIsUserFirst = true;

    public static final int mIDS[] = {R.id.zero_zero, R.id.zero_one, R.id.zero_two,
                                      R.id.one_zero, R.id.one_one, R.id.one_two,
                                      R.id.two_zero, R.id.two_one, R.id.two_two};



    // Runs once when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayAgainButton = (Button) findViewById(R.id.play_again);

        // Start the activity again when the user clicks play again
        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, MainActivity.class);
//                startActivity(i);

                 restartGame();
            }
        });

        // Disable the play again button for now, gets enabled when their is a winner
        // or a tie
        mPlayAgainButton.setEnabled(false);

        Random r = new Random();
        // Pick the user or computer to go first
        mIsUserFirst = r.nextInt(100) % 2 == 0;

        int count = 0;
        char n = '1';
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++){

                mGrid[i][j] = n++;
                mGridButton[i][j] = (Button) findViewById(mIDS[count]);
                mGridButton[i][j].setTextColor(Color.parseColor("#808080"));

                final int finalJ = j;
                final int finalI = i;
                mGridButton[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        updateModelAndUI(mPlayerSymbol,finalI,finalJ);

                        // Check to see if user wins after making move
                        if(checkForWinner()) {
                            Toast.makeText(getApplicationContext(),"User Wins!",Toast.LENGTH_SHORT)
                                    .show();
                            toggleButtons(false);

                        } else {

                            computerTurn();
                            // Check if computer won
                            if(checkForWinner()) {
                                Toast.makeText(getApplicationContext(),"Computer Wins!",Toast.LENGTH_SHORT)
                                        .show();
                                toggleButtons(false);
                            }
                        }

                    }


                });

                count = count + 1;
            }
        }


        // if the user doesn't go first then computer goes first
        if(!mIsUserFirst) {
            // make move in the middle
            updateModelAndUI(mComputerSymbol, 1, 1);

        }



    }

    private void restartGame() {
        toggleButtons(true);
        char letter = 'a';
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                mGridButton[i][j].setText("");
                mGridButton[i][j].setTextColor(Color.parseColor("#808080"));

                mGrid[i][j] = letter;
                letter += 1;

            }
        }
        mPlayAgainButton.setEnabled(false);
    }

    private void computerTurn() {


        // winning combinations
        int wins[][] = new int[][] {{0,0,1,0,2,0},
                {2,0,1,0,0,0}, {0,1,1,1,2,1}, {2,1,1,1,0,1}, {0,2,1,2,2,2}, {2,2,1,2,0,2},
                {0,0,0,1,0,2},{0,1,0,2,0,0},{1,0,1,1,1,2},{1,2,1,1,1,0}, {2,0,2,1,2,2},
                {2,2,2,1,2,0},{0,0,1,1,2,2},{1,1,2,2,0,0},{2,0,1,1,0,2},{0,2,1,1,2,0},
                {0,0,2,2,1,1},{2,0,0,2,1,1},{1,0,1,2,1,1},{0,0,0,2,0,1},{2,0,2,2,2,1},
                {2,2,0,2,1,2},{0,2,2,2,1,2},{0,0,2,0,1,0}};


        // If middle spot is not taken, make move here
        if(mGridButton[1][1].isEnabled()) {
            updateModelAndUI(mComputerSymbol, 1, 1);
            return;
        }


        // If their is a move were computer can win, take it
        for(int i = 0; i < wins.length;i++){
            if (pickBestMove(mComputerSymbol.charAt(0), wins[i])) {
                return;
            }
        }

        // try to block the user from winning
        for(int i = 0; i < wins.length;i++){
            if (pickBestMove(mPlayerSymbol.charAt(0), wins[i])) {
                return;
            }
        }

        // simulates random move - take the first empty spot
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3;j ++) {
                if(mGridButton[i][j].isEnabled()) {
                    updateModelAndUI(mComputerSymbol, i, j);
                    return;
                }
            }
        }
    }

    private boolean pickBestMove(char symbol, int[] possibleMoves) {

        int one = possibleMoves[0];
        int two = possibleMoves[1];
        int three = possibleMoves[2];
        int four = possibleMoves[3];
        int five = possibleMoves[4];
        int six = possibleMoves[5];

        if(mGrid[one][two] == symbol && mGrid[three][four] == symbol
                && mGridButton[five][six].isEnabled() ) {

            updateModelAndUI(mComputerSymbol, five, six);

            return true;
        }

        return false;

    }

    private boolean checkForWinner() {

        if(isTie()) {
            Toast.makeText(getApplicationContext(),"It's a tie!",Toast.LENGTH_SHORT)
                    .show();

            mPlayAgainButton.setEnabled(true);
        }

        int possibleWins[][] = new int[][]{{0, 0, 1, 0, 2, 0},
                                            {0, 1, 1, 1, 2, 1},
                                            {0, 2, 1, 2, 2, 2},
                                            {0, 0, 0, 1, 0, 2},
                                            {1, 0, 1, 1, 1, 2},
                                            {2, 0, 2, 1, 2, 2},
                                            {0, 0, 1, 1, 2, 2},
                                            {0, 2, 1, 1, 2, 0}};


        for(int i = 0; i < possibleWins.length; i++) {

            if(isRowWinner(possibleWins[i])) {
                return true;
            }
        }

        return false;

    }

    private boolean isRowWinner(int[] possibleWin) {

        int one = possibleWin[0];
        int two = possibleWin[1];
        int three = possibleWin[2];
        int four = possibleWin[3];
        int five = possibleWin[4];
        int six = possibleWin[5];

        if(mGrid[one][two] == mGrid[three][four] && mGrid[one][two] == mGrid[five][six]
                && mGrid[three][four] == mGrid[five][six]) {
            mGridButton[one][two].setTextColor(Color.parseColor("#ff0000"));
            mGridButton[three][four].setTextColor(Color.parseColor("#ff0000"));
            mGridButton[five][six].setTextColor(Color.parseColor("#ff0000"));

            return true;
        } else {
            return false;
        }
    }

    private void toggleButtons(boolean state) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                mGridButton[i][j].setEnabled(state);
            }
        }

        mPlayAgainButton.setEnabled(true);
    }

    // Function to update model and UI,
    private void updateModelAndUI(String symbol, int i, int j) {
        mGrid[i][j] = symbol.charAt(0);
        mGridButton[i][j].setText(symbol);
        mGridButton[i][j].setEnabled(false);
    }

    private boolean isTie() {
        boolean flag = true;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if (mGridButton[i][j].isEnabled()) {
                    flag = false;
                }
            }
        }
        return flag;
    }
}
