package com.dsource.idc.jellowintl.makemyboard.JsonDatabase;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;
import com.rey.material.app.Dialog;

public class CustomDialog extends Dialog{

    private Context context;
    private Dialog dialog;
    private onPositiveClickListener mPositiveClickListener;
    private onNegativeClickListener mNegativeClickListener;
    private Button postiveButton, negativeButton;
    private TextView dialogText;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
        prepareDialog();
    }
    public CustomDialog(Context context,String text)
    {
        super(context);
        this.context=context;
        prepareDialog();
        setText(text);
    }

    private void prepareDialog()
    {
        final LayoutInflater dialogLayout = LayoutInflater.from(context);
        View dialogContainerView = dialogLayout.inflate(R.layout.custom_dialog, null);
        postiveButton =dialogContainerView.findViewById(R.id.positive);
        negativeButton =dialogContainerView.findViewById(R.id.negative);
        postiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPositiveClickListener.onPositiveClickListener();
                dialog.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    public void setOnPositiveClickListener(final CustomDialog.onPositiveClickListener mPositiveClickListener) {
        this.mPositiveClickListener = mPositiveClickListener;
    }

    public void setOnNegativeClickListener(final CustomDialog.onNegativeClickListener mNegativeClickListener) {
        this.mNegativeClickListener = mNegativeClickListener;
    }


    public interface onPositiveClickListener
    {
        void onPositiveClickListener();
    }

    public interface onNegativeClickListener
    {
        void onNegativeClickListener();
    }

    public void show()
    {
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