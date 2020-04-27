package com.dsource.idc.jellowintl.makemyboard.iModels;

import android.content.Context;

import androidx.annotation.NonNull;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.iPresenter.IBoardListPresenter;
import com.dsource.idc.jellowintl.makemyboard.iView.IBoardListView;
import com.dsource.idc.jellowintl.makemyboard.interfaces.IDataCallback;
import com.dsource.idc.jellowintl.makemyboard.managers.BoardLanguageManager;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.utility.ImageStorageHelper;
import com.dsource.idc.jellowintl.makemyboard.utility.MyPair;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;

public class BoardListModel extends BaseModel<IBoardListView> implements IBoardListPresenter {

    private BoardDatabase database;
    private AppDatabase appDatabase;

    public BoardListModel(@NonNull AppDatabase database){
        this.appDatabase = database;
        this.database = new BoardDatabase(database);
    }

    @Override
    public void loadBoards(String language) {
        if(language.equals("All"))
            database.getAllBoards(new IDataCallback<ArrayList<BoardModel>>() {
                @Override
                public void onSuccess(ArrayList<BoardModel> list) {
                    mView.boardLoaded(list);
                }

                @Override
                public void onFailure(String msg) {

                }
            });

        else database.getAllBoards(language,new IDataCallback<ArrayList<BoardModel>>() {
            @Override
            public void onSuccess(ArrayList<BoardModel> object) {
                mView.boardLoaded(object);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public void deleteBoard(Context context,BoardModel board) {
        database.deleteBoard(board);
        ImageStorageHelper.deleteAllCustomImage(context,board.getIconModel());
    }

    @Override
    public void openBoard(Context context, BoardModel boardId) {
        new BoardLanguageManager(boardId,context,appDatabase).checkLanguageAvailabilityInBoard();
    }

    @Override
    public void loadLanguageVsBoardCount(Context context) {

        final ArrayList<MyPair<String,Integer>> list = new ArrayList<>();


        list.add(new MyPair<>(context.getResources().getString(R.string.all_boards),0));


        ArrayList<String> mLanguageList = new ArrayList<>(Arrays.asList(LanguageFactory.getAvailableLanguages()));

        for(String lang: SessionManager.NoTTSLang)
            mLanguageList.remove(SessionManager.LangValueMap.get(lang));


        for(String lang: mLanguageList)
            list.add(new MyPair<>(SessionManager.LangMap.get(lang),0));

        database.getAllBoards(new IDataCallback<ArrayList<BoardModel>>() {
            @Override
            public void onSuccess(ArrayList<BoardModel> object) {
                list.get(0).setSecond(object.size());
            }

            @Override
            public void onFailure(String msg) {

            }
        });

        for(int i=1;i<list.size();i++){
            final int finalI = i;
            database.getAllBoards(list.get(i).getFirst(), new IDataCallback<ArrayList<BoardModel>>() {
                @Override
                public void onSuccess(ArrayList<BoardModel> object) {
                   list.get(finalI).setSecond(object.size());
                }

                @Override
                public void onFailure(String msg) {

                }
            });
        }

        mView.languageVsBoardCountLoaded(list);
    }

}
