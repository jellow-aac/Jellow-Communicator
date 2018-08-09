package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;

public class FeedbackActivityTalkback extends AppCompatActivity {

    Spinner mEasyToUse, mClearPictures, mClearVoice, mNavigate;
    EditText mComments ;
    Button mBtnSubmit;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_feedback_talkback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>" + getString(R.string.menuFeedback) + "</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        findViewById(R.id.comments).clearFocus();

        mBtnSubmit = findViewById(R.id.bSubmit);
        addListenerOnSpinner();
        addListenerOnButton();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.languageSelect:
                startActivity(new Intent(this, LanguageSelectActivity.class));
                finish();
                break;
            case R.id.profile:
                startActivity(new Intent(FeedbackActivityTalkback.this, ProfileFormActivity.class));
                finish();
                break;
            case R.id.info:
                startActivity(new Intent(FeedbackActivityTalkback.this, AboutJellowActivity.class));
                finish();
                break;
            case R.id.usage:
                startActivity(new Intent(FeedbackActivityTalkback.this, TutorialActivity.class));
                finish();
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(FeedbackActivityTalkback.this, KeyboardInputActivity.class));
                finish();
                break;
            case R.id.settings:
                startActivity(new Intent(FeedbackActivityTalkback.this, SettingActivity.class));
                finish();
                break;
            case R.id.reset:
                startActivity(new Intent(FeedbackActivityTalkback.this, ResetPreferencesActivity.class));
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isAnalyticsActive()) {
            throw new Error("unableToResume");
        }
        if (Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void addListenerOnSpinner() {
        mEasyToUse = findViewById(R.id.easytouse);
        mClearPictures = findViewById(R.id.clearpictures);
        mClearVoice = findViewById(R.id.clearvoice);
        mNavigate = findViewById(R.id.navigate);
        adapter = ArrayAdapter.createFromResource(this, R.array.ratings,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEasyToUse.setAdapter(adapter);
        mClearPictures.setAdapter(adapter);
        mClearVoice.setAdapter(adapter);
        mNavigate.setAdapter(adapter);
    }

    public void addListenerOnButton() {
        final String strRateJellow = getString(R.string.rate_jellow);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mEasyToUse != null) && (mClearPictures != null) && (mClearVoice != null) && (mNavigate != null) &&
                        (!mEasyToUse.equals("0.0")) && (!mClearPictures.equals("0.0")) && (!mClearVoice.equals("0.0")) && (!mNavigate.equals("0.0"))) {
                    String cs = mComments.getText().toString();
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dsource.in@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Jellow Feedback");
                    email.putExtra(Intent.EXTRA_TEXT, "Easy to use: " + mEasyToUse +
                            "\nClear Pictures: " + mClearPictures + "\nClear Voices: "
                            + mClearVoice + "\nEasy to Navigate: " + mNavigate +
                            "\n\nComments and Suggestions:-\n" + cs);
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));

                } else {
                    Toast.makeText(FeedbackActivityTalkback.this,
                            strRateJellow, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
