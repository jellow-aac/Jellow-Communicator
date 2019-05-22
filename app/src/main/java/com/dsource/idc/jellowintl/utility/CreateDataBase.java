package com.dsource.idc.jellowintl.utility;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.dsource.idc.jellowintl.MainActivity;
import com.dsource.idc.jellowintl.SplashActivity;

public class CreateDataBase extends AsyncTask {
    Context mContext;
    public CreateDataBase(Context context) {
        mContext=context;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mContext.startActivity(new Intent(mContext, MainActivity.class));
        ((SplashActivity)mContext).finishAffinity();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        final IconDataBaseHelper iconDatabase=new IconDataBaseHelper(mContext);
        if(new SessionManager(mContext).isLanguageChanged()==0)//First Time
        {
            iconDatabase.createTable(new DataBaseHelper(mContext).getWritableDatabase());
        }
        else if(new SessionManager(mContext).isLanguageChanged()==1)//Language Change
        {
            iconDatabase.dropTable(new DataBaseHelper(mContext).getWritableDatabase());
            iconDatabase.createTable(new DataBaseHelper(mContext).getWritableDatabase());
        }

        return null;
    }
}
