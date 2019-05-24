package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.utility.SpeechUtils;
import com.dsource.idc.jellowintl.utility.TextToSpeechErrorUtils;
import com.github.paolorotolo.appintro.AppIntro;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.dsource.idc.jellowintl.utility.SessionManager.BE_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

/**
 * Created by Shruti on 09-08-2016.
 */
public class Intro extends AppIntro {
    private String mTTsDefaultLanguage, selectedLanguage;
    private boolean isOpenedSettingFromIntro8 = false;
    private String toastMsg,intro_title, intro_caption,
        intro2_title, intro2_caption, intro3_title, intro3_caption, intro4_title, intro4_caption,
        intro5_title, intro5_caption, intro7title, intro6_imgTxt1, intro6_imgTxt2,
        intro6_imgTxt3, intro8title, intro8_imgTxt1, intro8_imgTxt2, intro8_imgTxt3, intro8_btn,
        intro6_btn_bottom, intro6_btn_bottom1,intro6_btn_bottom3, intro7_btn_getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.intro_to_jellow)+"</font>"));
        //Set the language database create preference
        SessionManager manager = new SessionManager(this);
        manager.setLanguageChange(0);
        startService(new Intent(getApplication(), JellowTTSService.class));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro, "intro"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro5, "intro5"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro2, "intro2"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro3, "intro3"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro4, "intro4"));
        if(!SpeechUtils.isNoTTSLanguage(this))
            addSlide(SampleSlideFragment.newInstance(R.layout.intro8, "intro8"));
        else
            isOpenedSettingFromIntro8 = true;
        if(Build.VERSION.SDK_INT < 21
                && !(manager.getLanguage().equals(MR_IN))) {
            addSlide(SampleSlideFragment.newInstance(R.layout.intro6, "intro6"));
        }
        addSlide(SampleSlideFragment.newInstance(R.layout.intro7, "intro7"));

        setBarColor(getResources().getColor(R.color.app_background));
        setSeparatorColor(getResources().getColor(R.color.app_background));
        setIndicatorColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));
        showSkipButton(false);
        setProgressButtonEnabled(false);
        getViewResource();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        filter.addAction("com.dsource.idc.jellowintl.INIT_SERVICE_ERR");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseActivity ba = new BaseActivity();
        if(!ba.isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)))
            startService(new Intent(getApplication(), JellowTTSService.class));
        if(ba.isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))
                && isOpenedSettingFromIntro8){
            findViewById(R.id.btnMoveRight).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
        }
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
                case "com.dsource.idc.jellowintl.INIT_SERVICE_ERR":
                    new TextToSpeechErrorUtils(Intro.this)
                            .showErrorDialog();
                    break;
            }
        }
    };

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable final Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        Crashlytics.log("Slide visible:"+((SampleSlideFragment) newFragment).getLayoutName());
        if (Build.VERSION.SDK_INT < 21
                /*&& !(new SessionManager(this).getLanguage().equals(MR_IN))*/)
            if(((SampleSlideFragment) newFragment).getLayoutName().equals("intro6") ||
                    ((SampleSlideFragment) newFragment).getLayoutName().equals("intro7")){
                getTextToSpeechEngineLanguage("");
        }
        setupNextSlide((SampleSlideFragment) newFragment);
    }

    private String getSelectedLanguage() {
        switch (new SessionManager(this).getLanguage()){
            case ENG_IN:
                return "English (IN)";
            case HI_IN:
                return "Hindi (IN)";
            case ENG_UK:
                return "English (UK)";
            case ENG_US:
                return "English (US)";
            case ENG_AU:
                return "English (AU)";
            case BE_IN:
            case BN_IN:
                return "Bengali (IN)";
            default: return "";
        }
    }

    public void getStarted(View view) {
        SessionManager session = new SessionManager(this);
        if(!isOpenedSettingFromIntro8) {
            Toast.makeText(Intro.this, toastMsg, Toast.LENGTH_LONG).show();
            getPager().setCurrentItem(5, true);
        }else if(Build.VERSION.SDK_INT < 21) {
            if(!mTTsDefaultLanguage.equals("-r") ||
                (session.getLanguage().equals(mTTsDefaultLanguage)) ||
                    (session.getLanguage().equals(BN_IN) &&
                        ( mTTsDefaultLanguage.equals(BN_IN) || (mTTsDefaultLanguage.equals(BE_IN) ))) ||
                            session.getLanguage().equals(MR_IN)) {
                session.setCompletedIntro(true);
                Intent intent=new Intent(Intro.this, SplashActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Intro.this, toastMsg, Toast.LENGTH_LONG).show();
                getPager().setCurrentItem(6, true);
            }
        }else {
            session.setCompletedIntro(true);
            Intent intent = new Intent(Intro.this, SplashActivity.class);
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

    public void openedTTsSetting(View view){
        isOpenedSettingFromIntro8 = true;
        getStarted1(view);
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

    private void getViewResource() {
        selectedLanguage = getString(R.string.txt_intro6_skipActiveTtsDesc)
                .replace("-", getSelectedLanguage())
                .replace("_", getSelectedLanguage());
        toastMsg = getString(R.string.txt_set_tts_setup);
        intro_title = getString(R.string.txt_intro1_central9btn);
        intro_caption = getString(R.string.txt_intro1_categorybtn);

        intro2_title = getString(R.string.txt_intro2_appUsageDesc);
        intro2_caption = getString(R.string.txt_intro2_speakUsingJellow);

        intro3_title = getString(R.string.txt_intro3_level2CatDesc);
        intro3_caption = getString(R.string.txt_intro3_navWithJellow);

        intro4_title = getString(R.string.txt_intro4_customizeAppDesc);
        intro4_caption = getString(R.string.txt_intro4_customizeJellow);

        intro5_title = getString(R.string.txt_intro5_jellowUsageDesc);
        intro5_caption = getString(R.string.txt_intro5_expressiveBtn);

        intro6_imgTxt1 = getString(R.string.txt_intro6_ttsStep1);
        intro6_btn_bottom = getString(R.string.txt_intro6_activate);
        intro6_imgTxt2 = getString(R.string.txt_intro6_step2)
                .replace("_",getSelectedLanguage());
        intro6_btn_bottom1 = getString(R.string.txt_intro6_changeLang);
        intro6_imgTxt3 = getString(R.string.txt_intro6_step3)
                .replace("_",getSelectedLanguage());
        intro6_btn_bottom3 = getString(R.string.txt_intro6_download);

        intro7title = getString(R.string.txt_intro7_getStartedDesc);
        intro7_btn_getStarted = getString(R.string.txt_intro7_getStarted);

        intro8title = getString(R.string.txt_intro8_txtTitle);
        intro8_imgTxt1 = getString(R.string.txt_intro8_step1);
        intro8_imgTxt2 = getString(R.string.txt_intro8_step2);
        intro8_imgTxt3 = getString(R.string.txt_intro8_step3);
        intro8_btn = getString(R.string.txt_tts_stting);
    }

    public void setupNextSlide(SampleSlideFragment newFragment) {
        switch(newFragment.getLayoutName()){
            case "intro":
                setText2TextView(newFragment, R.id.tv_intro_title, intro_title);
                setText2TextView(newFragment, R.id.tv_intro_caption, intro_caption);
                break;
            case "intro2":
                setText2TextView(newFragment, R.id.tv_intro2_title, intro2_title);
                setText2TextView(newFragment, R.id.tv_intro2_caption, intro2_caption);
                break;
            case "intro3":
                setText2TextView(newFragment, R.id.tv_intro3_title, intro3_title);
                setText2TextView(newFragment, R.id.tv_intro3_caption, intro3_caption);
                break;
            case "intro4":
                setText2TextView(newFragment, R.id.tv_intro4_title, intro4_title);
                setText2TextView(newFragment, R.id.tv_intro4_caption, intro4_caption);
                break;
            case "intro5":
                setText2TextView(newFragment, R.id.tv_intro5_title, intro5_title);
                setText2TextView(newFragment, R.id.tv_intro5_caption, intro5_caption);
                break;
            case "intro6":
                setText2TextView(newFragment, R.id.tx_downloadMsg, selectedLanguage);
                setText2TextView(newFragment, R.id.tvtop, intro6_imgTxt1);
                setText2Button(newFragment, R.id.tvbottom, intro6_btn_bottom);
                setText2TextView(newFragment, R.id.tvtop1, intro6_imgTxt2);
                setText2Button(newFragment, R.id.tvbottom1, intro6_btn_bottom1);
                setText2TextView(newFragment, R.id.tvtop2, intro6_imgTxt3);
                setText2Button(newFragment, R.id.tvbottom3, intro6_btn_bottom3);
                break;
            case "intro7":
                setText2TextView(newFragment, R.id.intro7_tvtop, intro7title);
                setText2Button(newFragment, R.id.btn_getStarted, intro7_btn_getStarted);
                break;
            case "intro8":
                setText2TextView(newFragment, R.id.tv_intro8_title, intro8title);
                setText2TextView(newFragment, R.id.tv_intro8_imgTxt1, intro8_imgTxt1);
                setText2TextView(newFragment, R.id.tv_intro8_imgTxt2, intro8_imgTxt2);
                setText2TextView(newFragment, R.id.tv_intro8_imgTxt3, intro8_imgTxt3);
                setImgToImageView(newFragment, R.id.img1_intro8,R.drawable.tts_wifi_1);
                setImgToImageView(newFragment, R.id.img2_intro8,R.drawable.tts_wifi_2);
                setImgToImageView(newFragment, R.id.img3_intro8,R.drawable.tts_wifi_3);
                setText2Button(newFragment, R.id.btnTTsSetting, intro8_btn);
                break;
        }
    }

    private void setText2TextView(SampleSlideFragment parent, int tv, String text) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ((TextView) Objects.requireNonNull(parent.getView()).findViewById(tv)).setText(text);
            } else {
                ((TextView) parent.getView().findViewById(tv)).setText(text);
            }
        }catch(Exception e){
            Crashlytics.logException(e);
        }
    }

    private void setText2Button(SampleSlideFragment parent, int btn, String text) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ((Button) Objects.requireNonNull(parent.getView()).findViewById(btn)).setText(text);
            } else {
                ((Button) parent.getView().findViewById(btn)).setText(text);
            }
        }catch(Exception e){
            Crashlytics.logException(e);
        }
    }

    private void setImgToImageView(SampleSlideFragment parent, int view, int drawable) {
        try {
            ImageView imageView = parent.getView().findViewById(view);
            GlideApp.with(this)
                    .load(drawable)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .dontAnimate()
                    .into(imageView);
        }catch(Exception e){
            Crashlytics.logException(e);
        }
    }
}