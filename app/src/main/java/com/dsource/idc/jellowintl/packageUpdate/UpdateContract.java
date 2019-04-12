package com.dsource.idc.jellowintl.packageUpdate;



import com.google.firebase.storage.value;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public interface UpdateContract {


    value downloadSHA256MapJSON(File fSHA256MapJSON, StorageReference refSHA256MapJSON);

    int generateIconDownloadQueue();

    value downloadVerbiageMapJSON(File fVerbiageMapJSON, StorageReference refVerbiageMapJSON);

    void downloadIconFiles(File fIconDownloadDir, StorageReference refIconsDir);

    void retryFailedDownloads(File fIconDownloadDir, StorageReference refIconsDir);

    boolean updateSHA256MapJSONFile(File fNewSHA256MapJSON, File fOldSHA256MapJSON);

    boolean updateVerbiageMapJSONFile(File fVerbiageMapJSON,File fOldVerbiageMapJSON);

    boolean cleanUpdateFiles();

}
