package com.dsource.idc.jellowintl.makemyboard.verbiage_model;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class VerbiageHolderModelTest {

    @Test
    public void verbiageHolderModelTest(){
        String displayLabel = "Mango";
        String speechLabel = "Mango";
        String l = "I like mangoes";
        String ll = "I really like mangoes";
        String y = "I want a mango";
        String yy = "I really want a mango";
        String m = "I want more mangoes";
        String mm = "I really want some more mangoes";
        String d = "I don't like mangoes";
        String dd = "I really don't like mangoes";
        String n = "I don't want a mango";
        String nn = "I really don't want a mango";
        String s = "I don't want more mangoes";
        String ss = "I really don't want any more mangoes";
        JellowVerbiageModel model = new JellowVerbiageModel(displayLabel, speechLabel, l,  ll,  y,
                yy, m,  mm,  d,  dd,  n, nn, s, ss);
        String iconId = "0103040004";
        String iconName = "Mango";
        VerbiageHolder vHolder = new VerbiageHolder(iconId, iconName, model);
        assert vHolder.getIconID().equals(iconId) &&
                vHolder.getIconName().equals(iconName) &&
                vHolder.getIconVerbaige().equals(model);
        vHolder = new VerbiageHolder(null, null, null);
        vHolder.setIconID(iconId);
        vHolder.setIconName(iconName);
        vHolder.setIconVerbaige(model);
        assert vHolder.getIconID().equals(iconId) &&
                vHolder.getIconName().equals(iconName) &&
                vHolder.getIconVerbaige().equals(model);

    }
}
