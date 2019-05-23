package com.dsource.idc.jellowintl.packageUpdate;

import android.content.Context;

import androidx.annotation.NonNull;

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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static com.dsource.idc.jellowintl.packageUpdate.UpdateFileFactory.getNewSHA256MapJSON;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateFileFactory.getOldSHA256MapJSON;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateFileFactory.getOldVerbiageMapJSON;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateFileFactory.getSHA256MapJSON;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateFileFactory.getVerbiageMapJSON;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateRefFactory.getDrawablesUpdateStorageRef;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateRefFactory.getSHA256MapJSONRef;
import static com.dsource.idc.jellowintl.packageUpdate.UpdateRefFactory.getVerbiageMapJSONRef;
import static com.dsource.idc.jellowintl.packageUpdate.FileUtils.deleteDir;
import static com.dsource.idc.jellowintl.packageUpdate.FileUtils.doesExist;
import static com.dsource.idc.jellowintl.packageUpdate.FileUtils.getUpdateDir;
import static com.dsource.idc.jellowintl.packageUpdate.FileUtils.renameFile;
import static com.dsource.idc.jellowintl.packageUpdate.FileUtils.writeToFile;
import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logFileDownloadFailed;
import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logFileDownloadSuccess;
import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logFileNotFound;
import static com.dsource.idc.jellowintl.packageUpdate.LogUtils.logGeneralEvents;


public class UpdateManager implements UpdateContract {

    private static final boolean SUCCESS = true;
    private static final boolean FAILED = false;

    private Queue<String> iconDownloadQueue;
    private Queue<String> failedDownloadsQueue;
    private HashMap<String, String> mapIconSHA256;
    private HashMap<String, String> mapOldIconSHA256;
    private int iconDownloadQueueSize;
    private Set<String> downloadedIcons;
    private static final int FAILED_DOWNLOADS_RETRY_LIMIT = 2;
    private Context context;
    private ProgressReceiver progressReceiver;
    private int failedDownloadsRetryCount;

    public UpdateManager(Context context,ProgressReceiver progressReceiver) {
        this.iconDownloadQueue = new LinkedList<>();
        this.failedDownloadsQueue = new LinkedList<>();
        this.mapIconSHA256 = new HashMap<>();
        this.mapOldIconSHA256 = new HashMap<>();
        this.downloadedIcons = new HashSet<>();
        this.progressReceiver = progressReceiver;
        this.context = context;
    }

    public void startDownload(){
        updateStatusText("Starting Update...");
        downloadStage1();
    }

