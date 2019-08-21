package com.dsource.idc.jellowintl.makemyboard.interfaces;

import android.graphics.Bitmap;

import com.dsource.idc.jellowintl.models.Icon;

public interface VerbiageEditorInterface {

    void onPositiveButtonClick(String name, Bitmap bitmap, Icon verbiageList);
    void onPhotoModeSelect(int position);
    void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface);
}
