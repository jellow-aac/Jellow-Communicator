package com.dsource.idc.jellowintl.makemyboard.interfaces;

import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

public interface AddBoardListener {

    void onBoardCreated(BoardModel board);
    void onCancel();
    void onBoardUpdated(BoardModel board);

}
