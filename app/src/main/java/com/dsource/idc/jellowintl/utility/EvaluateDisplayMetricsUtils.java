package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by ekalpa on 5/31/2017.
 */
public class EvaluateDisplayMetricsUtils {
    private Context mContext;
    private SessionManager mSession;
    public  EvaluateDisplayMetricsUtils(Context context){
        this.mContext = context;
        mSession = new SessionManager(mContext);
    }

    public void calculateStoreDeviceHeightWidth(){
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mSession.setScreenWidth(displayMetrics.widthPixels / displayMetrics.density);
        mSession.setScreenHeight(displayMetrics.heightPixels / displayMetrics.density);
    }

    public void calculateStoreShadowRadiusAndBorderWidth(){
        if(mSession.getScreenHeight() >= 720)
            mSession.setShadowRadiusAndBorderWidth(0, 15);
        else
            mSession.setShadowRadiusAndBorderWidth(0, 7);
    }

    public int getPixelsFromDpVal(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, mContext.getResources().getDisplayMetrics());
    }
}