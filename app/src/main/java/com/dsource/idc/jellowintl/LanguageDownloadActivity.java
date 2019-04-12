package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.utility.DownloadManager;

import androidx.core.content.ContextCompat;

import static com.dsource.idc.jellowintl.LanguageSelectActivity.FINISH;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.TUTORIAL;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangValueMap;

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
        DownloadManager.ProgressReciever progressReciever = new DownloadManager.ProgressReciever() {
            @Override
            public void onprogress(int soFarBytes, int totalBytes) {
                progressBar.setProgress((float)soFarBytes/totalBytes);
            }

            @Override
            public void onComplete() {
                getSession().setDownloaded(langCode);
                if(!tutorial)
                    getSession().setToastMessage(strLanguageDownloaded
                        .replace("_", getShortenLangName(LangValueMap.get(langCode))));
                if(tutorial) {
                    Toast.makeText(LanguageDownloadActivity.this, strLanguageDownloaded
                            .replace("_", getShortenLangName(LangValueMap.get(langCode))),
                            Toast.LENGTH_SHORT).show();
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
                getShortenLangName(LangValueMap.get(langCode))), Toast.LENGTH_SHORT).show();

        if(langCode != null) {
            try {
                isConnected = isConnectedToNetwork((ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE));
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

    private String getShortenLangName(String langFullName) {
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                switch (langFullName) {
                    case "मराठी":
                        return getString(R.string.acc_lang_marathi);
                    case "हिंदी":
                        return getString(R.string.acc_lang_hindi);
                    case "বাঙালি":
                        return getString(R.string.acc_lang_bengali);
                    case "English (India)":
                        return getString(R.string.acc_lang_eng_in);
                    case "English (United Kingdom)":
                        return getString(R.string.acc_lang_eng_gb);
                    case "English (United States)":
                        return getString(R.string.acc_lang_eng_us);
                    case "English (Australia)":
                        return getString(R.string.acc_lang_eng_au);
                    default:
                        return langFullName;
                }
            }else{
                switch (langFullName) {
                    case "English (India)":
                        return "English (IN)";
                    case "English (United Kingdom)":
                        return "English (UK)";
                    case "English (United States)":
                        return "English (US)";
                    case "English (Australia)":
                        return "English (AU)";
                    default:
                        return langFullName;
                }
        }
    }
}
