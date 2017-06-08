package com.dsource.idc.jellow.Utility;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by ekalpa on 5/31/2017.
 */
public class EvaluateDisplayMetricsUtils {
    private Context mContext;
    private AppPreferences mAppPref;
    public  EvaluateDisplayMetricsUtils(Context context){
        this.mContext = context;
        this.mAppPref = new AppPreferences(mContext);
    }

    public void calculateStoreDeviceHeightWidth(){
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mAppPref.setScreenWidth(displayMetrics.widthPixels / displayMetrics.density);
        mAppPref.setScreenHeight(displayMetrics.heightPixels / displayMetrics.density);
    }

    public void calculateStoreShadowRadiusAndBorderWidth(){
        if(mAppPref.getScreenHeight() >= 720)
            mAppPref.setShadowRadiusAndBorderWidth(0, 15);
        else
            mAppPref.setShadowRadiusAndBorderWidth(0, 7);
    }

    public int getPixelsFromDpVal(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, mContext.getResources().getDisplayMetrics());
    }
}