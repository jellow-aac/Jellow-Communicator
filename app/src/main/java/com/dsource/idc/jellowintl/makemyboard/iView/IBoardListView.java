package com.dsource.idc.jellowintl.makemyboard.iView;

import com.dsource.idc.jellowintl.makemyboard.MyPair;
import com.dsource.idc.jellowintl.makemyboard.iView.IBaseView;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

import java.util.ArrayList;

public interface IBoardListView extends IBaseView {

    void boardLoaded(ArrayList<BoardModel> boardList);

    void languageVsBoardCountLoaded(ArrayList<MyPair<String, Integer>> list);
}
