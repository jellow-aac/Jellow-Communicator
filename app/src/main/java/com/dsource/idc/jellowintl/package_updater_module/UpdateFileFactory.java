package com.dsource.idc.jellowintl.package_updater_module;

import android.content.Context;

import java.io.File;

import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.getHmapFile;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.getUpdateFile;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.getVersionCodeFile;

public class UpdateFileFactory {

    private static final String FILE_ICONS_SHA256MAP_JSON = "hmap_icons.json";
    private static final String FILE_VERBIAGE_SHA256MAP_JSON = "hmap_verbiage.json";
    private static final String FILE_VERSION_CODE_JSON = "package_version.json";

    public static File getVersionCodeMapJSON(Context context){
        return getUpdateFile(context,FILE_VERSION_CODE_JSON);
    }

    public static File getOldVersionCodeMapJSON(Context context){
        return getVersionCodeFile(context,FILE_VERSION_CODE_JSON);
    }

    public static File getIconsSHA256MapJSON(Context context){
        return getUpdateFile(context,FILE_ICONS_SHA256MAP_JSON);
    }

    public static File getOldIconsSHA256MapJSON(Context context){
        return getHmapFile(context,FILE_ICONS_SHA256MAP_JSON);
    }

    public static File getVerbiageMapJSON(Context context){
        return getUpdateFile(context,FILE_VERBIAGE_SHA256MAP_JSON);
    }

    public static File getOldVerbiageMapJSON(Context context){
        return getHmapFile(context,FILE_VERBIAGE_SHA256MAP_JSON);
    }

    public static File getVerbiageSHA256MapJSON(Context context){
        return getUpdateFile(context,FILE_VERBIAGE_SHA256MAP_JSON);
    }

    public static File getOldVerbiageSHA256MapJSON(Context context){
        return getHmapFile(context,FILE_VERBIAGE_SHA256MAP_JSON);
    }

}
