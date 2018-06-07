package com.dsource.idc.jellowintl.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class LanguageHelper {


    /**
     * <p>{@returns} the Context having application default locale for all activities</p>
     * */
    public static Context onAttach(Context context) {
        String lang = new SessionManager(context).getLanguage();
        Locale locale;
        if(!lang.isEmpty())
            locale = new Locale(lang.split("-r")[0],lang.split("-r")[1]);
        else
            locale = new Locale("en","IN");
        return setLanguage(context, locale);
    }

    /**
     * <p>{@returns} the language code of a language saved by user.</p>
     * */
    /*public static String getLanguage(Context context) {
        return new SessionManager(context).getLanguage();
    }*/

    /**
     * This function updates application default locale.
     * {@returns} the {@Context} having application given locale.
     * **/
    public static Context setLanguage(Context context, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, locale);
        }
        return updateResourcesLegacy(context, locale);
    }

    /**
     * For android device versions above Nougat (7.0) updates application default
     * locale configurations and {@returns} new {@Context} object for the current
     * Context but whose resources are adjusted to match the given Configuration
     * **/
    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale) {

        Configuration configuration = context.getResources().getConfiguration();
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);

        return context.createConfigurationContext(configuration);
    }

    /**
     * For android device versions below Nougat (7.0) updates application default
     * locale configurations and {@return} new {@Context} object for the current
     * Context but whose resources are adjusted to match the given Configuration
     * **/
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, Locale locale) {

        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}