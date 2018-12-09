package com.dsource.idc.jellowboard.utility;

import android.support.annotation.Keep;

import com.dsource.idc.jellowboard.makemyboard.interfaces.AbstractDataProvider;

import java.io.Serializable;

public class JellowIcon extends AbstractDataProvider.Data implements Serializable{
    @Keep
    public String IconDrawable,IconTitle;
    @Keep
    public int parent0, parent1, parent2;
    @Keep private boolean isCustomIcon = false;

    public JellowIcon(String iconTitle, String iconDrawable, int p1, int p2, int p3) {
        IconDrawable = iconDrawable;
        IconTitle = iconTitle;
        this.parent0 = p1;
        this.parent1 = p2;
        this.parent2 = p3;
    }

    public boolean isEqual(JellowIcon icon)
    {
        return parent0 == icon.parent0 && parent1 == icon.parent1 && parent2 == icon.parent2;

    }

    public void setCustomIcon(boolean isCustomIcon) { this.isCustomIcon = isCustomIcon; }

    public boolean isCustomIcon() {return isCustomIcon;}

    public void setIconTitle(String iconTitle) {
        this.IconTitle = iconTitle;
    }

    @Override
    public long getId() {
        int numA=10;
        int numB=50;
        return  (numA*parent0+parent1)*numB+parent2;
    }

    @Override
    public boolean isSectionHeader() {
        return false;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public String getText() {
        return IconTitle;
    }

    @Override
    public boolean isPinned() {
        return false;
    }

    @Override
    public void setPinned(boolean pinned) {

    }
}
