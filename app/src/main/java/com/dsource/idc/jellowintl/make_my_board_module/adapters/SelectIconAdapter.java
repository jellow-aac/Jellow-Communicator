package com.dsource.idc.jellowintl.make_my_board_module.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CheckBox;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.managers.SelectionManager;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public class SelectIconAdapter extends BaseRecyclerAdapter<JellowIcon> {

    private boolean isCheckBoxMode;
    private int highlightedIconPos = -1;
    private int checkedPosition = -1;

    public SelectIconAdapter(Context context, ArrayList<JellowIcon> data, boolean isCheckBoxMode) {
        super(context, R.layout.icon_card, data);
        this.isCheckBoxMode = isCheckBoxMode;
    }

    @Override
    public void bindData(final BaseViewHolder viewHolder, JellowIcon item, int position) {
        viewHolder.setText(R.id.icon_title,item.getIconTitle());
        viewHolder.setTextColor(R.id.icon_title,Color.rgb(64, 64, 64));

        if(!item.isCustomIcon())
            viewHolder.setImageFromLibrary(R.id.icon_image_view,item.getIconDrawable());
        else
            viewHolder.setImageFromBoard(R.id.icon_image_view,item.getIconDrawable(),R.drawable.ic_icon_placeholder);

        //Manage views that are not used and disabling the clicks on sub views to ensure the icon is clicked all the time
        viewHolder.getView(R.id.icon_image_view).setClickable(false);
        viewHolder.getView(R.id.icon_selection_checkbox).setClickable(false);
        viewHolder.setVisible(R.id.edit_icon,false);

        if(isCheckBoxMode){
            viewHolder.setVisible(R.id.icon_selection_checkbox,true);
            CheckBox checkBox = viewHolder.getView(R.id.icon_selection_checkbox);
            checkBox.setChecked(SelectionManager.getInstance().isPresent(item));
        }else{
            viewHolder.setVisible(R.id.icon_selection_checkbox,false);
        }

        //Highlight the searched icon
        if (position == highlightedIconPos) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    highlightedIconPos = -1;
                    viewHolder.getView(R.id.parent).
                            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }
            }, 1500);
        }else if (position == checkedPosition){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewHolder.getView(R.id.parent).
                            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }
            }, 1500);
            checkedPosition = -1;
        }else
            viewHolder.setMenuImageBorder(R.id.borderView,false,-1);
    }

    public void setCheckBoxMode(boolean checkBoxMode) {
        isCheckBoxMode = checkBoxMode;
    }

    public void setHighlightedIconPos(int highlightedIconPos) {
        this.highlightedIconPos = highlightedIconPos;
        notifyItemChanged(highlightedIconPos);
    }

    public void setCheckedPosition(int position) {
        checkedPosition=position;
    }
}
