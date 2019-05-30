package com.dsource.idc.jellowintl.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dsource.idc.jellowintl.Presentor.CategoryPreferencesDao;

@Database(entities = {CategoryPreference.class, SearchIcons.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryPreferencesDao categoryPreferenceDao();
}
