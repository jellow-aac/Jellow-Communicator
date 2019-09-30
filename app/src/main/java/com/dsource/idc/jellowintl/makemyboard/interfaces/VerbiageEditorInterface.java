package com.dsource.idc.jellowintl.makemyboard.interfaces;

import android.graphics.Bitmap;

import com.dsource.idc.jellowintl.models.Icon;

public interface VerbiageEditorInterface {

    void onPositiveButtonClick(String name, Bitmap bitmap, int iconType);
    void onPhotoModeSelect(int position);
    void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface);
}
