package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.dsource.idc.jellowintl.utility.CustomDialog;
import com.dsource.idc.jellowintl.utility.GridSelectListener;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.utility.TextToSpeechErrorUtils;

import java.util.Objects;

public class LevelBaseActivity extends SpeechEngineBaseActivity implements TextToSpeechCallBacks {
    private String mErrorMessage, mDialogTitle, mLanguageSetting, mSwitchLang;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerSpeechEngineErrorHandle(this);
        mErrorMessage = getString(R.string.langauge_correction_message);
        mErrorMessage = mErrorMessage.replace("-", Objects.requireNonNull(SessionManager.
                LangValueMap.get(getSession().getLanguage())));
        mErrorMessage = mErrorMessage.replace("_", getString(R.string.Language));
        mErrorMessage = mErrorMessage.replace("$", getString(R.string.dialog_default_language_option));
        mDialogTitle = getString(R.string.changeLanguage);
        mLanguageSetting = getString(R.string.Language);
        mSwitchLang = getString(R.string.dialog_default_language_option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initiateSpeechEngineWithLanguage(getSession().getLanguage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.number_of_icons:
                new CustomDialog(this,getSession().getLanguage(), new GridSelectListener() {
                @Override
                public void onGridSelectListener(int size) {
                    getSession().setGridSize(size);
                    setGridSize();
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finish();
                }
            });
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                break;
            case R.id.aboutJellow:
                startActivity(new Intent(this, AboutJellowActivity.class));
                break;
            case R.id.tutorial:
                startActivity(new Intent(this, TutorialActivity.class));
                break;
            case R.id.keyboardInput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                break;
            case R.id.languageSelect:
                startActivity(new Intent(this, LanguageSelectActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.accessibilitySetting:
                startActivity(new Intent(this, AccessibilitySettingsActivity.class));
                break;
            case R.id.resetPreferences:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                break;
            case R.id.feedback:
                if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, FeedbackActivityTalkBack.class));
                }
                else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void setLevelActionBar(String title){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(title);
    }

    public void setCustomKeyboardView(Context context){
        this.context = context;
    }


    /**Text-To-Speech Engine error callbacks implementations are following**/
    @Override
    public void sendSpeechEngineLanguageNotSetCorrectlyError() {
        LevelBaseActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LevelBaseActivity.this);
                builder.setMessage(mErrorMessage)
                        .setTitle(mDialogTitle)
                        .setPositiveButton(mLanguageSetting, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(LevelBaseActivity.this,
                                        LanguageSelectActivity.class));
                                dialogInterface.dismiss();
                            }
                        })
                        .setNeutralButton(mSwitchLang, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getSession().setLanguage(SessionManager.ENG_US);
                                startActivity(new Intent(LevelBaseActivity.this,
                                        SplashActivity.class));
                                finishAffinity();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
                Button positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positive.setTextColor(LevelBaseActivity.this.getResources().getColor(R.color.colorAccent));
                Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setTextColor(LevelBaseActivity.this.getResources().getColor(R.color.colorAccent));
                Button neutral = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
                neutral.setTextColor(LevelBaseActivity.this.getResources().getColor(R.color.colorAccent));
            }
        });
    }

    @Override
    public void speechEngineNotFoundError() {
        LevelBaseActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new TextToSpeechErrorUtils(LevelBaseActivity.this).showErrorDialog();
            }
        });
    }

    @Override
    public void speechSynthesisCompleted() {
        if (context instanceof MainActivity){
            MainActivity activity = (MainActivity) context;
            activity.revertTheSpeakerIcon();
        }else if (context instanceof LevelTwoActivity){
            LevelTwoActivity activity = (LevelTwoActivity) context;
            activity.revertTheSpeakerIcon();
        }else if (context instanceof LevelThreeActivity){
            LevelThreeActivity activity = (LevelThreeActivity) context;
            activity.revertTheSpeakerIcon();
        }else{
            SequenceActivity activity = (SequenceActivity) context;
            activity.revertTheSpeakerIcon();
        }
    }
    /*-------------*/
}