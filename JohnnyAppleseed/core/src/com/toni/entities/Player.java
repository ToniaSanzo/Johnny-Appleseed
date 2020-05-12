package com.toni.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.toni.Game;


public class Player extends GameObjects {
    private boolean left, up, right, down;  // User key interaction
    private boolean hit;                    // Player hit state

    private float maxSpeed;                 // Player speed limit
    private float acceleration;             // Player acceleration
    private float deceleration;             // Player deceleration
    private float friction;                 // Player friction
    private float height, width;            // Width & height of rectangle

    private int hunger;                     // The player's currentHunger
    private float hungerTime;               // Time until a hunger bar is lost
    private float hungerTimer;              // Time since last hunger bar was lost


    private Texture afTex;                  // Apple full texture
    private Sprite afSprite;                // Apple full sprite
    private Texture aeTex;                  // Apple empty texture
    private Sprite aeSprite;                // apple empty sprites
    private Texture tex;                    // Johnny Appleseed texture
    private Sprite sprite;                  // Johnny Appleseed sprite

    /**
     * Initialize the Player
     */
    public Player() {
        pos = new Vector2(12, 12); // Start position
        direction = new Vector2(0, 0);    // Start direction
        height = 41;                         // Height
        width = 9;                          // Width

        maxSpeed = 250;                        // Speed limit of player
        acceleration = 240;                        // Player acceleration
        deceleration = 210;                        // Player deceleration
        friction = 170;                        // Player friction

        radians = 1.0462f;                    // Player's angle
        rotationSpeed = 4;                          // Player rotation speed

        shapex = new float[4];               // Player's a 4 vertex rectangle
        shapey = new float[4];               // Player's a 4 vertex rectangle

        hit = false;                      // Player hit state
        hunger = 5;                          // Player hunger state
        hungerTime = 2;                          // Seconds before a hunger bar is lost
        hungerTimer = 0;                          // Time since last hunger bar was lost


        // Player texture
        tex = new Texture(Gdx.files.internal("Johnny_Appleseed.png"));
        sprite = new Sprite(tex, 0, 0, 536, 825);
        sprite.setSize(28f, 43f);
        sprite.setPosition(pos.x, pos.y);
        sprite.setRotation(radians);

        // Apple full texture
        afTex = new Texture(Gdx.files.internal("HungerBarFull.png"));
        afSprite = new Sprite(afTex, 0, 0, 348, 448);
        afSprite.setSize(55.68f, 71.68f);

        // Apple empty texture
        aeTex = new Texture(Gdx.files.internal("HungerBarEmpty.png"));
        aeSprite = new Sprite(aeTex, 0, 0, 348, 448);
        aeSprite.setSize(55.68f,  71.68f);
    }


    /**
     * Set the vertices of the player rectangle
     */
    public void setShape(){
        // MathUtils used because of the cos and sin lookup tables

        // 0 - index of the vertex at the top right of the rectangle
        shapex[0] = pos.x + (width / 2) * MathUtils.cos(radians) - (height / 2) * MathUtils.sin(radians);
        shapey[0] = pos.y + (width / 2) * MathUtils.sin(radians) + (height / 2) * MathUtils.cos(radians);

        // 1 - index of the vertex at the top left of the rectangle
        shapex[1] = pos.x - (width / 2) * MathUtils.cos(radians) - (height / 2) * MathUtils.sin(radians);
        shapey[1] = pos.y - (width / 2) * MathUtils.sin(radians) + (height / 2) * MathUtils.cos(radians);

        // 2 - index of the vertex at the bottom left of the rectangle
        shapex[2] = pos.x - (width / 2) * MathUtils.cos(radians) + (height / 2) * MathUtils.sin(radians);
        shapey[2] = pos.y - (width / 2) * MathUtils.sin(radians) - (height / 2) * MathUtils.cos(radians);

        // 3 - index of the vertex at the bottom right of the rectangle
        shapex[3] = pos.x + (width / 2) * MathUtils.cos(radians) + (height / 2) * MathUtils.sin(radians);
        shapey[3] = pos.y + (width / 2) * MathUtils.sin(radians) - (height / 2) * MathUtils.cos(radians);
    }


    /**
     * Set left flag
     *
     * @param b (boolean) - Value to set left to
     */
    public void setLeft(boolean b){ left = b; }


