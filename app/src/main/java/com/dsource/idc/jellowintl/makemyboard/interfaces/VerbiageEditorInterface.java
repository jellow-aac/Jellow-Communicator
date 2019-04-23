package com.dsource.idc.jellowintl.makemyboard.interfaces;

import android.graphics.Bitmap;

import com.dsource.idc.jellowintl.makemyboard.verbiage_model.JellowVerbiageModel;

public interface VerbiageEditorInterface {

    void onPositiveButtonClick(String name, Bitmap bitmap, JellowVerbiageModel verbiageList);
    void onPhotoModeSelect(int position);
    void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface);
}
