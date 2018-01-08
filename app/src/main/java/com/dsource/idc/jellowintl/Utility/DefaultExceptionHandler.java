package com.dsource.idc.jellowintl.Utility;

import android.app.Activity;
import android.util.Log;

import static com.dsource.idc.jellowintl.Utility.Analytics.reportException;

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
        /*Intent intent = new Intent(activity, SplashActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                AppController.getInstance().getBaseContext(), 0, intent, intent.getFlags());

        //Following code will restart your application after 2 seconds
        AlarmManager mgr = (AlarmManager) AppController.getInstance().getBaseContext()
                .getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                pendingIntent);*/

        //This will finish your activity manually
        activity.finish();

        //This will stop your application and take out from it.
        System.exit(2);

    }
}