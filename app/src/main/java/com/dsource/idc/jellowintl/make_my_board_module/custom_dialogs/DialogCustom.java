package com.dsource.idc.jellowintl.make_my_board_module.custom_dialogs;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;
import com.rey.material.app.Dialog;

public class DialogCustom extends Dialog {

    private Context context;
    private Dialog dialog;
    private OnPositiveClickListener mPositiveClickListener;
    private OnNegativeClickListener mNegativeClickListener;
    private TextView dialogText;

    public DialogCustom(Context context){
        super(context);
        this.context=context;
        prepareDialog();
    }
    private void prepareDialog()
    {
        final LayoutInflater dialogLayout = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View dialogContainerView = dialogLayout.inflate(R.layout.dialog_custom_alert, null);
        Button positiveButton = dialogContainerView.findViewById(R.id.positive);
        Button negativeButton = dialogContainerView.findViewById(R.id.negative);
        positiveButton.setText(R.string.yes);
        negativeButton.setText(R.string.no);
        positiveButton.setOnClickListener(new View.OnClickListener() {
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
}
