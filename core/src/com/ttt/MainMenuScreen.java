package com.ttt;

import com.badlogic.gdx.Gdx;
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


/*
This class deals with everything on the Main Menu's screen, including the Play buttons and the About button
 */
public class MainMenuScreen implements Screen {


    private Stage stage;

    private Skin skin;

    private TicTacToeGame mainMenuGame;

    private OrthographicCamera camera;


    TextButton playerVsCPUButton;
    TextButton playerVsPlayerButton;
    TextButton aboutButton;

    private Texture texture;

    //MainMenuScreen constructor
    public MainMenuScreen(TicTacToeGame mainMenuGame){

        this.mainMenuGame = mainMenuGame;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        create();
    }


    public void create(){

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
        bfont.scale(0.03f);

        skin.add("default",bfont);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", BLUE);
        textButtonStyle.over = skin.newDrawable("white", LIGHT_GRAY);

        textButtonStyle.font = skin.getFont("default");

        skin.add("default", textButtonStyle);




        //center coordinates to help place buttons on screen
        float centerX = Gdx.graphics.getWidth() / 2;
        float centerY = Gdx.graphics.getHeight() / 2;

        //create default button size measurements
        float BUTTON_WIDTH = 300f;
        float BUTTON_HEIGHT = 60f;
        float BUTTON_SPACE = 10f;


        //create main menu text to display app name
        Label introLabel = new Label("SIMPLE TICTACTOE", new Label.LabelStyle(bfont, WHITE));
        introLabel.setPosition(centerX - introLabel.getWidth() / 2, centerY + 3 * BUTTON_HEIGHT);
        stage.addActor(introLabel);


        //create buttons and labels for main menu
        playerVsCPUButton = new TextButton("Player vs CPU", skin);
        playerVsPlayerButton = new TextButton("Player vs Player", skin);
        aboutButton = new TextButton("About", skin);


        //set size of buttons
        playerVsCPUButton.setWidth(BUTTON_WIDTH);
        playerVsCPUButton.setHeight(BUTTON_HEIGHT);

        playerVsPlayerButton.setWidth(BUTTON_WIDTH);
        playerVsPlayerButton.setHeight(BUTTON_HEIGHT);

        aboutButton.setWidth(BUTTON_WIDTH);
        aboutButton.setHeight(BUTTON_HEIGHT);


        //set placement of buttons
        playerVsCPUButton.setPosition(centerX - playerVsCPUButton.getWidth() / 2, centerY + BUTTON_HEIGHT);
        playerVsPlayerButton.setPosition(playerVsCPUButton.getX(), playerVsCPUButton.getY() - (BUTTON_HEIGHT + BUTTON_SPACE));
        aboutButton.setPosition(playerVsPlayerButton.getX(), playerVsPlayerButton.getY() - (BUTTON_HEIGHT + BUTTON_SPACE));

        //add buttons to stage
        stage.addActor(playerVsCPUButton);
        stage.addActor(playerVsPlayerButton);
        stage.addActor(aboutButton);




        /*
        Setup actions for when Main Menu buttons are pressed
         */

        playerVsCPUButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerVsCPUButton.setChecked(false);
                TTTScreen.playerVsCPUMode = true;
                TTTScreen.playerVsPlayerMode = false;
                mainMenuGame.setScreen(new TTTScreen(mainMenuGame));
            }
        });

        playerVsPlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerVsPlayerButton.setChecked(false);
                TTTScreen.playerVsCPUMode = false;
                TTTScreen.playerVsPlayerMode = true;
                mainMenuGame.setScreen(new TTTScreen(mainMenuGame));
            }
        });

        //change screens when aboutButton is touched
        aboutButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                aboutButton.setChecked(false);
                mainMenuGame.setScreen(mainMenuGame.aboutScreen);
            }
        });


    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCatchBackKey(false);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();

        Gdx.input.setInputProcessor(stage);


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
