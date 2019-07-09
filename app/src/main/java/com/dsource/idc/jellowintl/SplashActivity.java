package com.dsource.idc.jellowintl;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.utility.CreateDataBase;
import com.dsource.idc.jellowintl.utility.DataBaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.DE_DE;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.ES_ES;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.TA_IN;

/**
 * Created by ekalpa on 7/12/2016.
 */
public class SplashActivity extends BaseActivity {
    //Field to create IconDatabase
    CreateDataBase iconDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new DataBaseHelper(this).createDataBase();

        PlayGifView pGif = findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);

        if (getSession().isRequiredToPerformDbOperations()) {
            performDatabaseOperations();
            getSession().setCompletedDbOperations(true);
        }
        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
            (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED))
            getSession().setEnableCalling(false);

        if (getSession().isLanguageChanged() == 1) {
            CacheManager.clearCache();
            TextFactory.clearJson();
        }

        if(getSession().isLanguageChanged()!=2) {
            iconDatabase = new CreateDataBase(this, getAppDatabase());
            iconDatabase.execute();
        }else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finishAffinity();
        }
     }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
        setUserParameters();
    }

    private void setUserParameters() {
        final int GRID_3BY3 = 1, PICTURE_TEXT = 0;
        if(getSession().isGridSizeKeyExist()) {
            if(getSession().getGridSize() == GRID_3BY3){
                setUserProperty("GridSize", "9");
                setCrashlyticsCustomKey("GridSize", "9");
            }else{
                setUserProperty("GridSize", "3");
                setCrashlyticsCustomKey("GridSize", "3");
            }
        }else{
            setUserProperty("GridSize", "9");
            setCrashlyticsCustomKey("GridSize", "9");
        }

        if(getSession().getPictureViewMode() == PICTURE_TEXT) {
            setUserProperty("PictureViewMode", "PictureText");
            setCrashlyticsCustomKey("PictureViewMode", "PictureText");
        }else{
            setUserProperty("PictureViewMode", "PictureOnly");
            setCrashlyticsCustomKey("PictureViewMode", "PictureOnly");
        }
    }

    private void performDatabaseOperations() {
        DataBaseHelper helper = new DataBaseHelper(this);
        helper.openDataBase();
        helper.addLanguageDataToDatabase();
    }
}