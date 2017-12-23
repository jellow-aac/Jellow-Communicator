package com.dsource.idc.jellow.Utility;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.dsource.idc.jellow.R;

import java.util.Locale;

/**
 * Created by ekalpa on 11/27/2017.
 */
public class ChangeAppLocale {
    //private final int LANG_ENG = 0, LANG_HINDI = 1;
    private Context mContext;

    public ChangeAppLocale(Context context){
        mContext = context;
    }

    public void setLocale(){
        Locale locale;
        SessionManager session= new SessionManager(mContext);
        switch (session.getLanguage()){
            case SessionManager.ENG_IN:
                locale = new Locale("en","IN");
                break;
            case SessionManager.ENG_UK:
                locale = new Locale("en","GB");
                break;
            case SessionManager.ENG_US:
                locale = new Locale("en","US");
                break;
            case SessionManager.HI_IN:
                locale = new Locale(mContext.getString(R.string.locale_lang_hi),
                                    mContext.getString(R.string.locale_reg_IN));
                break;
            default:
                locale = Locale.US;
                break;
        }

        Configuration conf = mContext.getResources().getConfiguration();
        conf.locale = locale;
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        Locale.setDefault(locale);
        mContext.getResources().updateConfiguration(conf, dm);
    }
}
