package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.utility.DownloadManager;

import static com.dsource.idc.jellowintl.LanguageSelectActivity.FINISH;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.TUTORIAL;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

public class LanguageDownloadActivity extends BaseActivity {
    DownloadManager manager;
    RoundCornerProgressBar progressBar;
    String langCode;
    private String mCheckConn;
    Boolean tutorial = false;
    Boolean finish = true;
    Boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_download);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        try {

           langCode =  getIntent().getExtras().getString(LCODE);
           finish = getIntent().getBooleanExtra(FINISH,true);
           tutorial = getIntent().getBooleanExtra(TUTORIAL,false);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        progressBar = findViewById(R.id.pg);
        progressBar.setMax(1);

        final String strLanguageDownloaded = getString(R.string.language_downloaded);
        final String strLanguageDownloading = getString(R.string.language_downloading);
        mCheckConn = getString(R.string.checkConnectivity);
        DownloadManager.ProgressReciever progressReceiver = new DownloadManager.ProgressReciever() {
            @Override
            public void onprogress(int soFarBytes, int totalBytes) {
                progressBar.setProgress((float)soFarBytes/totalBytes);
            }

            @Override
            public void onComplete() {
                getSession().setDownloaded(langCode);
                getSession().setDownloaded(getSession().getLanguage());
                if(!tutorial)
                    getSession().setToastMessage(strLanguageDownloaded.replace("_", langCode));
                if(tutorial) {
                    Toast.makeText(LanguageDownloadActivity.this, strLanguageDownloaded
                            .replace("_", langCode), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LanguageDownloadActivity.this, Intro.class));
                }else if(finish)
                {
                    Intent intent=new Intent(LanguageDownloadActivity.this,SplashActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        };

        Toast.makeText(this, strLanguageDownloading.replace("_",
                langCode), Toast.LENGTH_SHORT).show();

        if(langCode != null) {
            try {
                isConnected = isConnectedToNetwork((ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE));
                if(isConnected)
                {
                    manager = new DownloadManager(langCode, this, progressReceiver);
                    manager.start();
                }else {

                    Toast.makeText(this,mCheckConn,Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(LanguageDownloadActivity.class.getSimpleName());
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
        isConnected = isConnectedToNetwork((ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE));
        if(isConnected) {
            if (manager != null)
                manager.resume();
        } else {
            Toast.makeText(this,mCheckConn,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(manager != null)
            manager.pause();
    }
}
