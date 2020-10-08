package com.dsource.idc.jellowintl.make_my_board_module.managers;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.widget.ImageView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.models.Icon;

import java.util.ArrayList;

/**
 * This class is for managing the clicks of expressive icons
 */
public class ExpressiveIconManager implements View.OnClickListener {

    private ArrayList<ImageView> expIconList;
    private ArrayList<Integer> buttonFlags;
    private expIconClickListener mExpIconClickListener;
    private TypedArray pressed;
    private TypedArray unPressed;

    public ExpressiveIconManager(Context context, View parent) {

        expIconList = new ArrayList<>();
        expIconList.add((ImageView) parent.findViewById(R.id.ivlike));
        expIconList.add((ImageView) parent.findViewById(R.id.ivyes));
        expIconList.add((ImageView) parent.findViewById(R.id.ivadd));
        expIconList.add((ImageView) parent.findViewById(R.id.ivdislike));
        expIconList.add((ImageView) parent.findViewById(R.id.ivno));
        expIconList.add((ImageView) parent.findViewById(R.id.ivminus));

        buttonFlags = new ArrayList<>();
        for (int i = 0; i < expIconList.size(); i++)
            buttonFlags.add(0);

        for (int i = 0; i < expIconList.size(); i++)
            expIconList.get(i).setOnClickListener(this);

        pressed = context.getResources().obtainTypedArray(R.array.expressive_icon_pressed);
        unPressed = context.getResources().obtainTypedArray(R.array.expressive_icon_unpressed);
    }

    @Override
    public void onClick(View v) {
        int index = expIconList.indexOf(v);
        mExpIconClickListener.expressiveIconClicked(index, buttonFlags.get(index));
        if (buttonFlags.get(index) == 1)
            buttonFlags.set(index, 0);
        else buttonFlags.set(index, 1);
        expIconList.get(index).setImageDrawable(pressed.getDrawable(index));
        setSelection(index);
    }

    private void setSelection(int index) {
        for (int i = 0; i < expIconList.size(); i++) {
            if (i == index) {
                expIconList.get(i).setImageDrawable(pressed.getDrawable(i));
            } else {
                expIconList.get(i).setImageDrawable(unPressed.getDrawable(i));
                buttonFlags.set(i, 0);
            }

        }


    }


    public void disableExpressiveIcons(boolean disable) {
        for (ImageView button : expIconList)
            disableButton(button, disable);
    }

    public void resetSelection() {
        for (int i = 0; i < expIconList.size(); i++) {
            expIconList.get(i).setImageDrawable(unPressed.getDrawable(i));
            expIconList.get(i).setAlpha(GlobalConstants.ENABLE_ALPHA);
            expIconList.get(i).setEnabled(true);
            buttonFlags.set(i, 0);
        }
    }

    public void setClickListener(expIconClickListener expIconClickListener) {
        this.mExpIconClickListener = expIconClickListener;
    }

    public void setAccordingVerbiage(Icon selectedIconVerbiage) {
        resetSelection();
        if (selectedIconVerbiage == null) return;
        if (selectedIconVerbiage.getL().equals("NA"))
            disableButton(expIconList.get(0), true);
        else disableButton(expIconList.get(0), false);
        if (selectedIconVerbiage.getY().equals("NA"))
            disableButton(expIconList.get(1), true);
        else disableButton(expIconList.get(1), false);
        if (selectedIconVerbiage.getM().equals("NA"))
            disableButton(expIconList.get(2), true);
        else disableButton(expIconList.get(2), false);
        if (selectedIconVerbiage.getD().equals("NA"))
            disableButton(expIconList.get(3), true);
        else disableButton(expIconList.get(3), false);
        if (selectedIconVerbiage.getN().equals("NA"))
            disableButton(expIconList.get(4), true);
        else disableButton(expIconList.get(4), false);
        if (selectedIconVerbiage.getS().equals("NA"))
            disableButton(expIconList.get(5), true);
        else disableButton(expIconList.get(5), false);
    }


    private void disableButton(ImageView imageView, boolean disable) {
        if (disable) {
            imageView.setEnabled(false);
            imageView.setAlpha(GlobalConstants.DISABLE_ALPHA);
        } else {
            imageView.setEnabled(true);
            imageView.setAlpha(GlobalConstants.ENABLE_ALPHA);
        }
    }

    public interface expIconClickListener {
        void expressiveIconClicked(int expIconPos, int time);
    }
}

