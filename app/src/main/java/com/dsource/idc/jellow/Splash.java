package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dsource.idc.jellow.Utility.EvaluateDisplayMetricsUtils;

import java.util.Locale;

/**
 * Created by ekalpa on 7/12/2016.
 */

public class Splash extends AppCompatActivity {

    PlayGifView pGif;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        pGif = (PlayGifView) findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);

        final MainActivity mainActivity = new MainActivity();
        mainActivity.mTts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mainActivity.mTts.setEngineByPackageName("com.google.android.mTts");
                    mainActivity.mTts.setLanguage(new Locale("hin", "IND"));
                }
            }
        });

        new CountDownTimer(7000, 1) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                Intent intent = new Intent(Splash.this, MainActivity.class); // IDC
                startActivity(intent);
                Toast.makeText(Splash.this, "App is loading...", Toast.LENGTH_SHORT).show();
            }
        }.start();
        new EvaluateDisplayMetricsUtils(this).calculateStoreDeviceHeightWidth();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}
