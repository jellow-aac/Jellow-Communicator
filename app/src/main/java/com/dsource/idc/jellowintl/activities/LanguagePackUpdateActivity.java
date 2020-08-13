package com.dsource.idc.jellowintl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.package_updater_module.ConnectionUtils;
import com.dsource.idc.jellowintl.package_updater_module.UpdateManager;
import com.dsource.idc.jellowintl.package_updater_module.interfaces.ProgressReceiver;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class LanguagePackUpdateActivity extends BaseActivity implements ProgressReceiver{

    UpdateManager updateManager;
    RoundCornerProgressBar progressBar;
    TextView statusText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_pack_update_activity);
        setNavigationUiConditionally();
        updateManager = new UpdateManager(this);
        statusText = findViewById(R.id.status);
        progressBar = findViewById(R.id.pg);
        progressBar.setMax(1);
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            findViewById(R.id.btnClose).setVisibility(View.VISIBLE);
        }
        if(ConnectionUtils.isConnected(this)){
            updateManager.startDownload();
        } else {
            statusText.setText(getString(R.string.checkConnectivity));
            statusText.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
        }
    }

    @Override
    public void onDownloadProgress(int completedDownloads, int totalDownloads) {
        progressBar.setProgress((float)completedDownloads/totalDownloads);
    }

    @Override
    public void onIconDownloadTaskCompleted(boolean success) {}

    @Override
    public void updateStatusText(String message) {
        statusText.setText(message);
        statusText.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
    }

    @Override
    public void showUpdateInfo(String message) {
        showToast(message);
    }

    @Override
    public void iconsModified(boolean modified) {
        if(modified){
            for (String language :
                    SessionManager.LangValueMap.keySet()) {
                getSession().setLanguageDataUpdateState(language,
                        GlobalConstants.LANGUAGE_STATE_CREATE_DB);
            }
            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            finishAffinity();
        }else{
            finish();
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(LanguagePackUpdateActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
        startMeasuring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);
        stopMeasuring("LanguagePackUpdateActivity");
    }

    public void closeActivity(View v){
        finish();
    }
}