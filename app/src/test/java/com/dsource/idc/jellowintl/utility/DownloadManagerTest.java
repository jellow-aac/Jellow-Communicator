package com.dsource.idc.jellowintl.utility;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DownloadManagerTest {
    DownloadManager.ProgressReciever progressReciever;
    DownloadManager downloadManager;
    Context context;

    @Before
    public void setup(){
        progressReciever = new DownloadManager.ProgressReciever() {
            @Override
            public void onprogress(int soFarBytes, int totalBytes) {

            }

            @Override
            public void onComplete() {
                assert true;
            }
        };

        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
    }


    @Test
    public void legacyTests(){
        downloadManager = new DownloadManager("test",context,progressReciever);
        downloadManager.start();
        for(int i=0;i<9999;i++){
            // wait for download
        }
        downloadManager.pause();
        for(int i=0;i<9999;i++){
            // wait for download
        }
        downloadManager.resume();
    }
}
