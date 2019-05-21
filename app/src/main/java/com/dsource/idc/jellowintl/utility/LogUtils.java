package com.dsource.idc.jellowintl.utility;

import android.util.Log;

public class LogUtils {

    private static final String TAG = "batchDown";

    public static void logFileNotFound(String fileName){
        Log.d(TAG,"Error: "+ fileName + " file not found");
    }

    public static void logFileDownloadSuccess(String fileName){
        Log.d(TAG,"File Download Success: "+fileName);
    }

    public static void logFileDownloadFailed(String fileName){
        Log.d(TAG,"File Download Failed: "+fileName);

    }

    public static void logGeneralEvents(String message){
        Log.d(TAG,message);
    }
}
