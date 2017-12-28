package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.dsource.idc.jellow.Utility.ChangeAppLocale;
import com.dsource.idc.jellow.Utility.SessionManager;

import static com.dsource.idc.jellow.Utility.Analytics.startMeasuring;
import static com.dsource.idc.jellow.Utility.Analytics.stopMeasuring;

/**
 * Created by user on 6/6/2016.
 */
public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        new ChangeAppLocale(this).setLocale();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.menuTutorials)+"</font>"));
        SessionManager mSession = new SessionManager(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        ((TextView)findViewById(R.id.tv6)).setText(
                getString(R.string.softwareVersion).concat(" " + String.valueOf(BuildConfig.VERSION_NAME)));

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
            case R.id.languageSelect: startActivity(new Intent(this, LanguageSelectActivity.class)); break;
            case R.id.profile: startActivity(new Intent(this, ProfileFormActivity.class)); finish(); break;
            case R.id.info: startActivity(new Intent(this, AboutJellowActivity.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(this, KeyboardInputActivity.class)); finish(); break;
            case R.id.settings: startActivity(new Intent(getApplication(), SettingActivity.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(this, ResetPreferencesActivity.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(this, FeedbackActivity.class)); finish(); break;
            case android.R.id.home: finish(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ChangeAppLocale(this).setLocale();
        startMeasuring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMeasuring("TutorialActivity");
        new ChangeAppLocale(this).setLocale();
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