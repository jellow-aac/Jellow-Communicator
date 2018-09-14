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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.DownloadManager;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

public class LanguagePackageUpdateActivity extends AppCompatActivity {
    private SessionManager mSession;
    private DownloadManager manager;
    private DownloadManager.ProgressReciever progressReciever;
    private RoundCornerProgressBar progressBar;
    private String[] langList2Update;
    private int langDownCount;
    private String mCheckConn, mPkgUpdateStr;
    Boolean isConnected;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        mSession = new SessionManager(this);
        if(mSession.getPackages2Update().isEmpty())
            langList2Update = getLanguageList2UpdatePackage(getOfflineLanguages());
        else
            langList2Update = mSession.getPackages2Update().split(",");

        if(langList2Update.length == 0) {
            mSession.setLanguagePackageUpdated(true);
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }
        setContentView(R.layout.activity_language_download);
        langDownCount  = 0;
        findViewById(R.id.txtDownloadCount).setVisibility(View.VISIBLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        progressBar = findViewById(R.id.pg);
        progressBar.setMax(1);

        mCheckConn = getString(R.string.checkConnectivity);
        mPkgUpdateStr = getString(R.string.icon_update_msg);
        progressReciever = new DownloadManager.ProgressReciever() {
            @Override
            public void onprogress(int soFarBytes, int totalBytes) {
                progressBar.setProgress((float)soFarBytes/totalBytes);
            }

            @Override
            public void onComplete() {
                if(++langDownCount == langList2Update.length) {
                    Intent intent = new Intent(LanguagePackageUpdateActivity.this, SplashActivity.class);
                    mSession.setLanguagePackageUpdated(true);
                    mSession.packages2Update("");
                    startActivity(intent);
                    finish();
                }else {
                    String packages2Download = "";
                    for (int i = langDownCount; i < langList2Update.length; i++) {
                        packages2Download += langList2Update[i] + ",";
                    }
                    mSession.packages2Update(packages2Download);
                    updateNextPackage(langList2Update);
                }
            }
        };
        updateNextPackage(langList2Update);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            resetAnalytics(this, mSession.getCaregiverNumber().substring(1));
        }
        if(!isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
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

    private void updateNextPackage(String[] langList2Update) {
        try {
            progressBar.setProgress(0);
            isConnected = isConnected();
            ((TextView)findViewById(R.id.txtDownloadCount))
                    .setText(mPkgUpdateStr.concat(" " +(langDownCount+1)+ " / "+langList2Update.length));
            if(isConnected){
                manager = new DownloadManager(langList2Update[langDownCount],
                        this, progressReciever);
                manager.start();
            }else {
                Toast.makeText(this,mCheckConn,Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Further development condition for specific icons for specific package. Download only new content and
    // add to existing package.
    private String[] getLanguageList2UpdatePackage(String[] offlineLanguages) {
        List<String> lang = new ArrayList<>();
        for (String ofLang : offlineLanguages) {
            File fileOne = new File(getBaseContext().getDir(ofLang, Context.MODE_PRIVATE).
                    getPath() + "/drawables/habits-01.png");
            File fileTwo = new File(getBaseContext().getDir(ofLang, Context.MODE_PRIVATE).
                    getPath() + "/drawables/idontwantanysound-01.png");
            File fileThree = new File(getBaseContext().getDir(ofLang, Context.MODE_PRIVATE).
                    getPath() + "/drawables/holdhandswhilecrossing.png");
            if (!fileOne.exists() || !fileTwo.exists() || !fileThree.exists())
                lang.add(ofLang);
        }
        return lang.toArray(new String[lang.size()]);
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

    private String[] getOfflineLanguages(){
        List<String> lang = new ArrayList<>();
        if(mSession.isDownloaded(SessionManager.ENG_IN))
            lang.add(SessionManager.ENG_IN);
        if(mSession.isDownloaded(SessionManager.ENG_US))
            lang.add(SessionManager.ENG_US);
        if(mSession.isDownloaded(SessionManager.ENG_UK))
            lang.add(SessionManager.ENG_UK);
        if(mSession.isDownloaded(SessionManager.HI_IN))
            lang.add(SessionManager.HI_IN);
        return lang.toArray(new String[lang.size()]);
    }
}
