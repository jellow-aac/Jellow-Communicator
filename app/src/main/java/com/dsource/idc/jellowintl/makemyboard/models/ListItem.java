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

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}