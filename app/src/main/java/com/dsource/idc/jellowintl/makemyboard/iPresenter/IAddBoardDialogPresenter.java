package com.dsource.idc.jellowintl.makemyboard.iPresenter;

import com.dsource.idc.jellowintl.makemyboard.iView.IAddBoardDialogView;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

public interface IAddBoardDialogPresenter extends IBasePresenter<IAddBoardDialogView> {

    void saveBoard(BoardModel boardModel);

    void getBoardModel(String id);

    void updateBoard(BoardModel board);
}
