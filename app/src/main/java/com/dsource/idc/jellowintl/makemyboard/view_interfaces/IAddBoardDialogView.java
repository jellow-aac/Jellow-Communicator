package com.dsource.idc.jellowintl.makemyboard.view_interfaces;

import com.dsource.idc.jellowintl.makemyboard.dataproviders.data_models.BoardModel;

public interface IAddBoardDialogView extends IBaseView {

    void boardRetrieved(BoardModel board);

    void savedSuccessfully(BoardModel board);

    void updatedSuccessfully(BoardModel boardId);

    void error(String msg);
}
