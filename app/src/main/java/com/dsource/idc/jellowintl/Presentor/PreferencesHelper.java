package com.dsource.idc.jellowintl.Presentor;

import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.CategoryPreference;

public class PreferencesHelper {

    public static String getPrefString(AppDatabase appDatabase, String iconCode){
        CategoryPreference catPref = appDatabase.categoryPreferenceDao().getCategoryPreference(iconCode);
        return  catPref != null ? catPref.getPrefString() : "";
    }

    public static void setPrefString(AppDatabase appDatabase, CategoryPreference cPref){
        appDatabase.categoryPreferenceDao().insertPreferences(cPref);
    }

    public static void clearPreferences(AppDatabase appDatabase){
        appDatabase.categoryPreferenceDao().clearPreferences();
    }
}
