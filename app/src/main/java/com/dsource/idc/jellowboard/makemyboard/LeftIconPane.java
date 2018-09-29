package com.dsource.idc.jellowboard.makemyboard;

import android.support.annotation.Keep;

import com.dsource.idc.jellowboard.utility.JellowIcon;

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
