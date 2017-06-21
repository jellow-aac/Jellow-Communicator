package com.dsource.idc.jellow;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.dsource.idc.jellow.Utility.EvaluateDisplayMetricsUtils;
import com.dsource.idc.jellow.Utility.SessionManager;

import java.util.Locale;

/**
 * Created by ekalpa on 7/12/2016.
 */

public class Splash extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        PlayGifView pGif = (PlayGifView) findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);
        if((new SessionManager(this).getLanguage()) == 0)
            setLocale(Locale.US);
        else
            setLocale(new Locale(getString(R.string.locale_lang_hi),getString(R.string.locale_reg_IN)));

        /*final MainActivity mainActivity = new MainActivity();
        mainActivity.mTts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mainActivity.mTts.setEngineByPackageName("com.google.android.tts");
                    mainActivity.mTts.setLanguage(new Locale("hin", "IND"));
                }
            }
        });*/

        new CountDownTimer(7000, 1) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        }.start();
        EvaluateDisplayMetricsUtils displayMetricsUtils = new EvaluateDisplayMetricsUtils(this);
        displayMetricsUtils.calculateStoreDeviceHeightWidth();
        displayMetricsUtils.calculateStoreShadowRadiusAndBorderWidth();
    }

    private void setLocale(Locale locale) {
        Configuration conf = getResources().getConfiguration();
        conf.locale = locale;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        getResources().updateConfiguration(conf, dm);
    }
}
