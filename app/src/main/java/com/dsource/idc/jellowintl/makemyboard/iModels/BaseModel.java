package com.dsource.idc.jellowintl.makemyboard.iModels;

import com.dsource.idc.jellowintl.makemyboard.iPresenter.IBasePresenter;
import com.dsource.idc.jellowintl.makemyboard.iView.IBaseView;

public class BaseModel<V extends IBaseView> implements IBasePresenter<V>{

    public V mView;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }
}
