package com.dsource.idc.jellowintl.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;

import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

public class FeedbackActivity extends BaseActivity {
    private RatingBar mRatingEasyToUse;
    private EditText mEtComments;
    private float strEaseOfUse=0f, stClearPicture=0f, strClearVoice=0f, strEaseToNav=0f;
    private String strRateJellow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        enableNavigationBack();
        setupActionBarTitle(View.VISIBLE, getString(R.string.home)+"/ "+getString(R.string.menuFeedback));
        setNavigationUiConditionally();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findViewById(R.id.comments).clearFocus();
        mRatingEasyToUse = findViewById(R.id.easy_to_use);
        mEtComments = findViewById(R.id.comments);
        strRateJellow = getString(R.string.rate_jellow);
        addListenerOnRatingBar();
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(FeedbackActivity.class.getSimpleName());
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getUserId());
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
                strEaseOfUse = rating;
            }
        });
        pictures.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                stClearPicture = rating;
            }
        });
        voice.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                strClearVoice = rating;
            }
        });
        navigate.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                strEaseToNav = rating;
            }
        });
    }

    public void sendFeedbackToDevelopers(View v){
        if(strEaseOfUse!=0f&&stClearPicture!=0f&&strClearVoice!=0f&&strEaseToNav!=0f) {
            FirebaseDatabase mDB = FirebaseDatabase.getInstance();
            DatabaseReference mRef = mDB.getReference(BuildConfig.DB_TYPE +"/in-app-reviews");
            HashMap<String, Object> map = new HashMap<>();
            map.put("Easy to use", strEaseOfUse);
            map.put("Clear pictures", stClearPicture);
            map.put("Clear voice", strClearVoice);
            map.put("Easy to navigate", strEaseToNav);
            map.put("Platform", "Android");
            map.put("Comments", mEtComments.getText().toString());
            mRef.child(mRef.push().getKey()).setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jellowcommunicator@gmail.com"});
                        email.putExtra(Intent.EXTRA_SUBJECT, "Jellow Feedback");
                        email.putExtra(Intent.EXTRA_TEXT, "Easy to use: " + strEaseOfUse
                                + "\nClear Pictures: " + stClearPicture
                                + "\nClear Voices: " + strClearVoice + "\nEasy to Navigate: "
                                + strEaseToNav + "\n\nComments and Suggestions:-\n" +
                                mEtComments.getText().toString());
                        email.setType("message/rfc822");
                        PackageManager packageManager = getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(email, 0);
                        boolean isIntentSafe = activities.size() > 0;
                        if (isIntentSafe)
                            startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        Toast.makeText(FeedbackActivity.this,
                                getString(R.string.received_your_feedback), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        }else{
            Toast.makeText(FeedbackActivity.this, strRateJellow, Toast.LENGTH_SHORT).show();
        }
    }

    public void rateTheJellowApp(View v){
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(goToMarket);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void shareJellowApp(View v){
        String message = getString(R.string.share_string);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}