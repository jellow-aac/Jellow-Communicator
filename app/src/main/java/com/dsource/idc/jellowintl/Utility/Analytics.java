package com.dsource.idc.jellowintl.Utility;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.dsource.idc.jellowintl.BuildConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

/**
 * Created by ekalpa on 6/14/2017.
 */

public class Analytics {
    private final static String TAG = "com.dsource.idc.jellowintl";
    static FirebaseAnalytics mFirebaseAnalytics;
    static FirebaseDatabase mDb;
    static DatabaseReference mRef;
    static long start, finish;
    static String pushId;
    static String randomKey;
    static Bundle bundle;




    // should be called only once at the start of the app

    public static void getAnalytics(Context context,String contact) {

        mDb = FirebaseDatabase.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.setMinimumSessionDuration(5*60*1000);
        mRef = mDb.getReference(BuildConfig.DB_TYPE+"/users/"+contact+"/sessions");
        pushId = mRef.push().getKey();
        bundle = new Bundle();

    }


    // should be called on onResume() of any Activity
    public static void startMeasuring()
    {
        start = System.currentTimeMillis();
    }





    // should be called on onPause() of any Activity
    public static void stopMeasuring(String Activity)
    {
        finish = System.currentTimeMillis();
        randomKey = mRef.push().getKey();
        long duration = (finish - start)/1000;
        if(duration != 0)
        {
            mRef.child(pushId).child(Activity).child(randomKey).child("identifier").setValue(ServerValue.TIMESTAMP);
            mRef.child(pushId).child(Activity).child(randomKey).child("duration").setValue(duration);
        }

    }


    public static void bundleEvent(String itemName, Bundle bundle)
    {
        mFirebaseAnalytics.logEvent(itemName, bundle);
    }


    public  static void singleEvent(String EventName, String EventValue){
        bundle.clear();
        bundle.putString(EventName,EventValue);
        mFirebaseAnalytics.logEvent(EventName,bundle);
    }


    public static void setUserProperty(String name,String value)
    {
        mFirebaseAnalytics.setUserProperty(name,value);
    }


    public static void reportLog(String message, int msgCode){
        if(msgCode == Log.ERROR)
            FirebaseCrash.logcat(Log.ERROR, TAG, message);
        else
            FirebaseCrash.logcat(Log.INFO, TAG, message);
    }

    public static void reportException(Throwable error){
        FirebaseCrash.report(error);
    }
}
