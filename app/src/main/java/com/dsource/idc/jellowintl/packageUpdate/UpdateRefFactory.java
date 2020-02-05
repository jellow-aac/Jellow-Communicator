package com.dsource.idc.jellowintl.packageUpdate;

import android.content.Context;

import com.google.firebase.storage.StorageReference;

import static com.dsource.idc.jellowintl.packageUpdate.FirebaseUtils.getBaseUpdateStorageRef;
import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logGeneralEvents;

public class UpdateRefFactory {
    private static final String FB_ICONS_SHA256MAP_JSON = "hmap_icons.json";
    private static final String FB_VERBIAGE_SHA256MAP_JSON = "hmap_verbiage.json";
    private static final String FB_DRAWABLES_DIR = "drawables";
    private static final String FB_VERBIAGE_DIR = "verbiage";

    public static StorageReference getIconsSHA256MapJSONRef(Context context){
        return getBaseUpdateStorageRef(context).child(FB_ICONS_SHA256MAP_JSON);
    }

    public static StorageReference getDrawablesUpdateStorageRef(Context context){
        StorageReference drawableRef = getBaseUpdateStorageRef(context).child(FB_DRAWABLES_DIR);
        logGeneralEvents(drawableRef.getPath());
        return drawableRef;
    }

    public static StorageReference getVerbiageUpdateStorageRef(Context context){
        StorageReference drawableRef = getBaseUpdateStorageRef(context).child(FB_VERBIAGE_DIR);
        logGeneralEvents(drawableRef.getPath());
        return drawableRef;
    }

    public static StorageReference getVerbiageSHA256MapJSONRef(Context context){
        return getBaseUpdateStorageRef(context).child(FB_VERBIAGE_SHA256MAP_JSON);
    }
}
