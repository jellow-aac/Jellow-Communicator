package com.dsource.idc.jellowintl.factories;

import android.content.Context;

import com.dsource.idc.jellowintl.utility.SessionManager;

public class LanguageFactory {

    /**
     * This function returns the Language Code of the currently active language
     * @param context
     * @return Language code (01- English (India), 02- English (US), 03 – English (UK), 04-
     * Hindi, 05- Marathi, and 06- Bengali)
     *
     */
    public static String getCurrentLanguageCode(Context context){

        String localeCode = getCurrentLocaleCode(context);

        switch (localeCode){
            case SessionManager.ENG_IN:
                return "01";
            case SessionManager.ENG_US:
                return "02";
            case SessionManager.ENG_UK:
                return "03";
            case SessionManager.HI_IN:
                return "04";
            case SessionManager.MR_IN:
                return "05";
            case SessionManager.BN_IN:
                return "06";
            case SessionManager.BE_IN:
                return "06";
            case SessionManager.ENG_AU:
                return  "07";
            default:
                return null;

        }
    }

    public static String getCurrentLocaleCode(Context context){
        SessionManager sessionManager = new SessionManager(context);
        return sessionManager.getLanguage();
    }
}
