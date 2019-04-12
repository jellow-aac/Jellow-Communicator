package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class SettingActivity extends SpeechEngineBaseActivity {
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private Spinner mSpinnerViewMode, mSpinnerGridSize;
    private TextView mTxtViewSpeechSpeed, mTxtViewVoicePitch;
    private SeekBar mSliderSpeed, mSliderPitch, mSliderVolume;
    private boolean mOpenSetting;
    private String  mCalPerMsg, mCalPerGranted,mCalPerRejected, mSettings, mDismiss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        enableNavigationBack();
        setActivityTitle(getString(R.string.action_settings));

        mOpenSetting = false;
        mSpinnerViewMode = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.picture_view_mode, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerViewMode.setAdapter(adapter);
        mSpinnerGridSize = findViewById(R.id.spinner4);
        if(!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            adapter = ArrayAdapter.createFromResource
                    (this, R.array.grid_size, R.layout.simple_spinner_item);
        }else {
            adapter = ArrayAdapter.createFromResource
                    (this, R.array.acc_grid_size, R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerGridSize.setAdapter(adapter);

        // If user have sim device and ready to call, only then show "enable call switch".
        if(isDeviceReadyToCall((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE))) {
            if(getSession().getEnableCalling())
                ((Switch) findViewById(R.id.switchEnableCall)).setChecked(true);

            ((Switch) findViewById(R.id.switchEnableCall)).setOnCheckedChangeListener
                    (new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean enableCall) {
                            if(enableCall)
                                //request call permission here.
                                requestCallPermissionToUser();
                            else
                                getSession().setEnableCalling(false);
                        }
                    });
        }else{
            findViewById(R.id.tv5).setVisibility(View.GONE);
            findViewById(R.id.switchEnableCall).setVisibility(View.GONE);
        }

        Button btnSave = findViewById(R.id.button4);
        final Button btnDemo = findViewById(R.id.demo);
        mSliderSpeed = findViewById(R.id.speed);
        mSliderPitch = findViewById(R.id.pitch);
        mSliderVolume = findViewById(R.id.volume);
        mTxtViewSpeechSpeed = findViewById(R.id.speechspeed);
        mTxtViewVoicePitch = findViewById(R.id.voicepitch);

        mSliderSpeed.setProgress(getSession().getSpeed());
        mSliderPitch.setProgress(getSession().getPitch());
        mSpinnerViewMode.setSelection(getSession().getPictureViewMode());
        mSpinnerGridSize.setSelection(getSession().getGridSize());

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
                speak(strDemoSpeech);
                Crashlytics.log("SettingAct Demo");
            }
        });


       mSliderSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               setSpeechRate((float) i / 50);
               mTxtViewSpeechSpeed.setText(strSpeechSpeed.concat(": "+ String.valueOf(i / 5)));
           }

           @Override public void onStartTrackingTouch(SeekBar seekBar) {}

           @Override public void onStopTrackingTouch(SeekBar seekBar) {}
       });

        mSliderPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setSpeechPitch((float) i / 50);
                mTxtViewVoicePitch.setText(strSpeechPitch.concat(": "+ String.valueOf(i / 5)));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        if(isNoTTSLanguage()){
            mSliderSpeed.setVisibility(View.GONE);
            mTxtViewSpeechSpeed.setVisibility(View.GONE);
            mSliderPitch.setVisibility(View.GONE);
            mTxtViewVoicePitch.setVisibility(View.GONE);
        }

        final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mSliderVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b && audio != null)
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, i,
                            AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);

            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Identify if language is changed, to app needs to restart from splash*/
                if(getSession().getPictureViewMode() != mSpinnerViewMode.getSelectedItemPosition() ||
                            getSession().getGridSize() != mSpinnerGridSize.getSelectedItemPosition()) {


                    if(getSession().getPictureViewMode() != mSpinnerViewMode.getSelectedItemPosition()) {
                        setUserProperty("PictureViewMode",
                                mSpinnerViewMode.getSelectedItemPosition() == 0 ? "PictureText": "PictureOnly");
                        setCrashlyticsCustomKey("PictureViewMode",
                                mSpinnerViewMode.getSelectedItemPosition() == 0 ? "PictureText": "PictureOnly");
                        getSession().setPictureViewMode(mSpinnerViewMode.getSelectedItemPosition());
                    }
                    if(getSession().getGridSize() != mSpinnerGridSize.getSelectedItemPosition()) {
                        switch(mSpinnerGridSize.getSelectedItemPosition()){
                            case 0: setUserProperty("GridSize", "1"); break;
                            case 1: setUserProperty("GridSize", "2"); break;
                            case 2: setUserProperty("GridSize", "3"); break;
                            case 3: setUserProperty("GridSize", "4"); break;
                            case 4: setUserProperty("GridSize", "9"); break;

                        }
                        getSession().setGridSize(mSpinnerGridSize.getSelectedItemPosition());
                    }
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finishAffinity();
                }
                if(getSession().getSpeed() != mSliderSpeed.getProgress()) {
                    setSpeechRate((float)mSliderSpeed.getProgress()/50);
                    getSession().setSpeed(mSliderSpeed.getProgress());
                }
                if(getSession().getPitch() != mSliderPitch.getProgress()) {
                    setSpeechPitch((float)mSliderPitch.getProgress()/ 50);
                    getSession().setPitch(mSliderPitch.getProgress());
                }
                getSession().setToastMessage(strSettingSaved);
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
                getSession().setEnableCalling(true);
                ((Switch) findViewById(R.id.switchEnableCall)).setChecked(true);
                Toast.makeText(this, mCalPerGranted , Toast.LENGTH_SHORT).show();
            } else {
                if(!ActivityCompat.shouldShowRequestPermissionRationale(
                        this, android.Manifest.permission.CALL_PHONE)){
                    showSettingRequestDialog();
                }else{
                    getSession().setEnableCalling(false);
                    Toast.makeText(this, mCalPerRejected, Toast.LENGTH_SHORT).show();
                }
                ((Switch) findViewById(R.id.switchEnableCall)).setChecked(false);
            }
        }
    }

    private void requestCallPermissionToUser() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            getSession().setEnableCalling(true);
            return;
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            getSession().setEnableCalling(true);
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
     * <p> This function will create and display Dialog with "Request" action button. It will
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
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        stopMeasuring("SettingsActivity");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            getSession().setEnableCalling(true);
            ((Switch) findViewById(R.id.switchEnableCall)).setChecked(true);
            mOpenSetting = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(SettingActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
        startMeasuring();

        //This code executed when user denied permission from app and gone to app settings -> permission
        // and enabled call permission and came back.
        if(mOpenSetting && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
            && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            getSession().setEnableCalling(true);
            ((Switch) findViewById(R.id.switchEnableCall)).setChecked(true);
            mOpenSetting = false;
        }
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if(audio != null)
            mSliderVolume.setProgress(audio.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    @Override
    protected void onDestroy() {
        stopSpeaking();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setSpeechPitch(getSession().getPitch()/50);
        setSpeechRate(getSession().getSpeed()/50);
        finish();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        addAccessibilityDelegateToSpinners();
    }

    private void addAccessibilityDelegateToSpinners() {
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            mSpinnerViewMode.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                @Override
                public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
                    super.onInitializeAccessibilityEvent(host, event);
                    if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                        findViewById(R.id.tv4).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                    }
                }
            });

            mSpinnerGridSize.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                @Override
                public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
                    super.onInitializeAccessibilityEvent(host, event);
                    if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                        findViewById(R.id.demo).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                    }
                }
            });
        }
    }
}
