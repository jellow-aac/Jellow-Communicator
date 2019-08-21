package com.dsource.idc.jellowintl.makemyboard.databases.transactors;

import android.content.Context;
import android.os.AsyncTask;

import com.dsource.idc.jellowintl.makemyboard.databases.IconDatabase;
import com.dsource.idc.jellowintl.makemyboard.interfaces.DatabaseInterface;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public class DatabaseThread extends AsyncTask<Integer, Context, ArrayList<JellowIcon>> {
    DatabaseInterface listener;
    private IconDatabase database;

    public DatabaseThread(IconDatabase database) {
        this.database = database;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(ArrayList<JellowIcon> list) {
        super.onPostExecute(list);
        if(listener!=null)
            listener.getIconsForQuery(list);
    }

    public void setOnTransactionCompleteListener(DatabaseInterface databaseInterface){
        this.listener = databaseInterface;

    }

    @Override
    protected ArrayList<JellowIcon> doInBackground(Integer... integers) {
        if(database!=null)
        return database.myBoardQuery(integers[0],integers[1]);
        else return new ArrayList<>();
    }
}
