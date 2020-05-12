package com.dsource.idc.jellowintl.makemyboard.presenter_interfaces;

import com.dsource.idc.jellowintl.makemyboard.view_interfaces.IAddBoardDialogView;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.data_models.BoardModel;

public interface IAddBoardDialogPresenter extends IBasePresenter<IAddBoardDialogView> {

    void saveBoard(BoardModel boardModel);

    void getBoardModel(String id);

    void updateBoard(BoardModel board);
}
