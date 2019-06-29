package com.dsource.idc.jellowintl.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JellowIconTest {
    @Test
    public void jellowIconTest(){
        String IconTitle = "icon";
        String IconDrawable = "draw";
        String IconSpeech = "speech";

        int parent0 = 0;
        int parent1 = 1;
        int parent2 = 2;

        int p0 = -1;
        int p1 = -2;
        int p2 = -3;

        JellowIcon jellowIcon = new JellowIcon(IconTitle,IconDrawable,parent0,parent1,parent2);

        JellowIcon jellowIconC = new JellowIcon(IconTitle,IconSpeech,IconDrawable,p0,p1,p2);

        jellowIcon.setIconTitle(IconTitle);

        assertTrue(jellowIcon.isEqual(jellowIcon));

        assertFalse(jellowIcon.isEqual(jellowIconC));

        assertFalse(jellowIconC.isEqual(jellowIcon));

        assertTrue(jellowIconC.isEqual(jellowIconC));

        assertFalse(jellowIcon.isCustomIcon());

        assertTrue(jellowIconC.isCustomIcon());

    }
}
