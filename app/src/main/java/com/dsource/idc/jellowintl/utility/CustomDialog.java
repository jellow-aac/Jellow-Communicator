package com.dsource.idc.jellowintl.utility;


import android.content.Context;
import android.content.Intent;

import com.rey.material.app.Dialog;

import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.models.GlobalConstants.DIALOG_TYPE;
import static com.dsource.idc.jellowintl.models.GlobalConstants.GRID_DIALOG;

public class CustomDialog extends Dialog {

    private Context context;
    private String langCode;

    public CustomDialog(Context context,String langCode, GridSelectListener mGridSizeSelectListener){
        super(context);
        this.context=context;
        this.langCode = langCode;
        prepareGridDialog(mGridSizeSelectListener);
    }

    private void prepareGridDialog(GridSelectListener mGridSizeSelectListener) {
        Intent dialog = new Intent(context, DialogBox.class);
        dialog.putExtra(DIALOG_TYPE, GRID_DIALOG);
        dialog.putExtra(LCODE, langCode);
        DialogBox.mGridSelectionListener = mGridSizeSelectListener;
        context.startActivity(dialog);
    }
}
