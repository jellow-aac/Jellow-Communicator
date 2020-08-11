package com.dsource.idc.jellowintl.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.AppUpdateUtil;
import com.dsource.idc.jellowintl.utility.PlayGifView;
import com.dsource.idc.jellowintl.utility.async.CreateDataBase;
import com.dsource.idc.jellowintl.utility.async.InternetTest;
import com.dsource.idc.jellowintl.utility.interfaces.CheckNetworkStatus;
import com.dsource.idc.jellowintl.utility.interfaces.CompletionCallback;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;

/**
 * Created by ekalpa on 7/12/2016.
 */
public class SplashActivity extends BaseActivity implements CheckNetworkStatus, CompletionCallback {
    private InternetTest internetTest;
    private CreateDataBase createDataBase;

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

        TextDatabase textDb = new TextDatabase(this, getSession().getLanguage(), getAppDatabase());
        if(getSession().getLanguageDataUpdateState(getSession().getLanguage()) ==
                GlobalConstants.LANGUAGE_STATE_CREATE_DB || !textDb.checkForTableExists()) {
            CacheManager.clearCache();
            TextFactory.clearJson();
            createDataBase = new CreateDataBase();
            createDataBase.registerReceiver(this);
            createDataBase.execute(this, getAppDatabase());
        }else {
            internetTest = new InternetTest();
            internetTest.registerReceiver(this);
            internetTest.execute(this);
        }
     }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getUserId());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && isNotchDevice()) {
            findViewById(R.id.parent).
                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));
        }

        setUserParameters();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            new AppUpdateUtil().
                    executeUpdateFlow(
                            AppUpdateUtil.UpdateStatus.RUNNING,this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AppUpdateUtil.UPDATE_REQUEST_CODE && resultCode == RESULT_OK){
            restartTheAppAfterUpdate();
        }else if(requestCode == AppUpdateUtil.UPDATE_REQUEST_CODE){
            continueLoadingTheApp();
        }
    }

    @Override
    public void onReceiveNetworkState(int state) {
        internetTest.unRegisterReceiver();
        if(state == GlobalConstants.NETWORK_CONNECTED){
            AppUpdateUtil updateUtil = new AppUpdateUtil();
            updateUtil.executeUpdateFlow(AppUpdateUtil.UpdateStatus.INIT,SplashActivity.this);
        }else{
            continueLoadingTheApp();
        }
    }

    @Override
    public void onTaskComplete(int status) {
        createDataBase.unRegisterReceiver();
        if(status == GlobalConstants.STATUS_SUCCESS)
            getSession().setLanguageDataUpdateState(getSession().getLanguage(),
                    GlobalConstants.LANGUAGE_STATE_NO_CHANGE);
        else
            getSession().setLanguageDataUpdateState(getSession().getLanguage(),
                    GlobalConstants.LANGUAGE_STATE_CREATE_DB);
        continueLoadingTheApp();
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

        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
            setUserProperty("VisualAccessibility", "true");
        }else{
            setUserProperty("VisualAccessibility", "false");
        }
    }

    public void continueLoadingTheApp() {
        try {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finishAffinity();
        }catch(Exception e){
            finish();
        }
    }

    private void restartTheAppAfterUpdate() {
        Intent mStartActivity = new Intent(getApplicationContext(), UserRegistrationActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,
                mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}