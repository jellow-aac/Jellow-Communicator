package com.dsource.idc.jellowintl.package_updater_module;

import android.content.Context;

import androidx.annotation.NonNull;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.package_updater_module.interfaces.ProgressReceiver;
import com.dsource.idc.jellowintl.package_updater_module.interfaces.UpdateContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.deleteDir;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.doesExist;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.getBaseDir;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.getIconsDir;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.getUpdateDir;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.getUpdateIconsDir;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.getUpdateVerbiageDir;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.renameFile;
import static com.dsource.idc.jellowintl.package_updater_module.FileUtils.writeToFile;
import static com.dsource.idc.jellowintl.package_updater_module.LogUtils.logFileDownloadFailed;
import static com.dsource.idc.jellowintl.package_updater_module.LogUtils.logFileDownloadSuccess;
import static com.dsource.idc.jellowintl.package_updater_module.LogUtils.logFileNotFound;
import static com.dsource.idc.jellowintl.package_updater_module.LogUtils.logGeneralEvents;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateFileFactory.getIconsSHA256MapJSON;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateFileFactory.getOldIconsSHA256MapJSON;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateFileFactory.getOldVerbiageMapJSON;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateFileFactory.getOldVerbiageSHA256MapJSON;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateFileFactory.getOldVersionCodeMapJSON;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateFileFactory.getVerbiageMapJSON;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateFileFactory.getVerbiageSHA256MapJSON;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateFileFactory.getVersionCodeMapJSON;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateRefFactory.getDrawablesUpdateStorageRef;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateRefFactory.getIconsSHA256MapJSONRef;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateRefFactory.getVerbiageSHA256MapJSONRef;
import static com.dsource.idc.jellowintl.package_updater_module.UpdateRefFactory.getVerbiageUpdateStorageRef;


public class UpdateManager implements UpdateContract {

    private static final boolean SUCCESS = true;
    private static final boolean FAILED = false;
    private static final int FAILED_DOWNLOADS_RETRY_LIMIT = 2;

    private Queue<String> iconDownloadQueue, verbiageDownloadQueue;
    private Queue<String> failedIconDownloadsQueue, failedVerbiageDownloadsQueue;
    private HashMap<String, String> mapIconSHA256, mapVerbiageSHA256;
    private HashMap<String, String> mapOldIconSHA256, mapOldVerbiageSHA256;
    private int iconDownloadQueueSize, verbiageDownloadQueueSize;
    private Set<String> downloadedIcons, downloadedVerbiage;
    private Context context;
    private ProgressReceiver progressReceiver;
    private int failedIconsDownloadsRetryCount, failedVerbiageDownloadsRetryCount;
    private boolean isIconsUpdated = false, isVerbiageUpdated = false;

    public UpdateManager(Context context) {
        this.iconDownloadQueue = new LinkedList<>();
        this.verbiageDownloadQueue = new LinkedList<>();
        this.failedIconDownloadsQueue = new LinkedList<>();
        this.failedVerbiageDownloadsQueue = new LinkedList<>();
        this.mapIconSHA256 = new HashMap<>();
        this.mapVerbiageSHA256 = new HashMap<>();
        this.mapOldIconSHA256 = new HashMap<>();
        this.mapOldVerbiageSHA256 = new HashMap<>();
        this.downloadedIcons = new HashSet<>();
        this.downloadedVerbiage = new HashSet<>();
        this.progressReceiver = (ProgressReceiver) context;
        this.context = context;
    }

    public void startDownload(){
        updateStatusText(context.getString(R.string.lpu_start_update));
        downloadStage1();
    }

