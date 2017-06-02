package com.dsource.idc.jellow.Utility;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by ekalpa on 5/31/2017.
 */

public class EvaluateDisplayMetricsUtils {
    private Context mContext;

    public  EvaluateDisplayMetricsUtils(Context context){
        this.mContext = context;
    }

    public void calculateStoreDeviceHeightWidth(){
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        AppPreferences appPref = new AppPreferences(mContext);
        appPref.setScreenWidth(dpWidth);    appPref.setScreenHeight(dpHeight);
    }

    public int getPixelsFromDpVal(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, mContext.getResources().getDisplayMetrics());
    }
}
