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

        SessionManager sessionManager = new SessionManager(context);
        String langCode = sessionManager.getLanguage();

        switch (langCode){
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
            case SessionManager.ES_ES:
                return "08";
            case SessionManager.TA_IN:
                return  "09";
            case SessionManager.DE_DE:
                return  "10";
            default:
                return null;

        }

    }
}
