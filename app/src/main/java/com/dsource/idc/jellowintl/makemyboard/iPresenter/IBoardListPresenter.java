package com.dsource.idc.jellowintl.makemyboard.iPresenter;

import android.content.Context;

import com.dsource.idc.jellowintl.makemyboard.iView.IBoardListView;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

public interface IBoardListPresenter extends IBasePresenter<IBoardListView> {
    void loadBoards(String language);
    void deleteBoard(BoardModel board);
    void openBoard(Context context, BoardModel boardId);

    void loadLanguageVsBoardCount(Context context);
}
