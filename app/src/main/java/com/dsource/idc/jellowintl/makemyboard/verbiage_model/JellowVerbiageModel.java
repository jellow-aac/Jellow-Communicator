package com.dsource.idc.jellowintl.makemyboard.verbiage_model;


import androidx.annotation.Keep;

public class JellowVerbiageModel {
    /**
     * L: Like
     * LL: Really Like
     * Y: Yes
     * YY: Really Yes
     * M: More
     * MM: Really More
     * D: Don't Like
     * DD: Really Don't Like
     * N: No
     * NN: Really No
     * S: Less
     * SS: Really Less
     */

    @Keep
    private String Display_Label,Speech_Label,Event_Tag,L,LL,Y,YY,M,MM,D,DD,N,NN,S,SS;

    public JellowVerbiageModel(String Display_Label, String Speech_Label, String L,
                               String LL, String Y,
                               String YY, String M,
                               String MM, String D,
                               String DD, String N,
                               String NN, String S,
                               String SS) {
        this.Display_Label = Display_Label;
        this.Speech_Label=Speech_Label;
        this.L = L;
        this.LL = LL;
        this.Y = Y;
        this.YY = YY;
        this.M = M;
        this.MM = MM;
        this.D = D;
        this.DD = DD;
        this.N = N;
        this.NN = NN;
        this.S = S;
        this.SS = SS;
    }

    //Empty constructor required | DO NOT REMOVE
    public JellowVerbiageModel() {
    }

    //This constructor is used to generate default speech on the basis of the name | DO NOT REMOVE
    public JellowVerbiageModel(String IconName) {
        this.L = "I like "+IconName;
        this.LL = "I really Like "+IconName;
        this.Y = "I want "+IconName;
        this.YY = "I really want "+IconName;
        this.M = "I want more "+IconName;
        this.MM = "I really want more "+IconName;
        this.D = "I  don't like "+IconName;
        this.DD = "I really don't like "+IconName;
        this.N = "I don't want "+IconName;
        this.NN = "I really don't want "+IconName;
        this.S = "I don't want "+IconName+" anymore";
        this.SS ="I really don't want "+IconName+" anymore";
        this.Speech_Label = IconName.toLowerCase();
        this.Display_Label = IconName;
    }

    public String getDisplay_Label() {
        return Display_Label;
    }

    public void setDisplay_Label(String display_Label) {
        Display_Label = display_Label;
    }

    public String getSpeech_Label() {
        return Speech_Label;
    }

    public void setSpeech_Label(String speech_Label) {
        Speech_Label = speech_Label;
    }

    public String getEvent_Tag() {
        return Event_Tag;
    }

    public void setEvent_Tag(String event_Tag) {
        Event_Tag = event_Tag;
    }

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getLL() {
        return LL;
    }

    public void setLL(String LL) {
        this.LL = LL;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }

    public String getYY() {
        return YY;
    }

    public void setYY(String YY) {
        this.YY = YY;
    }

    public String getM() {
        return M;
    }

    public void setM(String m) {
        M = m;
    }

    public String getMM() {
        return MM;
    }

    public void setMM(String MM) {
        this.MM = MM;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getDD() {
        return DD;
    }

    public void setDD(String DD) {
        this.DD = DD;
    }

    public String getN() {
        return N;
    }

    public void setN(String n) {
        N = n;
    }

    public String getNN() {
        return NN;
    }

    public void setNN(String NN) {
        this.NN = NN;
    }

    public String getS() {
        return S;
    }

    public void setS(String s) {
        S = s;
    }

    public String getSS() {
        return SS;
    }

    public void setSS(String SS) {
        this.SS = SS;
    }
}
