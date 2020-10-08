package com.dsource.idc.jellowintl.make_my_board_module.models;

import com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces.IBasePresenter;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.IBaseView;

public class BaseModel<V extends IBaseView> implements IBasePresenter<V>{

    public V mView;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }
}
