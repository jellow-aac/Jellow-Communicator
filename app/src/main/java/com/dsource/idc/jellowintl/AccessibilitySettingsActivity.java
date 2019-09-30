package com.dsource.idc.jellowintl;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import com.dsource.idc.jellowintl.utility.DeveloperKey;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

public class AccessibilitySettingsActivity extends BaseActivity {
    private final String VISUAL_ACCESS_VIDEO_ID = "QDU1Qp-u2Zs";
    private final String SWITCH_ACCESS_VIDEO_ID = "OJiOC0Wkvlk";
    ThumbnailListener thumbnailListener = new ThumbnailListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_settings);
        enableNavigationBack();
        setActivityTitle(getString(R.string.menuAccessibility));
        ((YouTubeThumbnailView)findViewById(R.id.thumbnailVisualAccess)).
                initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
        ((YouTubeThumbnailView)findViewById(R.id.thumbnailSwitchAccess)).
                initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(AccessibilitySettingsActivity.class.getSimpleName());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                finish(); break;
            case R.id.aboutJellow:
                startActivity(new Intent(this, AboutJellowActivity.class));
                finish();  break;
            case R.id.tutorial:
                startActivity(new Intent(this, TutorialActivity.class));
                finish(); break;
            case R.id.keyboardInput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                finish(); break;
            case R.id.languageSelect:
                startActivity(new Intent(this, LanguageSelectActivity.class));
                finish(); break;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
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
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void startVideoIntent(View view){
        String videoId="";
        if(view.equals(findViewById(R.id.thumbnailVisualAccess)))
            videoId = VISUAL_ACCESS_VIDEO_ID;
        else
            videoId = SWITCH_ACCESS_VIDEO_ID;
        startActivityForResult(YouTubeStandalonePlayer.createVideoIntent(
                AccessibilitySettingsActivity.this, DeveloperKey.DEVELOPER_KEY,
                videoId, 0, false, false), 0);
    }

    public void openSystemAccessibilitySetting(View view){
        try {
            startActivity(new Intent().setAction("android.settings.ACCESSIBILITY_SETTINGS"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(
                YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            if(view.equals(findViewById(R.id.thumbnailVisualAccess)))
                loader.setVideo(VISUAL_ACCESS_VIDEO_ID);
            else
                loader.setVideo(SWITCH_ACCESS_VIDEO_ID);
        }

        @Override
        public void onInitializationFailure(
                YouTubeThumbnailView view, YouTubeInitializationResult loader) {
            view.setImageResource(R.drawable.no_thumbnail);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
            view.setImageResource(R.drawable.no_thumbnail);
        }
    }
}
