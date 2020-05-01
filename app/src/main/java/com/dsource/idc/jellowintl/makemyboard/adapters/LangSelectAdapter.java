package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.utility.MyPair;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

public class LangSelectAdapter extends BaseRecyclerAdapter<MyPair<String, Integer>> {

    private int selectedPosition = 0;

    public LangSelectAdapter(Context context, int layoutResId, ArrayList<MyPair<String, Integer>> data) {
        super(context, layoutResId, data);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, MyPair<String, Integer> item, int position) {
        if (position != 0)
            viewHolder.setText(R.id.icon_title, SessionManager.LangValueMap.get(item.getFirst()));
        else
            viewHolder.setText(R.id.icon_title, item.getFirst());

        if (position == selectedPosition) {
            viewHolder.setTextColorRes(R.id.icon_title, R.color.app_background);
            viewHolder.setItemViewBackgroundColor(R.color.colorPrimary);
        } else {
            viewHolder.setTextColorRes(R.id.icon_title, R.color.level_select_text_color);
            viewHolder.setItemViewBackgroundColor(R.color.app_background);
        }
    }

    /*public void updateDataOnTouch(int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
    }

    public void increaseBoardCount(String language) {
        for (int i = 0; i < mData.size(); i++)
            if (mData.get(i).getFirst().equals(language)) {
                mData.get(i).setSecond(mData.get(i).getSecond() + 1);
                notifyItemChanged(i);
            }
    }

    public void increaseBoardCount() {
        mData.get(selectedPosition).setSecond(mData.get(selectedPosition).getSecond() + 1);
        notifyItemChanged(selectedPosition);
    }

    public void decreaseBoardCount() {
        mData.get(selectedPosition).setSecond(mData.get(selectedPosition).getSecond() - 1);
        notifyItemChanged(selectedPosition);
    }

    public void decreaseBoardCount(String language) {
        for (int i = 0; i < mData.size(); i++)
            if (mData.get(i).getFirst().equals(language)) {
                mData.get(i).setSecond(mData.get(i).getSecond() - 1);
                notifyItemChanged(i);
            }
    }*/

    @Override
    public int getSelectedPosition() {
        return selectedPosition;
    }
}
