package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.accessibility.AccessibilityManager;

import androidx.appcompat.app.AppCompatActivity;

import com.dsource.idc.jellowintl.makemyboard.AddEditIconAndCategoryActivity;
import com.dsource.idc.jellowintl.makemyboard.BoardSearchActivity;
import com.dsource.idc.jellowintl.makemyboard.HomeActivity;
import com.dsource.idc.jellowintl.makemyboard.IconSelectActivity;
import com.dsource.idc.jellowintl.makemyboard.MyBoardsActivity;
import com.dsource.idc.jellowintl.makemyboard.RepositionIconsActivity;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

public class BaseActivity extends AppCompatActivity{
    private static SessionManager mSession;
    private static String mVisibleAct;

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
        if (mSession == null)
            mSession = new SessionManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getBoardClass().contains(getVisibleAct()) || getNonMenuClass().contains(getVisibleAct()))
            return false;
        super.onCreateOptionsMenu(menu);
        if(getLevelClass().contains(getVisibleAct()))
            getMenuInflater().inflate(R.menu.menu_main_with_search, menu);
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
                if(getVisibleAct().equals(LanguageSelectActivity.class.getSimpleName()) ||
                        getVisibleAct().equals(LanguageSelectTalkBackActivity.class.getSimpleName()) )
                    break;
                if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, LanguageSelectActivity.class));
                } else {
                    startActivity(new Intent(this, LanguageSelectTalkBackActivity.class));
                }
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
        return mSession;
    }

    public boolean isConnectedToNetwork(ConnectivityManager connMgr){
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
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+title+"</font>"));
    }

    public void enableNavigationBack(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
    }

    public String getLevelClass() {
        return MainActivity.class.getSimpleName() + "," +
            LevelTwoActivity.class.getSimpleName() + "," +
            LevelThreeActivity.class.getSimpleName() + "," +
            SequenceActivity.class.getSimpleName();
    }

    public String getBoardClass() {
        return
                AddEditIconAndCategoryActivity.class.getSimpleName()+ ","+
                        BoardSearchActivity.class.getSimpleName() + ","+
                        HomeActivity.class.getSimpleName() + ","+
                        IconSelectActivity.class.getSimpleName() + ","+
                        MyBoardsActivity.class.getSimpleName() + ","+
                        RepositionIconsActivity.class.getSimpleName();
    }

    public String getNonMenuClass() {
        return UserRegistrationActivity.class.getSimpleName();
    }

    public String getVisibleAct() {
        return mVisibleAct;
    }

    public void setVisibleAct(String visibleAct) {
        mVisibleAct = visibleAct;
    }
}

