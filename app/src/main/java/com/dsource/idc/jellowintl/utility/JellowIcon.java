package com.dsource.idc.jellowintl.utility;

import android.support.annotation.Keep;

import java.io.Serializable;

public class JellowIcon implements Serializable{
    @Keep
    public String IconDrawable,IconTitle;
    @Keep
    public int parent0, parent1, parent2;
    @Keep
    boolean isChecked=false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public JellowIcon(String iconTitle, String iconDrawable, int p1, int p2, int p3) {
        IconDrawable = iconDrawable;
        IconTitle = iconTitle;
        this.parent0 = p1;
        this.parent1 = p2;
        this.parent2 = p3;
    }


    public long getID()
    {
        int numA=10;
        int numB=50;
        return  (numA*parent0+parent1)*numB+parent2;
    }

    public boolean isEqual(JellowIcon icon)
    {
        return parent0 == icon.parent0 && parent1 == icon.parent1 && parent2 == icon.parent2;

    }


}
