package com.dsource.idc.jellowintl.makemyboard.presenter_interfaces;

import com.dsource.idc.jellowintl.makemyboard.view_interfaces.IBaseView;

public interface IBasePresenter<T extends IBaseView> {
    void attachView(T view);
}
