package com.dsource.idc.jellowintl.make_my_board_module.dataproviders.helper_classes;

import android.os.AsyncTask;

import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.SuccessCallBack;

public class GeneralDatabaseCreator<T> extends AsyncTask<Void,Integer,Void> implements TextDatabase.ProgressUpdates {

    private T database;
    private SuccessCallBack callBack;
    public GeneralDatabaseCreator(T database, SuccessCallBack callBack){
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
            ((TextDatabase)database).fillDatabase(this);
        }
        return null;
    }

    @Override
    public void setMaxProgress(int progressSize) {
        callBack.setProgressSize(progressSize);
    }

    @Override
    public void setCurrentProgress(int progress) {
        callBack.updateProgress(progress);
    }
}
