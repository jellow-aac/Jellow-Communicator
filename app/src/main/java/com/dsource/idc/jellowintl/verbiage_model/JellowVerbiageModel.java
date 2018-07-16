package com.dsource.idc.jellowintl.verbiage_model;

import android.support.annotation.Keep;

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
    public String Display_Label,Speech_Label,L,LL,Y,YY,M,MM,D,DD,N,NN,S,SS;

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
}
