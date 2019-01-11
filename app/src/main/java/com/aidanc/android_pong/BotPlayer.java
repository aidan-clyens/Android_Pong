package com.aidanc.android_pong;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

/* BotPlayer
 * This class controls the bot player, played as one of the game blocks.
 *
 * Aidan Clyens
 */
public class BotPlayer extends BlockSprite {
    //  Tag used for debugging
    private final static String TAG = "BotPlayer";

    private final int TARGET_RANGE_THRESHOLD = 135;

    private BallSprite ball;
    private int targetX;
    private int xVelocity;

    /* BotPlayer
     * The BotPlayer constructor. Pass the current context and initial x and y coordinates
     */
    public BotPlayer(Context c, int xPos, int yPos) {
        super(c, xPos, yPos);
    }
    
    /* initializeBot
     * Pass through the used ball sprite object when the bot is initialized in the game.
     */
    public void initializeBot(BallSprite b) {
        ball = b;
    }

    /* draw
     * Draw the block sprite used by the bot player on the game board.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
    
    /* update
     * Update the block sprite used by the bot player on the game board.
     */
    @Override
    public void update() {
        //  Set the target position of the bot player block, accounting for the block dimensions
        targetX = setBallTargetPos() - BLOCK_WIDTH/2;
        
        //  Move the bot player block based on the calculated target position of the ball object
        if (ball.getYVelocity() < 0 && !isWithinTargetRange()) {
            if (getXCoord() < targetX && getXCoord() < GameView.SCREEN_WIDTH) {
                setXCoord(getXCoord() + xVelocity);
            } else if (getXCoord() > targetX && getXCoord() > 0) {
                setXCoord(getXCoord() - xVelocity);
            }
        }
    }

    /* setBallTargetPos
     * Calculate the future position of the ball object in order to set a target for the bot player.
     */
    private int setBallTargetPos() {

        int ballXCoord = ball.getXCoord();
        int ballYCoord = ball.getYCoord();
        double ballXVelocity = ball.getXVelocity();
        double ballYVelocity = ball.getYVelocity();

        double slope = ballYVelocity/ballXVelocity;

        double yIntercept = ballYCoord - slope*ballXCoord;

        return (int)((BlockSprite.BLOCK_HEIGHT - yIntercept)/slope);
    }

    /* isWithinTargetRange
     * Determine if the bot player block is within a reasonable margin of the calculated target position.
     */
    private boolean isWithinTargetRange() {

        boolean withinRange = false;
        double positionDifference = getXCoord() - targetX;

        Log.d(TAG, String.format("Difference: %f", positionDifference));

        if(Math.abs(positionDifference) < TARGET_RANGE_THRESHOLD) {
            withinRange = true;
        }

        return withinRange;
    }

    /* setVelocity
     * Set the velocity of the bot player block object.
     */
    public void setVelocity(int velocity) {
        xVelocity = velocity;
    }
}
