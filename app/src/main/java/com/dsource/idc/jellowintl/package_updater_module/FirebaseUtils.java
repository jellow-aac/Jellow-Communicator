package com.dsource.idc.jellowintl.package_updater_module;

import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.dsource.idc.jellowintl.package_updater_module.LogUtils.logGeneralEvents;

public class FirebaseUtils {


    public static final String BUILD_TYPE = BuildConfig.DB_TYPE;
    public static final String FB_UPDATES_DIR = "updates";


    public static StorageReference getBaseStorageRef(){
        return FirebaseStorage.getInstance().getReference();
    }

    public static StorageReference getBaseUpdateStorageRef(){
        StorageReference updateRef = getBaseStorageRef().child(BUILD_TYPE).child(FB_UPDATES_DIR)
                .child(SessionManager.UNIVERSAL_PACKAGE);
        logGeneralEvents(updateRef.getPath());
        return updateRef;
    }


}
