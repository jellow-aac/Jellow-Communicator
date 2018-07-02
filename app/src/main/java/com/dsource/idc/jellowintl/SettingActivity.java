package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.rey.material.widget.Slider;
import com.rey.material.widget.Switch;

import static com.dsource.idc.jellowintl.MainActivity.isDeviceReadyToCall;
import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class SettingActivity extends AppCompatActivity {
    private Spinner mSpinnerViewMode, mSpinnerGridSize;
    private SessionManager mSession;
    private TextView mTxtViewSpeechSpeed, mTxtViewVoicePitch;
    private Slider mSliderSpeed, mSliderPitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+
                getString(R.string.action_settings)+"</font>"));
        mSession = new SessionManager(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));


        mSpinnerViewMode = findViewById(R.id.spinner3);
        mSpinnerGridSize = findViewById(R.id.spinner4);
        mSpinnerViewMode.setAdapter(ArrayAdapter.createFromResource
                (this, R.array.picture_view_mode, android.R.layout.simple_spinner_item));
        mSpinnerGridSize.setAdapter(ArrayAdapter.createFromResource
                (this, R.array.grid_size, android.R.layout.simple_spinner_item));

        // If user have sim device and ready to call, only then show "enable call switch".
        if(isDeviceReadyToCall((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE))) {
            ((Switch) findViewById(R.id.switchEnableCall)).setChecked(mSession.getEnableCalling());
            ((Switch) findViewById(R.id.switchEnableCall)).setOnCheckedChangeListener
                    (new Switch.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(Switch view, boolean enableCall) {
                            mSession.setEnableCalling(enableCall);
                        }
                    });
        }else{
            findViewById(R.id.tv5).setVisibility(View.GONE);
            findViewById(R.id.switchEnableCall).setVisibility(View.GONE);
        }


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

        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        final String strSpeechSpeed = getString(R.string.txtSpeechSpeed);
        final String strDemoSpeech = getString(R.string.demoTtsSpeech);
        final String strSpeechPitch = getString(R.string.txtVoiceSpeech);
        final String strSettingSaved = getString(R.string.savedSettingsMessage);
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(strDemoSpeech);
                Crashlytics.log("SettingAct Demo");
            }
        });

        mSliderSpeed.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                setSpeechRate((float) newValue / 50);
                mTxtViewSpeechSpeed.setText(strSpeechSpeed.concat(": " + String.valueOf(newValue / 5)));
            }
        });

        mSliderPitch.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                setSpeechPitch((float) newValue / 50);
                mTxtViewVoicePitch.setText(strSpeechPitch.concat(": " + String.valueOf(newValue / 5)));
            }
        });

        mSpinnerViewMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        mSpinnerGridSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                        setUserProperty("PictureViewMode",
                                mSpinnerViewMode.getSelectedItemPosition() == 0 ? "PictureText": "PictureOnly");
                        setCrashlyticsCustomKey("PictureViewMode",
                                mSpinnerViewMode.getSelectedItemPosition() == 0 ? "PictureText": "PictureOnly");
                        mSession.setPictureViewMode(mSpinnerViewMode.getSelectedItemPosition());
                    }
                    if(mSession.getGridSize() != mSpinnerGridSize.getSelectedItemPosition()) {
                        setUserProperty("GridSize",
                                mSpinnerGridSize.getSelectedItemPosition() == 0 ? "3" : "9");
                        setCrashlyticsCustomKey("GridSize",
                                mSpinnerGridSize.getSelectedItemPosition() == 0 ? "3" : "9");
                        mSession.setGridSize(mSpinnerGridSize.getSelectedItemPosition());
                    }
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finishAffinity();
                }
                if(mSession.getSpeed() != mSliderSpeed.getValue()) {
                    setSpeechRate((float)mSliderSpeed.getValue()/50);
                    mSession.setSpeed(mSliderSpeed.getValue());
                }
                if(mSession.getPitch() != mSliderPitch.getValue()) {
                    setSpeechPitch((float)mSliderPitch.getValue()/ 50);
                    mSession.setPitch(mSliderPitch.getValue());
                }
                mSession.setToastMessage(strSettingSaved);
                Crashlytics.log("SettingAct Save");
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(mSession.getSessionCreatedAt());
        mSession.setSessionCreatedAt(sessionTime);

        stopMeasuring("SettingsActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            throw new Error("unableToResume");
        }
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
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
            case R.id.languageSelect:
                startActivity(new Intent(this, LanguageSelectActivity.class));
                finish();
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                finish();
                break;
            case R.id.info:
                startActivity(new Intent(this, AboutJellowActivity.class));
                finish();
                break;
            case R.id.usage:
                startActivity(new Intent(this, TutorialActivity.class));
                finish();
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                finish();
                break;
            case R.id.reset:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                finish();
                break;
            case R.id.feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setSpeechPitch(mSession.getPitch()/50);
        setSpeechRate(mSession.getSpeed()/50);
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