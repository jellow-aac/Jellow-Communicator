package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ekalpa on 7/12/2016.
 */

public class Splash extends AppCompatActivity {

    PlayGifView pGif;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        pGif = (PlayGifView) findViewById(R.id.viewGif);
        //System.out.println("adasd"+PlayGifView.mCurrentAnimationTime);
        pGif.setImageResource(R.drawable.jellow_j);

        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent intent = new Intent(Splash.this, SharedPreferences.class);
                startActivity(intent);
            }
        }.start();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}
