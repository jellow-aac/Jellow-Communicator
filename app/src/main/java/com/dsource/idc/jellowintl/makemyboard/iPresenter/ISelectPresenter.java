package com.dsource.idc.jellowintl.makemyboard.iPresenter;

import com.dsource.idc.jellowintl.makemyboard.iView.ISelectIconView;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public interface ISelectPresenter extends IBasePresenter<ISelectIconView> {
    void loadLevels(int level, int sublevel);
    void loadLevels(BoardModel model);
    void loadSubLevels();
    int getLevel();
    void addListToBoard(BoardModel currentBoard, ArrayList<JellowIcon> list);
}
