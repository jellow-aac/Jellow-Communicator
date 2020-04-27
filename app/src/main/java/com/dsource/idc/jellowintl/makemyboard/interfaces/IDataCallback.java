package com.dsource.idc.jellowintl.makemyboard.interfaces;

public interface IDataCallback<T> {
    void onSuccess(T object);
    void onFailure(String msg);
}