    /**Download the hMap file for icons.**/
    private void downloadStage1(){
        StorageReference refSHA256MapJSON = getIconsSHA256MapJSONRef();
        File fSHA256MapJSON = getIconsSHA256MapJSON(context);
        FileDownloadTask downloadSHA256MapJSON = downloadSHA256MapJSON(fSHA256MapJSON,refSHA256MapJSON);
        downloadSHA256MapJSON.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    logFileDownloadSuccess("New Icons HashMap JSON");
                    updateStatusText(context.getString(R.string.lpu_parse_update));
                    onStageDownloadResult(UpdateTaskStage.STAGE_1,SUCCESS);
                } else {
                    logFileDownloadFailed("New Icons HashMap JSON");
                    onStageDownloadResult(UpdateTaskStage.STAGE_1,FAILED);
                }
            }
        });
    }

    private boolean validateStage1(){

        File fSHA256MapJSON = getIconsSHA256MapJSON(context);

        if(!doesExist(fSHA256MapJSON)){
            logFileNotFound("New SHA256Map JSON");
            return false;
        }
        return true;
    }

    /**Generate icon queue size to download**/
    private void downloadStage2() {

        File fSHA256MapJSON = getIconsSHA256MapJSON(context);
        File fOldSHA256MapJSON = getOldIconsSHA256MapJSON(context);

        boolean stage1Success = validateStage1();

        if(!stage1Success){
            onStageDownloadResult(UpdateTaskStage.STAGE_2,FAILED);
            return;
        }

        generateIconsHashMaps(fSHA256MapJSON,fOldSHA256MapJSON);

        int downloadQueueSize = generateIconDownloadQueue();

        if(downloadQueueSize == 0){
            logGeneralEvents("No Icons available to Update or Download");
            /*SKIP STAGE 3 AND MOVE TO THE STAGE 4*/
            onStageDownloadResult(UpdateTaskStage.STAGE_3,SUCCESS);
        } else {
            logGeneralEvents("Icons Download Queue Generated size: "+downloadQueueSize);
            onStageDownloadResult(UpdateTaskStage.STAGE_2,SUCCESS);
        }
    }

    /**Download icon files**/
    private void downloadStage3(){
        File fUpdateDir = getUpdateIconsDir(context);
        StorageReference drawablesRef = getDrawablesUpdateStorageRef();
        updateStatusText(context.getString(R.string.lpu_download_icons));
        downloadIconFiles(fUpdateDir,drawablesRef);
        logGeneralEvents("Downloading Icon Files");
    }

    /**Download the hMap file for verbiage.**/
    private void downloadStage4(){
        StorageReference refSHA256MapJSON = getVerbiageSHA256MapJSONRef();
        File fSHA256MapJSON = getVerbiageSHA256MapJSON(context);
        FileDownloadTask downloadSHA256MapJSON = downloadSHA256MapJSON(fSHA256MapJSON,refSHA256MapJSON);
        downloadSHA256MapJSON.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    logFileDownloadSuccess("New Verbiage HashMap JSON");
                    updateStatusText(context.getString(R.string.lpu_parse_update));
                    onStageDownloadResult(UpdateTaskStage.STAGE_4,SUCCESS);
                } else {
                    logFileDownloadFailed("New Verbiage HashMap JSON");
                    onStageDownloadResult(UpdateTaskStage.STAGE_4,FAILED);
                }
            }
        });
    }

    private boolean validateStage4(){
        File fSHA256MapJSON = getVerbiageSHA256MapJSON(context);

        if(!doesExist(fSHA256MapJSON)){
            logFileNotFound("New Verbiage SHA256Map JSON");
            return false;
        }
        return true;
    }

    /**Generate files queue size to download**/
    private void downloadStage5(){
        File fSHA256MapJSON = getVerbiageSHA256MapJSON(context);
        File fOldSHA256MapJSON = getOldVerbiageSHA256MapJSON(context);

        boolean stage4Success = validateStage4();

        if(!stage4Success){
            onStageDownloadResult(UpdateTaskStage.STAGE_5,FAILED);
            return;
        }
        generateVerbiageHashMaps(fSHA256MapJSON,fOldSHA256MapJSON);

        int downloadQueueSize = generateVerbiageDownloadQueue();


        if(downloadQueueSize == 0){
            if (isIconsUpdated)
                onStageDownloadResult(UpdateTaskStage.STAGE_6,SUCCESS);
            else
                onUpdateTaskResult(UpdateTaskResult.NO_UPDATES_FOUND);
        } else {
            logGeneralEvents("Download Queue Generated size: "+downloadQueueSize);
            onStageDownloadResult(UpdateTaskStage.STAGE_5,SUCCESS);
        }
    }

    /**Download verbiage files**/
    private void downloadStage6(){
        File fUpdateDir = getUpdateVerbiageDir(context);
        StorageReference verbiageRef = getVerbiageUpdateStorageRef();
        updateStatusText(context.getString(R.string.lpu_download_verbiage));
        progressReceiver.onDownloadProgress(0,verbiageDownloadQueueSize);
        downloadVerbiageFiles(fUpdateDir,verbiageRef);
        logGeneralEvents("Downloading Verbiage Files");
    }

    private boolean validateStage6(){
        File fVerbiageMapJSON = getVerbiageMapJSON(context);
        if(!doesExist(fVerbiageMapJSON)){
            logFileNotFound("New VerbiageMap JSON");
            return false;
        }
        return true;
    }

    /**Check verbiage JSON and hMap files are correctly updated.**/
    private void downloadStage7() {

        boolean stage6Success = validateStage6();
        if(!stage6Success){
            onStageDownloadResult(UpdateTaskStage.STAGE_7,FAILED);
            return;
        }
        //ICONS FILE MOVE TO "drawables" FOLDER
        {
            if(isIconsUpdated) {
                Set<String> icons = mapIconSHA256.keySet();
                int totalFilesRenamed = 0;
                for (String icon : icons) {
                    File fIcon = new File(getIconsDir(context), icon);
                    File nFIcon = new File(getUpdateIconsDir(context), icon);
                    if (!fIcon.exists()) {
                        try {
                            fIcon.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (renameFile(nFIcon, fIcon))
                        totalFilesRenamed++;
                }
                if (totalFilesRenamed != icons.size()) {
                    onStageDownloadResult(UpdateTaskStage.STAGE_7, FAILED);
                    return;
                }
            }
        }
        //VERBIAGE FILE MOVE TO "app_universal" FOLDER
        {
            if(isVerbiageUpdated) {
                Set<String> verbiages = mapVerbiageSHA256.keySet();
                int totalFilesRenamed = 0;
                for (String verbiage : verbiages) {
                    File fVerbiage = new File(getBaseDir(context), verbiage);
                    File nFVerbiage = new File(getUpdateVerbiageDir(context), verbiage);
                    if (!fVerbiage.exists()) {
                        try {
                            fVerbiage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (renameFile(nFVerbiage, fVerbiage))
                        totalFilesRenamed++;
                }
                if (totalFilesRenamed != verbiages.size()) {
                    onStageDownloadResult(UpdateTaskStage.STAGE_7, FAILED);
                    return;
                }
            }
        }
        //MOVE ICONS HMAP FILE TO "hmaps" FOLDER
        {
            File fIconsSHA256MapJSON = getIconsSHA256MapJSON(context);
            File fOldIconsSHA256MapJSON = getOldIconsSHA256MapJSON(context);
            boolean iconMapUpdated = updateMapJSONFile(fIconsSHA256MapJSON, fOldIconsSHA256MapJSON);

            if (iconMapUpdated) {
                logGeneralEvents("Icon File Update Success");
            } else {
                logGeneralEvents("Icon File Update Failed");
                onStageDownloadResult(UpdateTaskStage.STAGE_7, FAILED);
                return;
            }
        }
        //MOVE VERBIAGE HMAP FILE TO "hmaps" FOLDER
        {
            File fVerbiageJSON = getVerbiageMapJSON(context);
            File fOldVerbiageJSON = getOldVerbiageMapJSON(context);
            boolean verbiageMapUpdated = updateMapJSONFile(fVerbiageJSON, fOldVerbiageJSON);

            if (verbiageMapUpdated) {
                logGeneralEvents("Verbiage File Update Success");
            } else {
                logGeneralEvents("Verbiage File Update Failed");
                onStageDownloadResult(UpdateTaskStage.STAGE_7, FAILED);
                return;
            }
        }
        //MOVE PACKAGE VERSION FILE TO "vc" FOLDER
        {
            File fVersionCodeJSON = getVersionCodeMapJSON(context);
            File fOldVersionCodeJSON = getOldVersionCodeMapJSON(context);
            boolean verbiageCodeUpdated = updateMapJSONFile(fVersionCodeJSON, fOldVersionCodeJSON);

            if (verbiageCodeUpdated) {
                logGeneralEvents("Version Code Update Success");
            } else {
                logGeneralEvents("Version Code Update Failed");
                onStageDownloadResult(UpdateTaskStage.STAGE_7, FAILED);
                return;
            }
        }
        onStageDownloadResult(UpdateTaskStage.STAGE_7,SUCCESS);
    }


    @Override
    public FileDownloadTask downloadSHA256MapJSON(File fSHA256MapJSON, StorageReference refSHA256MapJSON) {
        return refSHA256MapJSON.getFile(fSHA256MapJSON);
    }

    private void generateIconsHashMaps(File fSHA256MapJSON, File fOldSHA256MapJSON){
        this.mapIconSHA256 = getHashMap(fSHA256MapJSON);
        this.mapOldIconSHA256 = getHashMap(fOldSHA256MapJSON);
    }

    private void generateVerbiageHashMaps(File fSHA256MapJSON, File fOldSHA256MapJSON){
        this.mapVerbiageSHA256 = getHashMap(fSHA256MapJSON);
        this.mapOldVerbiageSHA256 = getHashMap(fOldSHA256MapJSON);
    }

    @Override
    public FileDownloadTask downloadVerbiageMapJSON(File fVerbiageMapJSON, StorageReference refVerbiageMapJSON) {
        return refVerbiageMapJSON.getFile(fVerbiageMapJSON);
    }

    @Override
    public int generateIconDownloadQueue() {
        Set<String> iconNames = mapIconSHA256.keySet();
        if (mapOldIconSHA256 == null) {
            iconDownloadQueue.addAll(mapIconSHA256.keySet());
            iconDownloadQueueSize = iconDownloadQueue.size();
            return iconDownloadQueueSize;
        }
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
    public int generateVerbiageDownloadQueue() {
        Set<String> verbiageNames = mapVerbiageSHA256.keySet();
        if (mapOldVerbiageSHA256 == null) {
            verbiageDownloadQueue.addAll(mapVerbiageSHA256.keySet());
            verbiageDownloadQueueSize = verbiageDownloadQueue.size();
            return verbiageDownloadQueueSize;
        }
        for(String verbiageName:verbiageNames){
            if(verbiageName != null){
                if(mapOldVerbiageSHA256.containsKey(verbiageName)){
                    if(!mapVerbiageSHA256.get(verbiageName).equals(mapOldVerbiageSHA256.get(verbiageName))){
                        verbiageDownloadQueue.add(verbiageName);
                    }
                } else {
                    verbiageDownloadQueue.add(verbiageName);
                }
            }
        }
        verbiageDownloadQueueSize = verbiageDownloadQueue.size();

        return verbiageDownloadQueueSize;
    }

    @Override
    public void downloadIconFiles(final File fIconDownloadDir,final StorageReference refIconsDir) {

        while (iconDownloadQueue.peek() != null){
            final String iconName = iconDownloadQueue.poll();
            StorageReference refIconFile = refIconsDir.child(iconName);
            File fIcon = new File(fIconDownloadDir.getAbsolutePath(),iconName);
            FileDownloadTask iconValue = refIconFile.getFile(fIcon);
            iconValue.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        downloadedIcons.add(iconName);
                        progressReceiver.onDownloadProgress(downloadedIcons.size(),iconDownloadQueueSize);
                    } else {
                        failedIconDownloadsQueue.add(iconName);
                    }

                    checkIconsDownloadingTaskCompleted(fIconDownloadDir,refIconsDir);
                }
            });
        }
    }

    @Override
    public void downloadVerbiageFiles(final File fVerbiageDownloadDir,final StorageReference refVerbiageDir) {

        while (verbiageDownloadQueue.peek() != null){
            final String verbiageName = verbiageDownloadQueue.poll();
            StorageReference refVerbiageFile = refVerbiageDir.child(verbiageName);
            File fVerbiage = new File(fVerbiageDownloadDir.getAbsolutePath(),verbiageName);
            FileDownloadTask verbiageValue = refVerbiageFile.getFile(fVerbiage);
            verbiageValue.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        downloadedVerbiage.add(verbiageName);
                        progressReceiver.onDownloadProgress(downloadedVerbiage.size(),verbiageDownloadQueueSize);
                    } else {
                        failedVerbiageDownloadsQueue.add(verbiageName);
                    }
                    checkVerbiageDownloadingTaskCompleted(fVerbiageDownloadDir,refVerbiageDir);
                }
            });
        }
    }

    @Override
    public void retryFailedIconsDownloads(final File fIconDownloadDir, final StorageReference refIconsDir) {
        while (failedIconDownloadsQueue.peek() != null){
            final String iconName = failedIconDownloadsQueue.poll();
            StorageReference refIconFile = refIconsDir.child(iconName);
            File fIcon = new File(fIconDownloadDir.getAbsolutePath(),iconName);
            FileDownloadTask iconvalue = refIconFile.getFile(fIcon);
            iconvalue.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        downloadedIcons.add(iconName);
                        progressReceiver.onDownloadProgress(downloadedIcons.size(),iconDownloadQueueSize);
                    } else {
                        failedIconDownloadsQueue.add(iconName);
                    }

                    checkIconsDownloadingTaskCompleted(fIconDownloadDir,refIconsDir);
                }
            });
        }
    }

    @Override
    public void retryFailedVerbiageDownloads(final File fVerbiageDownloadDir, final StorageReference refVerbiageDir) {
        while (failedVerbiageDownloadsQueue.peek() != null){
            final String verbiageName = failedVerbiageDownloadsQueue.poll();
            StorageReference refVerbiageFile = refVerbiageDir.child(verbiageName);
            File fVerbiage = new File(fVerbiageDownloadDir.getAbsolutePath(),verbiageName);
            FileDownloadTask verbiageValue = refVerbiageFile.getFile(fVerbiage);
            verbiageValue.addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        downloadedVerbiage.add(verbiageName);
                        progressReceiver.onDownloadProgress(downloadedVerbiage.size(),verbiageDownloadQueueSize);
                    } else {
                        failedVerbiageDownloadsQueue.add(verbiageName);
                    }
                    checkVerbiageDownloadingTaskCompleted(fVerbiageDownloadDir,refVerbiageDir);
                }
            });
        }
    }

    @Override
    public boolean updateIconsSHA256MapJSONFile(File fNewSHA256MapJSON, File fOldSHA256MapJSON) {

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
    public boolean updateVerbiageSHA256MapJSONFile(File fNewSHA256MapJSON, File fOldSHA256MapJSON) {

        Set<String> verbiageNamesN = mapVerbiageSHA256.keySet();
        for(String verbiageName:verbiageNamesN){
            if(mapOldVerbiageSHA256.containsKey(verbiageName)){
                if(!mapVerbiageSHA256.get(verbiageName).equals(mapOldVerbiageSHA256.get(verbiageName))){
                    if(downloadedVerbiage.contains(verbiageName)){
                        mapOldVerbiageSHA256.put(verbiageName,mapVerbiageSHA256.get(verbiageName));
                    }
                }
            } else {
                if(downloadedVerbiage.contains(verbiageName)){
                    mapOldVerbiageSHA256.put(verbiageName,mapVerbiageSHA256.get(verbiageName));
                }
            }
        }

        Gson gson = new Gson();
        String mapVerbiageSHA256JSONString = gson.toJson(mapOldVerbiageSHA256);

        boolean writeSuccess = writeToFile(fNewSHA256MapJSON,mapVerbiageSHA256JSONString);

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
    public boolean updateMapJSONFile(File fVerbiageMapJSON, File fOldVerbiageMapJSON) {
        boolean renameSuccess = false;
        if (!fOldVerbiageMapJSON.exists()) {
            try {
                renameSuccess = fOldVerbiageMapJSON.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(doesExist(fVerbiageMapJSON) && doesExist(fOldVerbiageMapJSON)){
            renameSuccess = renameFile(fVerbiageMapJSON,fOldVerbiageMapJSON);
        }
        return renameSuccess;
    }

    private void checkIconsDownloadingTaskCompleted(File fIconDownloadDir, StorageReference refIconsDir){
        if(downloadedIcons.size() + failedIconDownloadsQueue.size() == iconDownloadQueueSize){
            if(downloadedIcons.size() == iconDownloadQueueSize){
                isIconsUpdated = true;
                onIconDownloadTaskCompleted(true);
            } else {
                if(failedIconsDownloadsRetryCount <= FAILED_DOWNLOADS_RETRY_LIMIT){
                    failedIconsDownloadsRetryCount++;
                    logGeneralEvents("Retrying Failed Icon Downloads");
                    retryFailedIconsDownloads(fIconDownloadDir,refIconsDir);
                } else {
                    onIconDownloadTaskCompleted(false);
                    logGeneralEvents("Failed Icon Downloads Retry limit reached");
                }
            }
        }
    }

    private void checkVerbiageDownloadingTaskCompleted(File fVerbiageDownloadDir, StorageReference refVerbiageDir){
        if(downloadedVerbiage.size() + failedVerbiageDownloadsQueue.size() == verbiageDownloadQueueSize){
            if(downloadedVerbiage.size() == verbiageDownloadQueueSize){
                isVerbiageUpdated = true;
                onVerbiageDownloadTaskCompleted(true);
            } else {
                if(failedVerbiageDownloadsRetryCount <= FAILED_DOWNLOADS_RETRY_LIMIT){
                    failedVerbiageDownloadsRetryCount++;
                    logGeneralEvents("Retrying Failed Downloads");
                    retryFailedVerbiageDownloads(fVerbiageDownloadDir,refVerbiageDir);
                } else  {
                    onVerbiageDownloadTaskCompleted(false);
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

    private void onVerbiageDownloadTaskCompleted(boolean success) {
        progressReceiver.onIconDownloadTaskCompleted(success);
        if(success){
            onStageDownloadResult(UpdateTaskStage.STAGE_6,SUCCESS);
        } else {
            onStageDownloadResult(UpdateTaskStage.STAGE_6,FAILED);
        }
    }

    /*Update the final task result status*/
    private void onUpdateTaskResult(UpdateTaskResult updateTaskResult){

        cleanUpdateFiles();
        switch (updateTaskResult){
            case PACKAGE_SUCCESSFULLY_UPDATED:
                logGeneralEvents("Update task Successfully executed");
                updateStatusText(context.getString(R.string.lpu_update_success));
                showUpdateInfo(context.getString(R.string.lpu_toast_update_success));
                notifyIconsModified(true);
                break;
            case NO_UPDATES_FOUND:
                logGeneralEvents("No new/updated icons found");
                updateStatusText(context.getString(R.string.lpu_no_updates_found));
                showUpdateInfo(context.getString(R.string.lpu_toast_check_success));
                notifyIconsModified(false);
                break;
            case FAILED:
                logGeneralEvents("Update task execution failed!!");
                updateStatusText(context.getString(R.string.lpu_update_error));
                showUpdateInfo(context.getString(R.string.lpu_toast_check_error));
                notifyIconsModified(false);
                break;
        }
    }

    /*Select step*/
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
                    downloadStage6();
                    break;
                case STAGE_6:
                    downloadStage7();
                    break;
                case STAGE_7:
                    onUpdateTaskResult(UpdateTaskResult.PACKAGE_SUCCESSFULLY_UPDATED);
                    break;
            }
        } else {
            logGeneralEvents("Failed: " + stage);
            onUpdateTaskResult(UpdateTaskResult.FAILED);
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
}