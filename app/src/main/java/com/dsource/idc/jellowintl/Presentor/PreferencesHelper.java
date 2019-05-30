package com.dsource.idc.jellowintl.Presentor;

import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.CategoryPreference;

public class PreferencesHelper {

    public static CategoryPreference getPrefString(AppDatabase appDatabase, CategoryPreference cPref){
        return appDatabase.categoryPreferenceDao().getCategoryPreference(cPref.getCategoryPosition());
    }

    public static void setPrefString(AppDatabase appDatabase, CategoryPreference cPref){
        appDatabase.categoryPreferenceDao().insertPreferences(cPref);
    }

    public static void clearPreferences(AppDatabase appDatabase){
        appDatabase.categoryPreferenceDao().clearPreferences();
    }
}
