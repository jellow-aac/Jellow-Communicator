package com.dsource.idc.jellowboard.makemyboard.models;

import android.graphics.drawable.Drawable;

public class ListItem
{
    public String title;
    public android.graphics.drawable.Drawable drawable;

    public ListItem(String title, Drawable drawable) {
        this.title = title;
        this.drawable = drawable;
    }
}
