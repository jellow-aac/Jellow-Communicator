package com.dsource.idc.jellowintl.models;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SeqNavigationButtonTest {

    @Test
    public void SequenceNavigationButtonModelTest(){
        String displayLabel = "Next >>";
        String speechLabel = "Next";
        SeqNavigationButton model = new SeqNavigationButton();
        model.setDisplay_Label(displayLabel);
        model.setSpeech_Label(speechLabel);
        assert model.getDisplay_Label().equals(displayLabel) &&
                model.getSpeech_Label().equals(speechLabel);
    }
}
