package com.dsource.idc.jellowintl.utility;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.UserRegistrationActivity;

import java.util.Date;

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
        // Below if checks if previous crash and current crash has more than 10 seconds
        // time difference then only restart the app otherwise close app completely.
        // App closed if time difference is less than 10 seconds to
        // prevent app from crash loop.
        if (((new Date().getTime()) - session.getLastCrashReported()) > 10000L) {
            session.setLastCrashReported(new Date().getTime());
            Intent intent = new Intent(activity, UserRegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            //This will finish your activity manually
            activity.finish();
            //This will stop your application and take you out from it.
            System.exit(2);
        }else {
            session.setLastCrashReported(0L);
            //This will finish your activity manually
            activity.finish();
            //This will stop your application and take you out from it.
            System.exit(2);
        }
    }
}