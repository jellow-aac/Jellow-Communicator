package com.dsource.idc.jellowboard.makemyboard;

import com.dsource.idc.jellowboard.utility.JellowIcon;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelModel implements Serializable{

    private ArrayList<JellowIcon> levelOneIcons;
    private ArrayList<JellowIcon> levelTwoIcons;
    private ArrayList<JellowIcon> levelThreeIcons;

    public LevelModel(ArrayList<JellowIcon> levelOneIcons, ArrayList<JellowIcon> levelTwoIcons, ArrayList<JellowIcon> levelThreeIcons) {
        this.levelOneIcons = levelOneIcons;
        this.levelTwoIcons = levelTwoIcons;
        this.levelThreeIcons = levelThreeIcons;
    }

    public ArrayList<JellowIcon> getLevelOneIcons() {
        return levelOneIcons;
    }

    public ArrayList<JellowIcon> getLevelTwoIcons(int level0) {
        return levelTwoIcons;
    }

    public ArrayList<JellowIcon> getLevelThreeIcons(int level0,int level1) {
        return levelThreeIcons;
    }
}
