package com.dsource.idc.jellowboard.verbiage_model;

import android.support.annotation.Keep;

public class VerbiageHolder {
    private String IconID,IconName;
    private JellowVerbiageModel IconVerbaige;

    @Keep
    public VerbiageHolder(String iconID, String iconName, JellowVerbiageModel iconVerbaige) {
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

    public JellowVerbiageModel getIconVerbaige() {
        return IconVerbaige;
    }

    public void setIconVerbaige(JellowVerbiageModel iconVerbaige) {
        IconVerbaige = iconVerbaige;
    }
}
