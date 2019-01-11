package com.aidanc.android_pong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/* MenuSprite
 * This is the game's 'pause' menu. 
 *
 * Aidan Clyens
 */
public class MenuSprite extends Sprite {
    //  Tag used for debugging
    private static final String TAG = "MenuSprite";
    //  Get the dimensions of the menu icon based on the dimensions of the device
    public final static int MENU_WIDTH = (int)(GameView.SCREEN_WIDTH*0.08);
    public final static int MENU_HEIGHT = (int)(GameView.SCREEN_WIDTH*0.08);

    private Bitmap menu;

    /* MenuSprite
     * The MenuSprite constructor. Pass the current context and x and y coordinates.
     */
    public MenuSprite(Context c, int xPos, int yPos) {
        super(c);
        //  Create the menu bitmap
        menu = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.menu), MENU_WIDTH, MENU_HEIGHT, true);
        //  Set intitial coordinates
        setXCoord(xPos);
        setYCoord(yPos);
    }

    /* draw
     * Draw the menu icon on the game board.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(menu, getXCoord(), getYCoord(), null);
    }

    /* update
     * Update the menu icon sprite on the game board.
     */
    public void update() {
        //  Empty
    }

    /* getSpriteWidth
     * Return the width of the menu sprite.
     */
    public int getSpriteWidth() {
        return MENU_WIDTH;
    }

    /* getSpriteHeight
     * Return the height of the menu sprite.
     */
    public int getSpriteHeight() {
        return MENU_HEIGHT;
    }
}
