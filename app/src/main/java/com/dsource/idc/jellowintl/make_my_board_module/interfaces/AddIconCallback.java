package com.dsource.idc.jellowintl.make_my_board_module.interfaces;

import com.dsource.idc.jellowintl.models.JellowIcon;

public interface AddIconCallback {
    void onAddedSuccessfully(JellowIcon icon);
    void onFailure(String msg);
}
