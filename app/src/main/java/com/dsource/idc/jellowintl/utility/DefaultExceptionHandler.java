package com.dsource.idc.jellowintl.utility;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.UserRegistrationActivity;

/**
 * Created by ekalpa on 12/4/2017.
 */

public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Activity activity;

    public DefaultExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("Jellow","exception caught", ex);
        Crashlytics.logException(ex);
        SessionManager session = new SessionManager(activity);
        /*if(session.getAppRestarted()){
            session.setAppRestarted(false);
            Toast.makeText(activity, "Unfortunately, Jellow has stopped.", Toast.LENGTH_SHORT).show();
            activity.finish();
            //This will stop your application and take you out from it.
            System.exit(2);
        }*/


        Intent intent = new Intent(activity, UserRegistrationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        //session.setAppRestarted(true);
        //This will finish your activity manually
        activity.finish();
        //This will stop your application and take you out from it.
        System.exit(2);
    }
}