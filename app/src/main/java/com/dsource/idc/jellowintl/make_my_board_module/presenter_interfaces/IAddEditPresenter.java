package com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces;

import android.content.Context;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.IAddEditView;

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
