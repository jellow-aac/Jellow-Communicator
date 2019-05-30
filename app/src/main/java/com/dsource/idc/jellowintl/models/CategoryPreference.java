package com.dsource.idc.jellowintl.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.io.Serializable;

@Entity
public class CategoryPreference implements Serializable {

    @ColumnInfo(name = "preference_string")
    private String prefString;

    public String getPrefString() {
        return prefString;
    }

    public void setPrefString(String prefString) {
        this.prefString = prefString;
    }
}
