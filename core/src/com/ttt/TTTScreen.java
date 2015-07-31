package com.ttt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.graphics.Color.*;

/**
 * This class deals with the actual tictactoe game and its gameplay.
 * It's used when the Player vs CPU button or the Player vs Player button is pressed
 */
public class TTTScreen implements Screen {

    private TicTacToeGame mainMenuGame;


    OrthographicCamera cam;


    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Viewport viewport;
    private Texture texture;
    private Texture texture60;
    TextButton.TextButtonStyle textButtonStyle;
    TextButton.TextButtonStyle textButtonStyle60;
    TextButton.TextButtonStyle textButtonStyleBot;
    private BitmapFont bfont;
    private BitmapFont bfontLarge;
    private Dialog dialog;


    private Sprite square;

    private int spacing;

    /*
    Variables for the tic tac toe game itself
     */
    String[][] gameboard;

    private List<Integer> tracker;
    private List<Boolean> touchTracker;

    private Boolean gameOver;


    /*
    Variables for Settings screen
     */
    static boolean playerVsCPUMode;
    static boolean playerVsPlayerMode;
    private boolean xorO;


    /*
    TTTScreen constructor
     */
    public TTTScreen(TicTacToeGame mainMenuGame){

        this.mainMenuGame = mainMenuGame;

        cam = new OrthographicCamera();
        cam.setToOrtho(false);

        //setup the amount of button spacing (in pixels)
        spacing = 5;

        create();


        /*
        TicTacToe game stuff
         */

         //runs the setup of a tictactoe game
         setup();

    }


    /*
    Setup for tictactoe game
     */
    public void setup(){

        //create the gameboard and trackers for the gameboard
        gameOver = false;
        gameboard = new String[3][3];
        tracker = new ArrayList();
        touchTracker = new ArrayList();

        //fill touchTracker with initial values
        for (int i = 0; i < 9; i++) {
            touchTracker.add(false);
        }

        //initially set the first player's move to "X"
        xorO = true;

        //fill the gameboard and its tracker with initial values
        fillGameboard(gameboard);
        fillTracker(tracker);

        //print the current initial gameboard
        //printGameboard(gameboard);

    }



