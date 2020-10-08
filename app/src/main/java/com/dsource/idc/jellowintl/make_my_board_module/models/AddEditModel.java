package com.dsource.idc.jellowintl.make_my_board_module.models;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.activity.HomeActivity;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces.IAddEditPresenter;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.IAddEditView;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.BOARD_ID;

public class AddEditModel extends BaseModel<IAddEditView> implements IAddEditPresenter {


    private Context context;
    private  BoardModel currentBoard;
    private BoardDatabase database;

    public AddEditModel(Context context, BoardModel currentBoard, AppDatabase database){
        this.context = context;
        this.currentBoard = currentBoard;
        this.database = new BoardDatabase(database);
    }

    @Override
    public void loadIcons() {
        ArrayList<JellowIcon> icons = new ArrayList<>();
        icons.add(new JellowIcon(context.getString(R.string.add_icon),"add_icon",-1,-1,-1));
        icons.addAll(currentBoard.getIconModel().getAllIcons());
        mView.onIconLoaded(icons);
    }

    @Override
    public void removeIcon(int position) {
        //Handling invalid remove requests
        if(position<0 || position >= currentBoard.getIconModel().getChildren().size())
            return;
        //Try to delete it, this code is ori
        try {
            currentBoard.getIconModel().getChildren().remove(position);
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        updateBoard(currentBoard);
    }

    @Override
    public void updateBoard(final BoardModel currentBoard) {
        new AsyncTaskLoader<Void>(context){
            @Nullable
            @Override
            public Void loadInBackground() {
                database.updateBoardIntoDatabase(currentBoard);
                return null;
            }
        }.loadInBackground();
    }

    @Override
    public void nextPressed(Context mContext) {
        currentBoard.setSetupStatus(BoardModel.STATUS_L3);
        updateBoard(currentBoard);
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra(BOARD_ID, currentBoard.getBoardId());
        mContext.startActivity(intent);
    }


}
