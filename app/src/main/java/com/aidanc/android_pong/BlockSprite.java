package com.aidanc.android_pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/* BlockSprite
 * This is the sprite class for the game blocks. One block is controlled by
 * a human player and the other by a bot.
 *
 * Aidan Clyens
 */
public class BlockSprite extends Sprite {
    //  Tag used for debugging
    private final static String TAG = "BlockSprite";
    //  Get the block dimensions based from the user's device dimensions
    public final static int BLOCK_WIDTH = (int)(GameView.SCREEN_WIDTH*0.30);
    public final static int BLOCK_HEIGHT = (int)(GameView.SCREEN_HEIGHT*0.07);

    private Bitmap block;

    public boolean moving = false;
    public int offset;

    /* BlockSprite
     * The BlockSprite constructor. Pass the current context and initial x and y coordinates.
     */
    public BlockSprite(Context c, int xPos, int yPos) {
        super(c);
        //  Use the paddle bitmap to create the block sprite
        block = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.paddle), BLOCK_WIDTH, BLOCK_HEIGHT, true);

        setXCoord(xPos);
        setYCoord(yPos);
    }

    /* draw
     * Draws the block on the game board.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(block, getXCoord(), getYCoord(), null);
    }

    /* update
     * Updates the block on the game board.
     */
    public void update() {
    //  Empty
    }

    /* getSpriteWidth
     * Returns the block width as an int.
     */
    public int getSpriteWidth() {
        return BLOCK_WIDTH;
    }
    
    /* getSpriteHeight
     * Returns the block height as an int
     */
    public int getSpriteHeight() {
        return BLOCK_HEIGHT;
    }
}
