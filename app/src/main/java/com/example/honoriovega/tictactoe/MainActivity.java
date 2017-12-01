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

    private Button[][] mGridButton = new Button[3][3];
    private char[][] mGrid = new char[3][3];

    private Button mPlayAgainButton;

    private String mPlayerSymbol = "X";
    private String mComputerSymbol = "0";

    private boolean mIsUserFirst = true;


    public static final int mIDS[] = {R.id.zero_zero, R.id.zero_one, R.id.zero_two,
                                      R.id.one_zero, R.id.one_one, R.id.one_two,
                                      R.id.two_zero, R.id.two_one, R.id.two_two};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayAgainButton = (Button) findViewById(R.id.play_again);
        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        mPlayAgainButton.setEnabled(false);

        Toast.makeText(getApplicationContext(),"Hello your Symbol is " + mPlayerSymbol,Toast.LENGTH_SHORT)
                .show();


        Random r = new Random();

        if (r.nextInt(100) % 2 == 0) {
            mIsUserFirst = false;
        }




        int count = 0;
        char n = '1';
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++){
                mGrid[i][j] = n++;
                mGridButton[i][j] = (Button) findViewById(mIDS[count]);
                final String index = "" + i + j;
                final int finalJ = j;
                final int finalI = i;
                mGridButton[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mGrid[finalI][finalJ] = mPlayerSymbol.charAt(0);
                        mGridButton[finalI][finalJ].setText(mPlayerSymbol);
                        mGridButton[finalI][finalJ].setEnabled(false);
                        if(isTie()) {
                            Toast.makeText(getApplicationContext(),"It's a tie!",Toast.LENGTH_SHORT)
                                    .show();

                            mPlayAgainButton.setEnabled(true);
                        }

                        if(checkForWinner()) {
                            Toast.makeText(getApplicationContext(),"User Wins!",Toast.LENGTH_SHORT)
                                    .show();
                            disableAllButtons();

                        } else {

                            // Actions to do after 10 seconds
                            computerTurn();
                            if(checkForWinner()) {
                                Toast.makeText(getApplicationContext(),"Computer Wins!",Toast.LENGTH_SHORT)
                                        .show();
                                disableAllButtons();
                            }
                        }





                    }


                });

                count = count + 1;
            }
        }


        if(!mIsUserFirst) {
            mGridButton[1][1].setText(mComputerSymbol);
            mGridButton[1][1].setEnabled(false);
            mGrid[1][1] = mComputerSymbol.charAt(0);
        }



    }
    private void computerTurn() {


        int wins[][] = new int[][] {{0,0,1,0,2,0},
                {2,0,1,0,0,0}, {0,1,1,1,2,1}, {2,1,1,1,0,1}, {0,2,1,2,2,2}, {2,2,1,2,0,2},
                {0,0,0,1,0,2},{0,1,0,2,0,0},{1,0,1,1,1,2},{1,2,1,1,1,0}, {2,0,2,1,2,2},
                {2,2,2,1,2,0},{0,0,1,1,2,2},{1,1,2,2,0,0},{2,0,1,1,0,2},{0,2,1,1,2,0},
                {0,0,2,2,1,1},{2,0,0,2,1,1},{1,0,1,2,1,1},{0,0,0,2,0,1},{2,0,2,2,2,1},
                {2,2,0,2,1,2}};


        if(mGridButton[1][1].isEnabled()) {
            mGrid[1][1] = mComputerSymbol.charAt(0);
            mGridButton[1][1].setText(mComputerSymbol);
            mGridButton[1][1].setEnabled(false);
            return;
        }


        for(int i = 0; i < wins.length;i++){
            if (pickBestMove(mComputerSymbol.charAt(0), wins[i])) {
                return;
            }
        }
        for(int i = 0; i < wins.length;i++){
            if (pickBestMove(mPlayerSymbol.charAt(0), wins[i])) {
                return;
            }
        }

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3;j ++) {
                if(mGridButton[i][j].isEnabled()) {
                    mGrid[i][j] = mComputerSymbol.charAt(0);

                    mGridButton[i][j].setText(mComputerSymbol);
                    mGridButton[i][j].setEnabled(false);
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

            mGrid[five][six] = mComputerSymbol.charAt(0);
            mGridButton[five][six].setText(mComputerSymbol);
            mGridButton[five][six].setEnabled(false);
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

    private void disableAllButtons() {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                mGridButton[i][j].setEnabled(false);
            }
        }

        mPlayAgainButton.setEnabled(true);
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
