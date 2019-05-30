package com.dsource.idc.jellowintl.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class SearchIcons implements Serializable {

    @NonNull
    @ColumnInfo(name = "icon_title")
    private String iconTitle;

    @ColumnInfo(name = "icon_speech")
    private String iconSpeech;

    @ColumnInfo(name = "icon_drawable")
    private String iconDrawable;

    @ColumnInfo(name = "icon_p1")
    private String levelOne;

    @ColumnInfo(name = "icon_p2")
    private String levelTwo;

    @ColumnInfo(name = "icon_p3")
    private String levelThree;

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

    public String getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(String levelOne) {
        this.levelOne = levelOne;
    }

    public String getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(String levelTwo) {
        this.levelTwo = levelTwo;
    }

    public String getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(String levelThree) {
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
