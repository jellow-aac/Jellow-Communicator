package com.dsource.idc.jellowboard.makemyboard.models;

import android.support.annotation.Keep;

import com.dsource.idc.jellowboard.makemyboard.models.IconModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is for storing the instance of the Board object
 */

public class Board implements Serializable {
    @Keep
    public String boardTitle;
    @Keep
    public String boardID;
    private boolean boardCompleted=false;
    private boolean boardIconsSelected=false;
    private IconModel boardIconModel;
    private int gridSize=3;
    private boolean passedAddEditIconScreen=false;
    private ArrayList<String> customIconIDs;

    @Keep
    public Board(String Uid, String boardTitle) {
        this.boardTitle = boardTitle;
        this.boardID = Uid;
        customIconIDs = new ArrayList<>();
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public void setBoardCompleted()
    {
        boardCompleted=true;
    }
    public boolean getBoardCompleteStatus()
    {
        return boardCompleted;
    }
    public boolean isBoardIconsSelected() {
        return boardIconsSelected;
    }
    public boolean isAddEditIconScreenPassed()
    {
        return passedAddEditIconScreen;
    }
    public void setAddEditIconScreenPassed()
    {
         passedAddEditIconScreen=true;
    }


    private void setBoardIconsSelected(boolean boardIconsSelected) {
        this.boardIconsSelected = boardIconsSelected;
    }
    @Keep
    public IconModel getBoardIconModel() {
        return boardIconModel;
    }
    @Keep
    public void setBoardIconModel(IconModel boardIconModel) {
        this.boardIconModel = boardIconModel;
        if(boardIconModel.getChildren().size()>0)
        setBoardIconsSelected(true);
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void addCustomIconID(String ID){customIconIDs.add(ID);}

    public ArrayList<String> getCustomIconLists(){
        return customIconIDs;
    }
}
