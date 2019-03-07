package com.dsource.idc.jellowintl;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.dsource.idc.jellowintl.MainActivity.isAccessibilityTalkBackOn;

public class AccessibilitySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+ getString(R.string.menuAccessibility) +"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        ((TextView)findViewById(R.id.title_visual_access)).setText
                (getString(R.string.visual_access_title));
        ((TextView)findViewById(R.id.info_visual_access)).setText
                (getString(R.string.visual_acesss_info).
                        concat(" " + getString(R.string.visual_access_link)));
        Linkify.addLinks(((TextView)findViewById(R.id.info_visual_access)), Linkify.WEB_URLS);

        ((TextView)findViewById(R.id.title_switch_access)).setText
                (getString(R.string.switch_access_title));
        ((TextView)findViewById(R.id.info_switch_access)).setText
                (getString(R.string.switch_access_info).
                        concat(" "+ getString(R.string.serial_access_link)));
        Linkify.addLinks(((TextView)findViewById(R.id.info_switch_access)), Linkify.WEB_URLS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            menu.findItem(R.id.closePopup).setVisible(false);
        }
        return true;
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
                if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, LanguageSelectActivity.class));
                } else {
                    startActivity(new Intent(this, LanguageSelectTalkBackActivity.class));
                }
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

    public void openSystemAccessibilitySetting(View view){
        try {
            startActivity(new Intent().setAction("android.settings.ACCESSIBILITY_SETTINGS"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