    private void downloadStage1(){
        StorageReference refSHA256MapJSON = getSHA256MapJSONRef(context);
        File fSHA256MapJSON = getSHA256MapJSON(context);
        FileDownloadTask downloadSHA256MapJSON = downloadSHA256MapJSON(fSHA256MapJSON,refSHA256MapJSON);
        downloadSHA256MapJSON.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    logFileDownloadSuccess("New HashMap JSON");
                    updateStatusText("Parsing Download data...");
                    onStageDownloadResult(UpdateTaskStage.STAGE_1,SUCCESS);
                } else {
                    logFileDownloadFailed("New HashMap JSON");
                    onStageDownloadResult(UpdateTaskStage.STAGE_1,FAILED);
                }
            }
        });
    }

    private boolean validateStage1(){

        File fSHA256MapJSON = getSHA256MapJSON(context);

        if(!doesExist(fSHA256MapJSON)){
            logFileNotFound("New SHA256Map JSON");
            return false;
        }
        return true;
    }

    private void downloadStage2(){

        File fSHA256MapJSON = getSHA256MapJSON(context);
        File fOldSHA256MapJSON = getOldSHA256MapJSON(context);

        boolean stage1Success = validateStage1();

        if(!stage1Success){
            onStageDownloadResult(UpdateTaskStage.STAGE_2,FAILED);
            return;
        }

        if(!doesExist(fOldSHA256MapJSON)){
            logFileNotFound("Old SHA256Map JSON");
            onStageDownloadResult(UpdateTaskStage.STAGE_2,FAILED);
            return;
        }

        generateHashMaps(fSHA256MapJSON,fOldSHA256MapJSON);

        int downloadQueueSize = generateIconDownloadQueue();

        if(downloadQueueSize == 0){
            onUpdateTaskResult(UpdateTaskResult.NO_UPDATES_FOUND);
        } else {
            logGeneralEvents("Download Queue Generated size: "+downloadQueueSize);
            onStageDownloadResult(UpdateTaskStage.STAGE_2,SUCCESS);
        }

    }

    private void downloadStage3(){
        File fUpdateDir = getUpdateDir(context);
        StorageReference drawablesRef = getDrawablesUpdateStorageRef(context);
        updateStatusText("Downloading Icons...");
        downloadIconFiles(fUpdateDir,drawablesRef);
        logGeneralEvents("Downloading Icon Files");
    }

    private void downloadStage4() {
        updateStatusText("Updating Icon Data...");
        StorageReference refVerbiageMapJSON = getVerbiageMapJSONRef(context);
        File fVerbiageMapJSON = getVerbiageMapJSON(context);
        FileDownloadTask downloadVerbiageMapJSON= downloadVerbiageMapJSON(fVerbiageMapJSON,refVerbiageMapJSON);
        downloadVerbiageMapJSON.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    logFileDownloadSuccess("New VerbiageMap JSON");
                    onStageDownloadResult(UpdateTaskStage.STAGE_4,SUCCESS);
                } else {
                    logFileDownloadFailed("New VerbiageMap JSON");
                    onStageDownloadResult(UpdateTaskStage.STAGE_4,FAILED);
                }
            }
        });
    }

    private boolean validateStage4(){
        File fVerbiageMapJSON = getVerbiageMapJSON(context);
        if(!doesExist(fVerbiageMapJSON)){
            logFileNotFound("New VerbiageMap JSON");
            return false;
        }
        return true;
    }

    private void downloadStage5() {

        File fVerbiageMapJSON = getVerbiageMapJSON(context);
        File fOldVerbiageMapJSON = getOldVerbiageMapJSON(context);
        File fOldSHA256MapJSON = getOldSHA256MapJSON(context);
        File fNewSHA256MapJSON = getNewSHA256MapJSON(context);

        boolean stage4Success = validateStage4();

        if(!stage4Success){
            onStageDownloadResult(UpdateTaskStage.STAGE_5,FAILED);
            return;
        }

        if(!doesExist(fOldVerbiageMapJSON)){
            logFileNotFound("Old VerbiageMap JSON");
            onStageDownloadResult(UpdateTaskStage.STAGE_5,FAILED);
            return;
        }

        boolean verbiageMapUpdated = updateVerbiageMapJSONFile(fVerbiageMapJSON,fOldVerbiageMapJSON);

        if(verbiageMapUpdated){
            logGeneralEvents("Verbiage File Update Success");
        } else {
            logGeneralEvents("Verbiage File Update Failed");
            onStageDownloadResult(UpdateTaskStage.STAGE_5,FAILED);
            return;
        }

        boolean sha256MapUpdated = updateSHA256MapJSONFile(fNewSHA256MapJSON,fOldSHA256MapJSON);

        if(sha256MapUpdated){
            logGeneralEvents("SHA256Map File Update Success");
        } else {
            logGeneralEvents("SHA256Map File Update Failed");
            onStageDownloadResult(UpdateTaskStage.STAGE_5,FAILED);
            return;
        }

        onStageDownloadResult(UpdateTaskStage.STAGE_5,SUCCESS);
    }


    @Override
    public FileDownloadTask downloadSHA256MapJSON(File fSHA256MapJSON, StorageReference refSHA256MapJSON) {
        return refSHA256MapJSON.getFile(fSHA256MapJSON);
    }

    private void generateHashMaps(File fSHA256MapJSON,File fOldSHA256MapJSON){
        this.mapIconSHA256 = getHashMap(fSHA256MapJSON);
        this.mapOldIconSHA256 = getHashMap(fOldSHA256MapJSON);
    }

    @Override
    public FileDownloadTask downloadVerbiageMapJSON(File fVerbiageMapJSON, StorageReference refVerbiageMapJSON) {
        return refVerbiageMapJSON.getFile(fVerbiageMapJSON);
    }

    @Override
    public int generateIconDownloadQueue() {
        Set<String> iconNames = mapIconSHA256.keySet();
        for(String iconName:iconNames){
            if(iconName != null){
                if(mapOldIconSHA256.containsKey(iconName)){
                    if(!mapIconSHA256.get(iconName).equals(mapOldIconSHA256.get(iconName))){
                        iconDownloadQueue.add(iconName);
                    }
                } else {
                    iconDownloadQueue.add(iconName);
                }
            }
        }
        iconDownloadQueueSize = iconDownloadQueue.size();

        return iconDownloadQueueSize;
    }

    @Override
    public void downloadIconFiles(final File fIconDownloadDir,final StorageReference refIconsDir) {

        while (iconDownloadQueue.peek() != null){
            final String iconName = iconDownloadQueue.poll();
            StorageReference refIconFile = refIconsDir.child(iconName);
            File fIcon = new File(fIconDownloadDir.getAbsolutePath(),iconName);
            FileDownloadTask iconFileDownloadTask = refIconFile.getFile(fIcon);
            iconFileDownloadTask.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        downloadedIcons.add(iconName);
                        progressReceiver.onDownloadProgress(downloadedIcons.size(),iconDownloadQueueSize);
                    } else {
                        failedDownloadsQueue.add(iconName);
                    }

                    checkIconsDownloadingTaskCompleted(fIconDownloadDir,refIconsDir);
                }
            });
        }
    }

    @Override
    public void retryFailedDownloads(final File fIconDownloadDir, final StorageReference refIconsDir) {
        while (failedDownloadsQueue.peek() != null){
            final String iconName = failedDownloadsQueue.poll();
            StorageReference refIconFile = refIconsDir.child(iconName);
            File fIcon = new File(fIconDownloadDir.getAbsolutePath(),iconName);
            FileDownloadTask iconFileDownloadTask = refIconFile.getFile(fIcon);
            iconFileDownloadTask.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        downloadedIcons.add(iconName);
                        progressReceiver.onDownloadProgress(downloadedIcons.size(),iconDownloadQueueSize);
                    } else {
                        failedDownloadsQueue.add(iconName);
                    }

                    checkIconsDownloadingTaskCompleted(fIconDownloadDir,refIconsDir);
                }
            });
        }
    }

    @Override
    public boolean updateSHA256MapJSONFile(File fNewSHA256MapJSON, File fOldSHA256MapJSON) {

        Set<String> iconNamesN = mapIconSHA256.keySet();
        for(String iconName:iconNamesN){
            if(mapOldIconSHA256.containsKey(iconName)){
                if(!mapIconSHA256.get(iconName).equals(mapOldIconSHA256.get(iconName))){
                    if(downloadedIcons.contains(iconName)){
                        mapOldIconSHA256.put(iconName,mapIconSHA256.get(iconName));
                    }
                }
            } else {
                if(downloadedIcons.contains(iconName)){
                    mapOldIconSHA256.put(iconName,mapIconSHA256.get(iconName));
                }
            }
        }

        Gson gson = new Gson();
        String mapIconSHA256JSONString = gson.toJson(mapOldIconSHA256);

        boolean writeSuccess = writeToFile(fNewSHA256MapJSON,mapIconSHA256JSONString);

        boolean renameSuccess = false;

        if(writeSuccess && doesExist(fNewSHA256MapJSON)){
            renameSuccess = renameFile(fNewSHA256MapJSON,fOldSHA256MapJSON);
        }

        return writeSuccess && renameSuccess;
    }

    @Override
    public boolean cleanUpdateFiles() {
        File updateDir = getUpdateDir(context);

        boolean updateFilesCleaned = deleteDir(updateDir);

        if(updateFilesCleaned){
            logGeneralEvents("Success: Cleaned update cache");
        } else {
            logGeneralEvents("Failed: Unable to clean update cache");
        }

        return updateFilesCleaned;
    }

    @Override
    public boolean updateVerbiageMapJSONFile(File fVerbiageMapJSON,File fOldVerbiageMapJSON) {
        boolean renameSuccess = false;
        if(doesExist(fVerbiageMapJSON) && doesExist(fOldVerbiageMapJSON)){
            renameSuccess = renameFile(fVerbiageMapJSON,fOldVerbiageMapJSON);
        }
        return renameSuccess;
    }

    private void checkIconsDownloadingTaskCompleted(File fIconDownloadDir, StorageReference refIconsDir){
        if(downloadedIcons.size() + failedDownloadsQueue.size() == iconDownloadQueueSize){
            if(downloadedIcons.size() == iconDownloadQueueSize){
                onIconDownloadTaskCompleted(true);
            } else {
                if(failedDownloadsRetryCount <= FAILED_DOWNLOADS_RETRY_LIMIT){
                    failedDownloadsRetryCount++;
                    logGeneralEvents("Retrying Failed Downloads");
                    retryFailedDownloads(fIconDownloadDir,refIconsDir);
                } else {
                    onIconDownloadTaskCompleted(false);
                    logGeneralEvents("Failed Downloads Retry limit reached");

                }
            }
        }
    }

    private void onIconDownloadTaskCompleted(boolean success) {
        progressReceiver.onIconDownloadTaskCompleted(success);
        if(success){
            onStageDownloadResult(UpdateTaskStage.STAGE_3,SUCCESS);
        } else {
            onStageDownloadResult(UpdateTaskStage.STAGE_3,FAILED);
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

    private void updateStatusText(String message){
        progressReceiver.updateStatusText(message);
    }

    private void showUpdateInfo(String message){
        progressReceiver.showUpdateInfo(message);
    }

    private void notifyIconsModified(boolean modified){
        progressReceiver.iconsModified(modified);
    }

    private void onUpdateTaskResult(UpdateTaskResult updateTaskResult){

        cleanUpdateFiles();

        switch (updateTaskResult){
            case ICONS_SUCCESSFULLY_UPDATED:
                logGeneralEvents("Update task Successfully executed");
                updateStatusText("Language pack successfully updated!!");
                showUpdateInfo("Update Success!!");
                notifyIconsModified(true);
                break;
            case NO_UPDATES_FOUND:
                logGeneralEvents("No new/updated icons found");
                updateStatusText("No updates found!!");
                showUpdateInfo("Update Check Success!!");
                notifyIconsModified(false);
                break;
            case FAILED:
                logGeneralEvents("Update task execution failed!!");
                updateStatusText("Error completing update!! Please try again.");
                showUpdateInfo("Update Failed!!");
                notifyIconsModified(false);
                break;
        }
    }

    private void onStageDownloadResult(UpdateTaskStage stage,boolean success){
        if(success){

            logGeneralEvents("Successfully Completed: " + stage);

            switch (stage){
                case STAGE_1:
                    downloadStage2();
                    break;
                case STAGE_2:
                    downloadStage3();
                    break;
                case STAGE_3:
                    downloadStage4();
                    break;
                case STAGE_4:
                    downloadStage5();
                    break;
                case STAGE_5:
                    onUpdateTaskResult(UpdateTaskResult.ICONS_SUCCESSFULLY_UPDATED);
                    break;
            }
        } else {
            logGeneralEvents("Failed: " + stage);
            onUpdateTaskResult(UpdateTaskResult.FAILED);
        }
    }

}
