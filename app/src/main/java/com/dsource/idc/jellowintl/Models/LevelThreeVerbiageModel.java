package com.dsource.idc.jellowintl.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ekalpa on 6/23/2017.
 */
public class LevelThreeVerbiageModel {
    @SerializedName("arrays")
    private ArrayList<ArrayList<ArrayList <ArrayList <String>>>> mVerbiageModel;

    public ArrayList<ArrayList<ArrayList <ArrayList <String>>>> getVerbiageModel() {
        return mVerbiageModel;
    }
}
