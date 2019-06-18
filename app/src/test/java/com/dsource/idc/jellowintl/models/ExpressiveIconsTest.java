package com.dsource.idc.jellowintl.models;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExpressiveIconsTest {

    @Test
    public void expressiveIconModelTest(){
        String title = "Home";
        MiscellaneousIcon miscIcon = new MiscellaneousIcon();
        miscIcon.setTitle(title);
        assert miscIcon.getTitle().equals(title);
    }
}
