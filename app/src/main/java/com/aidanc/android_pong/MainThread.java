package com.aidanc.android_pong;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by aidan on 10/12/2017.
 */

public class MainThread extends Thread {

    private final static String TAG = "MainThread";

    private final SurfaceHolder surfaceHolder;
    private GameEngine gameEngine;
    private GameView gameView;
    private boolean running;

    private static Canvas canvas;

    public MainThread(SurfaceHolder sHolder, GameView gView) {

        super();

        surfaceHolder = sHolder;
        gameView = gView;
    }

    public void initialize(GameEngine gEngine) {
        gameEngine = gEngine;
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    @Override
    public void run() {

        Log.d(TAG, "run: Start.");

        while(running) {

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {

                    gameEngine.update();
                    gameView.draw(canvas);
                }

            } catch (Exception e) {
                Log.e(TAG, "run: Error drawing to canvas: " + e.getMessage());
            } finally {

                if (canvas != null) {

                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);

                    } catch (Exception e) {
                        Log.e(TAG, "run: Error unlocking canvas: " + e.getMessage());
                    }
                }
            }
        }
    }
}
