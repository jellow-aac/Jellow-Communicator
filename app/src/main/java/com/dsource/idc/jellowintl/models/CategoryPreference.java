package com.dsource.idc.jellowintl.models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CategoryPreference implements Serializable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "category_position")
    private String categoryPosition;

    @ColumnInfo(name = "preference_string")
    private String prefString;


    public String getCategoryPosition() {
        return categoryPosition;
    }

    public void setCategoryPosition(String categoryPosition) {
        this.categoryPosition = categoryPosition;
    }

    public void setPrefString(String prefString) {
        this.prefString = prefString;
    }

    public String getPrefString() {
        return this.prefString;
    }

    public String toString(){
        return getCategoryPosition()+"-"+ getPrefString();
    }
}
