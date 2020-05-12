package com.toni.gamestates;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.toni.ai.WolfState;
import com.toni.entities.Player;
import com.toni.entities.Ray;
import com.toni.entities.Tree;
import com.toni.entities.Wolf;
import com.toni.managers.GameKeys;
import com.toni.managers.GameStateManager;

import java.util.ArrayList;

public class PlayState extends GameState {
    private static final int NUMB_RAYS = 1024;
    private static boolean playerVisible = false;

    private ShapeRenderer sr;       // Used to draw the polygons
    private SpriteBatch batch;      // Used to draw textures
    private Player player;          // Player
    private Wolf wolf;              // Wolf
    private Ray[] rays;             // Vision rays
    private Vector2[] collPt;       // Collision point
    private ArrayList<Tree> trees;  // Trees
    private boolean rawVis = false; // Whether to draw vision rays


    /**
     * Connects PlayState to GameStateManager
     *
     * @param gsm (GameStateManager) - Wrapper class for GameStates
     */
    public PlayState(GameStateManager gsm){
        super(gsm);
    }


    /**
     * Initialize PlayState
     */
    public void init(){
        // Set up the renderer & sprite batch
        sr = new ShapeRenderer();
        batch = new SpriteBatch();

        // Create player
        player = new Player();

        // Create tree
        trees = new ArrayList<>();
        trees.add(new Tree());

        // Create wolf
        wolf = new Wolf();

        // Initialize guard's vision rays
        rays = new Ray[NUMB_RAYS];
        for(int i = 0; i < NUMB_RAYS; i++){
            rays[i] = new Ray(wolf.pos(), (i * (6.2832f / NUMB_RAYS)));
        }
        collPt = new Vector2[4];

    }


    /**
     * Update PlayState
     *
     * @param dt (float) - Time passed since last update
     */
    public void update(float dt){
        // Handle input
        handleInput();

        // Update player
        player.update(dt);
        if(player.getHunger() == 0){
            gsm.setGameState(GameStateManager.LOST);
        }

        // Update trees
        for(Tree tree: trees) {
            tree.update(dt);
        }

        // Update wolf
        wolf.update(dt);

        playerVisible = false;
        // Update rays
        for(int i =0; i < NUMB_RAYS; i += 4){
            rays[i].update(wolf, (i * (6.28319f / NUMB_RAYS)));
            rays[i + 1].update(wolf, ((i + 1) * (6.28319f / NUMB_RAYS)));
            rays[i + 2].update(wolf, ((i + 2) * (6.28319f / NUMB_RAYS)));
            rays[i + 3].update(wolf, ((i + 3) * (6.28319f / NUMB_RAYS)));

            // check collision points
            collPt[0] = cast(rays[i]);
            collPt[1] = cast(rays[i + 1]);
            collPt[2] = cast(rays[i + 2]);
            collPt[3] = cast(rays[i + 3]);

            if(collPt[0] != null){ rays[i].setCollisionPoint(collPt[0]); }
            else { rays[i].resetCollisionPoint(); }

            if(collPt[1] != null){ rays[i + 1].setCollisionPoint(collPt[1]); }
            else { rays[i + 1].resetCollisionPoint(); }

            if(collPt[2] != null){ rays[i + 2].setCollisionPoint(collPt[2]); }
            else { rays[i + 2].resetCollisionPoint(); }

            if(collPt[3] != null){ rays[i + 3].setCollisionPoint(collPt[3]); }
            else { rays[i + 3].resetCollisionPoint(); }
        }

        checkCollision();
    }


    /**
     * Draw PlayState
     */
    public void draw(){
        if(rawVis){
            // Draw player
            player.draw(sr);
            sr.begin(ShapeRenderer.ShapeType.Line);
            // Draw wolfs vision
            for(Ray ray: rays){ ray.draw(sr); }
            sr.end();
            // Draw wolf
            wolf.draw(sr);
            // Draw trees
            for(Tree tree: trees) { tree.draw(sr); }
        } else {
            batch.begin();
            // Display player
            player.draw(batch);
            // Display wolf
            wolf.draw(batch);
            // Display tree
            for(Tree tree: trees){
                tree.draw(batch);
            }
            // Display hunger bar
            player.drawHungerBar(batch);
            batch.end();
        }
    }


    /**
     * Handle user input
     */
    public void handleInput(){
        player.setLeft(GameKeys.isDown(GameKeys.LEFT));
        player.setRight(GameKeys.isDown(GameKeys.RIGHT));
        player.setUp(GameKeys.isDown(GameKeys.UP));
        player.setDown(GameKeys.isDown(GameKeys.DOWN));

        // Space toggle's between polygon and graphics
        if(GameKeys.isPressed(GameKeys.SPACE)){
            toggleVision();
        }
    }


    public void checkCollision(){
        // Player-wolf collision
        if(wolf.intersects(player) || player.intersects(wolf)){
            gsm.setGameState(GameStateManager.LOST);
        }

        // Player-tree collision
        for(int i = 0; i < trees.size(); i++){
            Tree t = trees.get(i);
            if(t.isEmpty()) continue;

            // Check tree collision
            if(player.intersects(t) || t.intersects(player)){
                if(trees.size() > 250){ gsm.setGameState(GameStateManager.WON); }

                player.refuel();
                t.empty();
                trees.add(new Tree());
            }
        }
    }


