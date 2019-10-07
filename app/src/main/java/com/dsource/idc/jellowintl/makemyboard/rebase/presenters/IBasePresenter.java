package com.dsource.idc.jellowintl.makemyboard.rebase.presenters;

import com.dsource.idc.jellowintl.makemyboard.rebase.views.IAddEditView;
import com.dsource.idc.jellowintl.makemyboard.rebase.views.IBaseView;

interface IBasePresenter<T extends IBaseView> {
    void attachView(T view);
}
