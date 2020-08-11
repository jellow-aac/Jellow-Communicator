package com.dsource.idc.jellowintl.make_my_board_module.datamodels;

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
