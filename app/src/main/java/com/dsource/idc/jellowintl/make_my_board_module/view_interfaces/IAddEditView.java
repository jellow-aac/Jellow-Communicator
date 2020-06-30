package com.dsource.idc.jellowintl.make_my_board_module.view_interfaces;

import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public interface IAddEditView extends IBaseView{

    void onIconLoaded(ArrayList<JellowIcon> icons);

}
