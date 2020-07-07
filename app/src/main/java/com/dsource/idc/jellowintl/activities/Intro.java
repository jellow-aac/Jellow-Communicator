package com.dsource.idc.jellowintl.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.github.paolorotolo.appintro.AppIntro;

import java.util.Objects;

/**
 * Created by Shruti on 09-08-2016. Modified by Rahul on 16-09-2019
 */
public class Intro extends AppIntro {
    private String intro_title, intro_caption, intro2_title, intro2_caption, intro3_title,
        intro3_caption, intro4_title, intro4_caption, intro5_title, intro5_caption, intro7title,
        intro7_btn_getStarted;
    private SpeechEngineBaseActivity parentAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.intro_to_jellow)+"</font>"));
        parentAct = new SpeechEngineBaseActivity();
        parentAct.setVisibleAct(Intro.class.getSimpleName());
        //Set the language database create preference
        parentAct.getSession().setLanguageDataUpdateState(parentAct.getSession().getLanguage(),
                GlobalConstants.LANGUAGE_STATE_CREATE_DB);
        addSlide(SampleSlideFragment.newInstance(R.layout.intro, "intro"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro5, "intro5"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro2, "intro2"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro3, "intro3"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro4, "intro4"));
        addSlide(SampleSlideFragment.newInstance(R.layout.intro7, "intro7"));

        setBarColor(getResources().getColor(R.color.app_background));
        setSeparatorColor(getResources().getColor(R.color.app_background));
        setIndicatorColor(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));
        showSkipButton(false);
        setProgressButtonEnabled(false);
        getViewResource();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(parentAct.isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
            findViewById(R.id.btnMoveRight).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isNotchDevice()) {
            getWindow().setNavigationBarColor(getColor(R.color.app_background));
        }else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));
        }
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable final Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        Crashlytics.log("Slide visible:"+((SampleSlideFragment) newFragment).getLayoutName());
        setupNextSlide((SampleSlideFragment) newFragment);
    }

    private void getViewResource() {
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

        intro7title = getString(R.string.txt_intro7_getStartedDesc);
        intro7_btn_getStarted = getString(R.string.txt_intro7_getStarted);
    }

    private void setupNextSlide(SampleSlideFragment newFragment) {
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
            case "intro7":
                setText2TextView(newFragment, R.id.intro7_tvtop, intro7title);
                setText2Button(newFragment, R.id.btn_getStarted, intro7_btn_getStarted);
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

    private boolean isNotchDevice(){
        float aspectRatio = (float)this.getResources().getDisplayMetrics().widthPixels /
                ((float)this.getResources().getDisplayMetrics().heightPixels);
        return (aspectRatio >= 2.0 && aspectRatio <= 2.15);
    }


    public void getStarted(View view) {
        parentAct.getSession().setCompletedIntro(true);
        Intent intent = new Intent(Intro.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    public void changeDemoScreen(View view){
        if (view.getId() == R.id.btnMoveLeft)
            getPager().setCurrentItem(getPager().getCurrentItem()-1, true);
        else
            getPager().setCurrentItem(getPager().getCurrentItem()+1, true);
    }
}