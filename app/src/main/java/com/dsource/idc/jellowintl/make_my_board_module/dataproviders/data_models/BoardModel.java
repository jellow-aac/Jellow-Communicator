package com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dsource.idc.jellowintl.make_my_board_module.datamodels.BoardIconModel;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class BoardModel implements Serializable {

    public static final int STATUS_L0 =0;//Not initialized
    public static final int STATUS_L1 =1;//Icons selected
    public static final int STATUS_L2 =2;//Add edit icon screen passed
    public static final int STATUS_L3 =3;//Reposition icon screen passed

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "board_id")
    private String boardId;

    @ColumnInfo(name = "board_name")
    private String boardName;

    @ColumnInfo(name = "board_icon_list")
    private BoardIconModel iconModel;

    @ColumnInfo(name = "setup_status")
    private int setupStatus=STATUS_L0;

    @ColumnInfo(name = "grid_sized")
    private int gridSize=4;

    @ColumnInfo(name = "language_code")
    private String language= SessionManager.ENG_IN;

    @ColumnInfo(name = "timestamp")
    private long timestamp;



    @ColumnInfo(name = "custom_icons")
    private ArrayList<String> customIconList;


    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public BoardIconModel getIconModel() {
        return iconModel;
    }

    public void setIconModel(BoardIconModel iconModel) {
        this.iconModel = iconModel;
    }

    public int getSetupStatus() {
        return setupStatus;
    }

    public void setSetupStatus(int setupStatus) {
        this.setupStatus = setupStatus;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void clearAllIcons() {
        iconModel = new BoardIconModel(new JellowIcon("","",-1,-1,-1));
    }


    public ArrayList<String> getCustomIconID() {
        return customIconList;
    }
    public ArrayList<String> getCustomIconList() {
        return customIconList;
    }

    public void setCustomIconList(ArrayList<String> customIconList) {
        this.customIconList = customIconList;
    }

    public void addCustomIconID(String customId) {
        if(customIconList==null) customIconList = new ArrayList<>();
        customIconList.add(customId);
    }
}