    /**
     * Safely close PlayState
     */
    public void dispose(){
        player.dispose();
        for(Tree tree: trees) { tree.dispose(); }
        wolf.dispose();
    }


    /**
     * Check to see if the ray intersects with a GameObject
     *
     * @param ray (Ray) - Ray being checked for line intersection
     * @return Coordinate's of an intersection, or null if no intersection
     */
    public Vector2 cast(Ray ray){
        Vector2 closestPt = null; // Return vector
        Vector2 collPt;           // If a collision occurs
        Vector2 p1, p2;           // line endpoint coordinates
        float t, u;               // t and u value used in line-line intersection
        float x1, x2;             // Coordinates of the wall
        float y1, y2;             // Coordinates of the wall

        // Check the ray against each wall
        for(Tree tree: trees) {

            for (int i = 0, j = tree.getShapex().length - 1; i < tree.getShapex().length; j = i++) {
                p1 = new Vector2(tree.getShapex()[i], tree.getShapey()[i]);
                p2 = new Vector2(tree.getShapex()[j], tree.getShapey()[j]);
                t = getT(p1, p2, ray);
                u = getU(p1, p2, ray);

                // return a vector if a wall collision occurs
                if ((t > 0 && t < 1) && (u > 0 && u < 1)) {
                    x1 = p1.x;
                    x2 = p2.x;
                    y1 = p1.y;
                    y2 = p2.y;
                    collPt = new Vector2(x1 + (t * (x2 - x1)), y1 + (t * (y2 - y1)));

                    // update closestPt
                    if (closestPt == null) {
                        closestPt = collPt;
                    }

                    if (ray.getPos().dst(collPt) <= ray.getPos().dst(closestPt)) {
                        closestPt = collPt;
                    }
                }
            }
        }

        // Check the ray against the player
        for(int i = 0, j = player.getShapex().length - 1; i < player.getShapex().length; j = i++){
            p1 = new Vector2(player.getShapex()[i], player.getShapey()[i]);
            p2 = new Vector2(player.getShapex()[j], player.getShapey()[j]);

            t = getT(p1, p2, ray);
            u = getU(p1, p2, ray);

            // return a vector if a wall collision occurs
            if((t > 0 && t < 1) && (u > 0 && u < 1)){
                x1 = p1.x;
                x2 = p2.x;
                y1 = p1.y;
                y2 = p2.y;
                collPt = new Vector2(x1 + (t * (x2 - x1)), y1 + (t * (y2 - y1)));

                // update closestPt
                if(closestPt == null){
                    closestPt = collPt;
                    playerVisible = true;
                    wolf.stateMachine.changeState(WolfState.CHASE);
                    WolfState.setPlayerPos(player.pos());
                }

                if(ray.getPos().dst(collPt) < ray.getPos().dst(closestPt)){
                    closestPt = collPt;
                    playerVisible = true;
                    wolf.stateMachine.changeState(WolfState.CHASE);
                    WolfState.setPlayerPos(player.pos());
                }
            }
        }

        return closestPt;
    }


    /**
     * Return t-value used in line-line intersection
     *
     * @param p1 (Vector2) - First xy-coordinate of line
     * @param p2 (Vector2) - Second xy-coordinate of line
     * @param ray (Ray) - Ray being checked for line intersection
     * @return (float) - t
     */
    public float getT(Vector2 p1, Vector2 p2, Ray ray){
        float x1 = p1.x, x2 = p2.x, x3 = ray.getPos().x, x4 = ray.getPos().x + ray.getAngle().x;
        float y1 = p1.y, y2 = p2.y, y3 = ray.getPos().y, y4 = ray.getPos().y + ray.getAngle().y;
        float numerator = ((x1 - x3) * (y3 - y4)) - ((y1 - y3) * (x3 - x4));
        float denominator = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));

        // Denominator is 0 check
        if(denominator < .0001f && denominator > -.0001f){ return Float.MAX_VALUE; }

        return numerator/denominator;
    }


    /**
     * Return u-value in line-line intersection
     *
     * @param p1 (Vector2) - First xy-coordinate of line
     * @param p2 (Vector2) - Second xy-coordinate of line
     * @param ray (Ray) - Ray being checked for line intersection
     * @return (float) - u
     */
    public float getU(Vector2 p1, Vector2 p2, Ray ray){
        float x1 = p1.x, x2 = p2.x, x3 = ray.getPos().x, x4 = ray.getPos().x + ray.getAngle().x;
        float y1 = p1.y, y2 = p2.y, y3 = ray.getPos().y, y4 = ray.getPos().y + ray.getAngle().y;
        float numerator = ((x1 - x2) * (y1 - y3)) - ((y1 - y2) * (x1 - x3));
        float denominator = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));

        // Denominator is 0 check
        if(denominator < .0001f && denominator > -.0001f){ return Float.MAX_VALUE; }

        return -1f * (numerator/denominator);
    }


    /**
     * Toggle vision boolean flag between true and false
     */
    public void toggleVision(){
        if(rawVis){
            rawVis = false;
        } else {
            rawVis = true;
        }
    }


    /**
     * Check if the player's visible to the guard
     *
     * @return (boolean) - true if visible, false if not visible
     */
    public static boolean getPlayerVisible(){ return playerVisible;}
}