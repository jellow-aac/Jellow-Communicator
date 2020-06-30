package com.dsource.idc.jellowintl.package_updater_module;

import com.google.firebase.storage.StorageReference;

import static com.dsource.idc.jellowintl.package_updater_module.FirebaseUtils.getBaseUpdateStorageRef;
import static com.dsource.idc.jellowintl.package_updater_module.LogUtils.logGeneralEvents;

public class UpdateRefFactory {
    private static final String FB_ICONS_SHA256MAP_JSON = "hmap_icons.json";
    private static final String FB_VERBIAGE_SHA256MAP_JSON = "hmap_verbiage.json";
    private static final String FB_VERSION_CODE_JSON = "package_version.json";
    private static final String FB_DRAWABLES_DIR = "drawables";
    private static final String FB_VERBIAGE_DIR = "verbiage";

    public static StorageReference getVersionCodeJSONRef(){
        return getBaseUpdateStorageRef().child(FB_VERSION_CODE_JSON);
    }

    public static StorageReference getIconsSHA256MapJSONRef(){
        return getBaseUpdateStorageRef().child(FB_ICONS_SHA256MAP_JSON);
    }

    public static StorageReference getDrawablesUpdateStorageRef(){
        StorageReference drawableRef = getBaseUpdateStorageRef().child(FB_DRAWABLES_DIR);
        logGeneralEvents(drawableRef.getPath());
        return drawableRef;
    }

    public static StorageReference getVerbiageUpdateStorageRef(){
        StorageReference drawableRef = getBaseUpdateStorageRef().child(FB_VERBIAGE_DIR);
        logGeneralEvents(drawableRef.getPath());
        return drawableRef;
    }

    public static StorageReference getVerbiageSHA256MapJSONRef(){
        return getBaseUpdateStorageRef().child(FB_VERBIAGE_SHA256MAP_JSON);
    }
}
