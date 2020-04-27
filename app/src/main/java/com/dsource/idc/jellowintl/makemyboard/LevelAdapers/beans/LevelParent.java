package com.dsource.idc.jellowintl.makemyboard.LevelAdapers.beans;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

public class LevelParent implements Parent<LevelChild> {

    private List<LevelChild> list;
    private String title;
    public LevelParent(String title, List<LevelChild> items) {
        this.list = items;
        this.title = title;
    }

    @Override
    public List<LevelChild> getChildList() {
        return list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
