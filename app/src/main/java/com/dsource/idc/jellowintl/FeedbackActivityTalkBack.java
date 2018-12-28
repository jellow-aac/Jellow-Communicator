package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.MainActivity.isAccessibilityTalkBackOn;
import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

public class FeedbackActivityTalkBack extends AppCompatActivity{

    Spinner mEasyToUse, mClearPictures, mClearVoice, mNavigate;
    Button mBtnSubmit;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_feedback_talkback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>" + getString(R.string.menuFeedback) + "</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findViewById(R.id.comments).clearFocus();
        findViewById(R.id.tv1).setFocusable(true);
        findViewById(R.id.tv1).setFocusableInTouchMode(true);

        mBtnSubmit = findViewById(R.id.bSubmit);
        addListenerOnSpinner();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.languageSelect:
                if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, LanguageSelectActivity.class));
                } else {
                    startActivity(new Intent(this, LanguageSelectTalkBackActivity.class));
                }
                finish();
                break;
            case R.id.profile:
                startActivity(new Intent(FeedbackActivityTalkBack.this, ProfileFormActivity.class));
                finish();
                break;
            case R.id.info:
                startActivity(new Intent(FeedbackActivityTalkBack.this, AboutJellowActivity.class));
                finish();
                break;
            case R.id.usage:
                startActivity(new Intent(FeedbackActivityTalkBack.this, TutorialActivity.class));
                finish();
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(FeedbackActivityTalkBack.this, KeyboardInputActivity.class));
                finish();
                break;
            case R.id.settings:
                startActivity(new Intent(FeedbackActivityTalkBack.this, SettingActivity.class));
                finish();
                break;
            case R.id.reset:
                startActivity(new Intent(FeedbackActivityTalkBack.this, ResetPreferencesActivity.class));
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            resetAnalytics(this, new SessionManager(this).getCaregiverNumber().substring(1));
        }
        if (!isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
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
