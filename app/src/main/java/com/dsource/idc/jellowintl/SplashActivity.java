package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.dsource.idc.jellowintl.Utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.Utility.EvaluateDisplayMetricsUtils;
import com.dsource.idc.jellowintl.Utility.JellowTTSService;
import com.dsource.idc.jellowintl.Utility.SessionManager;

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
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        try {
            new DataBaseHelper(this).createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(isMyServiceRunning(JellowTTSService.class))
            stopTTsService();
        startTTsService();
        PlayGifView pGif = findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);
        new ChangeAppLocale(this).setLocale();
        {
            SessionManager sManager = new SessionManager(this);
            if (sManager.isRequiredToPerformDbOperations()) {
                performDatabaseOperations();
                sManager.setCompletedDbOperations(true);
            }
            sManager = null;
        }
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

    private void performDatabaseOperations() {
        DataBaseHelper helper = new DataBaseHelper(this);
        helper.openDataBase();
        helper.addLanguageDataToDatabase();
    }

    private void startTTsService() {
        startService(new Intent(getApplication(), JellowTTSService.class));
    }

    private void stopTTsService() {
        Intent intent = new Intent("com.dsource.idc.jellowintl.STOP_SERVICE");
        sendBroadcast(intent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}