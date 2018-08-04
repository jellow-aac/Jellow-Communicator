package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
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

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.github.paolorotolo.appintro.AppIntro;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;

/**
 * Created by Shruti on 09-08-2016.
 */
public class Intro extends AppIntro {
    private String mTTsDefaultLanguage, selectedLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.intro_to_jellow)+"</font>"));
        //Set the language database create preference
        new SessionManager(this).setLanguageChange(0);
        startService(new Intent(getApplication(), JellowTTSService.class));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro, "intro"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro5, "intro5"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro2, "intro2"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro3, "intro3"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro4, "intro4"));
        if(Build.VERSION.SDK_INT < 21) {
            addSlide(SampleSlideFragment.newInstance(R.layout.intro6, "intro6"));
        }
        addSlide(SampleSlideFragment.newInstance(R.layout.intro7, "intro7"));

        setBarColor(getResources().getColor(R.color.colorIntro));
        setSeparatorColor(getResources().getColor(R.color.colorIntro));
        setIndicatorColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));
        showSkipButton(false);
        setProgressButtonEnabled(false);

        selectedLanguage = getString(R.string.txt_intro6_skipActiveTtsDesc)
                                .replace("-", getSelectedLanguage("-"))
                                    .replace("_", getSelectedLanguage("_"));
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)))
            startService(new Intent(getApplication(), JellowTTSService.class));
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
                    mTTsDefaultLanguage = intent.getStringExtra("systemTtsRegion");
                    break;
                case "com.dsource.idc.jellowintl.TTS_ENGINE_NAME_RES":
                    break;
            }
        }
    };

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        Crashlytics.log("Slide visible:"+((SampleSlideFragment) newFragment).getLayoutName());
        if (Build.VERSION.SDK_INT < 21)
            if(((SampleSlideFragment) newFragment).getLayoutName().equals("intro6")){
                ((TextView) findViewById(R.id.tvtop1)).setText(getString(R.string.txt_intro6_step2)
                        .replace("_",getSelectedLanguage("_")));
                ((TextView) findViewById(R.id.tvtop2)).setText(getString(R.string.txt_intro6_step3)
                        .replace("_",getSelectedLanguage("_")));
                getTextToSpeechEngineLanguage("");
                ((TextView) findViewById(R.id.tx_downloadMsg)).setText(selectedLanguage);
            }else if(((SampleSlideFragment) newFragment).getLayoutName().equals("intro7")){
                getTextToSpeechEngineLanguage("");
                ((TextView) findViewById(R.id.tx_downloadMsg)).setText(selectedLanguage);
        }
    }

    private String getSelectedLanguage(String replaceChar) {
        switch (new SessionManager(this).getLanguage()){
            case ENG_IN:
                if(replaceChar.equals("-"))
                    return "English (IN)";
            case HI_IN:
                return "Hindi (IN)";
            case ENG_UK:
                return "English (UK)";
            case ENG_US:
                return "English (US)";
            default: return "";
        }
    }

    public void getStarted(View view) {
        SessionManager session = new SessionManager(this);
        if(Build.VERSION.SDK_INT < 21) {
            if ((session.getLanguage().equals(ENG_IN) && mTTsDefaultLanguage.equals(HI_IN)) ||
                (!session.getLanguage().equals(ENG_IN) && session.getLanguage().equals(mTTsDefaultLanguage))) {
                session.setCompletedIntro(true);
                Intent intent=new Intent(Intro.this, SplashActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, getString(R.string.txt_set_tts_setup), Toast.LENGTH_LONG).show();
                getPager().setCurrentItem(5, true);
            }
        }else {
            session.setCompletedIntro(true);
            Intent intent=new Intent(Intro.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void changeDemoScreen(View view){
        if (view.getId() == R.id.btnMoveLeft)
            getPager().setCurrentItem(getPager().getCurrentItem()-1, true);
        else
            getPager().setCurrentItem(getPager().getCurrentItem()+1, true);

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

    /**
     *<p> This function send the broadcast request(to {@link JellowTTSService} class) to get
     *  currently selected (default) Text-to-speech engine for device, Text-to-speech engine
     *  selected (default) language and other data.
     *  Other extra data are
     *  "saveUserLanguage" is boolean value, if it set to true then Text-to-speech engine language and
     *  language selected by use inside app are same. This elucidate app have correct configuration
     *  for Text-to-speech engine and app can proceed to save user language directly.
     *
     * When {@link JellowTTSService} receives the request and send the response back
     * this class. The broadcast receiver in this class when receives the response it
     * stores an engine name to "mTTsDefaultEngineName" string variable.
     * This function is called only when user device have android version from Kitkat and
     * below to supported api level (16).
     * @param saveLanguage </p>
     * **/
    private void getTextToSpeechEngineLanguage(String saveLanguage){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_REQ");
        intent.putExtra("saveSelectedLanguage", saveLanguage);
        sendBroadcast(intent);
    }
}
