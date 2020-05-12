package com.toni.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Ray {
    private Vector2 pos;            // Ray's position
    private Vector2 angle;          // Ray's angle
    private Vector2 collisionPoint; // Ray collision point

    /**
     * Construct a ray using the ray's angle, and the starting position of the angle
     *
     * @param pos (Vector2)   - Vector pointing to the position the ray begins at
     * @param radians (float) - Angle the vector's pointing
     */
    public Ray(Vector2 pos, float radians){
        this.pos = pos;
        angle = new Vector2(MathUtils.cos(radians) * 512, MathUtils.sin(radians) * 512);
        collisionPoint = null;
    }


    /**
     * The ray's position is updated to match the wolf's position and angle
     *
     * @param wolf (Wolf) - Wolf this vision ray belongs too
     * @param radian (float) - Angle in radian's of this ray vector
     */
    public void update(Wolf wolf, float radian){
        this.pos.x = wolf.pos().x;
        this.pos.y = wolf.pos().y;

        if(wolf.radians < 1.0472f){
            if(radian < 1.0472f + wolf.radians || radian > 5.236f + wolf.radians){
                this.angle.x = MathUtils.cos(radian) * 516;
                this.angle.y = MathUtils.sin(radian) * 516;
            } else {
                this.angle.x = MathUtils.cos(radian) * 128;
                this.angle.y = MathUtils.sin(radian) * 128;
            }
        } else if(wolf.radians > 5.236f){
            if((wolf.radians - 1.0472f) < radian || radian < (1.0472f + wolf.radians) - 6.2832f){
                this.angle.x = MathUtils.cos(radian) * 516;
                this.angle.y = MathUtils.sin(radian) * 516;
            } else {
                this.angle.x = MathUtils.cos(radian) * 128;
                this.angle.y = MathUtils.sin(radian) * 128;
            }
        } else {
            if(radian < 1.0472f + wolf.radians && radian > wolf.radians - 1.0472f){
                this.angle.x = MathUtils.cos(radian) * 516;
                this.angle.y = MathUtils.sin(radian) * 516;
            } else {
                this.angle.x = MathUtils.cos(radian) * 128;
                this.angle.y = MathUtils.sin(radian) * 128;
            }
        }
    }


    /**
     * Draw ray
     *
     * @param sr (ShapeRenderer) - Draws ray
     */
    public void draw(ShapeRenderer sr){
        if(collisionPoint != null){
            sr.line(pos.x, pos.y, collisionPoint.x, collisionPoint.y);
        } else {
            sr.line(pos.x, pos.y, pos.x + angle.x, pos.y + angle.y);
        }
    }


    /**
     * Get ray's position
     *
     * @return (Vector2) - xy-coordinate position vector
     */
    public Vector2 getPos(){ return pos; }


    /**
     * Get ray's angle
     *
     * @return (Vector2) - xy-coordinate vision vector
     */
    public Vector2 getAngle(){ return angle; }


    /**
     * Set the collision point of the vector
     *
     * @param collisionPoint (Vector2) - Coordinate's of the collision point
     */
    public void setCollisionPoint(Vector2 collisionPoint){ this.collisionPoint = collisionPoint; }


    /**
     * Reset the collision point to null
     */
    public void resetCollisionPoint(){ this.collisionPoint = null; }
}
