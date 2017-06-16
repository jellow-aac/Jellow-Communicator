package com.dsource.idc.jellow.Utility;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by ekalpa on 6/14/2017.
 */

public class UserDataMeasure {
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
}
