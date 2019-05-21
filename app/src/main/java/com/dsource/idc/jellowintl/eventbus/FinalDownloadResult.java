package com.dsource.idc.jellowintl.eventbus;

public class FinalDownloadResult {
    private Boolean success;
    private int completedDownloads;
    private int failedDownloads;
    private int totalDownloads;

    public FinalDownloadResult(Boolean success, int completedDownloads, int failedDownloads, int totalDownloads) {
        this.success = success;
        this.completedDownloads = completedDownloads;
        this.failedDownloads = failedDownloads;
        this.totalDownloads = totalDownloads;
    }

    public Boolean getSuccess() {
        return success;
    }

    public int getCompletedDownloads() {
        return completedDownloads;
    }

    public int getFailedDownloads() {
        return failedDownloads;
    }

    public int getTotalDownloads() {
        return totalDownloads;
    }
}
