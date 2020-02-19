package com.dsource.idc.jellowintl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && isNotchDevice()) {
            findViewById(R.id.parent).
                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }

        PlayGifView pGif = findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);

        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
            (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED))
            getSession().setEnableCalling(false);

        if (getSession().isLanguageChanged() == GlobalConstants.LANGUAGE_STATE_CHANGED) {
            CacheManager.clearCache();
            TextFactory.clearJson();
        }
        if(!LanguageFactory.isLanguageDataAvailable(this)){
            /* 0 represents old value of one by three config*/
            if(getSession().getGridSize() == 0)
                 getSession().setGridSize(GlobalConstants.THREE_ICONS_PER_SCREEN);
            else
                 getSession().setGridSize(GlobalConstants.NINE_ICONS_PER_SCREEN);
            startActivity(new Intent(this, LanguageDownloadActivity.class)
                    .putExtra(LCODE, SessionManager.UNIVERSAL_PACKAGE)
                    .putExtra(SPLASH, true));
        }else if(getSession().isLanguageChanged()!= GlobalConstants.LANGUAGE_STATE_DB_CREATE) {
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
            resetAnalytics(this, getSession().getUserId());
        }
        setUserParameters();
    }

    private void setUserParameters() {
        if(getSession().isGridSizeKeyExist()) {
            setGridSize();
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
