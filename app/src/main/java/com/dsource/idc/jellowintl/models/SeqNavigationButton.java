package com.dsource.idc.jellowintl.models;

import com.google.gson.annotations.SerializedName;

public class SeqNavigationButton {
    @SerializedName("Display_Label")
    private String Display_Label;
    @SerializedName("Speech_Label")
    private String Speech_Label;

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
}
