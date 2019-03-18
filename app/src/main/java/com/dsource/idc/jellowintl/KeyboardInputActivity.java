package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.JellowTTSService;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;

/**
 * Created by user on 5/27/2016.
 */
public class KeyboardInputActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_input);
        enableNavigationBack();
        setActivityTitle(getString(R.string.getKeyboardControl));

        findViewById(R.id.abc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("KeyboardAct SerialABC");
                startActivity(new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS));
            }
        });

        findViewById(R.id.qwerty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("KeyboardAct Qwerty");
                startActivity(new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS));
            }
        });

        findViewById(R.id.default_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("KeyboardAct Save");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showInputMethodPicker();
                finish();
            }
        });
        int boldTxtLen = 7;
        SpannableString spannedStr = new SpannableString(getString(R.string.step1));
        if(getSession().getLanguage().equals(BN_IN)) boldTxtLen = 11;
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0, boldTxtLen,0);
        ((TextView)findViewById(R.id.t2)).setText(spannedStr);
        spannedStr = new SpannableString(getString(R.string.step2));
        if(getSession().getLanguage().equals(BN_IN)) boldTxtLen = 13;
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0, boldTxtLen,0);
        ((TextView)findViewById(R.id.t3)).setText(spannedStr);
        spannedStr = null;
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
            case R.id.languageSelect:
                if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, LanguageSelectActivity.class));
                } else {
                    startActivity(new Intent(this, LanguageSelectTalkBackActivity.class));
                }
                finish(); break;
            case R.id.settings:
                startActivity(new Intent(getApplication(), SettingActivity.class));
                finish(); break;
            case R.id.accessibilitySetting:
                startActivity(new Intent(this, AccessibilitySettingsActivity.class));
                finish(); break;
            case R.id.resetPreferences:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                finish(); break;
            case R.id.feedback:
                if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, FeedbackActivityTalkBack.class));
                }
                else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
                finish(); break;
            case android.R.id.home:
                finish(); break;
            default:
                return super.onOptionsItemSelected(item);
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
