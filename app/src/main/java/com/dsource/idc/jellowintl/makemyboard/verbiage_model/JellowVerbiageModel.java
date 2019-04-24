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
    public String Display_Label,Speech_Label,Event_Tag,L,LL,Y,YY,M,MM,D,DD,N,NN,S,SS;

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
}
