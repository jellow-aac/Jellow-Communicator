package com.dsource.idc.jellowintl.make_my_board_module.interfaces;

public interface SuccessCallBack<T> {

    void setProgressSize(int progressSize);
    void updateProgress(int progress);
    void onSuccess(T object);
}
