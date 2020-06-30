package com.dsource.idc.jellowintl.make_my_board_module.datamodels;


import com.dsource.idc.jellowintl.make_my_board_module.interfaces.AbstractDataProvider;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;
import java.util.Collections;

public class DragNDropDataProvider extends AbstractDataProvider {
    private ArrayList<JellowIcon> mData;
    private JellowIcon mLastRemovedData;
    private int mLastRemovedPosition = -1;

    public DragNDropDataProvider(ArrayList<JellowIcon> icons){
        mData =icons;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public JellowIcon getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }
        return mData.get(index);
    }

    @Override
    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final JellowIcon removedItem = mData.remove(position);
        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;

    }

    @Override
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        final JellowIcon item = mData.remove(fromPosition);
        mData.add(toPosition, item);
        mLastRemovedPosition = -1;
    }

    @Override
    public void swapItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        Collections.swap(mData, toPosition, fromPosition);
        mLastRemovedPosition = -1;
    }

    @Override
    public int undoLastRemoval() {
        return 0;
    }

}
