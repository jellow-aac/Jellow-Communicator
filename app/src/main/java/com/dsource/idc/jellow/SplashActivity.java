package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.dsource.idc.jellow.Utility.ChangeAppLocale;
import com.dsource.idc.jellow.Utility.EvaluateDisplayMetricsUtils;
import com.dsource.idc.jellow.Utility.JellowTTSService;

import java.io.IOException;

/**
 * Created by ekalpa on 7/12/2016.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        try {
            new DataBaseHelper(this).createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startTTsService();
        PlayGifView pGif = findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);
        new ChangeAppLocale(this).setLocale();
        new CountDownTimer(5000, 1) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }.start();
        EvaluateDisplayMetricsUtils displayMetricsUtils = new EvaluateDisplayMetricsUtils(this);
        displayMetricsUtils.calculateStoreDeviceHeightWidth();
        displayMetricsUtils.calculateStoreShadowRadiusAndBorderWidth();
    }

    private void startTTsService() {
        startService(new Intent(getApplication(), JellowTTSService.class));
    }
}