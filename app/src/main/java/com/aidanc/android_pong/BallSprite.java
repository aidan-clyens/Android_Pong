package com.aidanc.android_pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

/**
 * Created by aidan on 10/13/2017.
 */

public class BallSprite extends Sprite {

    private final static String TAG = "BallSprite";

    public final static int BALL_WIDTH = (int)(GameView.SCREEN_WIDTH*0.05);
    public final static int BALL_HEIGHT = (int)(GameView.SCREEN_WIDTH*0.05);

    private final double MAX_BOUNCE_ANGLE = Math.toRadians(65);

    //  TODO Make top and bottom blocks more compatible with eachother
    private BotPlayer topBlock;
    private BlockSprite bottomBlock;
    private Bitmap ball;
    private GameEngine gameEngine;
    private int maxVelocity;
    private double xVelocity;
    private double yVelocity;
    private double prevXVelocity;
    private double prevYVelocity;

    private boolean paused;

    public BallSprite(Context c, BotPlayer tBlock, BlockSprite bBlock, int difficulty) {
        super(c);

        ball = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ball), BALL_WIDTH, BALL_HEIGHT, true);

        topBlock = tBlock;
        bottomBlock = bBlock;

        setDifficulty(difficulty);
    }

    public void initialize(GameEngine gEngine) {
        gameEngine = gEngine;
        resetPosition();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(ball, getXCoord(), getYCoord(), null);
    }

    public void update() {
        //  Move ball at set velocity
        setXCoord(getXCoord() + (int)xVelocity);
        setYCoord(getYCoord() + (int)yVelocity);

        //  Ball collides with side walls
        if (getXCoord() <= 0 || getXCoord() >= GameView.SCREEN_WIDTH - BALL_WIDTH) {
            xVelocity *= -1;
        }
        //  Ball collides with top or bottom walls
        if (getYCoord() <= 0) {
            gameEngine.playerPointGained();
            resetPosition();

        } else if(getYCoord() >= GameView.SCREEN_HEIGHT - BALL_HEIGHT) {
            gameEngine.botPointGained();
            resetPosition();
        }
        //  Ball collides with top or bottom blocks
        collision();
    }

    private boolean collision() {
        //  TODO Add side collision on blocks

        boolean collides = false;

        //  Top block
        if(getYCoord() <= BlockSprite.BLOCK_HEIGHT){
            //  Bottom collision
            if(getXCoord() + BALL_WIDTH >= topBlock.getXCoord() && getXCoord() <= (topBlock.getXCoord() + BlockSprite.BLOCK_WIDTH)) {

                Log.d(TAG, "Collides");

                //  Move ball down 10px so that a collision will not be detected again
                setYCoord(getYCoord() + 10);
                setBallVelocity(topBlock);
                collides = true;
            }
        }

        //  Bottom block
        if((getYCoord() + BALL_HEIGHT) >= (GameView.SCREEN_HEIGHT - BlockSprite.BLOCK_HEIGHT)) {
            //  Top collision
            if(getXCoord() + BALL_WIDTH >= bottomBlock.getXCoord() && getXCoord() <= (bottomBlock.getXCoord() + BlockSprite.BLOCK_WIDTH)) {

                Log.d(TAG, "Collides");

                //  Move ball up 10px so that a collision will not be detected again
                setYCoord(getYCoord() - 10);
                setBallVelocity(bottomBlock);
                collides = true;
            }
        }

        return collides;
    }

    private void resetPosition() {

        double angle;

        Random random = new Random();
        //  Set ball to center of the screen on reset
        setXCoord(GameView.SCREEN_WIDTH / 2);
        setYCoord(GameView.SCREEN_HEIGHT / 2);

        //  Generate a random angle between 0 and 359 degrees and calculate x and y velocity using that
        angle = random.nextInt(359);

        //  If angle is too close to x-axis, randomize again
        if((angle >= 80 && angle <= 290)) {
            resetPosition();
        } else {

            Log.d(TAG, "resetPosition: " + String.format("%f", angle));

            xVelocity = maxVelocity *Math.sin(Math.toRadians(angle));
            yVelocity = maxVelocity *Math.cos(Math.toRadians(angle));

            Log.d(TAG, "resetPosition: " + String.format("xVelocity: %f, yVelocity: %f", xVelocity, yVelocity));

            gameEngine.resetPosition();   //  TODO Modify resetPosition() method later
        }
    }

    private void setBallVelocity(BlockSprite block) {

        double relativeIntersect = Math.abs(getXCoord() - block.getXCoord() - (BlockSprite.BLOCK_WIDTH/2));
        double multiplier = relativeIntersect/(BlockSprite.BLOCK_WIDTH/2);
        double bounceAngle = MAX_BOUNCE_ANGLE*multiplier;

        Log.e(TAG, "RelativeIntersect: " + String.format("%f", relativeIntersect));
        Log.e(TAG, "Bounce Angle: " + String.format("%f", Math.toDegrees(bounceAngle)));

        if(yVelocity <= 0) {    //  Moving up

            if(xVelocity >= 0) {    //  Moving right

                xVelocity = maxVelocity *Math.sin(bounceAngle);

            } else if(xVelocity < 0) {  //  Moving left
                xVelocity = maxVelocity *Math.sin(bounceAngle)*-1;
            }
            yVelocity = maxVelocity *Math.cos(bounceAngle);

        } else if(yVelocity > 0) {  //  Moving down

            if(xVelocity >= 0) {    //  Moving right

                xVelocity = maxVelocity *Math.sin(bounceAngle);

            } else if(xVelocity < 0) {  //  Moving left
                xVelocity = maxVelocity *Math.sin(bounceAngle)*-1;
            }

            yVelocity = maxVelocity *Math.cos(bounceAngle)*-1;
        }
        Log.d(TAG, "xVelocity, yVelocity: " + String.format("%f, %f", xVelocity, yVelocity));
    }

    private void setDifficulty(int difficulty) {

        switch(difficulty) {

            case 0: //  Easy
                maxVelocity = 35;
                topBlock.setVelocity(8);
                break;
            case 1: //  Hard
                maxVelocity = 50;
                topBlock.setVelocity(15);
                break;
            default:
                maxVelocity = 35;
                topBlock.setVelocity(8);
                break;
        }
    }

    public void pause() {

        if(!paused) {
            paused = true;

            prevXVelocity = xVelocity;
            prevYVelocity = yVelocity;
            xVelocity = 0;
            yVelocity = 0;
        }
    }

    public void resume() {

        if(paused) {
            paused = false;

            xVelocity = prevXVelocity;
            yVelocity = prevYVelocity;
        }
    }

    public double getXVelocity() {
        return xVelocity;
    }

    public double getYVelocity() {
        return yVelocity;
    }

    public int getSpriteWidth() {
        return BALL_WIDTH;
    }

    public int getSpriteHeight() {
        return BALL_HEIGHT;
    }
}
