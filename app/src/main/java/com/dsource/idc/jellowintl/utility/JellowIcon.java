package com.dsource.idc.jellowintl.utility;

import android.support.annotation.Keep;

public class JellowIcon {
    @Keep
    public String IconDrawable,IconTitle;
    @Keep
    public int parent0, parent1, parent2;

    public JellowIcon(String iconTitle, String iconDrawable, int p1, int p2, int p3) {
        IconDrawable = iconDrawable;
        IconTitle = iconTitle;
        this.parent0 = p1;
        this.parent1 = p2;
        this.parent2 = p3;
    }
}
