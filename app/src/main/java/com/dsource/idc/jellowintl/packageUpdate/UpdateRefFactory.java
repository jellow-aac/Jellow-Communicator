package com.dsource.idc.jellowintl.packageUpdate;

import android.content.Context;

import com.google.firebase.storage.StorageReference;

import static com.dsource.idc.jellowintl.packageUpdate.FirebaseUtils.getBaseUpdateStorageRef;
import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logGeneralEvents;

public class UpdateRefFactory {
    private static final String FB_SHA256MAP_JSON = "hmap.json";
    private static final String FB_VERBIAGEMAP_JSON = "map.json";
    private static final String FB_DRAWABLES_DIR = "drawables";

    public static StorageReference getSHA256MapJSONRef(Context context){
        return getBaseUpdateStorageRef(context).child(FB_SHA256MAP_JSON);
    }

    public static StorageReference getVerbiageMapJSONRef(Context context){
        return getBaseUpdateStorageRef(context).child(FB_VERBIAGEMAP_JSON);

    }

    public static StorageReference getDrawablesUpdateStorageRef(Context context){
        StorageReference drawableRef = getBaseUpdateStorageRef(context).child(FB_DRAWABLES_DIR);
        logGeneralEvents(drawableRef.getPath());
        return drawableRef;
    }
}
