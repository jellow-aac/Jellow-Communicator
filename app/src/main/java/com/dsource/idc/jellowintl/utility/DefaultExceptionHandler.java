package com.dsource.idc.jellowintl.utility;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import static com.dsource.idc.jellowintl.utility.Analytics.reportException;

/**
 * Created by ekalpa on 12/4/2017.
 */

public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultUEH;
    Activity activity;

    public DefaultExceptionHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        reportException(ex);
        Log.e("Jellow","exception caught", ex);
        Toast.makeText(activity, "Unfortunately, Jellow has stopped.", Toast.LENGTH_SHORT).show();
        //This will finish your activity manually
        activity.finish();
        //This will stop your application and take out from it.
        System.exit(2);
    }
}