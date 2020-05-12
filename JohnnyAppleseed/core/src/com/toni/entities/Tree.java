package com.toni.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.toni.Game;

public class Tree extends GameObjects {
    private static final float ANGLE  = 0.7854f; // Angle for a side of an octagon
    private static final float RADIUS = 30f;     // radius of octagon
    private static Texture fullTex = new Texture(Gdx.files.internal("FullTree.png"));
    private static Texture emptyTex = new Texture(Gdx.files.internal("EmptyTree.png"));

    private boolean empty;     // Tree empty state
    private float emptyTimer;  // Amount of time tree's been empty
    private float emptyTime;   // Time it takes for tree to become full

    private Sprite sprite;     // Tree sprite

    /**
     * Initialize a tree
     */
    public Tree(){
        // Random xy-coordinate
        pos = new Vector2((float)Math.random() * Game.WIDTH, (float)Math.random() * Game.HEIGHT);

        shapex = new float[8];                      // Vertices of octagon
        shapey = new float[8];                      // Vertices of octagon

        // Empty state and values
        empty      = false;
        emptyTimer = 0;
        emptyTime  = 18;

        radians = (float)Math.random() * 2 * PI;    // Random angle

        sprite = new Sprite(fullTex, 0, 0, 829, 626);
        sprite.setSize(80.32f,  68.08f);
        sprite.setPosition(pos.x + 4 - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);
        sprite.setOrigin(pos.x, pos.y);
        sprite.setOriginCenter();
        sprite.setRotation(radians);

        setShape();
    }


    /**
     * Set the vertices of tree octagon
     */
    private void setShape(){
        // MathUtils used for the sin & cos lookup tables

        // vertex[0]
        shapex[0] = pos.x + RADIUS;
        shapey[0] = pos.y;

        // vertex[1]
        shapex[1] = pos.x + RADIUS * MathUtils.cos(ANGLE);
        shapey[1] = pos.y + RADIUS * MathUtils.sin(ANGLE);

        // vertex[2]
        shapex[2] = pos.x;
        shapey[2] = pos.y + RADIUS;


        // vertex[3]
        shapex[3] = pos.x + RADIUS * MathUtils.cos(3 * ANGLE);
        shapey[3] = pos.y + RADIUS * MathUtils.sin(3 * ANGLE);

        // vertex[4]
        shapex[4] = pos.x - RADIUS;
        shapey[4] = pos.y;

        // vertex[5]
        shapex[5] = pos.x - RADIUS * MathUtils.cos(ANGLE);
        shapey[5] = pos.y - RADIUS * MathUtils.sin(ANGLE);

        // vertex[6]
        shapex[6] = pos.x;
        shapey[6] = pos.y - RADIUS;

        // vertex[7]
        shapex[7] = pos.x - RADIUS * MathUtils.cos(3 * ANGLE);
        shapey[7] = pos.y - RADIUS * MathUtils.sin(3 * ANGLE);
    }


    /**
     * Update the tree
     *
     * @param dt (float) - Time passed since last update
     */
    public void update(float dt) {
        // Check if empty
        if(empty){
            emptyTimer += dt;
            if(emptyTimer > emptyTime){
                sprite = new Sprite(fullTex, 0, 0, 829, 626);
                sprite.setSize(80.32f,  68.08f);
                sprite.setPosition(pos.x + 4 - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);
                sprite.setOrigin(pos.x, pos.y);
                sprite.setOriginCenter();
                sprite.setRotation(radians);
                empty = false;
                emptyTimer = 0;
            }
        }
    }


    /**
     * Draw the tree
     *
     * @param sr (ShapeRenderer) - Used to draw octagon
     */
    public void draw(ShapeRenderer sr){
        // Set tree color to white
        sr.setColor(1, 1, 1, 1);

        // Begin sr
        sr.begin(ShapeRenderer.ShapeType.Line);

        // Draw the tree
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
            sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }

        // End sr
        sr.end();
    }


    /**
     * Draw tree texture
     *
     * @param batch (SpriteBatch) - Display tree texture
     */
    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }

    /**
     * Set tree to empty state
     */
    public void empty(){
        // return if tree's already empty
        if(empty) return;

        sprite = new Sprite(emptyTex, 0, 0, 716, 584);
        sprite.setSize(70.32f,  63.08f);
        sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);
        sprite.setRotation(0);
        empty = true;
    }


    /**
     * Check tree state
     *
     * @return (boolean) - Value of empty
     */
    public boolean isEmpty(){ return empty; }


    /**
     * Dispose tree
     */
    public void dispose(){
        //tex.dispose();
    }
}
