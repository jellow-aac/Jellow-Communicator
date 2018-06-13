package com.dsource.idc.jellowintl;


import android.content.Context;
import android.os.AsyncTask;

import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class CreateDataBase extends AsyncTask {
    Context mContext;
    public CreateDataBase(Context context) {
        mContext=context;
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
