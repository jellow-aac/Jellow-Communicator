package com.dsource.idc.jellowintl.models;

import androidx.annotation.Keep;

import java.io.Serializable;

public class JellowIcon implements Serializable{
    @Keep
    public String IconDrawable,IconTitle,IconSpeech;
    @Keep
    public int parent0, parent1, parent2;

    public JellowIcon(String iconTitle, String iconDrawable, int p1, int p2, int p3) {
        IconDrawable = iconDrawable;
        IconTitle = iconTitle;
        this.parent0 = p1;
        this.parent1 = p2;
        this.parent2 = p3;
    }
    public JellowIcon(String iconTitle,String speechText, String iconDrawable, int p1, int p2, int p3) {
        IconDrawable = iconDrawable;
        IconTitle = iconTitle;
        IconSpeech = speechText;
        this.parent0 = p1;
        this.parent1 = p2;
        this.parent2 = p3;
    }

    public boolean isEqual(JellowIcon icon)
    {
        return parent0 == icon.parent0 && parent1 == icon.parent1 && parent2 == icon.parent2;

    }

    public boolean isCustomIcon() {return parent0==-1;}

    public void setIconTitle(String iconTitle) {
        this.IconTitle = iconTitle;
    }
}
