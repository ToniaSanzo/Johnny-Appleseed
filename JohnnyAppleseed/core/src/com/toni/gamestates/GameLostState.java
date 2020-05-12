package com.toni.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.toni.Game;
import com.toni.managers.GameKeys;
import com.toni.managers.GameStateManager;

public class GameLostState extends GameState {
    SpriteBatch batch;
    BitmapFont font;
    CharSequence str = "[ SPACE ] to play again";

    /**
     * Link GameLostState to GameStateManager
     *
     * @param gsm (GameStateManager) - Wrapper for game states
     */
    public GameLostState(GameStateManager gsm){
        super(gsm);
    }


    /**
     * Initialize GameLostState
     */
    public void init(){
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("myfont.fnt"), false);
    }


    /**
     * Update GameLostState
     *
     * @param dt (float) - Time passed since last update
     */
    public void update(float dt){
        handleInput();
    }


    /**
     * Draws the GameLostState
     */
    public void draw(){
        batch.begin();
        font.draw(batch, str, Game.WIDTH / 3 + 15f , Game.HEIGHT / 2 + 35f);
        batch.end();
    }


    /**
     * Handles user input
     */
    public void handleInput(){
        if(GameKeys.isPressed(GameKeys.SPACE)){
            gsm.setGameState(GameStateManager.PLAY);
        }
    }


    /**
     * Dispose GameLostState
     */
    public void dispose(){ }
}
