package com.dsource.idc.jellowintl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.DataBaseHelper;

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
    protected void onResume() {
        super.onResume();
        setVisibleAct(ResetPreferencesActivity.class.getSimpleName());
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
