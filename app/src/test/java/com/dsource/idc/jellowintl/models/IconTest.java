package com.dsource.idc.jellowintl.models;

import org.junit.Test;

public class IconTest {

    @Test
    public void iconModelTest(){

        String displayLabel = "Eating";
        String speechLabel = "Eating";
        String eventTag = "Eating";
        String l = "I like to eat";
        String ll = "I really like to eat";
        String y = "I want to eat";
        String yy = "I really want to eat";
        String m = "I want to eat some more";
        String mm = "I really want to eat some more";
        String d = "I don't like to eat";
        String dd = "I really don't like to eat";
        String n = "I don't want to eat";
        String nn = "I really don't want to eat";
        String s = "I don't want to eat any more";
        String ss = "I really don't want to eat any more";
        Icon icon = new Icon();
        icon.setDisplay_Label(displayLabel);
        icon.setSpeech_Label(speechLabel);
        icon.setEvent_Tag(eventTag);
        icon.setL(l);
        icon.setLL(ll);
        icon.setY(y);
        icon.setYY(yy);
        icon.setM(m);
        icon.setMM(mm);
        icon.setD(d);
        icon.setDD(dd);
        icon.setN(n);
        icon.setNN(nn);
        icon.setS(s);
        icon.setSS(ss);
        assert icon.getDisplay_Label().equals(displayLabel) &&
                icon.getSpeech_Label().equals(speechLabel) &&
                icon.getEvent_Tag().equals(eventTag) &&
                icon.getL().equals(l) && icon.getLL().equals(ll) &&
                icon.getY().equals(y) && icon.getYY().equals(yy) &&
                icon.getM().equals(m) && icon.getMM().equals(mm) &&
                icon.getD().equals(d) && icon.getDD().equals(dd) &&
                icon.getN().equals(n) && icon.getNN().equals(nn) &&
                icon.getS().equals(s) && icon.getSS().equals(ss);
    }
}
