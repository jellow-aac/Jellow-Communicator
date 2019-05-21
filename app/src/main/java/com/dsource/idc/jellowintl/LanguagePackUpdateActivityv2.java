package com.dsource.idc.jellowintl;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.packageUpdate.ProgressReceiver;
import com.dsource.idc.jellowintl.packageUpdate.UpdateManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import static com.dsource.idc.jellowintl.packageUpdate.UpdateRefFactory.getDrawablesUpdateStorageRef;
import static com.dsource.idc.jellowintl.utility.FileUtils.cleanUpdateFiles;
import static com.dsource.idc.jellowintl.utility.FileUtils.doesExist;
import static com.dsource.idc.jellowintl.utility.FileUtils.getFile;
import static com.dsource.idc.jellowintl.utility.FileUtils.getUpdateDir;
import static com.dsource.idc.jellowintl.utility.FileUtils.getUpdateFile;
import static com.dsource.idc.jellowintl.utility.FirebaseUtils.getBaseUpdateStorageRef;
import static com.dsource.idc.jellowintl.utility.LogUtils.logFileDownloadFailed;
import static com.dsource.idc.jellowintl.utility.LogUtils.logFileDownloadSuccess;
import static com.dsource.idc.jellowintl.utility.LogUtils.logFileNotFound;
import static com.dsource.idc.jellowintl.utility.LogUtils.logGeneralEvents;

public class LanguagePackUpdateActivityv2 extends BaseActivity implements ProgressReceiver{




    public static final String FB_SHA256MAP_JSON = "hmap.json";
    public static final String FILE_SHA256MAP_JSON = "hmap.json";
    private static final String FILE_VERBIAGEMAP_JSON = "map.json";
    private static final String FB_VERBIAGEMAP_JSON = "map.json";
    private static final String FILE_NEW_SHA256MAP_JSON = "hmapN.json";

