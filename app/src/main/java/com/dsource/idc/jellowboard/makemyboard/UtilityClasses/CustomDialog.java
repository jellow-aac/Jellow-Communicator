package com.dsource.idc.jellowboard.makemyboard.UtilityClasses;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsource.idc.jellowboard.R;
import com.rey.material.app.Dialog;

public class CustomDialog extends Dialog{

    private Context context;
    private Dialog dialog;
    private onPositiveClickListener mPositiveClickListener;
    private onNegativeClickListener mNegativeClickListener;
    private Button postiveButton, negativeButton;
    private TextView dialogText;
    public static final int NORMAL=111;
    public static final int GRID_SIZE=222;
    public static final int ICON_EDIT=333;
    public static final int BOARD_EDIT=444;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;

    }
    public CustomDialog(Context context,int Code){
        super(context);
        this.context=context;
        if(Code==GRID_SIZE)
        {
            prepareGridDialog();
        }
        else if(Code==ICON_EDIT)
        {
            prepareIconEditDialog();
        }
        else if(Code==NORMAL)
        {
            prepareDialog();
        }


    }

    private void prepareIconEditDialog() {
        final LayoutInflater dialogLayout = LayoutInflater.from(context);
        View dialogContainerView = dialogLayout.inflate(R.layout.icon_edit_dialog, null);
        dialog = new Dialog(context,R.style.MyDialogBox);
        dialog.applyStyle(R.style.MyDialogBox);
        dialog.backgroundColor(context.getResources().getColor(R.color.transparent));
        dialog.setContentView(dialogContainerView);
        dialog.setCancelable(true);


    }

    private void prepareGridDialog() {
        final LayoutInflater dialogLayout = LayoutInflater.from(context);
        View dialogContainerView = dialogLayout.inflate(R.layout.grid_dialog, null);
        dialog = new Dialog(context,R.style.MyDialogBox);
        dialog.applyStyle(R.style.MyDialogBox);
        dialog.backgroundColor(context.getResources().getColor(R.color.transparent));
        dialog.setContentView(dialogContainerView);
        dialog.setCancelable(true);

        final ImageView GridSize1=dialogContainerView.findViewById(R.id.grid_size_1);
        final ImageView GridSize2=dialogContainerView.findViewById(R.id.grid_size_2);
        final ImageView GridSize3=dialogContainerView.findViewById(R.id.grid_size_3);
        final ImageView GridSize4=dialogContainerView.findViewById(R.id.grid_size_2X2);
        final ImageView GridSize5=dialogContainerView.findViewById(R.id.grid_size_2X3);
        final ImageView GridSize6=dialogContainerView.findViewById(R.id.grid_size_3X3);


        GridSize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mGridSizeSelectListener.onGridSelectListener(1);
            }
        });
        GridSize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mGridSizeSelectListener.onGridSelectListener(2);
            }
        });
        GridSize3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mGridSizeSelectListener.onGridSelectListener(3);
            }
        });

        GridSize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mGridSizeSelectListener.onGridSelectListener(4);
            }
        });
        GridSize5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mGridSizeSelectListener.onGridSelectListener(5);
            }
        });
        GridSize6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mGridSizeSelectListener.onGridSelectListener(6);
            }
        });

    }

    private GridSelectListener mGridSizeSelectListener;
    public Dialog setGridSelectListener(GridSelectListener mListener)
    {
        this.mGridSizeSelectListener =mListener;
        return dialog;
    }
    public interface GridSelectListener
    {
        void onGridSelectListener(int size);
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
