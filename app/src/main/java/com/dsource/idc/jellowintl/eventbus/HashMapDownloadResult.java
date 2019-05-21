package com.dsource.idc.jellowintl.eventbus;

public class HashMapDownloadResult {
    private  boolean success;

    public HashMapDownloadResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
