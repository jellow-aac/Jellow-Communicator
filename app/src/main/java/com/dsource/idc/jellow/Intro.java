package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.dsource.idc.jellow.Utility.SessionManager;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by Shruti on 09-08-2016.
 */
public class Intro extends AppIntro {
    private SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSession = new SessionManager(getApplicationContext());
        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro5));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));
        addSlide(SampleSlide.newInstance(R.layout.intro6));
        addSlide(SampleSlide.newInstance(R.layout.intro7));

        //if the mSession is logged in: then directly go to the main activity
        if (mSession.isLoggedIn1()) {
            startActivity(new Intent(this, Splash.class));
            finish();
            Toast.makeText(Intro.this, getString(R.string.appLoading), Toast.LENGTH_LONG).show();
        }
        // addSlide(new InputDemoSlide());
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
    }

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
        // Do something when the slide changes.
    }

    public void getStarted(View view) {
        //set boolean true on getting started
        mSession.setLogin1(true);
        startActivity(new Intent(Intro.this, Splash.class));
        finish();
    }
}