package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellow.Utility.DownloadManager;
import com.dsource.idc.jellow.Utility.SessionManager;

import static com.dsource.idc.jellow.LanguageSelectActivity.FINISH;
import static com.dsource.idc.jellow.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellow.UserRegistrationActivity.TUTORIAL;
import static com.dsource.idc.jellow.Utility.SessionManager.LangMap;
import static com.dsource.idc.jellow.Utility.SessionManager.LangValueMap;

public class LanguageDownloadActivity extends AppCompatActivity {
    DownloadManager manager;
    RoundCornerProgressBar progressBar;
    private SessionManager mSession;
    String langCode;
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

        mSession = new SessionManager(this);

        DownloadManager.ProgressReciever progressReciever = new DownloadManager.ProgressReciever() {
            @Override
            public void onprogress(int soFarBytes, int totalBytes) {
                progressBar.setProgress((float)soFarBytes/totalBytes);
            }

            @Override
            public void onComplete() {
                mSession.setDownloaded(langCode);
                Toast.makeText(LanguageDownloadActivity.this,LangValueMap.get(langCode)+" Language Downloaded",Toast.LENGTH_SHORT).show();
                if(tutorial)
                    startActivity(new Intent(LanguageDownloadActivity.this,TutorialActivity.class));
                if(finish)
                    startActivity(new Intent(LanguageDownloadActivity.this,SplashActivity.class));
                finish();
            }


        };

        Toast.makeText(LanguageDownloadActivity.this,"Downloading "+LangValueMap.get(langCode)+ " Language",Toast.LENGTH_SHORT).show();

        if(langCode != null) {
            try {
                isConnected = isConnected();
                if(isConnected)
                {
                    manager = new DownloadManager(langCode, this, progressReciever);
                    manager.start();
                }else {

                    Toast.makeText(this,getString(R.string.checkConnectivity),Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }


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
        isConnected = isConnected();
        if(isConnected) {
            if (manager != null)
                manager.resume();
        } else {
            Toast.makeText(this,getString(R.string.checkConnectivity),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(manager != null)
            manager.pause();
    }
}
