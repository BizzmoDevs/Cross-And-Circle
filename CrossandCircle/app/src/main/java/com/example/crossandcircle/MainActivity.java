package com.example.crossandcircle;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //GLOBALS
    ConstraintLayout mainLayout,gameBoardLayout,settingsLayout;
    Button resetButton,settingsButton,infoButton,settingsOkButton;
    boolean gameOn,gameStop,crossTurn,circleTurn,winCross,winCircle,aIOn;
    GameField[][] gameFields;
    int boardSizeX,boardSizeY,fieldSize,screenHeight,screenWidth;
    float xFieldPos,yFieldPos;
    Random randomInt = new Random();
    String strColorField,strColorHighlight;
    ImageView boardImageView;
    TextView commentTextView,boardSizeSetTextView,versusAiSetTextView;
    CheckBox versusAiCheckBox,boardSizeCheckBox3,boardSizeCheckBox4,boardSizeCheckBox5;
    DrawLine drawLine;
    short[][] possibleAiMoves;


    //LISTENERS
    View.OnClickListener infoListener = new View.OnClickListener(){@Override
    public void onClick(View v) {}};
    View.OnClickListener resetListener = new View.OnClickListener(){@Override
    public void onClick(View v) {
        if (gameOn!=false){
            clearBoard();
            setInitVal();
            makeBoard();
        }
        }};
    View.OnClickListener settingsListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            showSettingsWindow();
        }
    };
    View.OnClickListener fieldListener = new View.OnClickListener(){@Override
        public void onClick(View v) {

            if ( ((GameField)v).isEmpty == true && gameOn == true && gameStop == false){

                if (crossTurn == true){
                    ((GameField)v).removeImage(MainActivity.this);
                    ((GameField)v).setFieldImage(1,MainActivity.this);
                    circleTurn = true;
                    crossTurn = false;
                    makeText("CIRCLE TURN");

                    if ( (circleTurn == true)&&(aIOn == true)&&(checkWinConditions()==false) ){
                        //calcAiMove();
                        randomAiMove();
                        crossTurn = true;
                        circleTurn = false;
                    }
                }
                else if ( (circleTurn == true)&&(aIOn == false) ) {
                    ((GameField)v).removeImage(MainActivity.this);
                    ((GameField)v).setFieldImage(2,MainActivity.this);
                    crossTurn = true;
                    circleTurn = false;
                    makeText("CROSS TURN");
                }

                if (checkWinConditions() == true){
                    if (winCross == true ){
                        makeText("CROSS WINS!!!");
                        gameStop = true;
                    }
                    if (winCircle == true ){
                        makeText("CIRCLE WINS!!!");
                        gameStop = true;
                    }
                }
            }
        }
    };
    View.OnClickListener settingsOkListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            clearBoard();
            if (versusAiCheckBox.isChecked()==true){
                aIOn = true;
            }else aIOn = false;

            if (boardSizeCheckBox3.isChecked()==true){
                boardSizeX = 3;
                boardSizeY = boardSizeX;
            }
            if (boardSizeCheckBox4.isChecked()==true){
                boardSizeX = 4;
                boardSizeY = boardSizeX;
            }
            if (boardSizeCheckBox5.isChecked()==true){
                boardSizeX = 5;
                boardSizeY = boardSizeX;
            }

            settingsLayout.setVisibility(View.INVISIBLE);

            setInitVal();
            makeBoard();
            makeText("CROSS TURN");
        }
    };

    //SET INITIAL VARIABLES
    private void createInit(){
        resetButton = new Button(this);
        settingsButton = new Button(this);
        infoButton = new Button(this);
        mainLayout = findViewById(R.id.mainLayout);
        gameBoardLayout = findViewById(R.id.gameBoardLayout);
        gameFields = new GameField[boardSizeX][boardSizeY];
        commentTextView = new TextView(MainActivity.this);

        //SETTINGS WINDOW
        settingsLayout = new ConstraintLayout(MainActivity.this);
        boardSizeSetTextView  = new TextView(MainActivity.this);
        versusAiSetTextView  = new TextView(MainActivity.this);
        versusAiCheckBox = new CheckBox(MainActivity.this);
        boardSizeCheckBox3 = new CheckBox(MainActivity.this);
        boardSizeCheckBox4 = new CheckBox(MainActivity.this);
        boardSizeCheckBox5 = new CheckBox(MainActivity.this);
        settingsOkButton = new Button(MainActivity.this);

        mainLayout.addView(commentTextView);
    }
    private void setInitVal(){
        strColorField="#033f10";
        strColorHighlight="#113119";
        xFieldPos = 0;
        yFieldPos = 0;
        gameStop = false;
        crossTurn = true;

        //SETTINGS WINDOW
        settingsLayout.setVisibility(View.INVISIBLE);
        boardSizeSetTextView.setVisibility(View.INVISIBLE);
        versusAiSetTextView.setVisibility(View.INVISIBLE);
        versusAiCheckBox.setVisibility(View.INVISIBLE);
        boardSizeCheckBox3.setVisibility(View.INVISIBLE);
        boardSizeCheckBox4.setVisibility(View.INVISIBLE);
        boardSizeCheckBox5.setVisibility(View.INVISIBLE);
        settingsOkButton.setVisibility(View.INVISIBLE);

    }
    //BOARD VISUALS AND METHODS
    private void getScreenResolution(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }
    private void makeButtons(){
        int xSize=screenWidth/7,ySize=screenHeight/14;
        resetButton.setLayoutParams(new android.view.ViewGroup.LayoutParams(xSize,ySize));
        resetButton.setText("RESET");
        resetButton.setBackgroundColor(Color.parseColor("#48664c"));
        resetButton.setTextSize(11);
        resetButton.setX((screenWidth/2)-(xSize/2));
        resetButton.setY(15);
        mainLayout.addView(resetButton);
        resetButton.setOnClickListener(resetListener);

        settingsButton.setLayoutParams(new android.view.ViewGroup.LayoutParams(xSize,ySize));
        settingsButton.setText("SETTINGS");
        settingsButton.setBackgroundColor(Color.parseColor("#48664c"));
        settingsButton.setTextSize(11);
        settingsButton.setX((screenWidth/2)-xSize-xSize/2-2);
        settingsButton.setY(15);
        mainLayout.addView(settingsButton);
        settingsButton.setOnClickListener(settingsListener);

        infoButton.setLayoutParams(new android.view.ViewGroup.LayoutParams(xSize,ySize));
        infoButton.setText("INFO");
        infoButton.setBackgroundColor(Color.parseColor("#48664c"));
        infoButton.setTextSize(11);
        infoButton.setX((screenWidth/2)+xSize-xSize/2+2);
        infoButton.setY(15);
        mainLayout.addView(infoButton);
        infoButton.setOnClickListener(infoListener);

    }
    private void makeBoard() {

        fieldSize = (screenWidth / boardSizeX);
        int gridSizerX = fieldSize;
        int gridSizerY = fieldSize;
        gameOn = true;
        gameFields = new GameField[boardSizeX][boardSizeY];

        //FIELDS
        for (int y = 0; y < boardSizeY; y++) {
            xFieldPos = 0;
            for (int x = 0; x < boardSizeX; x++) {
                gameFields[x][y] = new GameField(this, x, y,0);
                gameFields[x][y].setOnClickListener(fieldListener);
                gameFields[x][y].setLayoutParams(new android.view.ViewGroup.LayoutParams(fieldSize, fieldSize));
                gameFields[x][y].setX(xFieldPos);
                gameFields[x][y].setY(yFieldPos);
                gameBoardLayout.addView(gameFields[x][y]);
                gameFields[x][y].isEmpty = true;

                xFieldPos = xFieldPos + fieldSize;
            }
            yFieldPos = yFieldPos + fieldSize;
        }

        //GRID
        for (int i = 0; i < boardSizeX-1; i++){
            drawLine = new DrawLine(this);
            drawLine.setBackgroundColor(Color.TRANSPARENT);
            drawLine.posX = 0;
            drawLine.posX2 = screenWidth;
            drawLine.posY = gridSizerY;
            drawLine.posY2 = gridSizerY;
            gameBoardLayout.addView(drawLine);
            gridSizerY += fieldSize;
        }
        for (int i = 0; i < boardSizeY-1; i++){
            drawLine = new DrawLine(this);
            drawLine.setBackgroundColor(Color.TRANSPARENT);
            drawLine.posX = gridSizerX;
            drawLine.posX2 = gridSizerX;
            drawLine.posY = 0;
            drawLine.posY2 = screenWidth;
            gameBoardLayout.addView(drawLine);
            gridSizerX += fieldSize;
        }

    }
    private void calcAiMove(){
        possibleAiMoves = new short[boardSizeX][boardSizeY];
        for (int y = 0; y < boardSizeY; y++) {
            for (int x = 0; x < boardSizeX; x++) {
                if (gameFields[x][y].fieldType == 0){
                    possibleAiMoves[x][y] = 1;
                }
            }
        }
    }
    private void randomAiMove(){

        boolean movedAi = false;
        int x,y;

        while (movedAi == false){
            x = randomInt.nextInt(boardSizeX);
            y = randomInt.nextInt(boardSizeX);

            if (gameFields[x][y].fieldType == 0){
                gameFields[x][y].removeImage(MainActivity.this);
                gameFields[x][y].setFieldImage(2,MainActivity.this);
                movedAi = true;
                makeText("CROSS TURN");
            }
        }
    }
    private boolean checkWinConditions(){

        if ( (boardSizeX == 3)&&(gameStop==false) ){

            //CROSS
            if  ( (gameFields[0][0].fieldType == 1)&&(gameFields[1][0].fieldType == 1)&&(gameFields[2][0].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][0].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[2][2].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
                gameStop = true;
            }
            if  ( (gameFields[0][0].fieldType == 1)&&(gameFields[0][1].fieldType == 1)&&(gameFields[0][2].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = fieldSize/2;
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[1][0].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[1][2].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize +(fieldSize/2);
                drawLine.posX2 = fieldSize +(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[2][0].fieldType == 1)&&(gameFields[2][1].fieldType == 1)&&(gameFields[2][2].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*2)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[2][2].fieldType == 1)&&(gameFields[1][2].fieldType == 1)&&(gameFields[0][2].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = (fieldSize*2)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][2].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[2][0].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = (fieldSize*2)+(fieldSize/2);
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][1].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[2][1].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize+(fieldSize/2);
                drawLine.posY2 = fieldSize+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }


            //CIRCLE
            if  ( (gameFields[0][0].fieldType == 2)&&(gameFields[1][0].fieldType == 2)&&(gameFields[2][0].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = fieldSize/2;

                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][0].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[2][2].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][0].fieldType == 2)&&(gameFields[0][1].fieldType == 2)&&(gameFields[0][2].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = fieldSize/2;
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[1][0].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[1][2].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize +(fieldSize/2);
                drawLine.posX2 = fieldSize +(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[2][0].fieldType == 2)&&(gameFields[2][1].fieldType == 2)&&(gameFields[2][2].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*2)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[2][2].fieldType == 2)&&(gameFields[1][2].fieldType == 2)&&(gameFields[0][2].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = (fieldSize*2)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][2].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[2][0].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = (fieldSize*2)+(fieldSize/2);
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][1].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[2][1].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize+(fieldSize/2);
                drawLine.posY2 = fieldSize+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }

        }
        else if ( (boardSizeX == 4)&&(gameStop==false) ){

            //CROSS
            if  ( (gameFields[0][0].fieldType == 1)&&(gameFields[1][0].fieldType == 1)&&(gameFields[2][0].fieldType == 1)&&(gameFields[3][0].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][0].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[2][2].fieldType == 1)&&(gameFields[3][3].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][0].fieldType == 1)&&(gameFields[0][1].fieldType == 1)&&(gameFields[0][2].fieldType == 1)&&(gameFields[0][3].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = fieldSize/2;
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[1][0].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[1][2].fieldType == 1)&&(gameFields[1][3].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize +(fieldSize/2);
                drawLine.posX2 = fieldSize +(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[2][0].fieldType == 1)&&(gameFields[2][1].fieldType == 1)&&(gameFields[2][2].fieldType == 1)&&(gameFields[2][3].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*2)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[3][0].fieldType == 1)&&(gameFields[3][1].fieldType == 1)&&(gameFields[3][2].fieldType == 1)&&(gameFields[3][3].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*3)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][1].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[2][1].fieldType == 1)&&(gameFields[3][1].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize+(fieldSize/2);
                drawLine.posY2 = fieldSize+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][2].fieldType == 1)&&(gameFields[1][2].fieldType == 1)&&(gameFields[2][2].fieldType == 1)&&(gameFields[3][2].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = (fieldSize*2)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][3].fieldType == 1)&&(gameFields[1][3].fieldType == 1)&&(gameFields[2][3].fieldType == 1)&&(gameFields[3][3].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = (fieldSize*3)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][3].fieldType == 1)&&(gameFields[1][2].fieldType == 1)&&(gameFields[2][1].fieldType == 1)&&(gameFields[3][0].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = (fieldSize*3)+(fieldSize/2);
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCross = true;
            }

            //CIRCLE
            if  ( (gameFields[0][0].fieldType == 2)&&(gameFields[1][0].fieldType == 2)&&(gameFields[2][0].fieldType == 2)&&(gameFields[3][0].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][0].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[2][2].fieldType == 2)&&(gameFields[3][3].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][0].fieldType == 2)&&(gameFields[0][1].fieldType == 2)&&(gameFields[0][2].fieldType == 2)&&(gameFields[0][3].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = fieldSize/2;
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[1][0].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[1][2].fieldType == 2)&&(gameFields[1][3].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize +(fieldSize/2);
                drawLine.posX2 = fieldSize +(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[2][0].fieldType == 2)&&(gameFields[2][1].fieldType == 2)&&(gameFields[2][2].fieldType == 2)&&(gameFields[2][3].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*2)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[3][0].fieldType == 2)&&(gameFields[3][1].fieldType == 2)&&(gameFields[3][2].fieldType == 2)&&(gameFields[3][3].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*3)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][1].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[2][1].fieldType == 2)&&(gameFields[3][1].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize+(fieldSize/2);
                drawLine.posY2 = fieldSize+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][2].fieldType == 2)&&(gameFields[1][2].fieldType == 2)&&(gameFields[2][2].fieldType == 2)&&(gameFields[3][2].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = (fieldSize*2)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][3].fieldType == 2)&&(gameFields[1][3].fieldType == 2)&&(gameFields[2][3].fieldType == 2)&&(gameFields[3][3].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = (fieldSize*3)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][3].fieldType == 2)&&(gameFields[1][2].fieldType == 2)&&(gameFields[2][1].fieldType == 2)&&(gameFields[3][0].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = (fieldSize*3)+(fieldSize/2);
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
        }
        else if ( (boardSizeX == 5)&&(gameStop==false) ){

            //CROSS
            if  ( (gameFields[0][0].fieldType == 1)&&(gameFields[1][0].fieldType == 1)&&(gameFields[2][0].fieldType == 1)&&(gameFields[3][0].fieldType == 1)&&(gameFields[4][0].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][0].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[2][2].fieldType == 1)&&(gameFields[3][3].fieldType == 1)&&(gameFields[4][4].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][0].fieldType == 1)&&(gameFields[0][1].fieldType == 1)&&(gameFields[0][2].fieldType == 1)&&(gameFields[0][3].fieldType == 1)&&(gameFields[0][4].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = fieldSize/2;
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[1][0].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[1][2].fieldType == 1)&&(gameFields[1][3].fieldType == 1)&&(gameFields[1][4].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize +(fieldSize/2);
                drawLine.posX2 = fieldSize +(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[2][0].fieldType == 1)&&(gameFields[2][1].fieldType == 1)&&(gameFields[2][2].fieldType == 1)&&(gameFields[2][3].fieldType == 1)&&(gameFields[2][4].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*2)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[3][0].fieldType == 1)&&(gameFields[3][1].fieldType == 1)&&(gameFields[3][2].fieldType == 1)&&(gameFields[3][3].fieldType == 1)&&(gameFields[3][4].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*3)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[4][0].fieldType == 1)&&(gameFields[4][1].fieldType == 1)&&(gameFields[4][2].fieldType == 1)&&(gameFields[4][3].fieldType == 1)&&(gameFields[4][4].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*4)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][1].fieldType == 1)&&(gameFields[1][1].fieldType == 1)&&(gameFields[2][1].fieldType == 1)&&(gameFields[3][1].fieldType == 1)&&(gameFields[4][1].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = fieldSize+(fieldSize/2);
                drawLine.posY2 = fieldSize+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][2].fieldType == 1)&&(gameFields[1][2].fieldType == 1)&&(gameFields[2][2].fieldType == 1)&&(gameFields[3][2].fieldType == 1)&&(gameFields[4][2].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = (fieldSize*2)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][3].fieldType == 1)&&(gameFields[1][3].fieldType == 1)&&(gameFields[2][3].fieldType == 1)&&(gameFields[3][3].fieldType == 1)&&(gameFields[4][3].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = (fieldSize*3)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][4].fieldType == 1)&&(gameFields[1][4].fieldType == 1)&&(gameFields[2][4].fieldType == 1)&&(gameFields[3][4].fieldType == 1)&&(gameFields[4][4].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = (fieldSize*4)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCross = true;
            }
            if  ( (gameFields[0][4].fieldType == 1)&&(gameFields[1][3].fieldType == 1)&&(gameFields[2][2].fieldType == 1)&&(gameFields[3][1].fieldType == 1)&&(gameFields[4][0].fieldType == 1) ){
                winCross = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = (fieldSize*4)+(fieldSize/2);
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCross = true;
            }

