package com.dsource.idc.jellowintl.factories;

import android.content.Context;

import com.dsource.idc.jellowintl.utility.PackageRemoverAsync;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.Arrays;

import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;

public class LanguageFactory {

    /**
     * This function returns the Language Code of the currently active language
     * @param context
     * @return Language code (01- English (India), 02- English (US), 03 – English (UK), 04-
     * Hindi, 05- Marathi, 06- Bengali, 07- English (AU), 08- Spanish, 09- Tamil, 10- German
     * and 12- French)
     ***/
    public static String getCurrentLanguageCode(Context context){

        String localeCode = getCurrentLocaleCode(context);
        return getLanguageCode(localeCode);
    }

    public static String getCurrentLocaleCode(Context context){
        SessionManager sessionManager = new SessionManager(context);
        return sessionManager.getLanguage();
    }

    public static String getLanguageCode(String langCode){
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
            case SessionManager.FR_FR:
                return  "12";
            default:
                return null;
        }

    }

    public static void deleteOldLanguagePackagesInBackground(Context context){
        new PackageRemoverAsync().execute(context);
    }

    public static String[] getAvailableLanguages(){
        String[] languages = new String[LangMap.size()];
        LangMap.keySet().toArray(languages);
        Arrays.sort(languages);
        return languages;
    }

    public static boolean isMarathiPackageAvailable(Context context){
        try{
            File file = context.getDir(SessionManager.UNIVERSAL_PACKAGE, Context.MODE_PRIVATE);
            File packageZip = new File(file.getPath(),SessionManager.MR_IN+".zip");
            File packageZipTemp = new File(file.getPath(),SessionManager.MR_IN+".zip.temp");
            File audioFolder = new File(context.getDir(PathFactory.UNIVERSAL_FOLDER,
                    Context.MODE_PRIVATE).getAbsolutePath()+"/"+PathFactory.AUDIO_FOLDER);
            return !packageZip.exists() && !packageZipTemp.exists() && audioFolder.exists();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return false;
    }

    /** If zip is exist then data is not completely extracted from zip.
     *  If zip does is exist then data is extracted from zip successfully, the zip file is deleted.
     *  So when zip is exist we can consider the data is unavailable and when zip does not exist then
     *  data is available.
     *  **/
    public static boolean isLanguageDataAvailable(Context context){
        try{
            File file = context.getDir(SessionManager.UNIVERSAL_PACKAGE, Context.MODE_PRIVATE);
            File packageZip = new File(file.getPath(),SessionManager.UNIVERSAL_PACKAGE+".zip");
            File packageZipTemp = new File(file.getPath(),SessionManager.UNIVERSAL_PACKAGE+".zip.temp");
            File drawableFolder = new File(context.getDir(PathFactory.UNIVERSAL_FOLDER,
                    Context.MODE_PRIVATE).getAbsolutePath()+"/"+PathFactory.DRAWABLE_FOLDER);
            return !packageZip.exists() && !packageZipTemp.exists() && drawableFolder.exists();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return false;
    }
}
