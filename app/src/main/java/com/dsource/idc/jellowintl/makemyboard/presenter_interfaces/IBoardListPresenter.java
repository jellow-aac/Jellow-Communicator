package com.dsource.idc.jellowintl.makemyboard.presenter_interfaces;

import android.content.Context;

import com.dsource.idc.jellowintl.makemyboard.view_interfaces.IBoardListView;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.data_models.BoardModel;

import java.util.ArrayList;

public interface IBoardListPresenter extends IBasePresenter<IBoardListView> {

    void loadBoards(String language);

    void deleteBoard(Context context,BoardModel board);

    void openBoard(Context context, BoardModel boardId);

    ArrayList<BoardModel> getAllBoardsStartWithName(String query);
}
