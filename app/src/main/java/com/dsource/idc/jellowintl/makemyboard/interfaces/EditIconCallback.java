package com.dsource.idc.jellowintl.makemyboard.interfaces;

import com.dsource.idc.jellowintl.models.JellowIcon;

public interface EditIconCallback {
    void onIconEditClicked(JellowIcon iconToBeEdited,int positionInTheList);
}
