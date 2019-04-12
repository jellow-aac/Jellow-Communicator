package com.dsource.idc.jellowintl.models;

import com.google.gson.annotations.SerializedName;

public class MiscellaneousIcon {
    @SerializedName("Title")
    private String Title;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
