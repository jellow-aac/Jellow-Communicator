package com.dsource.idc.jellowintl.make_my_board_module.view_interfaces;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;

public interface IAddBoardDialogView extends IBaseView {

    void boardRetrieved(BoardModel board);

    void savedSuccessfully(BoardModel board);

    void updatedSuccessfully(BoardModel boardId);

    void error(String msg);
}
