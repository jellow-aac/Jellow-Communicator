package com.dsource.idc.jellowintl.Presentor;

import androidx.room.Dao;
import androidx.room.Query;

import com.dsource.idc.jellowintl.models.CategoryPreference;

@Dao
public interface CategoryPreferencesDao {

    @Query("SELECT * FROM category_pref WHERE language IN (:language) AND levelOne IN (:levelOne) AND levelTwo IN (:levelTwo)")
    CategoryPreference getCategoryPreference(String language, String levelOne, String levelTwo);

    @Query("SELECT * FROM category_pref WHERE language IN (:language) AND levelOne IN (:levelOne)")
    CategoryPreference getCategoryPreference(String language, String levelOne);

}
