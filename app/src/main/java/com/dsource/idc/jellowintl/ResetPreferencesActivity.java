package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.DataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowTTSService;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

/**
 * Created by ekalpa on 15-Jun-16.
 **/
public class ResetPreferencesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_preferences);
        enableNavigationBack();
        setActivityTitle(getString(R.string.menuResetPref));
        final DataBaseHelper myDbHelper = new DataBaseHelper(this);

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
                getSession().resetUserPeoplePlacesPreferences();
                getSession().setCompletedDbOperations(false);
                getSession().setLanguageChange(0);
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                Crashlytics.log("ResetPref Yes");
                finishAffinity();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                finish(); break;
            case R.id.aboutJellow:
                startActivity(new Intent(this, AboutJellowActivity.class));
                finish(); break;
            case R.id.tutorial:
                startActivity(new Intent(this, TutorialActivity.class));
                finish(); break;
            case R.id.keyboardInput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                finish(); break;
            case R.id.languageSelect:
                if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, LanguageSelectActivity.class));
                } else {
                    startActivity(new Intent(this, LanguageSelectTalkBackActivity.class));
                }
                finish(); break;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                finish(); break;
            case R.id.accessibilitySetting:
                startActivity(new Intent(this, AccessibilitySettingsActivity.class));
                finish(); break;
            case R.id.feedback:
                if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, FeedbackActivityTalkBack.class));
                } else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
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
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
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
