package com.dsource.idc.jellowintl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.dsource.idc.jellowintl.Utility.JellowTTSService;
import com.dsource.idc.jellowintl.Utility.SessionManager;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by Shruti on 09-08-2016.
 */
public class Intro extends AppIntro {
    private String mSysTtsLang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.intro_to_jellow)+"</font>"));
        startService(new Intent(getApplication(), JellowTTSService.class));
        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(SampleSlideFragment.newInstance(R.layout.intro, "intro"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro5, "intro5"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro2, "intro2"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro3, "intro3"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro4, "intro4"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro6, "intro6"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro7, "intro7"));

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        // addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        // OPTIONAL METHODS
        // Override bar/separator color/dots indicator color.
        setBarColor(getResources().getColor(R.color.colorIntro));
        setSeparatorColor(getResources().getColor(R.color.colorIntro));
        setIndicatorColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(false);
        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        //setVibrate(true);
        //setVibrateIntensity(30);
        mSysTtsLang="";
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        registerReceiver(receiver, filter);
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

    /*@Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        Intent intent = new Intent(Intro.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent intent = new Intent(Intro.this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        if(((SampleSlideFragment) newFragment).getLayoutName().equals("intro6") ||
                ((SampleSlideFragment) newFragment).getLayoutName().equals("intro7"))
            getSpeechLanguage("");
    }

    public void getStarted(View view) {
        SessionManager session = new SessionManager(this);
        if(session.getLanguage().equals(mSysTtsLang) ||
                (session.getLanguage().equals("en-rIN") && mSysTtsLang.equals("hi-rIN")) ) {
            session.setCompletedIntro(true);
            startActivity(new Intent(Intro.this, SplashActivity.class));
            finish();
        }else
            Toast.makeText(this, "Complete tts setup", Toast.LENGTH_SHORT).show();
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