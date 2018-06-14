package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.EvaluateDisplayMetricsUtils;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.Timer;
import java.util.TimerTask;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;

/**
 * Created by ekalpa on 7/12/2016.
 */
public class SplashActivity extends AppCompatActivity {

    //Field to create IconDatabase
    CreateDataBase iconDatabase;
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
        iconDatabase=new CreateDataBase(this);
        iconDatabase.execute();
     }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
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
                    checkIfDatabaseCreated();
                    break;
            }
        }
    };

    private void checkIfDatabaseCreated()
    {
        if(!(new SessionManager(this).isLanguageChanged()==2))
            startApp();//if changes are their in the app then check whether the data base is created or not
        else
            startJellow();//If no change in laguage then simply start the app.
    }

    private void startApp() {
        final Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(iconDatabase.getStatus()== AsyncTask.Status.FINISHED)
                {
                    startJellow();
                    timer.cancel();
                }
            }
        },0,100);
    }

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