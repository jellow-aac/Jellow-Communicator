package com.dsource.idc.jellowintl.models;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MiscellaneousIconTest {

    @Test
    public void miscellaneousIconModelTest(){
        String l = "I like to have fun";
        String ll = "I really like to have fun";
        String title = "Fun";
        ExpressiveIcon exIcon = new ExpressiveIcon();
        exIcon.setL(l);
        exIcon.setLL(ll);
        exIcon.setTitle(title);
        assert exIcon.getL().equals(l) && exIcon.getLL().equals(ll) &&
                exIcon.getTitle().equals(title);
    }
}
