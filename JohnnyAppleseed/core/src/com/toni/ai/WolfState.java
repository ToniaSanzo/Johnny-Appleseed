package com.toni.ai;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.toni.Game;
import com.toni.entities.Wolf;
import com.toni.gamestates.PlayState;

public enum WolfState implements State<Wolf> {

    PATROL(){
        Vector2 target = null;
        Vector2 desired = null;
        float desiredAngle;


        /**
         * Decide which actions to take based on current GameState
         *
         * @param wolf (Wolf) - wolf object
         */
        @Override
        public void update(Wolf wolf){
            if(target == null){
                target = new Vector2((float)Math.random() * Game.WIDTH, (float)Math.random() * Game.HEIGHT);
                desired = new Vector2(0, 0);
            }
            // If wolf's on target find new target
            if(onTarget(wolf.pos(), target)){
                target.x = (float)Math.random() * Game.WIDTH;
                target.y = (float)Math.random() * Game.HEIGHT;
            }

            // Update desired vector
            desired.x = target.x - wolf.pos().x;
            desired.y = target.y - wolf.pos().y;

            // Determine rotation the wolf needs to take
            desiredAngle = desired.angleRad(new Vector2(MathUtils.cos(wolf.radians()), MathUtils.sin(wolf.radians())));


            // If the desired angle is small enough start moving towards target
            if(desiredAngle < .12 && desiredAngle > -.12){ wolf.setUp(true); }

            // Rotate in the direction of the desired vector
            if(desiredAngle < 0){
                wolf.setLeft(true);
            } else {
                wolf.setRight(true);
            }
            return;
        }
    },
    CHASE(){
        Vector2 desired = null;
        float desiredAngle;


        @Override
        public void update(Wolf wolf){
            // Decide to continue chasing
            if(!PlayState.getPlayerVisible()){
                wolf.stateMachine.changeState(PATROL);
                return;
            }
            // Desired vector
            desired = new Vector2(playerPos.x - wolf.pos().x, playerPos.y - wolf.pos().y);

            // Rotation wolf makes to align with desired vector
            desiredAngle = desired.angleRad(new Vector2(MathUtils.cos(wolf.radians()), MathUtils.sin(wolf.radians())));

            // Decide to accelerate
            if(desiredAngle < .85 && desiredAngle > -.85){ wolf.setUp(true); }

            // decide which direction to rotate
            if(desiredAngle < 0){
                wolf.setLeft(true);
            } else {
                wolf.setRight(true);
            }
        }
    };
    private static Vector2 playerPos = null;


    /**
     * True vectors are in range of each other, othewrwise false
     *
     * @return (boolean) - Whether 2 vectors are in range of one another
     */
    public static boolean onTarget(Vector2 vec1, Vector2 vec2){
        // Check if the parameters are in range with one another
        if((vec1.x > vec2.x - 15 && vec1.x < vec2.x + 15) && (vec1.y > vec2.y - 15 && vec1.y < vec2.y + 15 ))
            return true;
        return false;
    }


    // Not implemented
    @Override
    public void enter(Wolf wolf){ }


    // Not implemented
    @Override
    public void exit(Wolf wolf){}


    // Not implemented
    @Override
    public boolean onMessage(Wolf wolf, Telegram telegram){
        return false;
    }


    /**
     * Update player position
     *
     * @param pos (Vector2) - xy-coordinate of player's last seen position
     */
    public static void setPlayerPos(Vector2 pos){ playerPos = pos; }
}
