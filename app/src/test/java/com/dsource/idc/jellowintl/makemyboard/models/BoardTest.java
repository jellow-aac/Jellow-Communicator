package com.dsource.idc.jellowintl.makemyboard.models;

import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.models.JellowIcon;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class BoardTest {

    @Test
    public void boardModelTest(){
        String boardId = UUID.randomUUID().toString();
        Board board = new Board(boardId, "Fruits");
        assert board.getBoardTitle().equals("Fruits");
        assert board.getBoardID().equals(boardId);
        int gridSize = 4;
        String boardTitle = "Eating->Fruit";
        String customBoardId = "0110040004";
        JellowIcon jellowIcon = new JellowIcon("Apple", "0103050001GG", 3, 5, 1);
        IconModel boardIconModel = new IconModel(jellowIcon);
        boardId = UUID.randomUUID().toString();
        board.setBoardTitle(boardTitle);
        board.setBoardID(boardId);
        board.setBoardCompleted();
        board.setAddEditIconScreenPassed();
        board.setBoardIconModel(boardIconModel);
        board.setGridSize(gridSize);
        board.addCustomIconID(customBoardId);
        assert board.getBoardTitle().equals(boardTitle) &&
            board.getBoardID().equals(boardId) &&
            board.getBoardCompleteStatus() &&
            board.isAddEditIconScreenPassed() &&
            board.getBoardIconModel().equals(boardIconModel) &&
            board.getGridSize() == gridSize &&
            board.getCustomIconLists().get(0).equals(customBoardId);
    }
}