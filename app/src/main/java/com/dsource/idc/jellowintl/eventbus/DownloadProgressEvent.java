package com.dsource.idc.jellowintl.eventbus;

public class DownloadProgressEvent {
    int totalDownloads;
    int completedDownloads;
    int failedDownloads;

    public DownloadProgressEvent(int totalDownloads, int completedDownloads, int failedDownloads) {
        this.totalDownloads = totalDownloads;
        this.completedDownloads = completedDownloads;
        this.failedDownloads = failedDownloads;
    }

}
