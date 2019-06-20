package com.dsource.idc.jellowintl.makemyboard.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.R;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ListItemTest {

    @Test
    public void ListItemTest(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String title = "Apple";
        Drawable drawable = context.getDrawable(R.drawable.ic_add_icon);
        ListItem listItem = new ListItem(title, drawable);
        assert listItem.getTitle().equals(title) &&
                listItem.getDrawable().equals(drawable);
        String secondTitle = "Banana";
        Drawable secondDrawable = context.getDrawable(R.drawable.ic_action_image_dehaze);
        listItem.setTitle(secondTitle);
        listItem.setDrawable(secondDrawable);
        assert listItem.getTitle().equals(secondTitle) &&
                listItem.getDrawable().equals(secondDrawable);

    }
}
