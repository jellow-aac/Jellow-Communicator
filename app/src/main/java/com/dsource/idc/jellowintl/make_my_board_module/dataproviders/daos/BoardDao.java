package com.dsource.idc.jellowintl.make_my_board_module.dataproviders.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;

import java.util.List;

@Dao
public interface BoardDao {

    @Query("SELECT * FROM BoardModel")
    List<BoardModel> getAllBoards();

    @Query("SELECT * FROM BoardModel where board_name like:query")
    List<BoardModel> getAllBoardsStartWithName(String query);

    @Query("SELECT * FROM BoardModel where language_code =(:languageCode)")
    List<BoardModel> getAllBoards(String languageCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBoard(BoardModel boardModel);

    @Query("SELECT * FROM BoardModel where board_id = (:boardID)")
    BoardModel getBoardById(String boardID);

    @Update
    void updateBoard(BoardModel boardModel);


    @Delete
    void deleteBoard(BoardModel boardModel);

    @Query("SELECT board_name from BoardModel where board_id = (:boardId)")
    String getBoardNameFromId(String boardId);
}
