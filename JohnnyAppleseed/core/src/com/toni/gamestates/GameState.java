package com.toni.gamestates;

import com.toni.managers.GameStateManager;

public abstract class GameState {

    protected GameStateManager gsm;


    /**
     * Construct GameState, set GameStateManager then initialize the current GameState
     *
     * @param gsm (GameStateManager) - Wrapper object for different GameStates
     */
    protected GameState(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }


    /**
     * Initialize GameState
     */
    public abstract void init();


    /**
     * Update is called during the render() method
     *
     * @param dt (float) - Time passed since last update
     */
    public abstract void update(float dt);


    /**
     * Draw current GameState
     */
    public abstract void draw();


    /**
     * Method for interacting with the "GameKeys"
     */
    public abstract void handleInput();


    /**
     * Dispose's current GameState
     */
    public abstract void dispose();
}
