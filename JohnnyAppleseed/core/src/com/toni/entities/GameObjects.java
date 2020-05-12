package com.toni.entities;

import com.badlogic.gdx.math.Vector2;
import com.toni.Game;

public class GameObjects {
    protected static final float PI = 3.14159f;

    protected Vector2 pos;          // pos           - GameObject's xy-coordinates
    protected Vector2 direction;    // direction     - GameObject's Velocity vector
    protected float radians;        // radians       - Angle GameObject's facing
    protected float speed;          // speed         - How fast GameObject's currently traveling
    protected float rotationSpeed;  // rotationSpeed - How fast GameObject rotates
    protected int width;            // width         - width of GameObject
    protected int height;           // height        - height of GameObject
    protected float[] shapex;       // shapex        - Array of vertices
    protected float[] shapey;       // shapey        - Array of vertices

    /**
     * Prevent GameObjects from moving outside the game boundaries
     */
    protected void boundary(){
        if(pos.x < 0) pos.x = 0;
        if(pos.x > Game.WIDTH) pos.x = Game.WIDTH;
        if(pos.y < 0) pos.y = 0;
        if(pos.y > Game.HEIGHT) pos.y = Game.HEIGHT;
    }


    /**
     * Get GameObject's position
     *
     * @return (Vector2) - GameObject's xy-coordinate
     */
    public Vector2 pos(){ return pos;}


    /**
     * Get GameObjects angle
     *
     * @return (float) - GameObjects angle
     */
    public float radians(){ return radians; }


    /**
     * get shapex
     *
     * @return (float[]) - array of vertices
     */
    public float[] getShapex(){ return shapex; }


    /**
     * get shapey
     *
     * @return (float[]) - array of vertices
     */
    public float[] getShapey(){ return shapey; }


    /**
     * Check if polygons intersect using the contains() method
     *
     * @param other (GameObjects) - polygon to check against
     * @return (boolean)          - True if the polygons are intersecting
     */
    public boolean intersects(GameObjects other){
        float[] sx = other.getShapex();
        float[] sy = other.getShapey();
        for(int i = 0; i < sx.length; i++){
            if(contains(sx[i], sy[i])){ return true; }
        }
        return false;
    }


    /**
     * Check if a polygon intersects a point, using the Even-Odd Winding Rule
     *
     * @param x (float)  - x-pos
     * @param y (float)  - y-pos
     * @return (boolean) - True if point is within polygon
     */
    public boolean contains(float x, float y){
        boolean b = false;
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
            // Even-Odd winding Rule
            if((shapey[i] > y) != (shapey[j] > y) && (x < (shapex[j] - shapex[i]) * (y - shapey[i]) /
                    (shapey[j] - shapey[i]) + shapex[i])){
                b = !b;
            }
        }
        return b;
    }
}
