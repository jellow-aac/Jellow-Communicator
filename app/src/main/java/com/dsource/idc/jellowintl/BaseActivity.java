package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.dsource.idc.jellowintl.makemyboard.AddEditIconAndCategoryActivity;
import com.dsource.idc.jellowintl.makemyboard.BoardActivity;
import com.dsource.idc.jellowintl.makemyboard.BoardListActivity;
import com.dsource.idc.jellowintl.makemyboard.BoardSearchActivity;
import com.dsource.idc.jellowintl.makemyboard.HomeActivity;
import com.dsource.idc.jellowintl.makemyboard.RepositionIconsActivity;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.activity.IconSelectActivity;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

public class BaseActivity extends AppCompatActivity{
    final private String APP_DB_NAME = "jellow_app_database";
    private static SessionManager sSession;
    private static String sVisibleAct ="";
    private static AppDatabase sAppDatabase;

    @Override
    protected void attachBaseContext(Context newBase) {
       SessionManager s = new SessionManager(newBase);
       if(s.getCurrentBoardLanguage()==null||s.getCurrentBoardLanguage().equals(""))
           super.attachBaseContext((LanguageHelper.onAttach(newBase)));
       else super.attachBaseContext(LanguageHelper.onAttach(newBase,s.getCurrentBoardLanguage()));
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
        if(getBoardClass().contains(getVisibleAct()) || getNonMenuClass().contains(getVisibleAct()))
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
        ((TextView)findViewById(R.id.tvActionbarTitle)).setText(title);
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+title+"</font>"));
    }

    public void enableNavigationBack(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
    }

    public void finishCurrentActivity(View view) {
        finish();
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

    public String getShortBloodGroup(int bloodGroup) {
        switch(bloodGroup){
            case 1: return "A+ve";
            case 2: return "A-ve";
            case 3: return "B+ve";
            case 4: return "B-ve";
            case 5: return "AB+ve";
            case 6: return "AB-ve";
            case 7: return "O+ve";
            case 8: return "O-ve";
            default: return "not selected";
        }
    }

    private String getLevelClass() {
        return MainActivity.class.getSimpleName() + "," +
            LevelTwoActivity.class.getSimpleName() + "," +
            LevelThreeActivity.class.getSimpleName() + "," +
            SequenceActivity.class.getSimpleName();
    }

    private String getBoardClass() {
        return
                AddEditIconAndCategoryActivity.class.getSimpleName()+ ","+
                        BoardSearchActivity.class.getSimpleName() + ","+
                        HomeActivity.class.getSimpleName() + ","+
                        IconSelectActivity.class.getSimpleName() + ","+
                        BoardActivity.class.getSimpleName() + ","+
                        BoardListActivity.class.getSimpleName() + ","+
                        RepositionIconsActivity.class.getSimpleName();
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


    @Override
    protected void onResume() {
        super.onResume();
        //isAlive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*The variable can be used to get does app is in fore ground or in background*/
        //isAlive = false;
    }
}

