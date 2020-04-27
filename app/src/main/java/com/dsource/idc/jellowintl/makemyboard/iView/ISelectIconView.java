package com.dsource.idc.jellowintl.makemyboard.iView;

import com.dsource.idc.jellowintl.makemyboard.LevelAdapers.beans.LevelParent;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public interface ISelectIconView extends IBaseView {

    void onLevelLoaded(ArrayList<JellowIcon> list);
    void onSublevelLoaded(ArrayList<LevelParent> list);
    void onBoardSaved();
    void onFailure(String msg);
}
