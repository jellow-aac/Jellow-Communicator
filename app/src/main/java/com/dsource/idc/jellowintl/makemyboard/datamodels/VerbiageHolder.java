package com.dsource.idc.jellowintl.makemyboard.datamodels;


import androidx.annotation.Keep;

import com.dsource.idc.jellowintl.models.Icon;

public class VerbiageHolder {
    private String IconID,IconName;
    private Icon IconVerbaige;

    @Keep
    public VerbiageHolder(String iconID, String iconName, Icon iconVerbaige) {
        IconID = iconID;
        IconName = iconName;
        IconVerbaige = iconVerbaige;
    }

    public String getIconID() {
        return IconID;
    }

    public void setIconID(String iconID) {
        IconID = iconID;
    }

    public String getIconName() {
        return IconName;
    }

    public void setIconName(String iconName) {
        IconName = iconName;
    }

    public Icon getIconVerbaige() {
        return IconVerbaige;
    }

    public void setIconVerbaige(Icon iconVerbaige) {
        IconVerbaige = iconVerbaige;
    }
}
