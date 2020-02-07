package com.dsource.idc.jellowintl.packageUpdate;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.dsource.idc.jellowintl.LanguageSelectActivity;
import com.dsource.idc.jellowintl.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;

import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logFileDownloadFailed;
import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logFileDownloadSuccess;
import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logGeneralEvents;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateFileFactory.getOldVersionCodeMapJSON;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateFileFactory.getVersionCodeMapJSON;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateRefFactory.getVersionCodeJSONRef;

public class UpdatePackageCheckUtils {
    private static final String KEY_VERSION_CODE = "versionCode";

    public void checkLanguagePackageUpdateAvailable(final Context context){
        if(!ConnectionUtils.isConnected(context))
            return ;
        StorageReference refVersionCodeJSON = getVersionCodeJSONRef();
        File fVersionCodeJSON = getVersionCodeMapJSON(context);
        FileDownloadTask downloadVersionCodeMapJSON = refVersionCodeJSON.getFile(fVersionCodeJSON);
        downloadVersionCodeMapJSON.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    logFileDownloadSuccess("Version Code HashMap JSON");
                } else {
                    logFileDownloadFailed("Version Code HashMap JSON");
                }
                compareVersionCode(context);
            }
        });
    }

    private void compareVersionCode(Context context) {
        LanguageSelectActivity activity;
        if(context instanceof LanguageSelectActivity) {
            activity = (LanguageSelectActivity) context;
        }else{
            return;
        }
        File fVersionCodeJSON = getVersionCodeMapJSON(context);
        File fOldVersionCodeJSON = getOldVersionCodeMapJSON(context);
        if (!fOldVersionCodeJSON.exists()){
            activity.getScreenMenu().getItem(10).
                setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_launcher));
            return;
        }

        HashMap<String, String> newVersionCode = getHashMap(fVersionCodeJSON);
        HashMap<String, String> OldVersionCode = getHashMap(fOldVersionCodeJSON);
        if (!newVersionCode.get(KEY_VERSION_CODE).equals(OldVersionCode.get(KEY_VERSION_CODE))){
            activity.getScreenMenu().getItem(10).
                    setIcon(ContextCompat.getDrawable(activity, R.drawable.ic_launcher));
        }
    }

    private HashMap<String,String> getHashMap(File fMapJSON){

        FileReader hmapJSONReader = null;
        HashMap<String,String> hashMap = null;

        try {
            hmapJSONReader = new FileReader(fMapJSON);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        Type typeOfHashMap = new TypeToken<HashMap<String,String>>(){}.getType();
        Gson gson = new Gson();
        if(hmapJSONReader != null){
            try {
                hashMap = gson.fromJson(hmapJSONReader,typeOfHashMap);

            } catch (Exception e){
                e.printStackTrace();
                logGeneralEvents("Error generating Hash Map from JSON file");
            }
        }

        return hashMap;
    }
}