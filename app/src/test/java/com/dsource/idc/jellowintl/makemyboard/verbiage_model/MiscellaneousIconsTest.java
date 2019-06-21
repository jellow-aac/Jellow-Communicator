package com.dsource.idc.jellowintl.makemyboard.verbiage_model;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MiscellaneousIconsTest {

    @Test
    public void miscellaneousIconsTest(){
        String title = "Like";
        String l = "Like";
        String ll = "Really Like";
        MiscellaneousIcons miscIcon = new MiscellaneousIcons(title, l, ll);
        assert miscIcon.getTitle().equals(title) &&
                miscIcon.getL().equals(l) &&
                miscIcon.getLL().equals(ll);
        miscIcon = new MiscellaneousIcons(null, null, null);
        miscIcon.setTitle(title);
        miscIcon.setL(l);
        miscIcon.setLL(ll);
        assert miscIcon.getTitle().equals(title) &&
                miscIcon.getL().equals(l) &&
                miscIcon.getLL().equals(ll);
    }

}
