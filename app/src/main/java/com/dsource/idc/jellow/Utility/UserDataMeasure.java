package com.dsource.idc.jellow.Utility;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.dsource.idc.jellow.BuildConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

/**
 * Created by ekalpa on 6/14/2017.
 */

public class UserDataMeasure {
    private final String TAG = "com.dsource.idc.jellow";
    private Context mContext;
    private FirebaseAnalytics mFirebaseAnalytics;
    static FirebaseDatabase mDb;
    static DatabaseReference mRef;
    static long start, finish;
    static String pushId;
    static String randomKey;


    // should be called only once at the start of the app

    public static void getAnalytics(Context context,String contact) {


        mDb = FirebaseDatabase.getInstance();

        mRef = mDb.getReference(BuildConfig.DB_TYPE+"/users/"+contact+"/sessions");
        pushId = mRef.push().getKey();
    }



    public static void startMeasuring()
    {
        start = System.currentTimeMillis();
    }

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

    public UserDataMeasure(Context context){
        mContext = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
    }

    public void recordGridItem(String gridItem){
        Bundle bundle = new Bundle();
        bundle.putString("gridItem", gridItem);
        mFirebaseAnalytics.logEvent("GridItem", bundle);
    }

    public void recordExpressiveGridItem(String gridItem){
        Bundle bundle = new Bundle();
        bundle.putString("expressiveGrid", gridItem);
        mFirebaseAnalytics.logEvent("ExpressiveGrid", bundle);
    }

    public void recordNavigationItem(String itemValue){
        Bundle bundle = new Bundle();
        bundle.putString("navigation", itemValue);
        mFirebaseAnalytics.logEvent("Navigation", bundle);
    }

    public void recordKeyboardItem(String itemValue){
        Bundle bundle = new Bundle();
        bundle.putString("keyboard", itemValue);
        mFirebaseAnalytics.logEvent("Keyboard", bundle);
    }

    public void genericEvent(String itemName,Bundle bundle)
    {
        mFirebaseAnalytics.logEvent(itemName, bundle);
    }


    public void recordScreen(String screenName){
        mFirebaseAnalytics.setCurrentScreen((Activity) mContext, screenName, null);
    }

    public void setProperty(String propertyName, String propertyValue){
        mFirebaseAnalytics.setUserProperty(propertyName, propertyValue);
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
