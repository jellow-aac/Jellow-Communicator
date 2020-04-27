package com.dsource.idc.jellowintl.makemyboard.iModels;

import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.iPresenter.IAddBoardDialogPresenter;
import com.dsource.idc.jellowintl.makemyboard.iView.IAddBoardDialogView;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.models.AppDatabase;

public class AddBoardDialogModel extends BaseModel<IAddBoardDialogView> implements IAddBoardDialogPresenter {

    private BoardDatabase database;

    public AddBoardDialogModel(AppDatabase appDatabase) {
        this.database = new BoardDatabase(appDatabase);
    }

    @Override
    public void saveBoard(BoardModel boardModel) {
        database.addBoardToDatabase(boardModel);
        mView.savedSuccessfully(boardModel);
    }

    @Override
    public void getBoardModel(String id) {
        mView.boardRetrieved(database.getBoardById(id));
    }

    @Override
    public void updateBoard(BoardModel board) {
        database.updateBoardIntoDatabase(board);
        mView.updatedSuccessfully(board);
    }
}
