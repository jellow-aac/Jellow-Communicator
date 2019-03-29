package com.dsource.idc.jellowintl;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

public class FeedbackActivity extends BaseActivity {
    private RatingBar mRatingEasyToUse;
    private Button mBtnSubmit;
    private EditText mEtComments;
    private String strEaseOfUse, mClearPicture, mClearVoice, mEaseToNav;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        enableNavigationBack();
        setActivityTitle(getString(R.string.menuFeedback));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findViewById(R.id.comments).clearFocus();
        mBtnSubmit = findViewById(R.id.bSubmit);
        addListenerOnRatingBar();
        addListenerOnButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(FeedbackActivity.class.getSimpleName());
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
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
