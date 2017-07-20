package com.dsource.idc.jellow;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dsource.idc.jellow.Utility.SessionManager;
import com.rey.material.widget.Slider;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    private Spinner mSpinnerLanguage, mSpinnerViewMode, mSpinnerGridSize;
    private SessionManager mSession;
    private TextView mTxtViewSpeechSpeed, mTxtViewVoicePitch;
    private Slider mSliderSpeed, mSliderPitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.action_settings)+"</font>"));
        mSession = new SessionManager(this);

        if (mSession.getScreenHeight() >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        mSpinnerLanguage = (Spinner) findViewById(R.id.spinner);
        mSpinnerViewMode = (Spinner) findViewById(R.id.spinner3);
        mSpinnerGridSize = (Spinner) findViewById(R.id.spinner4);
        mSpinnerLanguage.setAdapter(ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item));
        mSpinnerViewMode.setAdapter(ArrayAdapter.createFromResource(this, R.array.picture_view_mode, android.R.layout.simple_spinner_item));
        mSpinnerGridSize.setAdapter(ArrayAdapter.createFromResource(this, R.array.grid_size, android.R.layout.simple_spinner_item));

        Button btnSave = (Button)findViewById(R.id.button4);
        Button btnDemo = (Button)findViewById(R.id.demo);
        mSliderSpeed = (Slider) findViewById(R.id.speed);
        mSliderPitch = (Slider)findViewById(R.id.pitch);
        mTxtViewSpeechSpeed = (TextView)findViewById(R.id.speechspeed);
        mTxtViewVoicePitch = (TextView)findViewById(R.id.voicepitch);

        mSpinnerLanguage.setSelection(mSession.getLanguage());
        mSliderSpeed.setValue(mSession.getSpeed(),true);
        mSliderPitch.setValue(mSession.getPitch(),true);
        mSpinnerViewMode.setSelection(mSession.getPictureViewMode());
        mSpinnerGridSize.setSelection(mSession.getGridSize());

        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(getString(R.string.demoTtsSpeech));
            }
        });

        mSliderSpeed.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                setSpeechRate((float) newValue / 100);
                mTxtViewSpeechSpeed.setText(getString(R.string.txtSpeechSpeed).concat(": " + String.valueOf(newValue / 10)));
            }
        });

        mSliderPitch.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                setSpeechPitch((float) newValue / 100);
                mTxtViewVoicePitch.setText(getString(R.string.txtVoiceSpeech).concat(": " + String.valueOf(newValue / 10)));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Identify if language is changed, to app needs to restart from splash*/
                if(mSession.getLanguage() != mSpinnerLanguage.getSelectedItemPosition() ||
                        mSession.getPictureViewMode() != mSpinnerViewMode.getSelectedItemPosition() ||
                            mSession.getGridSize() != mSpinnerGridSize.getSelectedItemPosition()) {
                    switch(mSession.getLanguage()) {
                        case 0: setLocale(Locale.US); break;
                        case 1: setLocale(new Locale(getString(R.string.locale_lang_hi),getString(R.string.locale_reg_IN))); break;
                    }
                    mSession.setLanguage(mSpinnerLanguage.getSelectedItemPosition());
                    mSession.setPictureViewMode(mSpinnerViewMode.getSelectedItemPosition());
                    mSession.setGridSize(mSpinnerGridSize.getSelectedItemPosition());
                    Intent intent = new Intent(SettingActivity.this, SplashActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                if(mSession.getSpeed() != mSliderSpeed.getValue()) {
                    setSpeechRate(mSliderSpeed.getValue()/100);
                    mSession.setSpeed(mSliderSpeed.getValue());
                }
                if(mSession.getPitch() != mSliderPitch.getValue()) {
                    setSpeechPitch(mSliderPitch.getValue()/ 100);
                    mSession.setPitch(mSliderPitch.getValue());
                }
                Toast.makeText(SettingActivity.this, getString(R.string.savedSettingsMessage), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(6).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile: startActivity(new Intent(SettingActivity.this, ProfileFormActivity.class)); finish(); break;
            case R.id.info: startActivity(new Intent(SettingActivity.this, AboutJellowActivity.class));   finish(); break;
            case R.id.usage: startActivity(new Intent(SettingActivity.this, TutorialActivity.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(SettingActivity.this, KeyboardInputActivity.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(SettingActivity.this, FeedbackActivity.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(SettingActivity.this, ResetPreferencesActivity.class)); finish(); break;
            case android.R.id.home: onBackPressed(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setSpeechPitch(mSession.getPitch()/100);
        setSpeechRate(mSession.getSpeed()/100);
        finish();
    }

    private void setLocale(Locale locale) {
        Configuration conf = getResources().getConfiguration();
        conf.locale = locale;
        getResources().updateConfiguration(conf, getResources().getDisplayMetrics());
    }

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellow.SPEECH_TEXT");
        intent.putExtra("speechText", speechText);
        sendBroadcast(intent);
    }

    private void setSpeechRate(float speechRate){
        Intent intent = new Intent("com.dsource.idc.jellow.SPEECH_SPEED");
        intent.putExtra("speechSpeed", speechRate);
        sendBroadcast(intent);
    }

    private void setSpeechPitch(float speechPitch){
        Intent intent = new Intent("com.dsource.idc.jellow.SPEECH_PITCH");
        intent.putExtra("speechPitch", speechPitch);
        sendBroadcast(intent);
    }
}