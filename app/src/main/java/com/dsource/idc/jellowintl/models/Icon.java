package com.dsource.idc.jellowintl.models;

import com.google.gson.annotations.SerializedName;

public class Icon {

    /**
     L- Like
     LL - Really Like
     Y - Yes
     YY - Really Yes
     M - More
     MM - Really More
     N - No
     NN - Really No
     D - Don’t Like
     DD - Really Don’t Like
     S - Less
     SS - Really Less
     */
    @SerializedName("Display_Label")
    private String Display_Label;
    @SerializedName("Speech_Label")
    private String Speech_Label;
    @SerializedName("Event_Tag")
    private String Event_Tag;
    @SerializedName("L")
    private String L;
    @SerializedName("LL")
    private String LL;
    @SerializedName("Y")
    private String Y;
    @SerializedName("YY")
    private String YY;
    @SerializedName("M")
    private String M;
    @SerializedName("MM")
    private String MM;
    @SerializedName("D")
    private String D;
    @SerializedName("DD")
    private String DD;
    @SerializedName("N")
    private String N;
    @SerializedName("NN")
    private String NN;
    @SerializedName("S")
    private String S;
    @SerializedName("SS")
    private String SS;

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
