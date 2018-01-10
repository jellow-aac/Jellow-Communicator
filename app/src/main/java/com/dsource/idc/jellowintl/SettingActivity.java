package com.dsource.idc.jellowintl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dsource.idc.jellowintl.Utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.Utility.SessionManager;
import com.rey.material.widget.Slider;

import static com.dsource.idc.jellowintl.Utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.Utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.Utility.Analytics.stopMeasuring;

public class SettingActivity extends AppCompatActivity {
    private Spinner mSpinnerViewMode, mSpinnerGridSize;
    private SessionManager mSession;
    private TextView mTxtViewSpeechSpeed, mTxtViewVoicePitch;
    private Slider mSliderSpeed, mSliderPitch;
    private  ChangeAppLocale mChangeAppLocale;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F6F4E8'>"+getString(R.string.action_settings)+"</font>"));
        mSession = new SessionManager(this);
        mChangeAppLocale = new ChangeAppLocale(this);
        mChangeAppLocale.setLocale();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));


        mSpinnerViewMode = findViewById(R.id.spinner3);
        mSpinnerGridSize = findViewById(R.id.spinner4);
        mSpinnerViewMode.setAdapter(ArrayAdapter.createFromResource(this, R.array.picture_view_mode, android.R.layout.simple_spinner_item));
        mSpinnerGridSize.setAdapter(ArrayAdapter.createFromResource(this, R.array.grid_size, android.R.layout.simple_spinner_item));

        Button btnSave = findViewById(R.id.button4);
        Button btnDemo = findViewById(R.id.demo);
        mSliderSpeed = findViewById(R.id.speed);
        mSliderPitch = findViewById(R.id.pitch);
        mTxtViewSpeechSpeed = findViewById(R.id.speechspeed);
        mTxtViewVoicePitch = findViewById(R.id.voicepitch);



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

        mSpinnerViewMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setUserProperty("PictureViewMode", position == 0 ? "PictureText": "PictureOnly");
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        mSpinnerGridSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setUserProperty("GridSize", position == 0 ? "3": "9");
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Identify if language is changed, to app needs to restart from splash*/
                if(mSession.getPictureViewMode() != mSpinnerViewMode.getSelectedItemPosition() ||
                            mSession.getGridSize() != mSpinnerGridSize.getSelectedItemPosition()) {


                    if(mSession.getPictureViewMode() != mSpinnerViewMode.getSelectedItemPosition()) {
                        setUserProperty("PictureViewMode", mSpinnerViewMode.getSelectedItemPosition() == 0 ? "PictureText": "PictureOnly");
                        mSession.setPictureViewMode(mSpinnerViewMode.getSelectedItemPosition());
                    }
                    if(mSession.getGridSize() != mSpinnerGridSize.getSelectedItemPosition()) {
                        setUserProperty("GridSize", mSpinnerGridSize.getSelectedItemPosition() == 0 ? "3" : "9");
                        mSession.setGridSize(mSpinnerGridSize.getSelectedItemPosition());
                    }
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finishAffinity();
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
    protected void onPause() {
        super.onPause();
        stopMeasuring("SettingsActivity");
        mChangeAppLocale.setLocale();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChangeAppLocale.setLocale();
        startMeasuring();
    }

    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent("com.dsource.idc.jellowintl.SPEECH_STOP"));

        super.onDestroy();
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
            case R.id.info: startActivity(new Intent(this, AboutJellowActivity.class)); finish(); break;
            case R.id.usage: startActivity(new Intent(this, TutorialActivity.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(this, KeyboardInputActivity.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(this, ResetPreferencesActivity.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(this, FeedbackActivity.class)); finish(); break;
            case android.R.id.home: finish(); break;
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

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText);
        sendBroadcast(intent);
    }

    private void setSpeechRate(float speechRate){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_SPEED");
        intent.putExtra("speechSpeed", speechRate);
        sendBroadcast(intent);
    }

    private void setSpeechPitch(float speechPitch){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_PITCH");
        intent.putExtra("speechPitch", speechPitch);
        sendBroadcast(intent);
    }



}