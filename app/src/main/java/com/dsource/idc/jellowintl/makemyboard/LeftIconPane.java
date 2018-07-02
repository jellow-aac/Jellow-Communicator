package com.dsource.idc.jellowintl.makemyboard;

import android.support.annotation.Keep;

import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class LeftIconPane {

    @Keep
    public String category;
    @Keep
    public int pos;
    @Keep
    public ArrayList<JellowIcon> subList;

    @Keep
    public LeftIconPane(String category,int pos, ArrayList<JellowIcon> subList) {
        this.category = category;
        this.subList = subList;
        this.pos=pos;
    }
}
