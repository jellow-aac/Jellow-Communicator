package com.dsource.idc.jellow.Utility;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by ekalpa on 6/14/2017.
 */

public class UserDataMeasure {
    private final String TAG = "com.dsource.idc.jellow";
    private Context mContext;
    private FirebaseAnalytics mFirebaseAnalytics;

    public UserDataMeasure(Context context){
        mContext = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
    }

    public void recordGridItem(String gridItem){
        Bundle bundle = new Bundle();
        bundle.putString("gridItem", gridItem);
        mFirebaseAnalytics.logEvent("gridItem", bundle);
    }

    public void recordScreen(String screenName){
        mFirebaseAnalytics.setCurrentScreen((Activity) mContext, screenName, null);
    }

    public void reportLog(String message, int msgCode){
        if(msgCode == Log.ERROR)
            FirebaseCrash.logcat(Log.ERROR, TAG, message);
        else
            FirebaseCrash.logcat(Log.INFO, TAG, message);
    }

    public void reportException(Throwable error){
        FirebaseCrash.report(error);
    }
}
