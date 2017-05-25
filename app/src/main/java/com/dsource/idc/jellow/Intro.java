package com.dsource.idc.jellow;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by Shruti on 09-08-2016.
 */

public class Intro extends AppIntro {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getApplicationContext());
        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro5));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));
        addSlide(SampleSlide.newInstance(R.layout.intro6));
        addSlide(SampleSlide.newInstance(R.layout.intro7));

        //if the session is logged in: then directly go to the main activity
        if (session.isLoggedIn1()) {
            // User is already logged in. Take him to main activity
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            Toast.makeText(Intro.this, "App is loading...", Toast.LENGTH_LONG).show();
        }
//        addSlide(new InputDemoSlide());

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
//        addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        // OPTIONAL METHODS
        // Override bar/separator color/dots indicator color.
        setBarColor(Color.parseColor("#F7F3C6"));
        setSeparatorColor(Color.parseColor("#F7F3C6"));
        setIndicatorColor(Color.parseColor("#DC5252"), Color.parseColor("#DC5252"));

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
        session.setLogin1(true);
        Intent intent = new Intent(Intro.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}