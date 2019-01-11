package com.aidanc.android_pong;

import android.util.Log;


/* GameEngine
 * This class controls the game rules and mechanics.
 *
 * Aidan Clyens
 */
public class GameEngine {
    //  The 3 states of the game
    private enum GameState {RUNNING, STOPPED, PAUSED};
    //  Tag used for debugging
    private final static String TAG = "GameEngine";
    //  The number of points required for a win
    private final int WIN_CONDITION = 10;

    private static MainThread thread;
    private BotPlayer topBlock;
    private BlockSprite bottomBlock;
    private BallSprite ball;
    //  Track the human player's points and the bot's points
    private int playerPoints, botPoints;

    private GameState currentState;

    /* GameEngine
     * Pass the main game thread, the two block sprites, and the ball sprite.
     */
    public GameEngine(MainThread t, BotPlayer tBlock, BlockSprite bBlock, BallSprite b) {

        thread = t;
        topBlock = tBlock;
        bottomBlock = bBlock;
        ball = b;
        //  Initialize the game state to stopped
        currentState = GameState.STOPPED;
    }
    
    /* update
     * Update the current game state.
     */
    public void update() {
        topBlock.update();
        bottomBlock.update();
        ball.update();
    }

    /* start
     * Start the game, initializing the blocks, ball and start the main game thread.
     */
    public void start() {
        Log.d(TAG, "start: Game started");
        //  Initialize bot player and ball
        topBlock.initializeBot(ball);
        ball.initialize(this);
        //  Start main game thread
        thread.initialize(this);
        thread.setRunning(true);
        thread.start();
        //  Set the current game state to running
        currentState = GameState.RUNNING;
    }

    /* stop
     * Stop the game, stopping the main game thread.
     */
    public void stop() {
        Log.d(TAG, "stop: Game Stopped");

        boolean retry = true;

        while(retry) {
            
            try {
                //  Attempt to stop the main game thread
                if(thread != null) {
                    thread.setRunning(false);
                    thread.join();
                }
                //  Set the current game state to stopped
                currentState = GameState.STOPPED;

                Log.d(TAG, "stop: Stopping MainThread successful.");

            } catch(InterruptedException e) {
                Log.e(TAG, "stop: Error stopping MainThread: " + e.getMessage());
            }

            retry = false;
        }
    }

    /* pause
     * Pause the game by stopping the ball's movement and pausing the main game thread.
     */
    public void pause() {
        Log.d(TAG, "pause: Game Paused");
        //  Set the current game state to paused
        currentState = GameState.PAUSED;
        //  Stop the ball's movement
        ball.pause();
    }

    /* resume
     * Resume the game by continusing the ball's movement and resuming the main game thread.
     */
    public void resume() {
        Log.d(TAG, "resume: Game Resumed");
        //  Set the current game state to running
        currentState = GameState.RUNNING;
        //  Resume the ball's movement
        ball.resume();
    }

    /* resetPosition
     * When a point is scored pause the game thread for 1 second.
     */
    public void resetPosition() {
        //  Pause the game thread for 1 second on reset
        try {
            thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG, "resetPosition: Exception sleeping game thread. " + e.getMessage());
        }
    }

    /* playerPointGained
     * Increase the human player's points by 1 when a point is scored against the bot.
     */
    public void playerPointGained() {
        playerPoints++;
    }

    /* botPointGained
     * Increase the bot's points by 1 when a point is scored against the human player.
     */
    public void botPointGained() {
        botPoints++;
    }

    /* getPlayerPoints
     * Return the player's points.
     */
    public int getPlayerPoints() {
        return playerPoints;
    }

    /* getBotPoints
     * Return the bot's points.
     */
    public int getBotPoints() {
        return botPoints;
    }

    /* gameWin
     * When the game has been won by the human player, pause the ball's movement. Return true if so.
     */
    public boolean gameWin() {
        //  The player wins by getting the required points
        if(playerPoints >= WIN_CONDITION) {
            ball.pause();
            return true;
        } else {
            return false;
        }
    }

    /* gameLose
     * When the game has been lost by the human player, pause the ball's movement. Return true if so.
     */
    public boolean gameLose() {
        //  The bot wins and the player loses if the bot gets the required points
        if(botPoints >= WIN_CONDITION) {
            ball.pause();
            return true;
        } else {
            return false;
        }
    }

    /* isPaused
     * Return whether the game state is paused or not.
     */
    public boolean isPaused() {
        return (currentState == GameState.PAUSED);
    }
}
