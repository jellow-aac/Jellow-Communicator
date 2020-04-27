package com.dsource.idc.jellowintl.makemyboard.iPresenter;

import android.content.Context;

import com.dsource.idc.jellowintl.makemyboard.iView.IAddEditView;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

public interface IAddEditPresenter extends IBasePresenter<IAddEditView> {

    /**
     * function to start loading icon from the board
     */
    void loadIcons();

    /**
     *
     * @param adapterPosition position from which icon needs to be removed
     */
    void removeIcon(int adapterPosition);

    void updateBoard(BoardModel currentBoard);

    void nextPressed(Context mContext);
}
