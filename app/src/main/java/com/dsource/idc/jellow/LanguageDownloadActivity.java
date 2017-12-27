package com.dsource.idc.jellow;

import android.content.Intent;
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

public class LanguageDownloadActivity extends AppCompatActivity {
    DownloadManager manager;
    RoundCornerProgressBar progressBar;
    private SessionManager mSession;
    String langCode;
    Boolean finish = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_download);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        try {

           langCode =  getIntent().getExtras().getString(LCODE);
           finish = getIntent().getBooleanExtra(FINISH,true);
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
                Toast.makeText(LanguageDownloadActivity.this,"Language Pack Downloaded",Toast.LENGTH_SHORT).show();
                if(finish)
                startActivity(new Intent(LanguageDownloadActivity.this,SplashActivity.class));
                finish();
            }


        };

        Toast.makeText(LanguageDownloadActivity.this,"Downloading Language Pack",Toast.LENGTH_SHORT).show();

        if(langCode != null) {
            try {
                manager = new DownloadManager(langCode, this, progressReciever);
                manager.start();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(manager != null)
            manager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(manager != null)
            manager.pause();
    }
}
