package com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters;

public interface iDataPresenter<T> {
    void onSuccess(T object);
    void onFailure(String msg);
}
