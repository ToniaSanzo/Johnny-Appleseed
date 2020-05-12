package com.toni.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter {


    /**
     * Set GameKeys when key's down
     *
     * @param k (int)    - Integer representation of keyboard key
     * @return (boolean) - True
     */
    public boolean keyDown(int k){
        if(k == Input.Keys.UP){ GameKeys.setKey(GameKeys.UP, true); }

        if(k == Input.Keys.DOWN){ GameKeys.setKey(GameKeys.DOWN, true); }

        if(k == Input.Keys.LEFT){ GameKeys.setKey(GameKeys.LEFT, true); }

        if(k == Input.Keys.RIGHT){ GameKeys.setKey(GameKeys.RIGHT, true); }

        if(k == Input.Keys.SPACE){ GameKeys.setKey(GameKeys.SPACE, true); }

        return true;
    }


    /**
     * Set GameKeys when key's released
     *
     * @param k (int)    - Integer representation of keyboard key
     * @return (boolean) - True
     */
    public boolean keyUp(int k){
        if(k == Input.Keys.UP){ GameKeys.setKey(GameKeys.UP, false); }

        if(k == Input.Keys.DOWN){ GameKeys.setKey(GameKeys.DOWN, false); }

        if(k == Input.Keys.LEFT){ GameKeys.setKey(GameKeys.LEFT, false); }

        if(k == Input.Keys.RIGHT){ GameKeys.setKey(GameKeys.RIGHT, false); }

        if(k == Input.Keys.SPACE){ GameKeys.setKey(GameKeys.SPACE, false); }

        return true;
    }
}
