package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.MainActivity.isDeviceReadyToCall;
import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;

public class SettingActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private Spinner mSpinnerViewMode, mSpinnerGridSize;
    private SessionManager mSession;
    private TextView mTxtViewSpeechSpeed, mTxtViewVoicePitch;
    private SeekBar mSliderSpeed, mSliderPitch, mSliderVolume;
    private boolean mOpenSetting;
    private String  mCalPerMsg, mCalPerGranted,mCalPerRejected, mSettings, mDismiss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+
                getString(R.string.action_settings)+"</font>"));
        mSession = new SessionManager(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        mOpenSetting = false;
        mSpinnerViewMode = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.picture_view_mode, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerViewMode.setAdapter(adapter);
        mSpinnerGridSize = findViewById(R.id.spinner4);
        adapter = ArrayAdapter.createFromResource
                (this, R.array.grid_size, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerGridSize.setAdapter(adapter);

        // If user have sim device and ready to call, only then show "enable call switch".
        if(isDeviceReadyToCall((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE))) {
            if(mSession.getEnableCalling())
                ((Switch) findViewById(R.id.switchEnableCall)).setChecked(true);

            ((Switch) findViewById(R.id.switchEnableCall)).setOnCheckedChangeListener
                    (new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean enableCall) {
                            if(enableCall)
                                //request call permission here.
                                requestCallPermissionToUser();
                            else
                                mSession.setEnableCalling(false);
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
        mSliderVolume = findViewById(R.id.volume);
        mTxtViewSpeechSpeed = findViewById(R.id.speechspeed);
        mTxtViewVoicePitch = findViewById(R.id.voicepitch);

        mSliderSpeed.setProgress(mSession.getSpeed());
        mSliderPitch.setProgress(mSession.getPitch());
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


       mSliderSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               setSpeechRate((float) i / 50);
               mTxtViewSpeechSpeed.setText(strSpeechSpeed.concat(": "+ String.valueOf(i / 5)));
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });

        mSliderPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setSpeechPitch((float) i / 50);
                mTxtViewVoicePitch.setText(strSpeechPitch.concat(": "+ String.valueOf(i / 5)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mSliderVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b && audio != null)
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, i,
                            AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
                if(mSession.getSpeed() != mSliderSpeed.getProgress()) {
                    setSpeechRate((float)mSliderSpeed.getProgress()/50);
                    mSession.setSpeed(mSliderSpeed.getProgress());
                }
                if(mSession.getPitch() != mSliderPitch.getProgress()) {
                    setSpeechPitch((float)mSliderPitch.getProgress()/ 50);
                    mSession.setPitch(mSliderPitch.getProgress());
                }
                mSession.setToastMessage(strSettingSaved);
                Crashlytics.log("SettingAct Save");
                finish();
            }
        });
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        mCalPerMsg = getString(R.string.grant_permission_from_settings);
        mCalPerGranted = getString(R.string.granted_call_permission_req);
        mCalPerRejected = getString(R.string.rejected_call_permission_req);
        mSettings = getString(R.string.action_settings);
        mDismiss = getString(R.string.dismiss);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String Permissions[], int[] grantResults){
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mSession.setEnableCalling(true);
                ((Switch) findViewById(R.id.switchEnableCall)).setChecked(true);
                Toast.makeText(this, mCalPerGranted , Toast.LENGTH_SHORT).show();
            } else {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(
                        this, android.Manifest.permission.CALL_PHONE)){
                    showSettingRequestDialog();
                }else{
                    mSession.setEnableCalling(false);
                    Toast.makeText(this, mCalPerRejected, Toast.LENGTH_SHORT).show();
                }
                ((Switch) findViewById(R.id.switchEnableCall)).setChecked(false);
            }
        }
    }

    private void requestCallPermissionToUser() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mSession.setEnableCalling(true);
            return;
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            mSession.setEnableCalling(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(SettingActivity.this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(SettingActivity.this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }
    }

    /**
     * <p> This function will create and display SnackBar with "Request" action button. It will
     *  display message about why app requires the Call permission.</p>
     * */
    private void showSettingRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder
            .setPositiveButton(mSettings, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mOpenSetting = true;
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivityForResult(intent, 99);
                    dialog.dismiss();
                }
            })
            .setNegativeButton(mDismiss, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            })
            // Set other dialog properties
            .setCancelable(true)
            .setMessage(mCalPerMsg);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        // Show the AlertDialog
        dialog.show();
        Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positive.setTextColor(SettingActivity.this.getResources().getColor(R.color.colorAccent));
        Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negative.setTextColor(SettingActivity.this.getResources().getColor(R.color.colorAccent));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            mSession.setEnableCalling(true);
            ((Switch) findViewById(R.id.switchEnableCall)).setChecked(true);
            mOpenSetting = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            resetAnalytics(this, mSession.getCaregiverNumber().substring(1));
        }
        if(!isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        startMeasuring();

        //This code executed when user denied permission from app and gone to app settings -> permission
        // and enabled call permission and came back.
        if(mOpenSetting && Build.VERSION.SDK_INT > 22 && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            mSession.setEnableCalling(true);
            ((Switch) findViewById(R.id.switchEnableCall)).setChecked(true);
            mOpenSetting = false;
        }
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(audio != null)
            mSliderVolume.setProgress(audio.getStreamVolume(AudioManager.STREAM_MUSIC));
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
        if(mSession.getLanguage().equals(BN_IN))
            menu.findItem(R.id.keyboardinput).setVisible(false);
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
