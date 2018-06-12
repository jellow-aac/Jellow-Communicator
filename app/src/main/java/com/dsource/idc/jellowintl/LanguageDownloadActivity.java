package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.DownloadManager;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.LanguageSelectActivity.FINISH;
import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.TUTORIAL;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangValueMap;

public class LanguageDownloadActivity extends AppCompatActivity {
    DownloadManager manager;
    RoundCornerProgressBar progressBar;
    private SessionManager mSession;
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
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

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

        mSession = new SessionManager(this);

        final String strLanguageDownloaded = getString(R.string.language_downloaded);
        final String strLanguageDownloading = getString(R.string.language_downloading);
        mCheckConn = getString(R.string.checkConnectivity);
        DownloadManager.ProgressReciever progressReciever = new DownloadManager.ProgressReciever() {
            @Override
            public void onprogress(int soFarBytes, int totalBytes) {
                progressBar.setProgress((float)soFarBytes/totalBytes);
            }

            @Override
            public void onComplete() {
                mSession.setDownloaded(langCode);
                Toast.makeText(LanguageDownloadActivity.this, strLanguageDownloaded
                        .replace("_", LangValueMap.get(langCode)), Toast.LENGTH_SHORT).show();
                if(tutorial)
                    startActivity(new Intent(LanguageDownloadActivity.this,Intro.class));
                else if(finish)
                {
                    Intent intent=new Intent(LanguageDownloadActivity.this,SplashActivity.class);
                    intent.putExtra(getString(R.string.lang_change_code),getString(R.string.lang_change));
                    startActivity(intent);
                }
                finish();
            }
        };

        Toast.makeText(LanguageDownloadActivity.this, strLanguageDownloading
                .replace("_", LangValueMap.get(langCode)), Toast.LENGTH_SHORT).show();

        if(langCode != null) {
            try {
                isConnected = isConnected();
                if(isConnected)
                {
                    manager = new DownloadManager(langCode, this, progressReciever);
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }


    private boolean isConnected()
    {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }




    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            throw new Error("unableToResume");
        }
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        isConnected = isConnected();
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
