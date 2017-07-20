package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.dsource.idc.jellow.Utility.SessionManager;

/**
 * Created by user on 5/27/2016.
 */
public class AboutJellowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_jellow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+ getString(R.string.menuAbout)+"</font>"));
        ((WebView)findViewById(R.id.webAboutJellow)).
                loadDataWithBaseURL(null, getString(R.string.about_jellow), "text/html", "utf-8", null);

        if (new SessionManager(this).getScreenHeight() >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        ((Button)findViewById(R.id.speak)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(getString(R.string.about_jellow_speech));
            }
        });

        ((Button)findViewById(R.id.stop)).setOnClickListener(new View.OnClickListener() {
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
        menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile: startActivity(new Intent(AboutJellowActivity.this, ProfileFormActivity.class)); finish(); break;
            case R.id.info: startActivity(new Intent(AboutJellowActivity.this, AboutJellowActivity.class));   finish(); break;
            case R.id.usage: startActivity(new Intent(AboutJellowActivity.this, TutorialActivity.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(AboutJellowActivity.this, KeyboardInputActivity.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(AboutJellowActivity.this, FeedbackActivity.class)); finish(); break;
            case R.id.settings: startActivity(new Intent(AboutJellowActivity.this, SettingActivity.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(AboutJellowActivity.this, ResetPreferencesActivity.class)); finish(); break;
            case android.R.id.home: onBackPressed(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopSpeech();
        finish();
    }

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellow.SPEECH_TEXT");
        intent.putExtra("speechText", speechText);
        sendBroadcast(intent);
    }

    private void stopSpeech() {
        sendBroadcast(new Intent("com.dsource.idc.jellow.SPEECH_STOP"));
    }
}