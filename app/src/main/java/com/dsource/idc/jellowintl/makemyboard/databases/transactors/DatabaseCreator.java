package com.dsource.idc.jellowintl.makemyboard.databases.transactors;

import android.os.AsyncTask;

import com.dsource.idc.jellowintl.makemyboard.databases.TextDatabase;
import com.dsource.idc.jellowintl.makemyboard.interfaces.SuccessCallBack;

public class DatabaseCreator<T> extends AsyncTask<Void,Integer,Void> {

    private T database;
    private SuccessCallBack callBack;
    public DatabaseCreator(T database, SuccessCallBack callBack){
        this.database =database;
        this.callBack =callBack;
    }

    @Override
    protected void onPostExecute(Void o) {
        callBack.onSuccess(null);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(database instanceof TextDatabase)
        {
            ((TextDatabase)database).fillDatabase();
        }
        return null;
    }
}
