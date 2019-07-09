package com.dsource.idc.jellowintl.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class SearchIcon implements Serializable {

    @ColumnInfo(name = "icon_title")
    private String iconTitle;

    @ColumnInfo(name = "icon_speech")
    private String iconSpeech;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "icon_drawable")
    private String iconDrawable;

    @ColumnInfo(name = "icon_p1")
    private int levelOne;

    @ColumnInfo(name = "icon_p2")
    private int levelTwo;

    @ColumnInfo(name = "icon_p3")
    private int levelThree;

    @NonNull
    public String getIconTitle() {
        return iconTitle;
    }

    public void setIconTitle(@NonNull String iconTitle) {
        this.iconTitle = iconTitle;
    }

    public String getIconSpeech() {
        return iconSpeech;
    }

    public void setIconSpeech(String iconSpeech) {
        this.iconSpeech = iconSpeech;
    }

    public String getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(String iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public int getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(int levelOne) {
        this.levelOne = levelOne;
    }

    public int getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(int levelTwo) {
        this.levelTwo = levelTwo;
    }

    public int getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(int levelThree) {
        this.levelThree = levelThree;
    }


    public String toString(){
        return getIconTitle() +"-"+
                getIconSpeech() +"-"+
                getIconDrawable() +"-"+
                getLevelOne() +"-"+
                getLevelTwo() +"-"+
                getLevelThree();
    }
}
