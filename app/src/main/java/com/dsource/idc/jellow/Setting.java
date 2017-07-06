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

public class Setting extends AppCompatActivity {
    private Spinner mSpinnerLanguage, mSpinnerViewMode, mSpinnerGridSize;
    private SessionManager mSession;
    private TextView mTxtViewSpeechSpeed, mTxtViewVoicePitch;
    private Slider mSliderSpeed, mSliderPitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
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

        setSpeechPitch((float) mSliderPitch.getValue()/50);

        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(getString(R.string.demoTtsSpeech));
            }
        });

        mSliderSpeed.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                setSpeechRate((float) newValue / 50);
                mTxtViewSpeechSpeed.setText(getString(R.string.txtSpeechSpeed).concat(": " + String.valueOf((float) newValue / 50)));
            }
        });

        mSliderPitch.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                setSpeechPitch((float) newValue / 50);
                mTxtViewVoicePitch.setText(getString(R.string.txtVoiceSpeech).concat(": " + String.valueOf((float) newValue / 50)));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSession.setLanguage(mSpinnerLanguage.getSelectedItemPosition());
                mSession.setSpeed(mSliderSpeed.getValue());
                mSession.setPitch(mSliderPitch.getValue());
                mSession.setPictureViewMode(mSpinnerViewMode.getSelectedItemPosition());
                mSession.setGridSize(mSpinnerGridSize.getSelectedItemPosition());
                switch(mSession.getLanguage()) {
                    case 0: setLocale(Locale.US); break;
                    case 1: setLocale(new Locale(getString(R.string.locale_lang_hi),getString(R.string.locale_reg_IN))); break;
                }
                Toast.makeText(Setting.this, getString(R.string.savedSettingsMessage), Toast.LENGTH_SHORT).show();
                Intent intent = new  Intent(Setting.this, Splash.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
            case R.id.profile: startActivity(new Intent(Setting.this, ProfileForm.class)); finish(); break;
            case R.id.info: startActivity(new Intent(Setting.this, AboutJellow.class));   finish(); break;
            case R.id.usage: startActivity(new Intent(Setting.this, Tutorial.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(Setting.this, KeyboardInput.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(Setting.this, Feedback.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(Setting.this, ResetPreferences.class)); finish(); break;
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