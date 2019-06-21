package com.dsource.idc.jellowintl.makemyboard.verbiage_model;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JellowVerbiageModelTest {

    @Test
    public void jellowVerbiageModelTest(){
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
        assert  model.getDisplay_Label().equals(displayLabel) &&
                model.getSpeech_Label().equals(speechLabel) &&
                model.getL().equals(l) &&
                model.getLL().equals(ll) &&
                model.getY().equals(y) &&
                model.getYY().equals(yy) &&
                model.getM().equals(m) &&
                model.getMM().equals(mm) &&
                model.getD().equals(d) &&
                model.getDD().equals(dd) &&
                model.getN().equals(n) &&
                model.getNN().equals(nn) &&
                model.getS().equals(s) &&
                model.getSS().equals(ss);
        model = new JellowVerbiageModel(null, null, null,  null,  null,
                null, null,  null,  null,  null,  null, null, null, null);
        model.setDisplay_Label(displayLabel);
        model.setSpeech_Label(speechLabel);
        model.setL(l);
        model.setLL(ll);
        model.setY(y);
        model.setYY(yy);
        model.setM(m);
        model.setMM(mm);
        model.setD(d);
        model.setDD(dd);
        model.setN(n);
        model.setNN(nn);
        model.setS(s);
        model.setSS(ss);
        assert  model.getDisplay_Label().equals(displayLabel) &&
                model.getSpeech_Label().equals(speechLabel) &&
                model.getL().equals(l) &&
                model.getLL().equals(ll) &&
                model.getY().equals(y) &&
                model.getYY().equals(yy) &&
                model.getM().equals(m) &&
                model.getMM().equals(mm) &&
                model.getD().equals(d) &&
                model.getDD().equals(dd) &&
                model.getN().equals(n) &&
                model.getNN().equals(nn) &&
                model.getS().equals(s) &&
                model.getSS().equals(ss);
     }
}
