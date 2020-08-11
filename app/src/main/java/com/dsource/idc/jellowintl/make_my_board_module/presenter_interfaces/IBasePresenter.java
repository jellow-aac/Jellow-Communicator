package com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces;

import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.IBaseView;

public interface IBasePresenter<T extends IBaseView> {
    void attachView(T view);
}
