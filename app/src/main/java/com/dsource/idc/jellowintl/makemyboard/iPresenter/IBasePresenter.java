package com.dsource.idc.jellowintl.makemyboard.iPresenter;

import com.dsource.idc.jellowintl.makemyboard.iView.IBaseView;

public interface IBasePresenter<T extends IBaseView> {
    void attachView(T view);
}
