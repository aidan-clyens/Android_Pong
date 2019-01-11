package com.aidanc.android_pong;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.PopupMenu;

/**
 * GameView
 *
 * This class handles the user interaction with the game. It renders the game board, the sprites, and starts
 * the main game thread.
 *
 * Aidan Clyens
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    //  Tag used for debugging
    private final static String TAG = "GameView";
    //  Set the game dimensions to the user's device dimensions
    public final static int SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    public final static int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;

    private Context context;
    private GameActivity gameActivity;
    private int difficulty;
    //  The game engine, handles the game rules
    private GameEngine gameEngine;
    //  The main game thread
    private static MainThread thread;
    //  The sprites used for objects in the game
    private static BotPlayer topBlock;
    private static BlockSprite bottomBlock;
    private static BallSprite ball;
    private static MenuSprite menu;

    /* GameView
     * The GameView constructor. Pass the current context, game activity and difficulty setting
     */
    public GameView(Context c, GameActivity gActivity, int diff) {

        super(c);

        context = c;
        gameActivity = gActivity;
        difficulty = diff;

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    /* surfaceCreated
     * Called when the game begins.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d(TAG, "surfaceCreated.");
        //  When game is started, render all game objects in default positions
        topBlock = new BotPlayer(context, 0, 0);
        bottomBlock = new BlockSprite(context, 0, SCREEN_HEIGHT-BlockSprite.BLOCK_HEIGHT);
        ball = new BallSprite(context, topBlock, bottomBlock, difficulty);
        menu = new MenuSprite(context, 0, (SCREEN_HEIGHT/2 - MenuSprite.MENU_HEIGHT/2));

        gameEngine = new GameEngine(thread, topBlock, bottomBlock, ball);
        //  Start the main game thread
        gameEngine.start();
    }

    /* surfaceChanged
     * Called when the game surface changes.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //  Empty
    }

    /* surfaceDestroyed
     * Called when the game closes.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.d(TAG, "surfaceDestroyed.");
        //  Stop the main game thread when the game is closed
        gameEngine.stop();
    }

    /* draw
     * Draw the game board on the screen.
     */
    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        if(canvas != null) {
            //  Text paint
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(100);
            textPaint.setTextAlign(Paint.Align.CENTER);
            //  Line paint
            Paint linePaint = new Paint();
            linePaint.setColor(Color.WHITE);

            //  Draw a black canvas with a white line through the center for the game board
            canvas.drawColor(Color.BLACK);
            canvas.drawLine(0, SCREEN_HEIGHT/2, SCREEN_WIDTH, SCREEN_HEIGHT/2, linePaint);
            //  Player points are displayed at the left hand side of the screen, centered vertically
            canvas.drawText(String.format("%d", gameEngine.getBotPoints()), 50 , SCREEN_HEIGHT/2-200, textPaint);
            canvas.drawText(String.format("%d", gameEngine.getPlayerPoints()), 50 , SCREEN_HEIGHT/2+270, textPaint);
            //  Draw the pause menu at the top left corner of the screen
            menu.draw(canvas);
            //  Draw the player blocks at the top and bottom of the screen and the ball in the center
            topBlock.draw(canvas);
            bottomBlock.draw(canvas);
            ball.draw(canvas);

            if(gameEngine.isPaused()) {
                canvas.drawText("Paused", GameView.SCREEN_WIDTH/2, GameView.SCREEN_HEIGHT/2-50, textPaint);
            }
            
            if(gameEngine.gameWin()) {
                canvas.drawText("You Win!", GameView.SCREEN_WIDTH/2, GameView.SCREEN_HEIGHT/2-50, textPaint);
            }

            if(gameEngine.gameLose()) {
                canvas.drawText("You Lose!", GameView.SCREEN_WIDTH/2, GameView.SCREEN_HEIGHT/2-50, textPaint);
            }
        }
    }

    /* onSpriteClick
     * Determine whether a sprite object has been clicked depending on player touch coordinates.
     */
    public boolean onSpriteClick(Sprite sprite, int x, int y) {

        boolean inBoundaries = false;

        int xCoord = sprite.getXCoord();
        int yCoord = sprite.getYCoord();
        int width = sprite.getSpriteWidth();
        int height = sprite.getSpriteHeight();
        
        //  If touch coordinates reside inside the sprite's dimensions, than a touch event occurred
        if(x >= xCoord && x <= (xCoord + width)) {
            if(y >= yCoord && y <= (yCoord + height)) {
                inBoundaries = true;
            }
        }

        return inBoundaries;
    }

    /* showPopUpMenu
     * Display the pause menu when the icon has been clicked.
     */
    public void showPopupMenu() {
        Log.d(TAG, "showPopupMenu: Opened pop up menu");
        //  Pause the game when the pause popup menu is opened

        PopupMenu popupMenu = new PopupMenu(context, menu);
        
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    //  Resume the paused game
                    case R.id.resume_id:
                        gameEngine.resume();
                        return true;
                    //  Quit the game
                    case R.id.quit_id:
                        gameActivity.quitActivity();
                        return true;

                    default:
                        return false;
                }
            }
        });

        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    /* onTouchEvent
     * This is the GameView's touch screen handler, tracking player motion.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //  TODO Implement multitouch

        switch(event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: ACTION_DOWN: " + String.format("%f, %f", event.getRawX(), event.getRawY()));

                //  If game is paused, resume
                if(gameEngine.isPaused()) {
                    gameEngine.resume();
                }
                //  If bottom block is clicked, set it as moving
                if(onSpriteClick(bottomBlock, (int)event.getRawX(), (int)event.getRawY())) {
                    bottomBlock.offset = (int)event.getRawX() - bottomBlock.getXCoord();
                    bottomBlock.moving = true;
                    Log.d(TAG, "Clicked Block");
                }
                //  If the menu button is clicked, show the pop up menu
                if(onSpriteClick(menu, (int)event.getRawX(), (int)event.getRawY())) {
                    Log.d(TAG, "Clicked Menu");
                    gameEngine.pause();
                    showPopupMenu();
                }

                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ACTION_MOVE: " + String.format("%f, %f", event.getRawX(), event.getRawY()));

                if(topBlock.moving) {
                    topBlock.setXCoord((int)event.getRawX() - topBlock.offset);
                }
                if(bottomBlock.moving) {
                    bottomBlock.setXCoord((int)event.getRawX() - bottomBlock.offset);
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: ACTION_UP: " + String.format("%f, %f", event.getRawX(), event.getRawY()));

                topBlock.moving = false;
                bottomBlock.moving = false;

                invalidate();
                break;
        }

        return true;
    }
}
