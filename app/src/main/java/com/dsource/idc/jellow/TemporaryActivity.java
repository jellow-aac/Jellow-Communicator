package com.dsource.idc.jellow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dsource.idc.jellow.Utility.DownloadManager;

public class TemporaryActivity extends AppCompatActivity {
    DownloadManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporary);

        DownloadManager.ProgressReciever progressReciever = new DownloadManager.ProgressReciever() {
            @Override
            public void onprogress(int soFarBytes, int totalBytes) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(TemporaryActivity.this,"Completed",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TemporaryActivity.this,SplashActivity.class));
            }
        };


        manager = new DownloadManager("en-rIN",this,progressReciever);
        manager.start();


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
