package com.dsource.idc.jellowintl.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.dsource.idc.jellowintl.R;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class AccessibilitySettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_settings);
        enableNavigationBack();
        setupActionBarTitle(View.VISIBLE, getString(R.string.home)+"/ "+getString(R.string.menuAccessibility));
        setNavigationUiConditionally();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(AccessibilitySettingsActivity.class.getSimpleName());
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getUserId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer.
        stopMeasuring("AccessibilitySettingActivity");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void openSystemAccessibilitySetting(View view){
        try {
            startActivity(new Intent().setAction("android.settings.ACCESSIBILITY_SETTINGS"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void playInfoVideo(View view){
        String videoLink = view.equals(findViewById(R.id.btn_access_video_one)) ?
                "http://www.youtube.com/watch?v=QDU1Qp-u2Zs" : "http://www.youtube.com/watch?v=OJiOC0Wkvlk";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink)));
    }
}
