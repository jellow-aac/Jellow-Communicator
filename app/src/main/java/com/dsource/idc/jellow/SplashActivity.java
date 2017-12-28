package com.dsource.idc.jellow;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dsource.idc.jellow.Utility.ChangeAppLocale;
import com.dsource.idc.jellow.Utility.EvaluateDisplayMetricsUtils;
import com.dsource.idc.jellow.Utility.JellowTTSService;
import com.dsource.idc.jellow.Utility.SessionManager;
import com.dsource.idc.jellow.Utility.Analytics;

import java.io.IOException;

import static com.dsource.idc.jellow.Utility.Analytics.reportException;
import static com.dsource.idc.jellow.Utility.Analytics.reportLog;

/**
 * Created by ekalpa on 7/12/2016.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        if(!(new SessionManager(this).isUpdatedForNewContentV5()))
            addNewRowsInDatabaseForNewContent();
        startTTsService();
        PlayGifView pGif = (PlayGifView) findViewById(R.id.viewGif);
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

    private void addNewRowsInDatabaseForNewContent() {
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        String queryResult;
        try {
            dbHelper.createDataBase();
            dbHelper.openDataBase();
            queryResult = dbHelper.addNewRowsInDatabaseForNewContentVersionV5();
            if(queryResult.split(",")[0].equals("OK") &&
                queryResult.split(",")[1].equals("OK"))
                new SessionManager(this).setUpdatedForNewContentV5();
            else
                reportLog("Unable to add content in database at splash screen. QueryResult is "+ queryResult, Log.ERROR);
        } catch (SQLException e) {
            reportLog("Unable to add content in database at splash screen.", Log.ERROR);
            reportException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTTsService() {
        startService(new Intent(getApplication(), JellowTTSService.class));
    }
}