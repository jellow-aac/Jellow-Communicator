package com.dsource.idc.jellowintl.packageUpdate;

import android.content.Context;

import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logGeneralEvents;

public class FirebaseUtils {


    public static final String BUILD_TYPE = BuildConfig.DB_TYPE;
    public static final String FB_UPDATES_DIR = "updates";


    public static StorageReference getBaseStorageRef(){
        return FirebaseStorage.getInstance().getReference();
    }

    public static StorageReference getBaseUpdateStorageRef(Context context){
        String languageCode = LanguageFactory.getCurrentLocaleCode(context);
        StorageReference updateRef = getBaseStorageRef().child(BUILD_TYPE).child(FB_UPDATES_DIR).child(languageCode);
        logGeneralEvents(updateRef.getPath());
        return updateRef;
    }


}
