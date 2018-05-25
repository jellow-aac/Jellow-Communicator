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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

/**
 * Created by user on 6/6/2016.
 */
public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        new ChangeAppLocale(this).setLocale();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.menuTutorials)+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        ((TextView)findViewById(R.id.tv6)).setText(
                getString(R.string.softwareVersion).concat(" " + String.valueOf(BuildConfig.VERSION_NAME)));

        setImagesToImageViewUsingGlide();
    }

    private void setImagesToImageViewUsingGlide() {
        setImageUsingGlide(R.drawable.categorybuttons, ((ImageView)findViewById(R.id.pic1)));
        setImageUsingGlide(R.drawable.expressivebuttons, ((ImageView)findViewById(R.id.pic2)));
        setImageUsingGlide(R.drawable.speakingwithjellowimage2, ((ImageView)findViewById(R.id.pic4)));
        setImageUsingGlide(R.drawable.eatingcategory1, ((ImageView)findViewById(R.id.pic5)));
        setImageUsingGlide(R.drawable.eatingcategory2, ((ImageView)findViewById(R.id.pic6)));
        setImageUsingGlide(R.drawable.eatingcategory3, ((ImageView)findViewById(R.id.pic7)));
        setImageUsingGlide(R.drawable.settings, ((ImageView)findViewById(R.id.pic8)));
        setImageUsingGlide(R.drawable.sequencewithoutexpressivebuttons, ((ImageView)findViewById(R.id.pic9)));
        setImageUsingGlide(R.drawable.sequencewithexpressivebuttons, ((ImageView)findViewById(R.id.pic10)));
        setImageUsingGlide(R.drawable.gtts1, ((ImageView)findViewById(R.id.gtts1)));
        setImageUsingGlide(R.drawable.gtts2, ((ImageView)findViewById(R.id.gtts2)));
        setImageUsingGlide(R.drawable.gtts3, ((ImageView)findViewById(R.id.gtts3)));
    }

    private void setImageUsingGlide(int image, ImageView imgView) {
        GlideApp.with(this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(imgView);
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
            case R.id.languageSelect: startActivity(new Intent(this, LanguageSelectActivity.class)); finish(); break;
            case R.id.profile: startActivity(new Intent(this, ProfileFormActivity.class)); finish(); break;
            case R.id.info: startActivity(new Intent(this, AboutJellowActivity.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(this, KeyboardInputActivity.class)); finish(); break;
            case R.id.settings: startActivity(new Intent(getApplication(), SettingActivity.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(this, ResetPreferencesActivity.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(this, FeedbackActivity.class)); finish(); break;
            case android.R.id.home: finish(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
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
        new ChangeAppLocale(this).setLocale();
        startMeasuring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        SessionManager session = new SessionManager(this);
        long sessionTime = validatePushId(session.getSessionCreatedAt());
        session.setSessionCreatedAt(sessionTime);

        stopMeasuring("TutorialActivity");
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}