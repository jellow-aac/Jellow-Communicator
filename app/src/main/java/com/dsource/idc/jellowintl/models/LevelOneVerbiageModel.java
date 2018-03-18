package com.dsource.idc.jellowintl.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ekalpa on 6/23/2017.
 */
public class LevelOneVerbiageModel {
    @SerializedName("arrays")
    private ArrayList<ArrayList<String>> mVerbiageModel;

    public ArrayList<ArrayList<String>> getVerbiageModel() {
        return mVerbiageModel;
    }
}
