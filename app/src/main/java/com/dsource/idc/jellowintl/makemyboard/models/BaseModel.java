package com.dsource.idc.jellowintl.makemyboard.models;

import com.dsource.idc.jellowintl.makemyboard.presenter_interfaces.IBasePresenter;
import com.dsource.idc.jellowintl.makemyboard.view_interfaces.IBaseView;

public class BaseModel<V extends IBaseView> implements IBasePresenter<V>{

    public V mView;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }
}
