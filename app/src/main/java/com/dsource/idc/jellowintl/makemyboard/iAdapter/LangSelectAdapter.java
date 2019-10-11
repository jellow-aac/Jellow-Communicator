package com.dsource.idc.jellowintl.makemyboard.iAdapter;

import android.content.Context;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.MyPair;
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

        viewHolder.setText(R.id.board_count, item.getSecond().toString());

        if (position == selectedPosition) {
            viewHolder.setTextColorRes(R.id.icon_title, R.color.app_background);
            viewHolder.setTextColorRes(R.id.board_count, R.color.app_background);
            viewHolder.setItemViewBackgroundColor(R.color.colorPrimary);
        } else {
            viewHolder.setTextColorRes(R.id.board_count, R.color.level_select_text_color);
            viewHolder.setTextColorRes(R.id.icon_title, R.color.level_select_text_color);
            viewHolder.setItemViewBackgroundColor(R.color.app_background);
        }
    }

    @Override
    public void updateDataOnTouch(int position) {
        notifyItemChanged(selectedPosition);
        selectedPosition = position;
        notifyItemChanged(selectedPosition);
    }

    public void increaseBoardCount() {
        mData.get(selectedPosition).setSecond(mData.get(selectedPosition).getSecond()+1);
        notifyItemChanged(selectedPosition);
    }

    public void decreaseBoardCount() {
        mData.get(selectedPosition).setSecond(mData.get(selectedPosition).getSecond()-1);
        notifyItemChanged(selectedPosition);
    }
}
