package com.dsource.idc.jellowintl.makemyboard.utility;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.interfaces.GridSelectListener;
import com.rey.material.app.Dialog;

import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.DIALOG_TYPE;

public class CustomDialog extends Dialog {

    private Context context;
    private Dialog dialog;
    private OnPositiveClickListener mPositiveClickListener;
    private OnNegativeClickListener mNegativeClickListener;
    private Button postiveButton, negativeButton;
    private TextView dialogText;
    public static final int NORMAL=111;
    public static final int ICON_EDIT=333;
    private String langCode;

    public CustomDialog(Context context,int code,String langCode){
        super(context);
        this.context=context;
        this.langCode = langCode;
        if(code==NORMAL)
        {
            prepareDialog();
        }


    }
    public CustomDialog(Context context,String langCode, GridSelectListener mGridSizeSelectListener){
        super(context);
        this.context=context;
        this.langCode = langCode;
        prepareGridDialog(mGridSizeSelectListener);
    }

    private void prepareGridDialog(GridSelectListener mGridSizeSelectListener) {
        Intent dialog =new Intent(context,DialogBox.class);
        dialog.putExtra(DIALOG_TYPE,BoardConstants.GRID_DIALOG);
        dialog.putExtra(LCODE,langCode);
        DialogBox.mGridSelectionListener =mGridSizeSelectListener;
        context.startActivity(dialog);
    }


    private void prepareDialog()
    {
        final LayoutInflater dialogLayout = LayoutInflater.from(context);
        View dialogContainerView = dialogLayout.inflate(R.layout.custom_dialog, null);
        postiveButton =dialogContainerView.findViewById(R.id.positive);
        negativeButton =dialogContainerView.findViewById(R.id.negative);
        postiveButton.setText(R.string.yes);
        negativeButton.setText(R.string.no);
        postiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPositiveClickListener!=null)
                mPositiveClickListener.onPositiveClickListener();
                dialog.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNegativeClickListener!=null)
                mNegativeClickListener.onNegativeClickListener();
                dialog.dismiss();
            }
        });
        dialogText=dialogContainerView.findViewById(R.id.dialog_text);
        dialog = new Dialog(context,R.style.MyDialogBox);
        dialog.applyStyle(R.style.MyDialogBox);
        dialog.backgroundColor(context.getResources().getColor(R.color.transparent));
        dialog.setContentView(dialogContainerView);
    }


    public void setOnPositiveClickListener(final OnPositiveClickListener mPositiveClickListener) {
        this.mPositiveClickListener = mPositiveClickListener;
    }

    public void setOnNegativeClickListener(final OnNegativeClickListener mNegativeClickListener) {
        this.mNegativeClickListener = mNegativeClickListener;
    }


    public interface OnPositiveClickListener
    {
        void onPositiveClickListener();
    }

    public interface OnNegativeClickListener
    {
        void onNegativeClickListener();
    }

    public void show()
    {
        if(dialog!=null)
            dialog.show();
    }
    public void setText(String text)
    {
        dialogText.setText(text);
    }
    public void setPositiveText(String text)
    {
        postiveButton.setText(text);
    }
    public void setNegativeText(String text)
    {
       negativeButton.setText(text);
    }


}
