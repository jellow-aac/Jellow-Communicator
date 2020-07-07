package com.dsource.idc.jellowintl.utility.async;


import android.content.Context;
import android.os.AsyncTask;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.utility.interfaces.CompletionCallback;

public class CreateDataBase extends AsyncTask<Object, Void, Object> {
    private CompletionCallback handler;

    public void registerReceiver(CompletionCallback handler){
        this.handler = handler;
    }

    public void unRegisterReceiver(){
        this.handler = null;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        try{
            SessionManager session =new SessionManager((Context)objects[0]);
            TextDatabase database = new TextDatabase(((Context)objects[0]),
                    session.getLanguage(), ((AppDatabase)objects[1]));
            database.dropTable();
            database.fillDatabase(null);
            handler.onTaskComplete(GlobalConstants.STATUS_SUCCESS);
        }catch(Exception e){
            handler.onTaskComplete(GlobalConstants.STATUS_FAILED);
        }
        return null;
    }
}