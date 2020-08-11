package com.dsource.idc.jellowintl.make_my_board_module.interfaces;

public interface IDataCallback<T> {
    void onSuccess(T object);
    void onFailure(String msg);
}
