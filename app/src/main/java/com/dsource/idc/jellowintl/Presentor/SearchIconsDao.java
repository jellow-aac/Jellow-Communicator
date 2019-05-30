package com.dsource.idc.jellowintl.Presentor;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dsource.idc.jellowintl.models.CategoryPreference;
import com.dsource.idc.jellowintl.models.SearchIcons;

import java.util.List;

public interface SearchIconsDao {

    @Query("SELECT * FROM SearchIcons")
    List<SearchIcons> getAllIcons();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreferences(SearchIcons... searchIcons);

    @Query("DELETE FROM SearchIcons")
    void searchIconsData();
}
