package com.dsource.idc.jellowintl.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.dsource.idc.jellowintl.Presentor.CategoryPreferencesDao;
import com.dsource.idc.jellowintl.makemyboard.databases.Converters;
import com.dsource.idc.jellowintl.makemyboard.interfaces.BoardDao;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageDao;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.models.VerbiageModel;

@Database(entities = {CategoryPreference.class, BoardModel.class, VerbiageModel.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryPreferencesDao categoryPreferenceDao();
    public abstract BoardDao boardDao();
    public abstract VerbiageDao verbiageDao();
}
