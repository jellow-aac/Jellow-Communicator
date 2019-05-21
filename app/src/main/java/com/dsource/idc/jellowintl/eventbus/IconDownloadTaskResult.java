package com.dsource.idc.jellowintl.eventbus;

public class IconDownloadTaskResult {
    private Boolean success;
    private String iconName;

    public IconDownloadTaskResult(Boolean success, String iconName) {
        this.success = success;
        this.iconName = iconName;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getIconName() {
        return iconName;
    }
}
