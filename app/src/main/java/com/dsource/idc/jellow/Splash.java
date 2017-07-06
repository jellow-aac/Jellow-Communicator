package com.dsource.idc.jellow;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.dsource.idc.jellow.Utility.EvaluateDisplayMetricsUtils;
import com.dsource.idc.jellow.Utility.JellowTTSService;
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
        getSupportActionBar().hide();
        startService(new Intent(this, JellowTTSService.class));
        PlayGifView pGif = (PlayGifView) findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);
        setLocale(new Locale(getString(R.string.locale_lang_hi),getString(R.string.locale_reg_IN)));

        if((new SessionManager(this).getLanguage()) == 0)
            setLocale(Locale.US);
        else
            setLocale(new Locale(getString(R.string.locale_lang_hi),getString(R.string.locale_reg_IN)));

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
