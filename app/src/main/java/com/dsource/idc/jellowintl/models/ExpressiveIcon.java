package com.dsource.idc.jellowintl.models;

import com.google.gson.annotations.SerializedName;

public class ExpressiveIcon {

    @SerializedName("L")
    private String L;
    @SerializedName("LL")
    private String LL;
    @SerializedName("Title")
    private String Title;

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getLL() {
        return LL;
    }

    public void setLL(String LL) {
        this.LL = LL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
