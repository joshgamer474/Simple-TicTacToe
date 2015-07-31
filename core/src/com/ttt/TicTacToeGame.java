package com.ttt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
 * The actual Game class that initially runs the game
 * TicTacToeGame objects are called to switch screens from different screen classes
 */
public class TicTacToeGame extends Game {

    public TTTScreen tttScreen;
    public MainMenuScreen mainMenuScreen;
    public SettingsScreen settingsScreen;
    public AboutScreen aboutScreen;


	@Override
	public void create () {

        tttScreen = new TTTScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        aboutScreen = new AboutScreen(this);
        settingsScreen = new SettingsScreen(this);


        //set the initial settings in the settings screen
        TTTScreen.playerVsCPUMode = true;
        TTTScreen.playerVsPlayerMode = false;

        //set the starting screen to MainMenuScreen
        this.setScreen(mainMenuScreen);
	}


	@Override
	public void render () {
        super.render();
	}


}//end class TicTacToeGame
