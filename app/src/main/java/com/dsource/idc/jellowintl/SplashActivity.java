package com.dsource.idc.jellowintl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.CreateDataBase;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.LanguageDownloadActivity.SPLASH;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;

/**
 * Created by ekalpa on 7/12/2016.
 */
public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        PlayGifView pGif = findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);

        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
            (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED))
            getSession().setEnableCalling(false);

        if (getSession().isLanguageChanged() == 1) {
            CacheManager.clearCache();
            TextFactory.clearJson();
        }
        if(!getSession().isDownloaded(SessionManager.UNIVERSAL_PACKAGE)){
            /* 0 represents old value of one by three config*/
            if(getSession().getGridSize() == 0)
                 getSession().setGridSize(GlobalConstants.GRID_ONE_BY_THREE);
            else
                 getSession().setGridSize(GlobalConstants.GRID_THREE_BY_THREE);
            startActivity(new Intent(this, LanguageDownloadActivity.class)
                    .putExtra(LCODE, SessionManager.UNIVERSAL_PACKAGE)
                    .putExtra(SPLASH, true));
        }else if(getSession().isLanguageChanged()!=2) {
            new CreateDataBase(this, getAppDatabase()).execute();
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
        if(getSession().isGridSizeKeyExist()) {
            if(getSession().getGridSize() == GlobalConstants.GRID_THREE_BY_THREE){
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

        if(getSession().getPictureViewMode() == GlobalConstants.DISPLAY_PICTURE_TEXT) {
            setUserProperty("PictureViewMode", "PictureText");
            setCrashlyticsCustomKey("PictureViewMode", "PictureText");
        }else{
            setUserProperty("PictureViewMode", "PictureOnly");
            setCrashlyticsCustomKey("PictureViewMode", "PictureOnly");
        }
    }
}