package com.ttt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import static com.badlogic.gdx.graphics.Color.*;
import static com.badlogic.gdx.graphics.Color.WHITE;

/**
 * An unused Settings class that deals with placing objects onto the Settings screen when the Settings button is pressed
 * Ended up not needing it, but I'll keep it here in case I add more features and have to implement a settings button
 * again.
 */
public class SettingsScreen implements Screen {


    private Stage stage;


    private Skin skin;

    private SpriteBatch batch;

    private TicTacToeGame mainMenuGame;


    private OrthographicCamera camera;

    private Music backgroundMusic;

    private boolean test = true;

    private Texture texture;


    //Settings screen Constructor
    public SettingsScreen(TicTacToeGame mainMenuGame){
        this.mainMenuGame = mainMenuGame;


        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        create();

    }


    public void create(){


        batch = new SpriteBatch();
        stage = new Stage();

        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin();

        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(GRAY);
        pixmap.fill();

        skin.add("white", new Texture(pixmap));



        //setup texture for font
        texture = new Texture(Gdx.files.internal("ariel.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // Store the default libgdx font under the name "default".
        BitmapFont bfont=new BitmapFont(Gdx.files.internal("ariel.fnt"), new TextureRegion(texture), false);
        bfont.scale(0.015f);

        BitmapFont bfontSmaller = new BitmapFont(Gdx.files.internal("ariel.fnt"), new TextureRegion(texture), false);
        bfontSmaller.scale(0.015f);


        skin.add("default",bfont);
        skin.add("defaultSmaller", bfontSmaller);


        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", BLUE);
        textButtonStyle.over = skin.newDrawable("white", LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);


        //create a smaller font textbutton style
        TextButton.TextButtonStyle textButtonStyle2 = new TextButton.TextButtonStyle();
        textButtonStyle2.up = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle2.down = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle2.checked = skin.newDrawable("white", BLUE);
        textButtonStyle2.over = skin.newDrawable("white", LIGHT_GRAY);
        textButtonStyle2.font = skin.getFont("defaultSmaller");
        skin.add("defaultSmaller", textButtonStyle2);


        //center coordinates to help place buttons on screen
        float centerX = Gdx.graphics.getWidth() / 2;
        float centerY = Gdx.graphics.getHeight() / 2;


        //setup standard button widths, heights, and spacing
        float BUTTON_WIDTH = 375f;
        float BUTTON_HEIGHT = 60f;
        float BUTTON_SPACE = 10f;


        //create setting text to display app name
        Label introLabel = new Label("SETTINGS", new Label.LabelStyle(bfont, WHITE));
        introLabel.setPosition(centerX - introLabel.getWidth() / 2, centerY + 3 * BUTTON_HEIGHT);
        stage.addActor(introLabel);


        //create buttons and labels for main menu
        final TextButton playerVsCPU = new TextButton("Player vs CPU", textButtonStyle2);
        final TextButton playerVsPlayer = new TextButton("Player vs Player", textButtonStyle2);
        playerVsCPU.getStyle().font.setScale((float) 0.75);
        playerVsPlayer.getStyle().font.setScale((float) 0.75);




        //set size of buttons
        playerVsCPU.setWidth((BUTTON_WIDTH / 2) - BUTTON_SPACE);
        playerVsCPU.setHeight(BUTTON_HEIGHT);

        playerVsPlayer.setWidth((BUTTON_WIDTH / 2) - BUTTON_SPACE);
        playerVsPlayer.setHeight(BUTTON_HEIGHT);


        //setup Game Mode label that'll list all the game modes
        Label gameModeLabel = new Label("Game Mode", new Label.LabelStyle(bfont, WHITE));
        gameModeLabel.setPosition(centerX - playerVsCPU.getWidth() / 2 - (centerX * (float) 0.5), centerY + BUTTON_HEIGHT * 3/2);
        //gameModeLabel.setPosition(centerX - gameModeLabel.getWidth() / 2, centerY + BUTTON_HEIGHT * 3/2);
        stage.addActor(gameModeLabel);


        //set placement of buttons
        playerVsCPU.setPosition(centerX - BUTTON_WIDTH / 2 - (centerX * (float) 0.5), gameModeLabel.getY() - (BUTTON_HEIGHT + BUTTON_SPACE));
        playerVsPlayer.setPosition(playerVsCPU.getX() + playerVsCPU.getWidth() + BUTTON_SPACE, playerVsCPU.getY());




        //add buttons to stage
        stage.addActor(playerVsCPU);
        stage.addActor(playerVsPlayer);



        //set Player vs CPU mode to checked since it's on by default
        playerVsCPU.setChecked(true);



        /*
        Setup actions for when settings screen buttons are pressed
         */

        //setup listener for player vs CPU mode button
        playerVsCPU.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if(playerVsCPU.isChecked()){
                    playerVsPlayer.setChecked(false);
                    TTTScreen.playerVsCPUMode = true;
                    TTTScreen.playerVsPlayerMode = false;
                    System.out.println("player vs CPU mode is: " + playerVsCPU.isChecked());
                    System.out.println("player vs player mode is: " + playerVsPlayer.isChecked());
                }else{
                    playerVsCPU.setChecked(false);
                }

            }
        });


        //setup listener for player vs player mode button
        playerVsPlayer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if(playerVsPlayer.isChecked()){
                    playerVsCPU.setChecked(false);
                    TTTScreen.playerVsCPUMode = false;
                    TTTScreen.playerVsPlayerMode = true;
                    System.out.println("player vs CPU mode is: " + playerVsCPU.isChecked());
                    System.out.println("player vs player mode is: " + playerVsPlayer.isChecked());
                }else{
                    playerVsPlayer.setChecked(false);
                }

            }
        });



    }//end create method


    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();
        //tex = new Texture(Gdx.files.internal())

        Gdx.input.setCatchBackKey(true);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();


        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();


        batch.begin();
        //spriteBatch.draw(tex, 0, 0);
        batch.end();



        Gdx.input.setInputProcessor(stage);

        /*
        Android input - check if back button is pressed, if pressed, go back to main menu screen
         */
        if(Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
            mainMenuGame.setScreen(mainMenuGame.mainMenuScreen);
        }

    }

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


    }









}