    public void create(){

        skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(GRAY);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));


        //setup texture for fonts
        texture = new Texture(Gdx.files.internal("ariel.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texture60 = new Texture(Gdx.files.internal("arielsize60.png"));
        texture60.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        // Store the default libgdx font under the name "default".
        bfont = new BitmapFont(Gdx.files.internal("ariel.fnt"), new TextureRegion(texture), false);
        //bfont.scale(0.9f);
        skin.add("default", bfont);


        bfontLarge = new BitmapFont(Gdx.files.internal("arielsize60.fnt"), new TextureRegion(texture60), false);
        bfontLarge.scale(1f);
        skin.add("defaultLarge", bfontLarge);


        //setup a textButtonStyle used to the player
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", GRAY);
        textButtonStyle.over = skin.newDrawable("white", LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);


        //setup a TextButtonStyle for the bot aka textButtonStyleBot
        textButtonStyleBot = new TextButton.TextButtonStyle();
        textButtonStyleBot.up = skin.newDrawable("white", BLUE);
        textButtonStyleBot.down = skin.newDrawable("white", BLUE);
        textButtonStyleBot.checked = skin.newDrawable("white", BLUE);
        textButtonStyleBot.over = skin.newDrawable("white", BLUE);
        textButtonStyleBot.font = skin.getFont("defaultLarge");
        skin.add("defaultBot", textButtonStyleBot);


        //setup a bigger textButtonStyle60
        textButtonStyle60 = new TextButton.TextButtonStyle();
        textButtonStyle60.up = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle60.down = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle60.checked = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle60.over = skin.newDrawable("white", LIGHT_GRAY);
        textButtonStyle60.font = skin.getFont("defaultLarge");
        skin.add("defaultLarge", textButtonStyle60);





        //setup viewport to be used by the stage, add viewport to stage
        viewport = new ScreenViewport();
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        stage.getViewport().setCamera(cam);



        //add buttons as Actors to the Stage stage, aka create the button gameboard
        for(int i = 0; i < 9; i++){
            stage.addActor(new TextButton("", textButtonStyle));
        }


        //draw the tictactoe board
        drawButtons();

        //add listeners to board so they react to touch
        addListenersToButtons(stage);


    }//end method create



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }



    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();


        batch.begin();

        batch.end();


        /*
        Android input and keyboard input - check if back button is pressed or backspace key is pressed,
        if it is then go back to main menu screen
         */
        if(Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
            dispose();
            mainMenuGame.setScreen(mainMenuGame.mainMenuScreen);
        }


    }//end render method




    /*
    Fills the initial tracker board with "0"s
    */
    public static void fillTracker(List tracker){
        for(int i = 0; i < 9; i++){
            tracker.add(i);
        }
    }//end method fillTracker




    /*
    Fills the initial gameboard with "."s
    */
    public static void fillGameboard(String[][] gameboard){
        for(int i = 0; i < gameboard.length; i++){
            for(int j = 0; j < gameboard.length; j++){
                gameboard[i][j] = ".";
            }
        }
    }//end method fillGameboard


    /*
    Prints the current gameboard to console
    */
    public static void printGameboard(String[][] gameboard){
        System.out.println("Current gameboard:");

        for(int i = 0; i < gameboard.length; i++){
            for(int j = 0; j < gameboard.length; j++){
                System.out.print(gameboard[i][j]);
                System.out.print("\t");
            }
            System.out.println("");
        }
        System.out.println("\n");
    }//end method printGameboard



    /*
    Method that draws the tic tac toe board as buttons
    Used in the render() method
     */
    public void drawButtons(){

        float buttonWidth = (cam.viewportWidth / 3);
        //float buttonHeight = (cam.viewportHeight / 3);
        float buttonHeight = ((cam.viewportHeight / (float) 1.66666) / 3);

        float newButtonWidth = buttonWidth - spacing;
        float newButtonHeight = buttonHeight - spacing;

        int counter = 0;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                TextButton button = (TextButton) stage.getActors().get(counter);
                button.setStyle(textButtonStyle60);
                counter++;

                //button.setText(i + ", " + j);
                button.setX(j * buttonWidth);
                //button.setY(2 * buttonHeight - i * buttonHeight);
                button.setY((float) ((2 * buttonHeight - i * buttonHeight) + Gdx.graphics.getHeight() / 5));


                //make sure the last right side of buttons fills the screen instead of having a spacing gap
                if(j == 2){
                    button.setWidth(buttonWidth);
                }else {
                    button.setWidth(newButtonWidth);
                }

                button.setHeight(newButtonHeight);
                button.setChecked(false);
            }//end for j
        }//end for i

    }//end method drawButtons



    /*
    Method adds listeners to buttons on stage
     */
    public void addListenersToButtons(Stage stage){

        for(int i = 0; i < stage.getActors().size; i++) {

            final TextButton button2 = (TextButton) stage.getActors().get(i);

            button2.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                    //checks if game mode is player vs CPU or player vs player, then sets
                    //goes through the gameplay method for the touched button
                    if(playerVsCPUMode) {
                        playGameVsCPU(button2);
                    }else{
                        playGameVsPlayer(button2);
                    }

                    return true;
                }
            });
        }//end for i
    }//end method addListenersToButtons



    public void printTouchTracker(){
        System.out.println("Printing touchTracker: ");
        for(int i = 0; i < touchTracker.size(); i++){
            System.out.print(touchTracker.get(i) + ", ");
        }
        System.out.println();
    }//end method printTouchTracker




    @Override
    public void resize(int width, int height) {


    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        bfont.dispose();

    }



    /*
    Method handles playing a player's move and playing the bot's move
    ***Specifically for player vs CPU game mode***
    Takes TextButton button2 variable which is the button that the player touched
     */
    public void playGameVsCPU(TextButton button2){


        int[] xy2 = intToCoordinate(button2.getZIndex());

        if(!spotTaken(gameboard, xy2[0], xy2[1])) {
            button2.setText("X");
            button2.setChecked(true);


            //plays player's move
            touchTracker.set(button2.getZIndex(), true);
            tracker.remove(Integer.valueOf(button2.getZIndex()));

            //add player mark to board
            int[] xy = intToCoordinate(button2.getZIndex());
            gameboard[xy[0]][xy[1]] = "X";

            /*
            Debug print out on console stuff
             */
            //print touch tracker list
            //printTouchTracker();

            //print the gameboard
            //printGameboard(gameboard);


            //find out if gameover aka if someone won
            gameOver = gameOver(gameboard);

            //if game isn't over, let the bot play his turn
            if (!gameOver) {
                getBotMove(gameboard, tracker);
                //printGameboard(gameboard);
                gameOver = gameOver(gameboard);
            }

        }else{
            //the spot is already taken so do nothing

        }//end checking if spot is already taken if-else

    }//end method playGameVsCPU


    /*
    Method gets and returns the player's button text ("X" or "O") then sets the
    boolean tracker xorO to the opposite boolean value, setting the variable xorO
    up for the next move for the next player's turn
     */
    public String getPlayerXorO(){

        //if xorO is true (initially it is) then the player's move is X, if not then it's O
        if(xorO){
            this.xorO = false;
            return "X";
        }else{
            this.xorO = true;
            return "O";
        }
    }//end method getPlayerXorO()



    /*
    Method handles playing a player's move
    ***Specifically for the player vs player game mode***
    Takes TextButton button2 variable which is the button that the player touched
    */
    public void playGameVsPlayer(TextButton button2){

        int[] xy2 = intToCoordinate(button2.getZIndex());

        if(!spotTaken(gameboard, xy2[0], xy2[1])) {

            //store "X" or "O" into temp variable
            String temp = getPlayerXorO();

            //sets the button's text ("X" or "O") to the appropriate player's text
            button2.setText(temp);

            //changes the button style for player 2's touched buttons (makes them blue)
            if(temp == "O") {
                button2.setStyle(textButtonStyleBot);
            }


            //plays player's move
            touchTracker.set(button2.getZIndex(), true);
            tracker.remove(Integer.valueOf(button2.getZIndex()));

            //add player mark to board
            int[] xy = intToCoordinate(button2.getZIndex());
            gameboard[xy[0]][xy[1]] = temp;

            /*
            Debug print out on console stuff
             */
            //print touch tracker list
            //printTouchTracker();

            //print the gameboard
            //printGameboard(gameboard);



            //find out if gameover aka if someone won
            gameOver = gameOver(gameboard);

        }else{
            //the spot is already taken so do nothing

        }//end check if spot is already taken if-else

    }//end method playGameVsPlayer





    /*
    Plays a move for the bot
    ***Specifically for player vs CPU game mode***
    */
    public void getBotMove(String[][] gameboard, List tracker){

        int x = 0;
        int y = 0;
        int[] xy = new int[2];
        int[] temp = {-1, -1};
        int[] temp2 = new int[2];
        int[] temp3 = new int[2];

        System.out.println("Bot's turn");

        //set temp = {x, y} of where the winning move is that could be played
        temp2 = playerGoingToWin(gameboard, "X", "O");
        temp3 = playerGoingToWin(gameboard, "O", "X");


        //check if bot could win first. If the bot can win first, let the bot win
        if(!(temp[0] == temp3[0] && temp[1] == temp3[1])){

            x = temp3[0];
            y = temp3[1];



            //get index spot to remove gameboard spot from touch tracker
            int touchTrackerIndexSpot = coordinateToInt(x, y);

            //put down bot's move onto gameboard
            gameboard[x][y] = "O";
            TextButton button = (TextButton) stage.getActors().get(touchTrackerIndexSpot);
            button.setText("O");
            button.setChecked(true);
            button.setStyle(textButtonStyleBot);

            int gameboardSpot = coordinateToInt(x, y);


            //remove bot's spot from available spot list
            tracker.remove(Integer.valueOf(gameboardSpot));
            touchTracker.set(touchTrackerIndexSpot, true);


            //plays player's move
            //touchTracker.set(button2.getZIndex(), true);
            //tracker.remove(Integer.valueOf(button2.getZIndex()));







            //check if player could win first and if bot could block the player's next move
        }else if(!(temp[0] == temp2[0] && temp[1] == temp2[1])){

            x = temp2[0];
            y = temp2[1];


            //get index spot to remove gameboard spot from touch tracker
            int touchTrackerIndexSpot = coordinateToInt(x, y);


            //put down bot's move onto gameboard
            gameboard[x][y] = "O";
            TextButton button = (TextButton) stage.getActors().get(touchTrackerIndexSpot);
            button.setText("O");
            button.setChecked(true);
            button.setStyle(textButtonStyleBot);

            int gameboardSpot = coordinateToInt(x, y);

            //remove bot's spot from available spot list
            tracker.remove(Integer.valueOf(gameboardSpot));

            //remove bot's spot from available spot list
            touchTracker.set(touchTrackerIndexSpot, true);

        }else{
            //else play a random spot that's available for the bot

            Random r = new Random();

            //pick random value from availble spot list
            Object randomValue = tracker.get(r.nextInt(tracker.size()));

            //get gameboard coordinates from random value on available spot list
            xy = intToCoordinate((Integer) randomValue);

            x = xy[0];
            y = xy[1];


            //get index spot to remove gameboard spot from touch tracker
            int touchTrackerIndexSpot = coordinateToInt(x, y);


            //put down bot's move onto gameboard
            gameboard[x][y] = "O";
            TextButton button = (TextButton) stage.getActors().get(touchTrackerIndexSpot);
            button.setText("O");
            button.setChecked(true);
            button.setStyle(textButtonStyleBot);

            //remove bot's spot from available spot list
            tracker.remove(Integer.valueOf(((Integer) randomValue).intValue()));
            touchTracker.set(touchTrackerIndexSpot, true);

        }//end if-else statement
    }//end method getBotMove




    /*
    Checks if a spot is already 'taken' or is already marked
    */
    public boolean spotTaken(String[][] gameboard, int x, int y){

        //check if spot is taken
        if(gameboard[x][y] != "." || touchTracker.get(coordinateToInt(x, y)).equals(true)){
            return true;
        }else{
            return false;
        }
    }//end method spotTaken




    /*
    Checks if someone is going to win the next round
    String a = X or O
    String b = X or O
    Example: String a = X, String b = O
    Returns 1D int array with {-1, -1} if player isn't going to win, or {x, y} of winning spot for the next round
    */
    public static int[] playerGoingToWin(String[][] gameboard, String a, String b){

        int[] xy = {-1, -1};
        int[] temp = new int[2];

        temp = goingToWinVertically(gameboard, a, b);

        for(int i = 0; i < 1; i++){
            if(temp[0] != -1 && temp[1] != -1){
                System.out.println("someone is going to win");
                xy[0] = temp[0];
                xy[1] = temp[1];
                break;
            }


            temp = goingToWinHorizontally(gameboard, a, b);

            if(temp[0] != -1 && temp[1] != -1){
                System.out.println("someone is going to win");
                xy[0] = temp[0];
                xy[1] = temp[1];
                break;
            }


            temp = goingToWinDiagonally(gameboard, a, b);

            if(temp[0] != -1 && temp[1] != -1){
                System.out.println("someone is going to win");
                xy[0] = temp[0];
                xy[1] = temp[1];
                break;
            }

        }//end for loop

        return xy;
    }//end method playerGoingToWin



    /*
    Checks if someone is going to win the next round vertically
    String a = X or O
    String b = X or O
    Example: String a = X, String b = O
    Returns 1D int array with {-1, -1} if player isn't going to win, or {x, y} of winning spot for the next round
    */
    public static int[] goingToWinVertically(String[][] gameboard, String a, String b){

        int[] c = new int[2];

        for(int i = 0; i < 3; i++){
            //create boolean arrays to check if one row/column is completely played on to ignore 'going to win'
            boolean[] checkRowFor3X = {false, false, false};



            //check if board spots are filled by Xs or Os
            int yesX = 0;
            int yesO = 0;

            if(gameboard[0][i].equals(a)){
                yesX++;
                checkRowFor3X[0] = true;
            }

            if(gameboard[1][i].equals(a)){
                yesX++;
                checkRowFor3X[1] = true;
            }

            if(gameboard[2][i].equals(a)){
                yesX++;
                checkRowFor3X[2] = true;
            }

            //check if O is placed in the same row
            if(gameboard[0][i].equals(b)){
                yesO++;
            }

            if(gameboard[1][i].equals(b)){
                yesO++;
            }

            if(gameboard[2][i].equals(b)){
                yesO++;
            }


            //if there are exactly 2 Xs in a row, return the coordinates, if not set coordinates to -1, -1
            if(yesX == 2 && yesO < 1){
                for(int k = 0; k < 3; k++){
                    if(checkRowFor3X[k] == false){
                        c[0] = k;
                        c[1] = i;
                    }
                }
            }else{
                c[0] = -1;
                c[1] = -1;
            }

            if(yesX == 2 && yesO < 1){
                break;
            }
        }
        return c;
    }//end method goingToWinVertically()





    /*
   Checks if someone is going to win the next round horizontally
   String a = X or O
   String b = X or O
   Example: String a = X, String b = O
   Returns 1D int array with {-1, -1} if player isn't going to win, or {x, y} of winning spot for the next round
   */
    public static int[] goingToWinHorizontally(String[][] gameboard, String a, String b){

        int[] c = new int[2];

        //create boolean arrays to check if one row/column is completely played on to ignore 'going to win'
        boolean[] checkRowFor3X = {false, false, false};


        //check if board spots are filled by Xs or Os
        for(int i = 0; i < 3; i++){

            int yesX = 0;
            int yesO = 0;


            if(gameboard[i][0].equals(a)){
                yesX++;
                checkRowFor3X[0] = true;
            }

            if(gameboard[i][1].equals(a)){
                yesX++;
                checkRowFor3X[1] = true;
            }

            if(gameboard[i][2].equals(a)){
                yesX++;
                checkRowFor3X[2] = true;
            }


            //check if O is placed in the same row
            if(gameboard[i][0].equals(b)){
                yesO++;
            }

            if(gameboard[i][1].equals(b)){
                yesO++;
            }

            if(gameboard[i][2].equals(b)){
                yesO++;
            }


            //if there are exactly 2 Xs in a row, return the coordinates, if not set coordinates to -1, -1
            if(yesX == 2 && yesO < 1){
                for(int k = 0; k < 3; k++){
                    if(checkRowFor3X[k] == false){
                        c[0] = i;
                        c[1] = k;
                    }
                }
            }else{
                c[0] = -1;
                c[1] = -1;
            }


            if(yesX == 2 && yesO < 1){
                break;
            }
        }
        return c;
    }//end method goingToWinHorizontally()




    /*
   Checks if someone is going to win the next round diagonally
   String a = X or O
   Returns 1D int array with {-1, -1} if player isn't going to win, or {x, y} of winning spot for the next round
   */
    public static int[] goingToWinDiagonally(String[][] gameboard, String a, String b){

        int[] c = new int[2];

        //create boolean arrays to check if one row/column is completely played on to ignore 'going to win'
        boolean[] checkRowFor3X = {false, false, false};


        //check if board spots are filled by Xs or Os
        for(int i = 0; i < 2; i++){

            int yesX = 0;
            int yesO = 0;

            //check String a spots
            if(gameboard[0][0].equals(a) && i == 0){
                yesX++;
                checkRowFor3X[0] = true;
            }

            if(gameboard[1][1].equals(a)){
                yesX++;
                checkRowFor3X[1] = true;
            }

            if(gameboard[2][2].equals(a) && i == 0){
                yesX++;
                checkRowFor3X[2] = true;
            }

            if(gameboard[0][2].equals(a) && i == 1){
                yesX++;
                checkRowFor3X[0] = true;
            }

            if(gameboard[2][0].equals(a) && i == 1){
                yesX++;
                checkRowFor3X[2] = true;
            }


            //check String b spots
            if(gameboard[0][0].equals(b) && i == 0){
                yesO++;
            }

            if(gameboard[1][1].equals(b)){
                yesO++;
            }

            if(gameboard[2][2].equals(b) && i == 0){
                yesO++;
            }

            if(gameboard[0][2].equals(b) && i == 1){
                yesO++;
            }

            if(gameboard[2][0].equals(b) && i == 1){
                yesO++;
            }


            //if there are exactly 2 Xs in a row, return the coordinates, if not set coordinates to -1, -1
            if(yesX == 2 && yesO < 1){
                for(int k = 0; k < 3; k++){
                    if(checkRowFor3X[k] == false && i == 0){
                        c[0] = k;
                        c[1] = k;
                    }else if(checkRowFor3X[k] == false && i == 1 && k == 1){
                        c[0] = k;
                        c[1] = k;
                    }else if(checkRowFor3X[k] == false && i == 1 && k == 0){
                        c[0] = 0;
                        c[1] = 2;
                    }else if(checkRowFor3X[k] == false && i == 1 && k == 2){
                        c[0] = 2;
                        c[1] = 0;
                    }
                }
            }else{
                c[0] = -1;
                c[1] = -1;
            }


            if(yesX == 2 && yesO < 1){
                break;
            }
        }
        return c;
    }//end method goingToWinDiagonally






    /*
    Returns a boolean value based if someone won the game
    */
    public boolean gameOver(String[][] gameboard){

        boolean won = false;

        //create WindowStyle so Dialog dialog can run in method gameOver()
        Window.WindowStyle ws = new Window.WindowStyle();
        ws.titleFont = bfont;
        skin.add("dialog", ws);

        dialog = new Dialog(" ", skin, "dialog"){
            public void result(Object obj){
                Boolean result = (Boolean) obj;
                if(result){
                    //player wants to play again
                    dialog.hide();
                    dialog.cancel();
                    dispose();
                    mainMenuGame.setScreen(new TTTScreen(mainMenuGame));
                }else{
                    //player wants to quit
                    mainMenuGame.setScreen(mainMenuGame.mainMenuScreen);
                }
            }
        };




        //check if player won
        if(wonVertically(gameboard, "X") || wonHorizontally(gameboard, "X") || wonDiagonally(gameboard, "X")){
            won = true;
            System.out.println("Player wins!\n");

            //setup dialog text and buttons when the player/player1 wins
            if(playerVsCPUMode) {
                dialog.text("Player wins! Do you want to play again?", new Label.LabelStyle(bfont, WHITE));
            }else{
                dialog.text("Player X wins! Do you want to play again?", new Label.LabelStyle(bfont, WHITE));
            }
            dialog.button("Yes", true);
            dialog.button("No", false);
            dialog.show(stage);
        }

        //check if bot won
        if(wonVertically(gameboard, "O") || wonHorizontally(gameboard, "O") || wonDiagonally(gameboard, "O")){
            won = true;
            System.out.println("Bot won!\n");

            //setup dialog text and buttons when the bot or player 2 wins
            if(playerVsCPUMode) {
                dialog.text("Bot wins! Do you want to play again?", new Label.LabelStyle(bfont, WHITE));
            }else{
                dialog.text("Player O wins! Do you want to play again?", new Label.LabelStyle(bfont, WHITE));
            }
            dialog.button("Yes", true);
            dialog.button("No", false);
            dialog.show(stage);
        }

        //check if game was tied
        if(tracker.size() == 0 && won == false){
            won = true;
            System.out.println("Game tied!\n");

            //setup dialog text and buttons when there's a tie
            dialog.text("Game tied! Do you want to play again?", new Label.LabelStyle(bfont, WHITE));
            dialog.button("Yes", true);
            dialog.button("No", false);
            dialog.show(stage);
        }

        return won;
    }//end method gameOver



    /*
    Checks if String s (X or O) won diagonally, returns boolean where true = s won
    */
    public static boolean wonDiagonally(String[][] gameboard, String s){

        boolean won = false;

        //check if board spots are filled by String s
        for(int i = 0; i < 2; i++){

            int yes = 0;

            if(gameboard[0][0].equals(s) && i == 0){
                yes++;
            }

            if(gameboard[1][1].equals(s)){
                yes++;
            }

            if(gameboard[2][2].equals(s) && i == 0){
                yes++;
            }

            if(gameboard[0][2].equals(s) && i == 1){
                yes++;
            }

            if(gameboard[2][0].equals(s) && i == 1){
                yes++;
            }


            //if there are 3 of String s in a row, set won = true
            if(yes == 3){
                won = true;
                break;
            }
        }//end for loop

        return won;
    }//end method wonDiagonally



    /*
    Checks if String s (X or O) won vertically, returns boolean where true = s won
    */
    public static boolean wonVertically(String[][] gameboard, String s){

        boolean won = false;

        //check if board spots are filled by String s
        for(int i = 0; i < 3; i++){

            int yes = 0;

            if(gameboard[0][i].equals(s)){
                yes++;
            }

            if(gameboard[1][i].equals(s)){
                yes++;
            }

            if(gameboard[2][i].equals(s)){
                yes++;
            }

            //if there are 3 of String s in a row, set won = true
            if(yes == 3){
                won = true;
                break;
            }
        }//end for loop

        return won;
    }//end method wonVertically



    /*
    Checks if String s (X or O) won horizontally, returns boolean where true = s won
    */
    public static boolean wonHorizontally(String[][] gameboard, String s){

        boolean won = false;

        //check if board spots are filled by String s
        for(int i = 0; i < 3; i++){

            int yes = 0;

            if(gameboard[i][0].equals(s)){
                yes++;
            }

            if(gameboard[i][1].equals(s)){
                yes++;
            }

            if(gameboard[i][2].equals(s)){
                yes++;
            }

            //if there are 3 of String s in a row, set won = true
            if(yes == 3){
                won = true;
                break;
            }
        }//end for loop

        return won;
    }//end method wonHorizontally





    /*
    Takes array coordinates gameboard[x][y] and returns an int of the gameboard spot
    Used for conversion of 2D int array to int gameboard 'spot' for the gameboard 'spot' List tracker

    Gameboard 'spots':
    0   1   2
    3   4   5
    6   7   8

    Examples: coordinateToInt(2, 2) --> int[2][2] --> returns 8
    coordinateToInt(0, 1) --> int[0][1] --> returns 1
    */
    public static int coordinateToInt(int x, int y){

        //setup initial variable that will be returned
        int value = 0;

        switch(x){
            case 0:
                switch(y){
                    case 0:
                        value = 0;
                        break;

                    case 1:
                        value = 1;
                        break;

                    case 2:
                        value = 2;
                        break;
                }
                break;

            case 1:
                switch(y){
                    case 0:
                        value = 3;
                        break;

                    case 1:
                        value = 4;
                        break;

                    case 2:
                        value = 5;
                        break;
                }
                break;

            case 2:
                switch(y){
                    case 0:
                        value = 6;
                        break;

                    case 1:
                        value = 7;
                        break;

                    case 2:
                        value = 8;
                        break;
                }
                break;

        }//end switch x statement

        return value;
    }//end method coordinateToInt



    /*
    Takes int of gameboard 'spot' and returns array coordinates gameboard[x][y]
    Used for conversion of int spot to 2D int array for the gameboard 'spot' List tracker

    Gameboard 'spots':
    0   1   2
    3   4   5
    6   7   8

    Examples: intToCoordinate(2) --> returns int[0][2]
    intToCoordinate(7) --> returns int[2][1]
    */
    public static int[] intToCoordinate(int value){

        //setup initial variables that will be returned
        int[] coor = new int[2];
        int x = 0;
        int y = 0;

        switch(value){
            case 0:
                x = 0;
                y = 0;
                break;

            case 1:
                x = 0;
                y = 1;
                break;

            case 2:
                x = 0;
                y = 2;
                break;

            case 3:
                x = 1;
                y = 0;
                break;

            case 4:
                x = 1;
                y = 1;
                break;

            case 5:
                x = 1;
                y = 2;
                break;

            case 6:
                x = 2;
                y = 0;
                break;

            case 7:
                x = 2;
                y = 1;
                break;

            case 8:
                x = 2;
                y = 2;
                break;

        }//end switch statement

        coor[0] = x;
        coor[1] = y;

        return coor;

    }//end method intToCoordinate



}//end class TTTScreen
