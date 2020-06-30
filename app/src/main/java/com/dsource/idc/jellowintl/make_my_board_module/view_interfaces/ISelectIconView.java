package com.dsource.idc.jellowintl.make_my_board_module.view_interfaces;

import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.datamodels.LevelParent;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public interface ISelectIconView extends IBaseView {

    void onLevelLoaded(ArrayList<JellowIcon> list);
    void onSublevelLoaded(ArrayList<LevelParent> list);
    void onBoardSaved();
    void onFailure(String msg);
}
