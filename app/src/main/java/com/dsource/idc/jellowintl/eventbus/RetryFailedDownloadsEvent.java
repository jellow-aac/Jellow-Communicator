package com.dsource.idc.jellowintl.eventbus;

public class RetryFailedDownloadsEvent {
    private int retryDownloadCount;

    public RetryFailedDownloadsEvent(int retryDownloadCount) {
        this.retryDownloadCount = retryDownloadCount;
    }

    public int getRetryDownloadCount() {
        return retryDownloadCount;
    }
}
