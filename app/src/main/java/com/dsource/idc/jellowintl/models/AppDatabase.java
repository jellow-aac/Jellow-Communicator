package com.dsource.idc.jellowintl.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dsource.idc.jellowintl.Presentor.CategoryPreferencesDao;

@Database(entities = {CategoryPreference.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryPreferencesDao categoryPreferenceDao();
}
