package com.toni.managers;

public class GameKeys {
    private static final int NUM_KEYS = 5; // Number of interactions a user can make with the game

    private static boolean [] keys     = new boolean[NUM_KEYS]; // Keys currently pressed
    private static boolean [] prevKeys = new boolean[NUM_KEYS]; // State of keys last update method

    // Key user interacted with
    public static final int UP    = 0;
    public static final int LEFT  = 1;
    public static final int DOWN  = 2;
    public static final int RIGHT = 3;
    public static final int SPACE = 4;


    /**
     * Set prevKeys to keys
     */
    public static void update(){
        for(int i = 0; i < NUM_KEYS; i++){
            prevKeys[i] = keys[i];
        }
    }


    /**
     * Set key
     *
     * @param k (int)     - Integer representation of key
     * @param b (boolean) - Value to set key to
     */
    public static void setKey(int k, boolean b){
        keys[k] = b;
    }


    /**
     * Check if a key is currently down
     *
     * @param k (int) - Integer representation of key
     * @return (boolean) - True if key is currently down, otherwise false
     */
    public static boolean isDown(int k){
        return keys[k];
    }


    /**
     * Check if a key has been pressed
     *
     * @param k (int) - Integer representation of key
     * @return (boolean) - True if key has been pressed, otherwise false
     */
    public static boolean isPressed(int k){
        return !prevKeys[k] && keys[k];
    }
}
