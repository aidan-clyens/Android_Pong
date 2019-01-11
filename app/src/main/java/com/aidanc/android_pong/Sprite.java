package com.aidanc.android_pong;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by aidan on 10/26/2017.
 */

public abstract class Sprite extends View {

    private int x, y;

    public Sprite(Context c) {
        super(c);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public abstract void update();

    public int getXCoord() {
        return x;
    }

    public int getYCoord() {
        return y;
    }

    public void setXCoord(int xCoord) {
        x = xCoord;
    }

    public void setYCoord(int yCoord) {
        y = yCoord;
    }

    public abstract int getSpriteWidth();

    public abstract int getSpriteHeight();
}
