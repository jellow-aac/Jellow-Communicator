package com.dsource.idc.jellowintl.makemyboard.interfaces;

import android.graphics.Bitmap;

public interface VerbiageEditorInterface {

    void onPositiveButtonClick(String name, Bitmap bitmap, int iconType);
    void onPhotoModeSelect(int position);
    void initPhotoResultListener(OnPhotoResultCallBack onPhotoResultCallBack);
}
