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
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.DataBaseHelper;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.MainActivity.isAccessibilityTalkBackOn;
import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;

/**
 * Created by ekalpa on 15-Jun-16.
 **/
public class ResetPreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_reset_preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+ getString(R.string.menuResetPref) +"</font>"));
        final SessionManager session = new SessionManager(this);
        final DataBaseHelper myDbHelper = new DataBaseHelper(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        final String strIconsResetMsg = getString(R.string.iconsHasBeenReset);
        findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResetPreferencesActivity.this, strIconsResetMsg, Toast.LENGTH_SHORT).show();
                myDbHelper.delete();
                session.resetUserPeoplePlacesPreferences();
                session.setCompletedDbOperations(false);
                session.setLanguageChange(0);
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                Crashlytics.log("ResetPref Yes");
                finishAffinity();
            }
        });
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
        if(session.getLanguage().equals(BN_IN))
            menu.findItem(R.id.keyboardinput).setVisible(false);
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            menu.findItem(R.id.closePopup).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.languageSelect:
                if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, LanguageSelectActivity.class));
                } else {
                    startActivity(new Intent(this, LanguageSelectTalkBackActivity.class));
                }
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
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                finish(); break;
            case R.id.feedback:
                if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, FeedbackActivityTalkBack.class));
                } else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
                finish();
                break;
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
}
