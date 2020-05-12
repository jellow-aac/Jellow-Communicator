package com.dsource.idc.jellowintl.makemyboard.view_interfaces;

import com.dsource.idc.jellowintl.makemyboard.dataproviders.data_models.BoardModel;

import java.util.ArrayList;

public interface IBoardListView extends IBaseView {

    void boardLoaded(ArrayList<BoardModel> boardList);
}
