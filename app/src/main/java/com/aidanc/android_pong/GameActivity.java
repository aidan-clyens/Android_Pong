package com.aidanc.android_pong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by aidan on 10/12/2017.
 */

public class GameActivity extends Activity {

    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            difficulty = extras.getInt("Difficulty");
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this, this, difficulty));
    }

    public void quitActivity() {
        Intent i = new Intent(GameActivity.this, MainActivity.class);
        startActivity(i);
    }
}
