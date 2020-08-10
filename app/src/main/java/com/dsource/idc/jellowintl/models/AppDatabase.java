package com.dsource.idc.jellowintl.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.dsource.idc.jellowintl.Presentor.CategoryPreferencesDao;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.daos.BoardDao;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.daos.VerbiageDao;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.VerbiageModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.helper_classes.CustomTypeConverter;

@Database(entities = {CategoryPreference.class, BoardModel.class, VerbiageModel.class}, version = 2, exportSchema = false)
@TypeConverters(CustomTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryPreferencesDao categoryPreferenceDao();
    public abstract BoardDao boardDao();
    public abstract VerbiageDao verbiageDao();
}
