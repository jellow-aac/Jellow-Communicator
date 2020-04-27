package com.dsource.idc.jellowintl.makemyboard.models;

import android.graphics.drawable.Drawable;

public class ListItem
{
    private String title;
    private Drawable drawable;

    public ListItem(String title, Drawable drawable) {
        this.title = title;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
