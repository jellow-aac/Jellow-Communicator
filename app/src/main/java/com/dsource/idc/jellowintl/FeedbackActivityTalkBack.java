package com.dsource.idc.jellowintl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

public class FeedbackActivityTalkBack extends BaseActivity{

    Spinner mEasyToUse, mClearPictures, mClearVoice, mNavigate;
    Button mBtnSubmit;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_talkback);
        enableNavigationBack();
        setActivityTitle(getString(R.string.menuFeedback));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findViewById(R.id.comments).clearFocus();
        findViewById(R.id.tv1).setFocusable(true);
        findViewById(R.id.tv1).setFocusableInTouchMode(true);

        mBtnSubmit = findViewById(R.id.bSubmit);
        addListenerOnSpinner();
        addListenerOnButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(FeedbackActivityTalkBack.class.getSimpleName());
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
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
        mClearPictures = findViewById(R.id.clearpictures);
        mClearPictures.setAdapter(adapter);
        mClearVoice = findViewById(R.id.clearvoice);
        mClearVoice.setAdapter(adapter);
        mNavigate = findViewById(R.id.navigate);
        mNavigate.setAdapter(adapter);
    }

    public void addListenerOnButton() {
        final String strRateJellow = getString(R.string.rate_jellow);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mEasyToUse != null) && (mClearPictures != null) && (mClearVoice != null) && (mNavigate != null)) {
                    String cs = ((EditText)findViewById(R.id.comments)).getText().toString();
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dsource.in@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Jellow Feedback");
                    email.putExtra(Intent.EXTRA_TEXT, "Easy to use: " + mEasyToUse.getSelectedItem() +
                            "\nClear Pictures: " + mClearPictures.getSelectedItem() + "\nClear Voices: "
                            + mClearVoice.getSelectedItem() + "\nEasy to Navigate: " + mNavigate.getSelectedItem() +
                            "\n\nComments and Suggestions:-\n" + cs);
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));

                } else {
                    Toast.makeText(FeedbackActivityTalkBack.this,
                            strRateJellow, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        mClearPictures.setAccessibilityDelegate(new View.AccessibilityDelegate() {
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
        mNavigate.setAccessibilityDelegate(new View.AccessibilityDelegate() {
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
