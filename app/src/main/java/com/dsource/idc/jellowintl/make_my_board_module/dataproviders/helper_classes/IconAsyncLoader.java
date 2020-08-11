package com.dsource.idc.jellowintl.make_my_board_module.dataproviders.helper_classes;

import android.os.AsyncTask;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.IconDatabaseFacade;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.IDataCallback;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public class IconAsyncLoader extends AsyncTask<Integer, Void, ArrayList<JellowIcon>> {

    private final IconDatabaseFacade database;
    private IDataCallback<ArrayList<JellowIcon>> callback;

    public IconAsyncLoader(IconDatabaseFacade database, IDataCallback<ArrayList<JellowIcon>> callback){
        this.database =database;
        this.callback =callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<JellowIcon> jellowIcons) {
        super.onPostExecute(jellowIcons);
        if(callback!=null)
            callback.onSuccess(jellowIcons);
    }

    @Override
    protected ArrayList<JellowIcon> doInBackground(Integer... integers) {
        ArrayList<JellowIcon> icons =new ArrayList<>();
        try {
            if (integers.length >=2) {
                int p1 = integers[0];
                int p2 = integers[1];
                icons = database.myBoardQuery(p1, p2);
            }
            else throw new Exception("Less parameters passed");
        }catch (Exception e){
            e.printStackTrace();
            if(callback!=null)
                callback.onFailure(e.getMessage());
        }
        return icons;
    }

    @Override
    protected void onCancelled(ArrayList<JellowIcon> jellowIcons) {
        super.onCancelled(jellowIcons);
    }
}
