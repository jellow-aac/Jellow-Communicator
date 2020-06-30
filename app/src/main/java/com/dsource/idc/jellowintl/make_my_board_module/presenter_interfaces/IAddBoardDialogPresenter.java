package com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.IAddBoardDialogView;

public interface IAddBoardDialogPresenter extends IBasePresenter<IAddBoardDialogView> {

    void saveBoard(BoardModel boardModel);

    void getBoardModel(String id);

    void updateBoard(BoardModel board);
}