    UpdateManager updateManager;
    RoundCornerProgressBar progressBar;
    TextView lpuTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_pack_update_activityv2);

        updateManager = new UpdateManager(getApplicationContext(),this);
        lpuTextView = findViewById(R.id.lpu);
        progressBar = findViewById(R.id.pg);
        progressBar.setMax(1);

        updateManager.startDownload();

        //TODO: change layout files for this v2 activity with separate files for different screen sizes
        //TODO: if possible add a pause method for update
        //TODO: check for internet connection before starting a update

        /* lpuTextView.setText("Starting Downloads...");
        StorageReference refBase = getBaseUpdateStorageRef(getApplicationContext());
        StorageReference refSHA256MapJSON = refBase.child(FB_SHA256MAP_JSON);
        File fSHA256MapJSON = getUpdateFile(getApplicationContext(),FILE_SHA256MAP_JSON);
        FileDownloadTask downloadSHA256MapJSON= updateManager.downloadSHA256MapJSON(fSHA256MapJSON,refSHA256MapJSON);
        downloadSHA256MapJSON.addOnCompleteListener(this, new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    logFileDownloadSuccess("New HashMap JSON");
                    lpuTextView.setText("Parsing Download data...");
                    onSHA256MapJsonDownloaded();
                } else {
                    logFileDownloadFailed("New HashMap JSON");
                    onUpdateTaskFailed();
                }
            }
        });*/
    }

    @Override
    public void onDownloadProgress(int completedDownloads, int totalDownloads) {
            lpuTextView.setText("Downloading Icons...");
            progressBar.setProgress((float)completedDownloads/totalDownloads);
    }

    @Override
    public void onIconDownloadTaskCompleted(boolean success) {

    }

    @Override
    public void updateStatusText(String message) {
        lpuTextView.setText(message);
    }

    @Override
    public void showUpdateInfo(String message) {
        showToast(message);
    }

    /*private void onUpdateTaskFailed() {
        logGeneralEvents("Update task execution failed!!");
        lpuTextView.setText("Update Failed!! Please try again.");
        cleanUpdateFiles(getApplicationContext());
    }

    private void onUpdateTaskSuccess() {
        cleanUpdateFiles(getApplicationContext());
        lpuTextView.setText("Update Completed!");
        logGeneralEvents("Update task Successfully executed");

    }

    private void onSHA256MapJsonDownloaded() {

        File fSHA256MapJSON = getUpdateFile(getApplicationContext(),FILE_SHA256MAP_JSON);
        File fOldSHA256MapJSON = getFile(getApplicationContext(),FILE_SHA256MAP_JSON);

        if(!doesExist(fSHA256MapJSON)){
            logFileNotFound("New SHA256Map JSON");
            return;
        }

        if(!doesExist(fOldSHA256MapJSON)){
            logFileNotFound("Old SHA256Map JSON");
            return;
        }

        updateManager.generateHashMaps(fSHA256MapJSON,fOldSHA256MapJSON);

        int downloadQueueSize = updateManager.generateIconDownloadQueue();

        if(downloadQueueSize == 0){
            logGeneralEvents("No new/updated icons found");
            showToast("No Updates Found");
            onUpdateTaskSuccess();
        } else {
            logGeneralEvents("Download Queue Generated size: "+downloadQueueSize);
            downloadIconFiles();
        }

    }

    private void downloadIconFiles() {
        File fUpdateDir = getUpdateDir(getApplicationContext());
        StorageReference drawablesRef = getDrawablesUpdateStorageRef(getApplicationContext());
        updateManager.downloadIconFiles(fUpdateDir,drawablesRef);
        logGeneralEvents("Downloading Icon Files");
    }


    private void downloadVerbiageMapJSON(){
        lpuTextView.setText("Updating Icon Data...");
        StorageReference refBase = getBaseUpdateStorageRef(getApplicationContext());
        StorageReference refVerbiageMapJSON = refBase.child(FB_VERBIAGEMAP_JSON);
        File fVerbiageMapJSON = getUpdateFile(getApplicationContext(),FILE_VERBIAGEMAP_JSON);
        FileDownloadTask downloadVerbiageMapJSON= updateManager.downloadVerbiageMapJSON(fVerbiageMapJSON,refVerbiageMapJSON);
        downloadVerbiageMapJSON.addOnCompleteListener(this, new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    logFileDownloadSuccess("New VerbiageMap JSON");
                    onVerbiageMapJsonDownloaded();
                } else {
                    logFileDownloadFailed("New VerbiageMap JSON");
                    onUpdateTaskFailed();
                }
            }
        });
    }

    private void onVerbiageMapJsonDownloaded(){

        File fVerbiageMapJSON = getUpdateFile(getApplicationContext(),FILE_VERBIAGEMAP_JSON);
        File fOldVerbiageMapJSON = getFile(getApplicationContext(),FILE_VERBIAGEMAP_JSON);

        if(!doesExist(fVerbiageMapJSON)){
            logFileNotFound("New VerbiageMap JSON");
            return;
        }

        if(!doesExist(fOldVerbiageMapJSON)){
            logFileNotFound("Old VerbiageMap JSON");
            return;
        }

        boolean success = updateManager.updateVerbiageMapJSONFile(fVerbiageMapJSON,fOldVerbiageMapJSON);

        if(success){
            logGeneralEvents("Verbiage File Update Success");
            onVerbiageMapJSONUpdated();
        } else {
            logGeneralEvents("Verbiage File Update Failed");
        }
    }


    private void onVerbiageMapJSONUpdated(){

        File fOldSHA256MapJSON = getFile(getApplicationContext(),FILE_SHA256MAP_JSON);
        File fNewSHA256MapJSON = getFile(getApplicationContext(),FILE_NEW_SHA256MAP_JSON);


        boolean success = updateManager.updateSHA256MapJSONFile(fNewSHA256MapJSON,fOldSHA256MapJSON);

        if(success){
            logGeneralEvents("SHA256Map File Update Success");
            onSHA256MapJsonUpdated();
        } else {
            logGeneralEvents("SHA256Map File Update Failed");
        }


    }

    private void onSHA256MapJsonUpdated() {
        boolean success = updateManager.cleanUpdateFiles();

        if(success){
            logGeneralEvents("Update folder deletion success");
        } else {
            logGeneralEvents("Update folder deletion failed");
        }

        onUpdateTaskSuccess();
    }*/

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(LanguagePackUpdateActivityv2.class.getSimpleName());
    }


}
