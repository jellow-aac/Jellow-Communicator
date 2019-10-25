package com.dsource.idc.jellowintl.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dsource.idc.jellowintl.Presentor.CategoryPreferencesDao;
import com.dsource.idc.jellowintl.Presentor.VerbiageDao;

@Database(entities = {CategoryPreference.class,VerbiageModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryPreferencesDao categoryPreferenceDao();
    public abstract VerbiageDao verbiageDao();
}
