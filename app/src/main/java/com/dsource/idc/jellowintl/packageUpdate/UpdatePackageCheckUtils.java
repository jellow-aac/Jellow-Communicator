package com.dsource.idc.jellowintl.packageUpdate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.dsource.idc.jellowintl.LanguagePackUpdateActivity;
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
            showUpdateDialog(context);
            return;
        }

        HashMap<String, String> newVersionCode = getHashMap(fVersionCodeJSON);
        HashMap<String, String> OldVersionCode = getHashMap(fOldVersionCodeJSON);
        if (!newVersionCode.get(KEY_VERSION_CODE).equals(OldVersionCode.get(KEY_VERSION_CODE))){
            showUpdateDialog(context);
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


    private void showUpdateDialog(final Context context){
        String updateNow = context.getString(R.string.update_now);
        String updateLater = context.getString(R.string.update_later);
        String message = context.getString(R.string.update_message);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Add the buttons
        builder
            .setPositiveButton(updateNow, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    context.startActivity(new Intent(context, LanguagePackUpdateActivity.class));
                    dialog.dismiss();
                }
            })
            .setNegativeButton(updateLater, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            })
            // Set other dialog properties
            .setCancelable(true)
            .setMessage(message);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        // Show the AlertDialog
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(context.getResources().getColor(R.color.colorAccent));
        positiveButton.setTextSize(18f);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(context.getResources().getColor(R.color.colorAccent));
        negativeButton.setTextSize(18f);
    }
}