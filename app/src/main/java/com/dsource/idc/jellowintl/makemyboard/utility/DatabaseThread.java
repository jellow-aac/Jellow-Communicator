package com.dsource.idc.jellowintl.makemyboard.utility;


import android.content.Context;
import android.os.AsyncTask;

import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;
public class DatabaseThread extends AsyncTask<Integer,Context,ArrayList<JellowIcon>> {
    String p1,p2,p3;

    private Context context;
    public void setContext(Context context)
    {
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<JellowIcon> list) {
        super.onPostExecute(list);
    }

    @Override
    protected ArrayList<JellowIcon> doInBackground(Integer... integers) {
        IconDatabase database = new IconDatabase(context);
        return null;
    }
}
