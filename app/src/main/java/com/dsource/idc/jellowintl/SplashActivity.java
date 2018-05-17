package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dsource.idc.jellowintl.utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.EvaluateDisplayMetricsUtils;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;

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
        new DataBaseHelper(this).createDataBase();

        if(isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)))
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
        EvaluateDisplayMetricsUtils displayMetricsUtils = new EvaluateDisplayMetricsUtils(this);
        displayMetricsUtils.calculateStoreDeviceHeightWidth();
        displayMetricsUtils.calculateStoreShadowRadiusAndBorderWidth();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.INIT_SERVICE");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            throw new Error("unableToResume");
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellowintl.INIT_SERVICE":
                    startJellow();
                    break;
            }
        }
    };

    private void startJellow() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        unregisterReceiver(receiver);
        finish();
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
}