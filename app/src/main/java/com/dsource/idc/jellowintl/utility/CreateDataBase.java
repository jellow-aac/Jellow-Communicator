package com.dsource.idc.jellowintl.utility;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.dsource.idc.jellowintl.MainActivity;
import com.dsource.idc.jellowintl.Presentor.TextDatabase;
import com.dsource.idc.jellowintl.SplashActivity;
import com.dsource.idc.jellowintl.models.AppDatabase;

public class CreateDataBase extends AsyncTask {
    private Context mContext;
    private AppDatabase mAppDatabase;
    public CreateDataBase(Context context, AppDatabase appDatabase) {
        mContext=context;
        mAppDatabase=appDatabase;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mContext.startActivity(new Intent(mContext, MainActivity.class));
        ((SplashActivity)mContext).finishAffinity();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        TextDatabase database = new TextDatabase(mContext,new SessionManager(mContext).getLanguage(), mAppDatabase);
        if(!database.checkForTableExists()){
            database.fillDatabase();
        }
        return null;
    }
}
