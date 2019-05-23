package com.dsource.idc.jellowintl;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.packageUpdate.ProgressReceiver;
import com.dsource.idc.jellowintl.packageUpdate.UpdateManager;
import com.dsource.idc.jellowintl.packageUpdate.ConnectionUtils;

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
            statusText.setText("Please Check Your Internet Connection!!");
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
    }


}
