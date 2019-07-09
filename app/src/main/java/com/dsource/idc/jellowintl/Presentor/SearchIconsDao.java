package com.dsource.idc.jellowintl.Presentor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dsource.idc.jellowintl.models.SearchIcon;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SearchIconsDao {

    @Query("SELECT * FROM SearchIcon WHERE icon_title LIKE (:searchString) LIMIT 20")
    List<SearchIcon> getSearchIconsList(String searchString);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPreferences(ArrayList<SearchIcon> searchIcons);

    @Query("DELETE FROM SearchIcon")
    void clearSearchDatabase();
}
