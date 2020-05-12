package com.dsource.idc.jellowintl.makemyboard.interfaces;

public interface SuccessCallBack<T> {

    void setProgressSize(int progressSize);
    void updateProgress(int progress);
    void onSuccess(T object);
}
