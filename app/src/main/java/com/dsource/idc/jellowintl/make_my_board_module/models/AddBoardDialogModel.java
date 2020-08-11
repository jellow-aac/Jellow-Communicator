package com.dsource.idc.jellowintl.make_my_board_module.models;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces.IAddBoardDialogPresenter;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.IAddBoardDialogView;
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
