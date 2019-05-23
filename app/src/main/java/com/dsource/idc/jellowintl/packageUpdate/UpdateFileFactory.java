package com.dsource.idc.jellowintl.packageUpdate;

import android.content.Context;

import java.io.File;

import static com.dsource.idc.jellowintl.packageUpdate.FileUtils.getFile;
import static com.dsource.idc.jellowintl.packageUpdate.FileUtils.getUpdateFile;

public class UpdateFileFactory {

    private static final String FILE_SHA256MAP_JSON = "hmap.json";
    private static final String FILE_VERBIAGEMAP_JSON = "map.json";
    private static final String FILE_NEW_SHA256MAP_JSON = "hmapN.json";

    public static File getSHA256MapJSON(Context context){
        return getUpdateFile(context,FILE_SHA256MAP_JSON);
    }

    public static File getVerbiageMapJSON(Context context){
        return getUpdateFile(context,FILE_VERBIAGEMAP_JSON);
    }

    public static File getOldVerbiageMapJSON(Context context){
        return getFile(context,FILE_VERBIAGEMAP_JSON);
    }

    public static File getOldSHA256MapJSON(Context context){
        return getFile(context,FILE_SHA256MAP_JSON);
    }

    public static File getNewSHA256MapJSON(Context context){
        return getFile(context,FILE_NEW_SHA256MAP_JSON);
    }

}
