package com.dsource.idc.jellowintl;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.packageUpdate.ConnectionUtils;
import com.dsource.idc.jellowintl.packageUpdate.ProgressReceiver;
import com.dsource.idc.jellowintl.packageUpdate.UpdateManager;

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

        updateManager = new UpdateManager(getApplicationContext(),this);
        statusText = findViewById(R.id.status);
        progressBar = findViewById(R.id.pg);
        progressBar.setMax(1);

        if(ConnectionUtils.isConnected(this)){
            updateManager.startDownload();
        } else {
            statusText.setText(getString(R.string.lpu_connectivity_error));
        }
    }

    @Override
    public void onDownloadProgress(int completedDownloads, int totalDownloads) {
        progressBar.setProgress((float)completedDownloads/totalDownloads);
    }

    @Override
    public void onIconDownloadTaskCompleted(boolean success) {

    }

    @Override
    public void updateStatusText(String message) {
        statusText.setText(message);
    }

    @Override
    public void showUpdateInfo(String message) {
        showToast(message);
    }

    @Override
    public void iconsModified(boolean modified) {
        if(modified){
            getSession().setLanguageChange(0);
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
}