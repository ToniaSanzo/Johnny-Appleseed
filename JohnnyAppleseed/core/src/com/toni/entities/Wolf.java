package com.toni.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.toni.Game;
import com.toni.ai.WolfState;

public class Wolf extends GameObjects {
    private static final float PI_2 = PI * 2;

    private boolean left, right, up;  // Decision flags

    private float maxSpeed;                 // Wolf's speed limit
    private float acceleration;             // How fast the player accelerates
    private float friction;                 // Friction

    // An instance of the state machine class
    public StateMachine<Wolf, WolfState> stateMachine;

    Texture tex;
    Sprite sprite;


    /**
     * Initialize wolf
     */
    public Wolf(){
        // Init state machine
        stateMachine = new DefaultStateMachine<Wolf, WolfState>(this, WolfState.PATROL);

        // Set wolf position & direction
        pos = new Vector2(Game.WIDTH - 12f, Game.HEIGHT - 12f);
        direction = new Vector2(0, 0);

        maxSpeed = 400;     // Wolf speed limit
        acceleration = 260; // Wolf acceleration
        friction = 150;     // Wolf deceleration

        // Wolf's triangle vertices
        shapex = new float[3];
        shapey = new float[3];

        // Angle & rotation speed
        radians = PI;
        rotationSpeed = 1.9f;

        // Wolf texture
        tex = new Texture(Gdx.files.internal("Wolf.png"));
        sprite = new Sprite(tex, 0, 0, 843, 259);
        sprite.setSize(65.754f,  20.202f);
        sprite.setPosition(pos.x, pos.y);
        sprite.setRotation(radians);
    }


    /**
     * Set wolf vertices
     */
    private void setShape(){
        // 0 - top vertex
        shapex[0] = pos.x + MathUtils.cos(radians) * 35;
        shapey[0] = pos.y + MathUtils.sin(radians) * 35;

        // 1 - bottom left vertex
        shapex[1] = pos.x + MathUtils.cos(radians - 11 * PI / 12) * 30;
        shapey[1] = pos.y + MathUtils.sin(radians - 11 * PI / 12) * 30;

        // 2 - Bottom right vertex
        shapex[2] = pos.x + MathUtils.cos(radians + 11 * PI / 12) * 30;
        shapey[2] = pos.y + MathUtils.sin(radians + 11 * PI / 12) * 30;
    }


    /**
     * Update the wolf's data
     *
     * @param dt (float) - Time passed since last update
     */
    public void update(float dt){
        // Set all the commands to false
        this.clearCommand();

        // stateMachine decides what flags to set
        stateMachine.update();

        // Update wolf
        if(left) radians += rotationSpeed * dt;
        if(right) radians -= rotationSpeed * dt;
        if(this.radians > PI_2) radians = radians % PI_2;
        if(this.radians < 0) radians += PI_2;

        // Accelerate
        if(up){
            direction.x += MathUtils.cos(radians) * acceleration * dt;
            direction.y += MathUtils.sin(radians) * acceleration * dt;
        }

        // Friction
        float vec = (float) Math.sqrt(direction.x * direction.x + direction.y * direction.y);
        // use friction on wolf
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


        // Prevent Wolf moving out of bounds
        boundary();
    }


    /**
     * Draw wolf polygon
     *
     * @param sr (ShapeRenderer) - Draws wolf
     */
    public void draw(ShapeRenderer sr){
        // Color - Orange-ish
        sr.setColor(.5f, .2f, 0, 1);

        // Draw wolf
        sr.begin(ShapeRenderer.ShapeType.Line);
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }

        // End sr
        sr.end();
    }


    /**
     * Draw wolf texture
     *
     * @param batch (SpriteBatch) - Display wolf texture
     */
    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }


    /**
     * Left updated to reflect argument
     *
     * @param b (boolean) - Argument value
     */
    public void setLeft(boolean b){ left = b; }


    /**
     * Right updated to reflect argument
     *
     * @param b (boolean) - Argument value
     */
    public void setRight(boolean b){ right = b; }


    /**
     * Up updated to reflect argument
     *
     * @param b (boolean) - Argument value
     */
    public void setUp(boolean b){ up = b; }


    /**
     * Set all decision flags to false
     */
    public void clearCommand(){
        up = false;
        left = false;
        right = false;
    }


    /**
     * Dispose tree
     */
    public void dispose(){
        tex.dispose();
    }
}
