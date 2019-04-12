package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

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
    protected void onResume() {
        super.onResume();
        setVisibleAct(KeyboardInputActivity.class.getSimpleName());
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
