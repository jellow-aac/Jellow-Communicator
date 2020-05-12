package com.dsource.idc.jellowintl.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.dsource.idc.jellowintl.Presentor.CategoryPreferencesDao;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.helper_classes.CustomTypeConverter;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.daos.BoardDao;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.daos.VerbiageDao;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.data_models.VerbiageModel;

@Database(entities = {CategoryPreference.class, BoardModel.class, VerbiageModel.class}, version = 1, exportSchema = false)
@TypeConverters(CustomTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryPreferencesDao categoryPreferenceDao();
    public abstract BoardDao boardDao();
    public abstract VerbiageDao verbiageDao();
}
