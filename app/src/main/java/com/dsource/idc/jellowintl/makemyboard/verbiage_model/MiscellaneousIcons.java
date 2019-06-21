package com.dsource.idc.jellowintl.makemyboard.verbiage_model;

public class MiscellaneousIcons {
    private String Title,L,LL;

    //TODO Change when making next Json File.
    public MiscellaneousIcons(String Speech_Label, String L, String LL) {
        this.Title = Speech_Label;
        this.L = L;
        this.LL = LL;
    }

    public MiscellaneousIcons(){
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

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
}
