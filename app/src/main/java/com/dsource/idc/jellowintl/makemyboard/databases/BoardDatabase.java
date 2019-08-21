package com.dsource.idc.jellowintl.makemyboard.databases;

import android.content.Context;

import androidx.room.Room;

import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.iDataPresenter;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.models.AppDatabase;

import java.util.ArrayList;

/**
 * Created by Ayaz Alam on 31/05/2018.
 */
public class BoardDatabase{


    private static final String DATABASE_NAME ="board_database";
    private AppDatabase database;
    public BoardDatabase(Context context) {
        database = Room.databaseBuilder(context,AppDatabase.class,DATABASE_NAME).allowMainThreadQueries().build();
    }

    public void addBoardToDatabase(final BoardModel board) {
        database.boardDao().insertBoard(board);
    }

    public BoardModel getBoardById(String boardID) {
        return database.boardDao().getBoardById(boardID);
    }

    public void getAllBoards(final iDataPresenter<ArrayList<BoardModel>> presenter) {
        presenter.onSuccess(new ArrayList<>(database.boardDao().getAllBoards()));
    }
    public void getAllBoards(final String langCode, final iDataPresenter<ArrayList<BoardModel>> presenter) {
        presenter.onSuccess(new ArrayList<>(database.boardDao().getAllBoards(langCode)));
    }
    public void updateBoardIntoDatabase(BoardModel board) {
        database.boardDao().updateBoard(board);
    }

    public void deleteBoard(BoardModel boardID) {
        database.boardDao().deleteBoard(boardID);
    }
}