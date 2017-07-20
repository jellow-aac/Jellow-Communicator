package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by user on 6/6/2016.
 */
public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.menuTutorials)+"</font>"));
        /*SessionManager mSession = new SessionManager(this);
        if (mSession.getScreenHeight() >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);*/

        ((TextView)findViewById(R.id.tv6)).setText(
                getString(R.string.softwareVersion).concat(" " + String.valueOf(BuildConfig.VERSION_NAME)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(2).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile: startActivity(new Intent(TutorialActivity.this, ProfileFormActivity.class)); finish(); break;
            case R.id.info: startActivity(new Intent(TutorialActivity.this, AboutJellowActivity.class));   finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(TutorialActivity.this, KeyboardInputActivity.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(TutorialActivity.this, FeedbackActivity.class)); finish(); break;
            case R.id.settings: startActivity(new Intent(TutorialActivity.this, SettingActivity.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(TutorialActivity.this, ResetPreferencesActivity.class)); finish(); break;
            case android.R.id.home: finish(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}