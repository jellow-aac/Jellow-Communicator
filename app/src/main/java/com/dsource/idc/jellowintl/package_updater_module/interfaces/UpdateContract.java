package com.dsource.idc.jellowintl.package_updater_module.interfaces;



import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public interface UpdateContract {


    FileDownloadTask downloadSHA256MapJSON(File fSHA256MapJSON, StorageReference refSHA256MapJSON);

    int generateIconDownloadQueue();

    int generateVerbiageDownloadQueue();

    FileDownloadTask downloadVerbiageMapJSON(File fVerbiageMapJSON, StorageReference refVerbiageMapJSON);

    void downloadIconFiles(File fIconDownloadDir, StorageReference refIconsDir);

    void downloadVerbiageFiles(File fIconDownloadDir, StorageReference refIconsDir);

    void retryFailedIconsDownloads(File fIconDownloadDir, StorageReference refIconsDir);

    void retryFailedVerbiageDownloads(File fIconDownloadDir, StorageReference refIconsDir);

    boolean updateIconsSHA256MapJSONFile(File fNewSHA256MapJSON, File fOldSHA256MapJSON);

    boolean updateVerbiageSHA256MapJSONFile(File fNewSHA256MapJSON, File fOldSHA256MapJSON);

    boolean updateMapJSONFile(File fVerbiageMapJSON, File fOldVerbiageMapJSON);

    boolean cleanUpdateFiles();

}
