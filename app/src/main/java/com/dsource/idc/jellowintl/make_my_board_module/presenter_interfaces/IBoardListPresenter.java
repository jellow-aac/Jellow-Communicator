package com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces;

import android.content.Context;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.IBoardListView;

import java.util.ArrayList;

public interface IBoardListPresenter extends IBasePresenter<IBoardListView> {

    void loadBoards(String language);

    void deleteBoard(Context context,BoardModel board);

    void openBoard(Context context, BoardModel boardId);

    ArrayList<BoardModel> getAllBoardsStartWithName(String query);
}
