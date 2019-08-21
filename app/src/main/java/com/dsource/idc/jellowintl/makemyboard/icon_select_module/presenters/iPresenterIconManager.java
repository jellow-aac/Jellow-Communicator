package com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters;

import com.dsource.idc.jellowintl.models.JellowIcon;

public interface iPresenterIconManager {
    void onListUpdated();
    void onItemSelected(JellowIcon icon, boolean checked);
    void onItemEdited();
    void onNextPressed();
    void onSelectionClear();

}
