package com.dsource.idc.jellowintl.makemyboard.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dsource.idc.jellowintl.makemyboard.models.VerbiageModel;

import java.util.List;

@Dao
public interface VerbiageDao {

    @Query("SELECT * FROM VerbiageModel where language_code =(:languageCode)")
    List<VerbiageModel> getAllVerbiage(String languageCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVerbiage(VerbiageModel verbiageModel);

    @Query("SELECT * FROM VerbiageModel where language_code =  (:languageCode) and id LIKE :verbiage_id")
    VerbiageModel getVerbiageById(String verbiage_id,String languageCode);

    @Update
    void updateVerbiage(VerbiageModel verbiageModel);

    @Query("DELETE FROM VERBIAGEMODEL WHERE language_code = (:language_code)")
    void deleteVerbiage(String language_code);

    @Query("SELECT COUNT(*) FROM VerbiageModel WHERE language_code = (:languageCode) and id LIKE :id")
    int getLevelOneIconCount(String id,String languageCode);

    @Query("SELECT * FROM VerbiageModel where language_code  = (:languageCode) and id like :id")
    List<VerbiageModel> getVerbiageList(String  id,String languageCode);

    @Query("SELECT * FROM VerbiageModel WHERE language_code  = (:languageCode) and title LIKE :iconTitle")
    List<VerbiageModel> getVerbiageListByTitle(String iconTitle,String languageCode);
}
