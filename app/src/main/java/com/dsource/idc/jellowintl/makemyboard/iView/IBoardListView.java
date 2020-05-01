package com.dsource.idc.jellowintl.makemyboard.iView;

import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

import java.util.ArrayList;

public interface IBoardListView extends IBaseView {

    void boardLoaded(ArrayList<BoardModel> boardList);
}