    /**
     * Set right flag
     *
     * @param b (boolean) - Value to set right to
     */
    public void setRight(boolean b){ right = b; }


    /**
     * Set up flag
     *
     * @param b (boolean) - Value to set up to
     */
    public void setUp(boolean b){ up = b; }


    /**
     * Set down flag
     *
     * @param b (boolean) - Value to set down to
     */
    public void setDown(boolean b){ down = b; }


    /**
     * Update the player, based on user input
     *
     * @param dt (float) - Delta time, time passed since last update
     */
    public void update(float dt){
        // Deal with hunger
        hungerTimer += dt;
        if(hungerTimer > hungerTime){
            hunger--;
            hungerTimer = 0;
        }

        // Rotate Player based on keyboard input
        if(left) { radians += rotationSpeed * dt; }
        else if(right) { radians -= rotationSpeed * dt; }

        // Acceleration & deceleration
        if(up){
            direction.x += MathUtils.cos(radians) * acceleration * dt;
            direction.y += MathUtils.sin(radians) * acceleration * dt;
        } else if(down){
            direction.x -= MathUtils.cos(radians) * deceleration * dt;
            direction.y -= MathUtils.sin(radians) * deceleration * dt;
        }

        // Friction
        float vec = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        // use friction on player
        if(vec > 0){
            direction.x -= (direction.x / vec) * friction * dt;
            direction.y -= (direction.y / vec) * friction * dt;
        }
        // Cap the player's speed
        if(vec > maxSpeed){
            direction.x = (direction.x / vec) * maxSpeed;
            direction.y = (direction.y / vec) * maxSpeed;
        }

        // Set position
        pos.x += direction.x * dt;
        pos.y += direction.y * dt;

        // Set shape
        setShape();

        // Set image position
        sprite.setOrigin(pos.x, pos.y);
        sprite.setOriginCenter();
        sprite.setRotation(180 * (radians / PI));
        sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);

        // Prevent user from moving outside of the boundaries
        boundary();
    }


    /**
     * Draw player
     *
     * @param sr (ShapeRenderer) - Draws player wireframe
     */
    public void draw(ShapeRenderer sr){
        // Set the Player object to white
        sr.setColor(1,1,1,1);

        // All the drawing happens within the sr.begin and sr.end methods
        sr.begin(ShapeRenderer.ShapeType.Line);

        // Draw player
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }

        sr.end();
    }


    /**
     * Draw player batch
     *
     * @param batch (SpriteBatch) - Display Johnny Appleseed
     */
    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }


    /**
     * Draw player hunger bar
     *
     * @param batch (SpriteBatch) - Display hunger bar
     */
    public void drawHungerBar(SpriteBatch batch){
        switch(hunger){
            case 5:
                afSprite.setPosition(7, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(69f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(131f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(191f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(253f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                break;
            case 4:
                afSprite.setPosition(7f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(69f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(131f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(191f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                aeSprite.setPosition(253f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                break;
            case 3:
                afSprite.setPosition(7f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(69f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(131f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                aeSprite.setPosition(191f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                aeSprite.setPosition(252f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                break;
            case 2:
                afSprite.setPosition(7f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                afSprite.setPosition(69f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                aeSprite.setPosition(131f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                aeSprite.setPosition(191f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                aeSprite.setPosition(253f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                break;
            case 1:
                afSprite.setPosition(7f, Game.HEIGHT - 80f);
                afSprite.draw(batch);
                aeSprite.setPosition(69f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                aeSprite.setPosition(131f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                aeSprite.setPosition(191f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                aeSprite.setPosition(253f, Game.HEIGHT - 80f);
                aeSprite.draw(batch);
                break;
        }
    }


    /**
     * Update player when hit
     */
    public void hit(){
        // Return if Player's already hit
        if(hit) return;

        // Update players state, stop player movement
        hit = true;
        direction.x = direction.y = 0;
        left = right = up = false;
    }


    /**
     * Refuel player
     */
    public void refuel(){
        hunger = 5;
        hungerTimer = 0;
    }


    /**
     * Get hunger
     *
     * @return (int) - Current hunger
     */
    public int getHunger(){ return hunger; }


    /**
     * Dispose player
     */
    public void dispose(){
        tex.dispose();
    }
}
