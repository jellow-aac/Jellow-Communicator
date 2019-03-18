package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.utility.JellowTTSService;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

/**
 * Created by user on 6/6/2016.
 */
public class TutorialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        enableNavigationBack();
        setActivityTitle(getString(R.string.menuTutorials));
        setImagesToImageViewUsingGlide();
    }

    private void setImagesToImageViewUsingGlide() {
        setImageUsingGlide(R.drawable.categorybuttons, ((ImageView)findViewById(R.id.pic1)));
        setImageUsingGlide(R.drawable.expressivebuttons, ((ImageView)findViewById(R.id.pic2)));
        setImageUsingGlide(R.drawable.speakingwithjellowimage2, ((ImageView)findViewById(R.id.pic4)));
        setImageUsingGlide(R.drawable.eatingcategory1, ((ImageView)findViewById(R.id.pic5)));
        setImageUsingGlide(R.drawable.eatingcategory2, ((ImageView)findViewById(R.id.pic6)));
        setImageUsingGlide(R.drawable.eatingcategory3, ((ImageView)findViewById(R.id.pic7)));
        setImageUsingGlide(R.drawable.settings, ((ImageView)findViewById(R.id.pic8)));
        setImageUsingGlide(R.drawable.sequencewithoutexpressivebuttons, ((ImageView)findViewById(R.id.pic9)));
        setImageUsingGlide(R.drawable.sequencewithexpressivebuttons, ((ImageView)findViewById(R.id.pic10)));
        setImageUsingGlide(R.drawable.gtts1, ((ImageView)findViewById(R.id.gtts1)));
        setImageUsingGlide(R.drawable.gtts2, ((ImageView)findViewById(R.id.gtts2)));
        setImageUsingGlide(R.drawable.gtts3, ((ImageView)findViewById(R.id.gtts3)));
    }

    private void setImageUsingGlide(int image, ImageView imgView) {
        GlideApp.with(this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(imgView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                finish(); break;
            case R.id.aboutJellow:
                startActivity(new Intent(this, AboutJellowActivity.class));
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
            case R.id.resetPreferences:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
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
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
        if(!isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        startMeasuring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        stopMeasuring("TutorialActivity");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
