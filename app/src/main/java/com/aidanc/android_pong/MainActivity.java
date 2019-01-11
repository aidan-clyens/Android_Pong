package com.aidanc.android_pong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Play with an easy difficulty, a slower ball speed
        Button easyButton = (Button) findViewById(R.id.easy_difficulty_button);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty = 0;
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("Difficulty", difficulty);
                startActivity(i);
            }
        });

        //  Play with a hard difficulty, a faster ball speed
        Button hardButton = (Button) findViewById(R.id.hard_difficulty_button);
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty = 1;
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                i.putExtra("Difficulty", difficulty);
                startActivity(i);
            }
        });
    }
}
