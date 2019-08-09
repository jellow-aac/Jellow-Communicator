package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;

import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;

public class PackageRemoverAsync extends AsyncTask<Context, Void, Void> {

    @Override
    protected Void doInBackground(Context... contexts) {
        try {
            for (String language : LangMap.values()) {
                File file = contexts[0].getDir(language, Context.MODE_PRIVATE);
                if (file != null && file.exists() && file.isDirectory()) {
                    deleteRecursive(file);
                }
                file.delete();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    private void deleteRecursive(File fileObj) {
        if (fileObj.isDirectory())
            for (File child : fileObj.listFiles())
                deleteRecursive(child);
        fileObj.delete();
    }
}
