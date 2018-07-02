package com.dsource.idc.jellowintl.makemyboard;

import android.support.annotation.Keep;

public class Board {
    @Keep
    public String boardTitle;
    @Keep
    public int boardID,totalIcons;

    public Board(String boardTitle, int boardID, int totalIcons) {
        this.boardTitle = boardTitle;
        this.boardID = boardID;
        this.totalIcons = totalIcons;
    }
}
