package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;

public class FeedbackActivity extends AppCompatActivity {
    private RatingBar mRatingEasyToUse;
    private Button mBtnSubmit;
    private EditText mEtComments;
    private String strEaseOfUse, mClearPicture, mClearVoice, mEaseToNav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>" + getString(R.string.menuFeedback) + "</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findViewById(R.id.comments).clearFocus();
        mBtnSubmit = findViewById(R.id.bSubmit);
        addListenerOnRatingBar();
        addListenerOnButton();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SessionManager session = new SessionManager(this);
        menu.findItem(R.id.closePopup).setVisible(false);
        if(session.getLanguage().equals(BN_IN))
            menu.findItem(R.id.keyboardinput).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.languageSelect:
                startActivity(new Intent(this, LanguageSelectActivity.class));
                finish(); break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                finish(); break;
            case R.id.info:
                startActivity(new Intent(this, AboutJellowActivity.class));
                finish(); break;
            case R.id.usage:
                startActivity(new Intent(this, TutorialActivity.class));
                finish(); break;
            case R.id.keyboardinput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                finish(); break;
            case R.id.settings: startActivity(new Intent(this, SettingActivity.class));
                finish(); break;
            case R.id.reset:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                finish(); break;
            case android.R.id.home:
                finish(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            resetAnalytics(this, new SessionManager(this).getCaregiverNumber().substring(1));
        }
        if(!isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void addListenerOnRatingBar() {
        mRatingEasyToUse = findViewById(R.id.easy_to_use);
        RatingBar pictures = findViewById(R.id.pictures);
        RatingBar voice = findViewById(R.id.voice);
        RatingBar navigate = findViewById(R.id.navigate);
        if(Build.VERSION.SDK_INT < 23) {
            Drawable progress1 = mRatingEasyToUse.getProgressDrawable();
            DrawableCompat.setTint(progress1, Color.argb(255, 255, 204, 20));
            Drawable progress2 = pictures.getProgressDrawable();
            DrawableCompat.setTint(progress2, Color.argb(255, 255, 204, 20));
            Drawable progress3 = voice.getProgressDrawable();
            DrawableCompat.setTint(progress3, Color.argb(255, 255, 204, 20));
            Drawable progress4 = navigate.getProgressDrawable();
            DrawableCompat.setTint(progress4, Color.argb(255, 255, 204, 20));
        }
        mRatingEasyToUse.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                strEaseOfUse = String.valueOf(rating);
            }
        });
        pictures.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                mClearPicture = String.valueOf(rating);
            }
        });
        voice.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                mClearVoice = String.valueOf(rating);
            }
        });
        navigate.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                mEaseToNav = String.valueOf(rating);
            }
        });
    }

    public void addListenerOnButton() {
        mRatingEasyToUse = findViewById(R.id.easy_to_use);
        mEtComments = findViewById(R.id.comments);
        mBtnSubmit = findViewById(R.id.bSubmit);
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        final String strRateJellow = getString(R.string.rate_jellow);
        mBtnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if((strEaseOfUse != null) && (mClearPicture != null) && (mClearVoice != null) && (mEaseToNav != null) &&
                        (!strEaseOfUse.equals("0.0")) && (!mClearPicture.equals("0.0")) && (!mClearVoice.equals("0.0")) && (!mEaseToNav.equals("0.0"))) {
                    String cs = mEtComments.getText().toString();
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dsource.in@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Jellow Feedback");
                    email.putExtra(Intent.EXTRA_TEXT, "Easy to use: " + strEaseOfUse + "\nClear Pictures: " + mClearPicture + "\nClear Voices: " + mClearVoice + "\nEasy to Navigate: " + mEaseToNav + "\n\nComments and Suggestions:-\n" + cs);
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }else{
                    Toast.makeText(FeedbackActivity.this, strRateJellow, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
