package com.dsource.idc.jellowintl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.Toast;

import com.dsource.idc.jellowintl.utility.TextToSpeechErrorUtils;

import androidx.appcompat.app.AlertDialog;

public class LevelBaseActivity extends SpeechEngineBaseActivity implements TextToSpeechError{
    private String mTBackMsg, mChgLang, mStrYes, mStrNo, mNeverShowAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerSpeechEngineErrorHandle(this);
        mTBackMsg = getString(R.string.change_language);
        mChgLang = getString(R.string.changeLanguage);
        mStrYes = getString(R.string.dialog_yes);
        mStrNo = getString(R.string.dialog_no);
        mNeverShowAgain = getString(R.string.never_show_again);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
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
                if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, LanguageSelectActivity.class));
                } else {
                    startActivity(new Intent(this, LanguageSelectTalkBackActivity.class));
                }
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.languagePackUpdate:
                startActivity(new Intent(this, LanguagePackUpdateActivity.class));
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


    /*Text-To-Speech Engine error callbacks implementations are following*/
    @Override
    public void sendFailedToSynthesizeError(final String message) {
        LevelBaseActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LevelBaseActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void sendLanguageIncompatibleError(final String message) {
        LevelBaseActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LevelBaseActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void sendLanguageIncompatibleForAccessibility() {
        LevelBaseActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LevelBaseActivity.this);
                builder.setMessage(mTBackMsg)
                        .setTitle(mChgLang)
                        .setPositiveButton(mStrYes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent();
                                intent.setAction("com.android.settings.TTS_SETTINGS");
                                startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNeutralButton(mStrNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton(mNeverShowAgain, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                getSession().setChangeLanguageNeverAsk(true);
                            }
                        });

                AlertDialog dialog = builder.create();
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
    /*-------------*/

}
