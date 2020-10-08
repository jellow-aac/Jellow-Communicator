package com.dsource.idc.jellowintl.make_my_board_module.view_interfaces;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;

import java.util.ArrayList;

public interface IBoardListView extends IBaseView {

    void boardLoaded(ArrayList<BoardModel> boardList);
}
