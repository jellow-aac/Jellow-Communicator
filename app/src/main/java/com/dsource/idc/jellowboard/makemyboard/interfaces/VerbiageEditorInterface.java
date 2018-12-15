package com.dsource.idc.jellowboard.makemyboard.interfaces;

import android.graphics.Bitmap;
import com.dsource.idc.jellowboard.verbiage_model.JellowVerbiageModel;

public interface VerbiageEditorInterface {

    void onSaveButtonClick(String name, Bitmap bitmap, JellowVerbiageModel verbiageList);
    void onPhotoModeSelect(int position);
    void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface);
}
