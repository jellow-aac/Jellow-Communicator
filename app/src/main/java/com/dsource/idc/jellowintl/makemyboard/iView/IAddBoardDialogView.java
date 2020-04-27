package com.dsource.idc.jellowintl.makemyboard.iView;

import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

public interface IAddBoardDialogView extends IBaseView {

    void boardRetrieved(BoardModel board);

    void savedSuccessfully(BoardModel board);

    void updatedSuccessfully(BoardModel boardId);

    void error(String msg);
}
