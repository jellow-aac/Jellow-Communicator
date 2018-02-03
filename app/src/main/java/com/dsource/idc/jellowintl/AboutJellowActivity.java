package com.dsource.idc.jellowintl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import com.dsource.idc.jellowintl.Utility.ChangeAppLocale;

/**
 * Created by user on 5/27/2016.
 */
public class AboutJellowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_jellow);
        new ChangeAppLocale(this).setLocale();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+ getString(R.string.menuAbout)+"</font>"));
        ((WebView)findViewById(R.id.webAboutJellow)).loadData(getString(R.string.about_jellow),  "text/html; charset=utf-8","UTF-8");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        findViewById(R.id.speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(getString(R.string.about_jellow_speech));
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSpeech();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.languageSelect: startActivity(new Intent(this, LanguageSelectActivity.class)); finish(); break;
            case R.id.profile: startActivity(new Intent(this, ProfileFormActivity.class)); finish(); break;
            case R.id.usage: startActivity(new Intent(this, TutorialActivity.class)); finish(); break;
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        new ChangeAppLocale(this).setLocale();
        stopSpeech();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSpeech();
        finish();
    }

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText);
        sendBroadcast(intent);
    }

    private void stopSpeech() {
        sendBroadcast(new Intent("com.dsource.idc.jellowintl.SPEECH_STOP"));
    }
}