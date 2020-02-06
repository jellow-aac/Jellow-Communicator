package com.dsource.idc.jellowintl.packageUpdate;

import android.content.Context;

import java.io.File;

import static com.dsource.idc.jellowintl.packageUpdate.FileUtils.getHmapFile;
import static com.dsource.idc.jellowintl.packageUpdate.FileUtils.getUpdateFile;

public class UpdateFileFactory {

    private static final String FILE_ICONS_SHA256MAP_JSON = "hmap_icons.json";
    private static final String FILE_VERBIAGE_SHA256MAP_JSON = "hmap_verbiage.json";

    public static File getIconsSHA256MapJSON(Context context){
        return getUpdateFile(context,FILE_ICONS_SHA256MAP_JSON);
    }

    public static File getVerbiageMapJSON(Context context){
        return getUpdateFile(context,FILE_VERBIAGE_SHA256MAP_JSON);
    }

    public static File getOldVerbiageMapJSON(Context context){
        return getHmapFile(context,FILE_VERBIAGE_SHA256MAP_JSON);
    }

    public static File getOldIconsSHA256MapJSON(Context context){
        return getHmapFile(context,FILE_ICONS_SHA256MAP_JSON);
    }

    public static File getVerbiageSHA256MapJSON(Context context){
        return getUpdateFile(context,FILE_VERBIAGE_SHA256MAP_JSON);
    }

    public static File getOldVerbiageSHA256MapJSON(Context context){
        return getHmapFile(context,FILE_VERBIAGE_SHA256MAP_JSON);
    }

}
