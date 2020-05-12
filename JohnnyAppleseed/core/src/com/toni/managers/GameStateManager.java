package com.toni.managers;

import com.toni.gamestates.GameLostState;
import com.toni.gamestates.GameState;
import com.toni.gamestates.GameWonState;
import com.toni.gamestates.PlayState;

public class GameStateManager {

    public static final int PLAY = 0;
    public static final int LOST = 1;
    public static final int WON  = 2;

    private GameState gameState;      // Current game state


    /**
     * Game state initializer
     */
    public GameStateManager(){
        setGameState(PLAY);
    }


    /**
     * Set the GameState
     *
     * @param state (int) - An integer representing the GameState to switch to
     */
    public void setGameState(int state){
        if(gameState != null) gameState.dispose();  // Dispose the current GameState

        if(state == PLAY){
            // Switch to play state
            gameState = new PlayState(this);
        }

        if(state == LOST){
            // Switch to lost state
            gameState = new GameLostState(this);
        }

        if(state == WON){
            // Switch to won state
            gameState = new GameWonState(this);
        }
    }


    /**
     * Update current GameState
     *
     * @param dt (float) - Time passed since the last update
     */
    public void update(float dt){
        if(dt > .7){
            return;
        }
        gameState.update(dt);
    }


    /**
     * Draw current GameState
     */
    public void draw(){
        gameState.draw();
    }
}