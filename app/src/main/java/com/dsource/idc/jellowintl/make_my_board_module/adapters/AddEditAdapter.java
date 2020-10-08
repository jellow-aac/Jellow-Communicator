package com.dsource.idc.jellowintl.make_my_board_module.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.EditAdapterCallback;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

public class AddEditAdapter extends BaseRecyclerAdapter<JellowIcon> {

    private EditAdapterCallback callback;
    private int highlightedPosition = -1;

    public AddEditAdapter(Context context, int layoutResId, ArrayList<JellowIcon> data) {
        super(context, layoutResId, data);
    }

    @Override
    public void bindData(final BaseViewHolder viewHolder, JellowIcon item, int position) {

        String iconTitle = item.getIconTitle().length() <= 24 ?
                item.getIconTitle() :
                item.getIconTitle().substring(0,23)+ getContext().getString(R.string.limiter);
        viewHolder.setText(R.id.te1, iconTitle);
        /**
         * Below line is very important line and it should be always visible for add/edit icon screen
         * */
        //Enable the edit/remove view container
        viewHolder.setVisible(R.id.edit_remove_container,true);

        //Set the image according to various conditions
        if (item.getIconDrawable().equals("add_icon")) {
            viewHolder.setImageDrawable(R.id.icon1, getContext().getResources().getDrawable(R.drawable.ic_plus));
        } else if (item.isCustomIcon()) {
            viewHolder.setImageFromBoard(R.id.icon1, item.getIconDrawable(), R.drawable.ic_icon_placeholder);
        } else
            viewHolder.setImageFromLibrary(R.id.icon1, item.getIconDrawable());
        viewHolder.setSetAccessibility(R.id.icon1, false);
        viewHolder.getView(R.id.linearlayout_icon1).
                setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);

        //Hide the edit and remove icon for the add_icon button, which is at the 0th position
        viewHolder.setVisible(R.id.delete_icons, position != 0);
        viewHolder.setVisible(R.id.edit_icons, position != 0);
        viewHolder.setDescriptionToIcon(R.id.delete_icons,
                getContext().getString(R.string.delete_current_icon));
        viewHolder.setDescriptionToIcon(R.id.edit_icons,
                getContext().getString(R.string.edit_current_icon));

        //Setting up listeners
        viewHolder.setOnClickListener(R.id.delete_icons, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onIconRemove(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.setOnClickListener(R.id.edit_icons, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onIconEdit(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.setOnClickListener(R.id.icon1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onIconClicked(viewHolder.getAdapterPosition());
            }
        });


        //Highlight the searched icon
        if (position == highlightedPosition) {
            viewHolder.setMenuImageBorder(R.id.borderView, true, -1);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewHolder.getView(R.id.linearlayout_icon1).
                            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                }
            }, 1500);
        }else viewHolder.setMenuImageBorder(R.id.borderView,false,-1);

    }

    public void setListener(EditAdapterCallback editAdapterCallback) {
        this.callback = editAdapterCallback;
    }

    public void setHighlightedPosition(int position) {
        highlightedPosition = position;
        notifyDataSetChanged();
    }
}