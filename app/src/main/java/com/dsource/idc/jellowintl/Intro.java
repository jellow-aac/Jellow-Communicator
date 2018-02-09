package com.dsource.idc.jellowintl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dsource.idc.jellowintl.Utility.JellowTTSService;
import com.dsource.idc.jellowintl.Utility.SessionManager;
import com.github.paolorotolo.appintro.AppIntro;

import static com.dsource.idc.jellowintl.Utility.SessionManager.LangValueMap;

/**
 * Created by Shruti on 09-08-2016.
 */
public class Intro extends AppIntro {
    private String mSysTtsLang, selectedLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.intro_to_jellow)+"</font>"));
        startService(new Intent(getApplication(), JellowTTSService.class));

        addSlide(SampleSlideFragment.newInstance(R.layout.intro, "intro"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro5, "intro5"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro2, "intro2"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro3, "intro3"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro4, "intro4"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro6, "intro6"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro7, "intro7"));

        setBarColor(getResources().getColor(R.color.colorIntro));
        setSeparatorColor(getResources().getColor(R.color.colorIntro));
        setIndicatorColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));

        showSkipButton(false);
        setProgressButtonEnabled(false);

        selectedLanguage = getString(R.string.txt_intro6_skipActiveTtsDesc)+" "+getTTsLanguage();
        mSysTtsLang="";
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        registerReceiver(receiver, filter);
    }

    private String getTTsLanguage() {
        switch (new SessionManager(this).getLanguage()){
            case SessionManager.ENG_IN:
            case SessionManager.HI_IN:
                return "Hindi (India)";
            case SessionManager.ENG_UK:
                return LangValueMap.get(SessionManager.ENG_UK);
            case SessionManager.ENG_US:
                return LangValueMap.get(SessionManager.ENG_US);
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES":
                    mSysTtsLang = intent.getStringExtra("systemTtsRegion");
                    break;
            }
        }
    };

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        if (((SampleSlideFragment) newFragment).getLayoutName().equals("intro6") ||
                ((SampleSlideFragment) newFragment).getLayoutName().equals("intro7")) {
            if(Build.VERSION.SDK_INT < 23) {
                getSpeechLanguage("");
                ((TextView) findViewById(R.id.tx_downloadMsg)).setText(selectedLanguage);
            }else
                ((TextView) findViewById(R.id.tx_downloadMsg)).setText(getString(R.string.txt_intro6_skipTtsSetup));
        }
    }

    public void getStarted(View view) {
        SessionManager session = new SessionManager(this);
        if(Build.VERSION.SDK_INT < 23) {
            if ((session.getLanguage().equals("en-rIN") && mSysTtsLang.equals("hi-rIN")) ||
                    (!session.getLanguage().equals("en-rIN") && session.getLanguage().equals(mSysTtsLang))) {
                session.setCompletedIntro(true);
                startActivity(new Intent(Intro.this, SplashActivity.class));
                finish();
            } else
                Toast.makeText(this, getString(R.string.txt_set_tts_setup), Toast.LENGTH_LONG).show();
        }else {
            session.setCompletedIntro(true);
            startActivity(new Intent(Intro.this, SplashActivity.class));
            finish();
        }
    }

    public void getStarted1(View view){
        Intent intent = new Intent();
        intent.setAction("com.android.settings.TTS_SETTINGS");
        startActivity(intent);
    }

    public void getStarted2(View view){
        getStarted1(view);
    }

    public void getStarted3(View view){
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getSpeechLanguage(String saveLanguage){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_REQ");
        intent.putExtra("saveSelectedLanguage", saveLanguage);
        sendBroadcast(intent);
    }
}