package com.dsource.idc.jellowintl.JsonConverter;

import android.support.annotation.Keep;

public class JELLOW_ICON {
    @Keep
    public String Display_Label,Speech_Label,L,LL,Y,YY,M,MM,D,DD,N,NN,S,SS;

    public JELLOW_ICON(String Display_Label,String Speech_Label, String L,
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

    public JELLOW_ICON() {
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
