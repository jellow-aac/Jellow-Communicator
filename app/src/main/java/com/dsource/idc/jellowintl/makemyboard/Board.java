package com.dsource.idc.jellowintl.makemyboard;

import android.graphics.Bitmap;
import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * This class is for storing the instance of the Board object
 */

public class Board implements Serializable {
    @Keep
    public String boardTitle;
    @Keep
    public String boardID;
    private Bitmap boardIcon;

    public Board(String Uid,String boardTitle,Bitmap boardIcon) {
        this.boardTitle = boardTitle;
        this.boardID = Uid;
        this.boardIcon=boardIcon;
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

    public Bitmap getBoardIcon() {
        return boardIcon;
    }

    public void setBoardIcon(Bitmap boardIcon) {
        this.boardIcon = boardIcon;
    }
}
