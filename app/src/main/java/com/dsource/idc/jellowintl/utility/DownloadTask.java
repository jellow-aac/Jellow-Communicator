package com.dsource.idc.jellowintl.utility;

import com.google.firebase.storage.FileDownloadTask;

public class DownloadTask {
    public String fileName;
    public FileDownloadTask fileDownloadTask;


    public DownloadTask(String key, FileDownloadTask value) {
        this.fileName = key;
        this.fileDownloadTask = value;
    }

    public String getTaskName() {
        return fileName;
    }

    public FileDownloadTask getFileDownloadTask(){
        return fileDownloadTask;
    }
}
