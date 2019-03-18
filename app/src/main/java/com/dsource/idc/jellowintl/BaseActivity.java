package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.Menu;
import android.view.accessibility.AccessibilityManager;

import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity{
    private static SessionManager mSession;
    TextToSpeechEngineSetup mTtsSetupHandler;

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
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            menu.findItem(R.id.closePopup).setVisible(false);
        }
        return true;
    }

    protected SessionManager getSession(){
        return mSession;
    }

    boolean isConnectedToNetwork(ConnectivityManager connMgr){
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * <p>This function check whether Text-to-speech service is running? It will
     * return true if service is running else false is service is closed.</p>
     * */
    public boolean isTTSServiceRunning(ActivityManager manager) {
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            // JellowTTSService is name of TTS service class.
            if (JellowTTSService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
}

