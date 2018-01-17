package com.dsource.idc.jellowintl.Utility;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by ekalpa on 11/27/2017.
 */
public class ChangeAppLocale {
    private Context mContext;
    public ChangeAppLocale(Context context){
        mContext = context;
    }

    public void setLocale(){
        Locale locale;
        SessionManager session= new SessionManager(mContext);
        switch (session.getLanguage()){
            case SessionManager.ENG_UK:
                locale = Locale.UK;
                break;
            case SessionManager.ENG_US:
                locale = Locale.US;
                break;
            case SessionManager.HI_IN:
                locale = new Locale("hi","IN");
                break;
            case SessionManager.ENG_IN:
            default:
                locale = new Locale("en","IN");
                break;
        }

        Configuration conf = mContext.getResources().getConfiguration();
        conf.locale = locale;
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        Locale.setDefault(locale);
        mContext.getResources().updateConfiguration(conf, dm);
    }
}
