package com.dsource.idc.jellowintl.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

public class FeedbackActivityTalkBack extends BaseActivity{
    Spinner mEasyToUse, mClearPicture, mClearVoice, mEaseToNavigate;
    ArrayAdapter<CharSequence> adapter;
    private String strRateJellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_talkback);
        enableNavigationBack();
        setupActionBarTitle(View.VISIBLE, getString(R.string.home)+"/ "+getString(R.string.menuFeedback));
        setNavigationUiConditionally();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findViewById(R.id.comments).clearFocus();
        findViewById(R.id.tv1).setFocusable(true);
        findViewById(R.id.tv1).setFocusableInTouchMode(true);
        strRateJellow = getString(R.string.rate_jellow);
        addListenerOnSpinner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(FeedbackActivityTalkBack.class.getSimpleName());
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getUserId());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        addAccessibilityDelegateToSpinners();
    }

    public void addListenerOnSpinner() {
        adapter = ArrayAdapter.createFromResource(this, R.array.ratings,
                R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEasyToUse = findViewById(R.id.easytouse);
        mEasyToUse.setAdapter(adapter);
        mClearPicture = findViewById(R.id.clearpictures);
        mClearPicture.setAdapter(adapter);
        mClearVoice = findViewById(R.id.clearvoice);
        mClearVoice.setAdapter(adapter);
        mEaseToNavigate = findViewById(R.id.navigate);
        mEaseToNavigate.setAdapter(adapter);
    }

    public void sendFeedbackToDevelopers(View v){
        if((mEasyToUse != null) && (mClearPicture != null) && (mClearVoice != null) && (mEaseToNavigate != null)) {
            FirebaseDatabase mDB = FirebaseDatabase.getInstance();
            DatabaseReference mRef = mDB.getReference(BuildConfig.DB_TYPE +"/in-app-reviews");
            HashMap<String, Object> map = new HashMap<>();
            map.put("Easy to use", mEasyToUse.getSelectedItem());
            map.put("Clear pictures", mClearPicture.getSelectedItem());
            map.put("Clear voice", mClearVoice.getSelectedItem());
            map.put("Easy to navigate", mEaseToNavigate.getSelectedItem());
            map.put("Platform", "Android");
            map.put("Comments", ((EditText)findViewById(R.id.comments)).getText().toString());
            mRef.child(mRef.push().getKey())
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jellowcommunicator@gmail.com"});
                        email.putExtra(Intent.EXTRA_SUBJECT, "Jellow Feedback");
                        email.putExtra(Intent.EXTRA_TEXT, "Easy to use: " + mEasyToUse.getSelectedItem()
                                + "\nClear Pictures: " + mClearPicture.getSelectedItem()
                                + "\nClear Voices: " + mClearVoice.getSelectedItem()
                                + "\nEasy to Navigate: " + mEaseToNavigate.getSelectedItem()
                                + "\n\nComments and Suggestions:-\n" +
                                ((EditText)findViewById(R.id.comments)).getText().toString());
                        email.setType("message/rfc822");
                        PackageManager packageManager = getPackageManager();
                        List<ResolveInfo> activities = packageManager.queryIntentActivities(email, 0);
                        boolean isIntentSafe = activities.size() > 0;
                        if (isIntentSafe)
                            startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        Toast.makeText(FeedbackActivityTalkBack.this,
                                getString(R.string.received_your_feedback), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        finish();
                    }
                });
        }else{
            Toast.makeText(FeedbackActivityTalkBack.this, strRateJellow, Toast.LENGTH_SHORT).show();
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

    private void addAccessibilityDelegateToSpinners() {
        mEasyToUse.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onInitializeAccessibilityEvent(host, event);
                if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                    findViewById(R.id.tv2).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }
            }
        });
        mClearPicture.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onInitializeAccessibilityEvent(host, event);
                if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                    findViewById(R.id.tv3).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }
            }
        });
        mClearVoice.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onInitializeAccessibilityEvent(host, event);
                if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                    findViewById(R.id.tv4).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }
            }
        });
        mEaseToNavigate.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            @Override
            public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onInitializeAccessibilityEvent(host, event);
                if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                    findViewById(R.id.tv5).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }
            }
        });
    }
}