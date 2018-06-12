package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.EvaluateDisplayMetricsUtils;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
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
        //This is to create database for the JellowIcon
        iconDatabaseAction();

    }
    /**
     * <p>
     * This function is added to check whether the intent is fired from Language change activity or
     * Intro activity, if so then icon database is set up for the first time.
     * and create the icon table in the database accordingly.
     * @Author AyazAlam
     * </p>
     * */
    void iconDatabaseAction()
    {

        if(getIntent().getStringExtra(getString(R.string.lang_change_code))!=null)
        {
            IconDataBaseHelper iconDatabase=new IconDataBaseHelper(this);
            //When language is changed drop current table and create new
            if(getIntent().getStringExtra
                    (getString(R.string.lang_change_code)).equals(getString(R.string.lang_change))) {
                iconDatabase.dropTable(new DataBaseHelper(this).getWritableDatabase());
                iconDatabase.createTable(new DataBaseHelper(this).getWritableDatabase());

                /*
                iconDatabase.dropTable(new DataBaseHelper(this).getWritableDatabase());
                iconDatabase.onCreate(new DataBaseHelper(this).getWritableDatabase());
                */
            }
            //When user logs in for the first time create the database for the first time
            else if((getIntent().getStringExtra
                    (getString(R.string.lang_change_code)).equals(getString(R.string.first_time_login))))
            {
                iconDatabase.createTable(new DataBaseHelper(this).getWritableDatabase());
                //
                // iconDatabase.onCreate(new DataBaseHelper(this).getWritableDatabase());
            }
        }
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