package com.dsource.idc.jellowintl.make_my_board_module.datamodels;


import androidx.annotation.Keep;

import com.dsource.idc.jellowintl.models.Icon;

public class VerbiageHolder {
    private String IconID,IconName;
    private Icon IconVerbiage;

    @Keep
    public VerbiageHolder(String iconID, String iconName, Icon iconVerbiage) {
        IconID = iconID;
        IconName = iconName;
        IconVerbiage = iconVerbiage;
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

    public Icon getIconVerbiage() {
        return IconVerbiage;
    }

    public void setIconVerbiage(Icon iconVerbiage) {
        IconVerbiage = iconVerbiage;
    }
}
