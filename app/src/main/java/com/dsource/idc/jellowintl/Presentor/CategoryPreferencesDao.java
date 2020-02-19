package com.dsource.idc.jellowintl.Presentor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dsource.idc.jellowintl.models.CategoryPreference;

@Dao
public interface CategoryPreferencesDao {

    @Query("SELECT * FROM CategoryPreference WHERE category_position=(:catPos)")
    CategoryPreference getCategoryPreference(String catPos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreferences(CategoryPreference preferences);

    @Query("DELETE FROM CategoryPreference")
    void clearPreferences();
}