//CIRCLE
            if  ( (gameFields[0][0].fieldType == 2)&&(gameFields[1][0].fieldType == 2)&&(gameFields[2][0].fieldType == 2)&&(gameFields[3][0].fieldType == 2)&&(gameFields[4][0].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][0].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[2][2].fieldType == 2)&&(gameFields[3][3].fieldType == 2)&&(gameFields[4][4].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][0].fieldType == 2)&&(gameFields[0][1].fieldType == 2)&&(gameFields[0][2].fieldType == 2)&&(gameFields[0][3].fieldType == 2)&&(gameFields[0][4].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = fieldSize/2;
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[1][0].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[1][2].fieldType == 2)&&(gameFields[1][3].fieldType == 2)&&(gameFields[1][4].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize +(fieldSize/2);
                drawLine.posX2 = fieldSize +(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[2][0].fieldType == 2)&&(gameFields[2][1].fieldType == 2)&&(gameFields[2][2].fieldType == 2)&&(gameFields[2][3].fieldType == 2)&&(gameFields[2][4].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*2)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*2)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[3][0].fieldType == 2)&&(gameFields[3][1].fieldType == 2)&&(gameFields[3][2].fieldType == 2)&&(gameFields[3][3].fieldType == 2)&&(gameFields[3][4].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*3)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*3)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[4][0].fieldType == 2)&&(gameFields[4][1].fieldType == 2)&&(gameFields[4][2].fieldType == 2)&&(gameFields[4][3].fieldType == 2)&&(gameFields[4][4].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = (fieldSize*4)+(fieldSize/2);
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = fieldSize/2;
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][1].fieldType == 2)&&(gameFields[1][1].fieldType == 2)&&(gameFields[2][1].fieldType == 2)&&(gameFields[3][1].fieldType == 2)&&(gameFields[4][1].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = fieldSize+(fieldSize/2);
                drawLine.posY2 = fieldSize+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][2].fieldType == 2)&&(gameFields[1][2].fieldType == 2)&&(gameFields[2][2].fieldType == 2)&&(gameFields[3][2].fieldType == 2)&&(gameFields[4][2].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = (fieldSize*2)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*2)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][3].fieldType == 2)&&(gameFields[1][3].fieldType == 2)&&(gameFields[2][3].fieldType == 2)&&(gameFields[3][3].fieldType == 2)&&(gameFields[4][3].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = (fieldSize*3)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*3)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][4].fieldType == 2)&&(gameFields[1][4].fieldType == 2)&&(gameFields[2][4].fieldType == 2)&&(gameFields[3][4].fieldType == 2)&&(gameFields[4][4].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = (fieldSize*4)+(fieldSize/2);
                drawLine.posY2 = (fieldSize*4)+(fieldSize/2);
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
            if  ( (gameFields[0][4].fieldType == 2)&&(gameFields[1][3].fieldType == 2)&&(gameFields[2][2].fieldType == 2)&&(gameFields[3][1].fieldType == 2)&&(gameFields[4][0].fieldType == 2) ){
                winCircle = true;
                drawLine = new DrawLine(this);
                drawLine.colorInt = 1;
                drawLine.setBackgroundColor(Color.TRANSPARENT);
                drawLine.posX = fieldSize/2;
                drawLine.posX2 = (fieldSize*4)+(fieldSize/2);
                drawLine.posY = (fieldSize*4)+(fieldSize/2);
                drawLine.posY2 = fieldSize/2;
                gameBoardLayout.addView(drawLine);winCircle = true;
            }
        }

        if ( (winCross == true)||(winCircle == true) ){
            return true;
        } else return false;
    }
    private void makeSettingsWindow(){
        int xSize=screenWidth/3,ySize=screenHeight/4;

        settingsLayout.setX(xSize);
        settingsLayout.setY(ySize);
        settingsLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(xSize,ySize));
        settingsLayout.setBackgroundColor(Color.GRAY);
        mainLayout.addView(settingsLayout);

        //TEXT
        boardSizeSetTextView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT));
        boardSizeSetTextView.setTextSize(12);
        boardSizeSetTextView.setX(40);
        boardSizeSetTextView.setY(20);
        boardSizeSetTextView.setText("Board size:");
        settingsLayout.addView(boardSizeSetTextView);

        versusAiSetTextView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT));
        versusAiSetTextView.setTextSize(12);
        versusAiSetTextView.setX(40);
        versusAiSetTextView.setY(220);
        versusAiSetTextView.setText("Versus AI:");
        settingsLayout.addView(versusAiSetTextView);

        //BUTTONS
        versusAiCheckBox.setX(200);
        versusAiCheckBox.setY(200);
        settingsLayout.addView(versusAiCheckBox);

        boardSizeCheckBox3.setX(200);
        boardSizeCheckBox3.setY(1);
        boardSizeCheckBox3.setText("3");
        settingsLayout.addView(boardSizeCheckBox3);

        boardSizeCheckBox4.setX(200);
        boardSizeCheckBox4.setY(51);
        boardSizeCheckBox4.setText("4");
        settingsLayout.addView(boardSizeCheckBox4);

        boardSizeCheckBox5.setX(200);
        boardSizeCheckBox5.setY(101);
        boardSizeCheckBox5.setText("5");
        settingsLayout.addView(boardSizeCheckBox5);

        settingsOkButton.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,80));
        settingsOkButton.setText("OK");
        settingsOkButton.setBackgroundColor(Color.parseColor("#48574c"));
        settingsOkButton.setTextSize(8);
        settingsOkButton.setX((xSize/2)-40);
        settingsOkButton.setY(ySize-(ySize/4));
        settingsLayout.addView(settingsOkButton);
        settingsOkButton.setOnClickListener(settingsOkListener);

        settingsLayout.setVisibility(View.INVISIBLE);
        boardSizeSetTextView.setVisibility(View.INVISIBLE);
        versusAiSetTextView.setVisibility(View.INVISIBLE);
        versusAiCheckBox.setVisibility(View.INVISIBLE);
        boardSizeCheckBox3.setVisibility(View.INVISIBLE);
        boardSizeCheckBox4.setVisibility(View.INVISIBLE);
        boardSizeCheckBox5.setVisibility(View.INVISIBLE);
        settingsOkButton.setVisibility(View.INVISIBLE);
    }
    private void showSettingsWindow(){
        settingsLayout.setVisibility(View.VISIBLE);
        boardSizeSetTextView.setVisibility(View.VISIBLE);
        versusAiSetTextView.setVisibility(View.VISIBLE);
        versusAiCheckBox.setVisibility(View.VISIBLE);
        boardSizeCheckBox3.setVisibility(View.VISIBLE);
        boardSizeCheckBox4.setVisibility(View.VISIBLE);
        boardSizeCheckBox5.setVisibility(View.VISIBLE);
        settingsOkButton.setVisibility(View.VISIBLE);
    }
    private void makeText(String sText){
        commentTextView.setText(sText);
        commentTextView.measure(0, 0);
        commentTextView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT));
        commentTextView.setTextSize(30);
        commentTextView.setX( screenWidth/2 - (commentTextView.getMeasuredWidth()/2) );
        commentTextView.setY(screenHeight-200);
    }
    private void clearBoard() {

        winCross = false;
        winCircle = false;
        makeText("");

        if (gameOn == true){
            for (int y = 0; y < boardSizeY; y++) {
                for (int x = 0; x < boardSizeX; x++) {
                    gameFields[x][y].isEmpty = true;
                }
            }
            gameBoardLayout.removeAllViews();
        } }

    //MAIN METHOD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getScreenResolution();
        createInit();
        setInitVal();
        makeButtons();
        makeSettingsWindow();
        showSettingsWindow();
    }
}
