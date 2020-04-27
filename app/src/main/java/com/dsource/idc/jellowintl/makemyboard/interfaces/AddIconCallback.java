package com.dsource.idc.jellowintl.makemyboard.interfaces;

import com.dsource.idc.jellowintl.models.JellowIcon;

public interface AddIconCallback {
    void onAddedSuccessfully(JellowIcon icon);
    void onFailure(String msg);
}
