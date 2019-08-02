package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.utility.DownloadManager;

import java.io.File;

import static com.dsource.idc.jellowintl.LanguageSelectActivity.FINISH;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.TUTORIAL;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.UNIVERSAL_PACKAGE;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangValueMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

public class LanguageDownloadActivity extends BaseActivity {
    DownloadManager manager;
    RoundCornerProgressBar progressBar;
    String langCode;
    private String mCheckConn;
    Boolean tutorial = false;
    Boolean finish = true;
    Boolean isConnected;
    private String strLanguageDownloaded;
    private String strLanguageDownloading;

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
        {
            String str = getString(R.string.language_downloaded);
            if (langCode.equals(MR_IN)){
                str = str.replace("_", LangValueMap.get(langCode));
                strLanguageDownloaded = str;
                str = getString(R.string.language_downloading);
                str = str.replace("_",  LangValueMap.get(langCode));
                strLanguageDownloading = str;
            }else{
                str = str.replace("_", "");
                strLanguageDownloaded = str;
                str = getString(R.string.language_downloading);
                str = str.replace("_",  "");
                strLanguageDownloading = str;
            }
        }

        mCheckConn = getString(R.string.checkConnectivity);
        final DownloadManager.ProgressReceiver progressReceiver = new DownloadManager.ProgressReceiver() {
            @Override
            public void onprogress(int soFarBytes, int totalBytes) {
                progressBar.setProgress((float)soFarBytes/totalBytes);
            }

            @Override
            public void onComplete() {
                getSession().setDownloaded(langCode);
                getSession().setDownloaded(getSession().getLanguage());
                if(!tutorial) {
                    getSession().setToastMessage(strLanguageDownloaded);
                    finish();
                    return;
                }
                Toast.makeText(LanguageDownloadActivity.this, strLanguageDownloaded,
                        Toast.LENGTH_SHORT).show();
                File file = new File(getBaseContext().getDir(PathFactory.UNIVERSAL_FOLDER,
                        Context.MODE_PRIVATE).getAbsolutePath()+"/audio");
                if(!getSession().isAudioPackageDownloaded() && getSession().isDownloaded(MR_IN)
                        && !file.exists()){
                    progressBar.setProgress(0);
                    progressBar.invalidate();
                    manager.setLanguage(MR_IN);
                    strLanguageDownloading = strLanguageDownloading.replace(UNIVERSAL_PACKAGE,
                            LangValueMap.get(MR_IN));
                    Toast.makeText(LanguageDownloadActivity.this, strLanguageDownloading,
                            Toast.LENGTH_SHORT).show();
                    strLanguageDownloaded = strLanguageDownloaded.replace(UNIVERSAL_PACKAGE,
                            LangValueMap.get(MR_IN));
                    manager.start();
                }else if(tutorial) {
                    getSession().setAudioExtraPackage(true);
                    startActivity(new Intent(LanguageDownloadActivity.this, Intro.class));
                    finish();
                }else if(finish) {
                    startActivity(new Intent(LanguageDownloadActivity.this,SplashActivity.class));
                    finish();
                }
            }
        };

        Toast.makeText(this, strLanguageDownloading, Toast.LENGTH_SHORT).show();

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