package com.dsource.idc.jellow.Utility;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dsource.idc.jellow.SplashActivity;
import com.dsource.idc.jellow.app.AppController;
import com.google.firebase.crash.FirebaseCrash;

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

        FirebaseCrash.report(ex);
        Log.e("Jellow","exception caught", ex);
        Intent intent = new Intent(activity, SplashActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                AppController.getInstance().getBaseContext(), 0, intent, intent.getFlags());

        //Following code will restart your application after 2 seconds
        AlarmManager mgr = (AlarmManager) AppController.getInstance().getBaseContext()
                .getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                pendingIntent);

        //This will finish your activity manually
        activity.finish();

        //This will stop your application and take out from it.
        System.exit(2);

    }
}