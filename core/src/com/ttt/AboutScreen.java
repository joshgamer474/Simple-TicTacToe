package com.ttt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import static com.badlogic.gdx.graphics.Color.GRAY;
import static com.badlogic.gdx.graphics.Color.GREEN;
import static com.badlogic.gdx.graphics.Color.WHITE;

/**
 * This class deals with the About screen, placing text onto the screen when the About button is pressed form the
 * main menu
 */
public class AboutScreen implements Screen {

    private Stage stage;

    private Skin skin;
    private SpriteBatch batch;

    private TicTacToeGame mainMenuGame;

    private OrthographicCamera camera;

    private Texture texture;

    private Label introLabel;
    private Label aboutMeLabel;
    private Label aboutMeLabel2;
    private Label shrekIsLove;

    private boolean touched;


    //About Screen constructor
    public AboutScreen(TicTacToeGame mainMenuGame){

        this.mainMenuGame = mainMenuGame;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        create();


    }//end constructor


    public void create(){

        touched = false;

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


        //center coordinates to help place buttons on screen
        float centerX = Gdx.graphics.getWidth() / 2;
        float centerY = Gdx.graphics.getHeight() / 2;

        float BUTTON_WIDTH = 375f;
        float BUTTON_HEIGHT = 60f;
        float BUTTON_SPACE = 10f;


        //create main menu text to display app name
        introLabel = new Label("ABOUT", new Label.LabelStyle(bfont, WHITE));
        aboutMeLabel = new Label("Just a simple tic tac toe game with AI", new Label.LabelStyle(bfont, WHITE));
        aboutMeLabel2 = new Label("Developed by Josh Childers", new Label.LabelStyle(bfont, WHITE));
        shrekIsLove = new Label("Shrek is love, Shrek is life", new Label.LabelStyle(bfont, WHITE));


        //set position of labels on the screen
        introLabel.setPosition(centerX - introLabel.getWidth() / 2, centerY + 3 * BUTTON_HEIGHT);
        aboutMeLabel.setPosition(centerX - aboutMeLabel.getWidth() / 2, introLabel.getY() - 2 * BUTTON_HEIGHT);
        aboutMeLabel2.setPosition(centerX - aboutMeLabel2.getWidth() / 2, aboutMeLabel.getY() - (3/2) * BUTTON_HEIGHT);


        //add labels to stage
        stage.addActor(introLabel);
        stage.addActor(aboutMeLabel);
        stage.addActor(aboutMeLabel2);






    }




    @Override
    public void show() {
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(stage);

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






        //controls for touch of pong players
        for(int i = 0; i < 1; i++){
            if(Gdx.input.isTouched(i)){
                //make touch vector
                Vector3 touchPos = new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                camera.unproject(touchPos);

                //create rectangle for touch
                Rectangle touch = new Rectangle(touchPos.x, touchPos.y, 150, 200);

                //create general touch areas to control pong players
                Rectangle touchArea1 = new Rectangle(aboutMeLabel.getX(), aboutMeLabel.getY(), aboutMeLabel.getWidth(), aboutMeLabel.getHeight());


                //if you touch a pong player, move the pong wherever you touch (Y-axis only)
                if(touch.overlaps(touchArea1)){
                    aboutMeLabel.setText("Shrek is love, Shrek is life");
                    aboutMeLabel.setColor(GREEN);
                    aboutMeLabel.setPosition(Gdx.graphics.getWidth() / 2 - shrekIsLove.getWidth() / 2, introLabel.getY() - 2 * 60f);

                }

            }else{
                aboutMeLabel.setText("Just a simple tic tac toe game with AI");
                aboutMeLabel.setColor(WHITE);
                aboutMeLabel.setPosition(Gdx.graphics.getWidth() / 2 - aboutMeLabel.getWidth() / 2, introLabel.getY() - 2 * 60f);
            }



        }//end for i




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
