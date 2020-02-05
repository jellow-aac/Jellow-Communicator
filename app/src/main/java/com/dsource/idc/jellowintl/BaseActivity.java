package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.room.Room;

import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;

public class BaseActivity extends AppCompatActivity{
    final private String APP_DB_NAME = "jellow_app_database";
    private static SessionManager sSession;
    private static String sVisibleAct ="";
    private static AppDatabase sAppDatabase;

    @Override
    protected void attachBaseContext(Context newBase) {
       super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        if (sSession == null)
            sSession = new SessionManager(this);
        if(sAppDatabase == null)
            sAppDatabase = Room.databaseBuilder(this, AppDatabase.class, APP_DB_NAME)
                    .allowMainThreadQueries()
                    .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getNonMenuClass().contains(getVisibleAct()))
            return false;
        super.onCreateOptionsMenu(menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        if(getLevelClass().contains(getVisibleAct()))
            getMenuInflater().inflate(R.menu.menu_main_with_search, menu);
        else if(getLanguageSettingsActivity().equals(getVisibleAct()))
            getMenuInflater().inflate(R.menu.menu_main_with_language_pack_update, menu);
        else
            getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            menu.findItem(R.id.closePopup).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(getLevelClass().contains(getVisibleAct()))
            return false;
        switch(item.getItemId()) {
            case R.id.profile:
                if(getVisibleAct().equals(ProfileFormActivity.class.getSimpleName()))
                    break;
                startActivity(new Intent(this, ProfileFormActivity.class));
                finish();
                break;
            case R.id.aboutJellow:
                if(getVisibleAct().equals(AboutJellowActivity.class.getSimpleName()))
                    break;
                startActivity(new Intent(this, AboutJellowActivity.class));
                finish();
                break;
            case R.id.tutorial:
                if(getVisibleAct().equals(TutorialActivity.class.getSimpleName()))
                    break;
                startActivity(new Intent(this, TutorialActivity.class));
                finish();
                break;
            case R.id.keyboardInput:
                if(getVisibleAct().equals(KeyboardInputActivity.class.getSimpleName()))
                    break;
                startActivity(new Intent(this, KeyboardInputActivity.class));
                finish();
                break;
            case R.id.languageSelect:
                if(getVisibleAct().equals(LanguageSelectActivity.class.getSimpleName()) )
                    break;
                startActivity(new Intent(this, LanguageSelectActivity.class));
                finish();
                break;
            case R.id.settings:
                if(getVisibleAct().equals(SettingActivity.class.getSimpleName()))
                    break;
                startActivity(new Intent(this, SettingActivity.class));
                finish();
                break;
            case R.id.accessibilitySetting:
                if(getVisibleAct().equals(AccessibilitySettingsActivity.class.getSimpleName()))
                    break;
                startActivity(new Intent(this, AccessibilitySettingsActivity.class));
                finish();
                break;
            case R.id.resetPreferences:
                if(getVisibleAct().equals(ResetPreferencesActivity.class.getSimpleName()))
                    break;
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                finish();
                break;
            case R.id.languagePackUpdate:
                if(getVisibleAct().equals(LanguagePackUpdateActivity.class.getSimpleName()))
                    break;
                startActivity(new Intent(this, LanguagePackUpdateActivity.class));
                finish();
                break;
            case R.id.feedback:
                if(getVisibleAct().equals(FeedbackActivity.class.getSimpleName()) ||
                        getVisibleAct().equals(FeedbackActivityTalkBack.class.getSimpleName()) )
                    break;
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, FeedbackActivityTalkBack.class));
                } else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
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

    protected SessionManager getSession(){
        return sSession;
    }

    protected AppDatabase getAppDatabase(){
        return sAppDatabase;
    }

    boolean isConnectedToNetwork(ConnectivityManager connMgr){
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * <p>This function check whether user device is not wifi only and
     * has sim card inserted into SIM slot and user can make a call.
     * @return true if device can able to make phone calls.</p>
     * */
    public boolean isDeviceReadyToCall(TelephonyManager tm){
        return tm != null
            && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE
                && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * <p>This function check whether does Accessibility Talkback feature turned of or not.
     * @return true if Accessibility Talkback feature is on.</p>
     * */
    public boolean isAccessibilityTalkBackOn(AccessibilityManager am) {
        return am != null && am.isEnabled() && am.isTouchExplorationEnabled();
    }

    /**
     * <p>This function gives screen aspect ratio.
     * @return aspect ratio value in float.</p>
     * */
    public boolean isNotchDevice(){
        float aspectRatio = (float)this.getResources().getDisplayMetrics().widthPixels /
                ((float)this.getResources().getDisplayMetrics().heightPixels);
        return (aspectRatio >= 2.0 && aspectRatio <= 2.15);
    }

    public void setActivityTitle(String title){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        if (title.contains("("))
            ((TextView)findViewById(R.id.tvActionbarTitle)).setText(title.substring(0,title.indexOf("(")));
        else
            ((TextView)findViewById(R.id.tvActionbarTitle)).setText(title);
    }

    public void enableNavigationBack(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
    }

    public void finishCurrentActivity(View view) {
        finish();
    }

    public void openPrivacyPolicyPage(View view){
        startActivity(new Intent("android.intent.action.VIEW",
                Uri.parse(getString(R.string.privacy_link))));
    }

    /**
     * <p>This function will find and return the blood group of user
     * @return blood group of user.</p>
     * */
    public String getBloodGroup(int bloodGroup) {
        switch(bloodGroup){
            case  1: return getString(R.string.aPos);
            case  2: return getString(R.string.aNeg);
            case  3: return getString(R.string.bPos);
            case  4: return getString(R.string.bNeg);
            case  5: return getString(R.string.abPos);
            case  6: return getString(R.string.abNeg);
            case  7: return getString(R.string.oPos);
            case  8: return getString(R.string.oNeg);
            default: return "";
        }
    }

    private String getLevelClass() {
        return MainActivity.class.getSimpleName() + "," +
            LevelTwoActivity.class.getSimpleName() + "," +
            LevelThreeActivity.class.getSimpleName() + "," +
            SequenceActivity.class.getSimpleName();
    }

    private String getNonMenuClass() {
        return UserRegistrationActivity.class.getSimpleName();
    }

    private String getLanguageSettingsActivity(){
        return LanguageSelectActivity.class.getSimpleName();
    }


    public String getVisibleAct() {
        return sVisibleAct;
    }

    public void setVisibleAct(String visibleAct) {
        sVisibleAct = visibleAct;
    }

    public void setGridSize(){
        if(getSession().getGridSize() == GlobalConstants.NINE_ICONS_PER_SCREEN){
            setUserProperty("GridSize", "9");
            setCrashlyticsCustomKey("GridSize", "9");
        }else if(getSession().getGridSize() == GlobalConstants.FOUR_ICONS_PER_SCREEN){
            setUserProperty("GridSize", "4");
            setCrashlyticsCustomKey("GridSize", "4");
        }else if(getSession().getGridSize() == GlobalConstants.THREE_ICONS_PER_SCREEN){
            setUserProperty("GridSize", "3");
            setCrashlyticsCustomKey("GridSize", "3");
        }else if(getSession().getGridSize() == GlobalConstants.TWO_ICONS_PER_SCREEN){
            setUserProperty("GridSize", "2");
            setCrashlyticsCustomKey("GridSize", "2");
        }else if(getSession().getGridSize() == GlobalConstants.ONE_ICON_PER_SCREEN){
            setUserProperty("GridSize", "1");
            setCrashlyticsCustomKey("GridSize", "1");
        }
    }

    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}

