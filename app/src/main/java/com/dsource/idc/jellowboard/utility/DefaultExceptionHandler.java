package com.dsource.idc.jellowboard.utility;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowboard.UserRegistrationActivity;

/**
 * Created by ekalpa on 12/4/2017.
 */

public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {
    Activity activity;

    public DefaultExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("Jellow","exception caught", ex);
        Crashlytics.logException(ex);

        if(ex.getMessage().equals("unableToResume")){
            Intent intent = new Intent(activity, UserRegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            //This will finish your activity manually
            activity.finish();
            //This will stop your application and take out from it.
            System.exit(2);
        }

        Toast.makeText(activity, "Unfortunately, Jellow has stopped.", Toast.LENGTH_SHORT).show();
        //This will finish your activity manually
        activity.finishAffinity();
        //This will stop your application and take out from it.
        System.exit(2);
    }
}