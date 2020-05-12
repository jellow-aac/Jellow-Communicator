package com.dsource.idc.jellowintl.makemyboard.presenter_interfaces;

import android.content.Context;

import com.dsource.idc.jellowintl.makemyboard.view_interfaces.IAddEditView;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.data_models.BoardModel;

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
