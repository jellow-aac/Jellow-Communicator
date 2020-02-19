package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.BuildConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Date;

/**
 * Created by ekalpa on 6/14/2017.
 */

public class Analytics {
    private static FirebaseAnalytics mFirebaseAnalytics;
    private static FirebaseDatabase mDb;
    private static DatabaseReference mRef;
    private static long start, finish;
    private static String pushId;
    private static String randomKey;
    private static Bundle bundle;

    // should be called only once at the start of the app
    public static void getAnalytics(Context context, String userId) {

        mDb = FirebaseDatabase.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.setMinimumSessionDuration(5*60*1000);
        mRef = mDb.getReference(BuildConfig.DB_TYPE+"/users/"+userId+"/sessions");
        pushId = mRef.push().getKey();
        bundle = new Bundle();
    }

    // should be called on onResume() of any Activity
    public static void startMeasuring()
    {
        start = System.currentTimeMillis();
    }


    // should be called on onPause() of any Activity
    public static void stopMeasuring(String screenName)
    {
        finish = System.currentTimeMillis();
        randomKey = mRef.push().getKey();
        long duration = (finish - start)/1000;
        if(duration != 0)
        {
            mRef.child(pushId).child(screenName).child(randomKey).child("identifier").setValue(ServerValue.TIMESTAMP);
            mRef.child(pushId).child(screenName).child(randomKey).child("duration").setValue(duration);
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

    // if last pushId is older than 24hours (86400000 milliseconds) then create new pushId.
    public static long validatePushId(long previousTimeStamp){
        long curTimeStamp = new Date().getTime();
        //Current time is equal to or more than 24 hours when previous pushId is created then
        // create new pushId.
        if(curTimeStamp >= previousTimeStamp + 86400000L) {
            pushId = mRef.push().getKey();
            return curTimeStamp;
        }
        return previousTimeStamp;
    }

    public static boolean isAnalyticsActive(){
        return mFirebaseAnalytics != null && mDb != null &&
                mRef != null && bundle != null;
    }

    public static void setCrashlyticsCustomKey(String key, String value){
        Crashlytics.setString(key, value);
    }

    public static void resetAnalytics(Context context, String userId){
        mFirebaseAnalytics = null;
        mDb = null;
        mRef = null;
        bundle = null;
        getAnalytics(context, userId);
    }
}