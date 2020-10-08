package com.dsource.idc.jellowintl.make_my_board_module.models;

import android.content.Context;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.datamodels.BoardIconModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.IconDatabaseFacade;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.helper_classes.IconAsyncLoader;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.datamodels.LevelChild;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.datamodels.LevelParent;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.IDataCallback;
import com.dsource.idc.jellowintl.make_my_board_module.managers.SelectionManager;
import com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces.ISelectPresenter;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.ISelectIconView;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectIconModel extends BaseModel<ISelectIconView> implements ISelectPresenter {

    private int mLevel = 0;
    private int mSubLevel = -1;
    private IconDatabaseFacade database;
    private AppDatabase appDatabase;
    private Context context;

    public SelectIconModel(Context context, AppDatabase database, String language) {
        this.context = context;
        appDatabase = database;
        this.database = new IconDatabaseFacade(language, database);
    }

    @Override
    public void loadLevels(final int level, final int sublevel) {
        IconAsyncLoader iconLoader = new IconAsyncLoader(database, new IDataCallback<ArrayList<JellowIcon>>() {
            @Override
            public void onSuccess(ArrayList<JellowIcon> list) {
                mLevel = level;
                mSubLevel = sublevel;
                mView.onLevelLoaded(list);
            }

            @Override
            public void onFailure(String msg) {
                mView.onFailure(msg);
            }
        });
        iconLoader.execute(level - 1, sublevel);


    }

    @Override
    public void loadLevels(BoardModel currentBoard) {
        mLevel = 0;
        mSubLevel = -1;
        ArrayList<JellowIcon> list = currentBoard.getIconModel().getAllIcons();
        list.addAll(SelectionManager.getInstance().getList());
        mView.onLevelLoaded(list);
    }

    @Override
    public void loadSubLevels() {
        mView.onSublevelLoaded(getLevels());
    }

    @Override
    public int getLevel() {
        return mLevel;
    }

    @Override
    public void addListToBoard(BoardModel currentBoard, ArrayList<JellowIcon> list) {

        BoardDatabase boardDatabase = new BoardDatabase(appDatabase);
        //Sort the list according to the verbiage id
        Collections.sort(list);
        //Fetch the current list
        BoardIconModel currentBoardModel = currentBoard.getIconModel();
        //if the board model is null instantiate it
        if(currentBoardModel==null)
            currentBoardModel = new BoardIconModel(new JellowIcon("","",-1,-1,-1));
        //Append current list into the previous list
        currentBoardModel.addAllChild(list);
        //Update the Icon model
        currentBoard.setIconModel(currentBoardModel);
        //Save new board into database
        boardDatabase.updateBoardIntoDatabase(currentBoard);
        //Delete the current list instance in selection manager
        SelectionManager.getInstance().delete();
        //Notify the view about the event
        mView.onBoardSaved();
    }

    private ArrayList<LevelParent> getLevels() {
        ArrayList<LevelParent> list = new ArrayList<>();
        ArrayList<String> levelOne = new ArrayList<>();
        levelOne.add(context.getResources().getString(R.string.current_board));
        list.add(new LevelParent(levelOne.get(0), new ArrayList<LevelChild>()));
        levelOne.addAll(database.getLevelOneIconsTitles());

        for (int i = 1; i <= levelOne.size(); i++) {

            ArrayList<String> dropDownList = database.
                    getLevelTwoIconsTitles(i - 1);
            if (i == 6 || i == 9)
                list.add(new LevelParent(levelOne.get(i), new ArrayList<LevelChild>()));
            else if (dropDownList != null && dropDownList.size() > 1) {
                dropDownList.remove(0);
                list.add(new LevelParent(levelOne.get(i), getChildren(dropDownList)));
            }

        }

        return list;
    }

    private List<LevelChild> getChildren(ArrayList<String> dropDownList) {
        ArrayList<LevelChild> list = new ArrayList<>();
        for (int i = 0; i < dropDownList.size(); i++)
            list.add(new LevelChild(dropDownList.get(i)));
        return list;
    }
}
