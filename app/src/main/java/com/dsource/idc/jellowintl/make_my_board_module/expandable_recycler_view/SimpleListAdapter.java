package com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.datamodels.ListItem;

import java.util.ArrayList;

public class SimpleListAdapter extends ArrayAdapter<ListItem> {

    private ArrayList<ListItem> list;
    // View lookup cache
    private static class ViewHolder {
        TextView name;
        ImageView home;
    }

    public SimpleListAdapter(Context context, ArrayList<ListItem> list) {
        super(context, R.layout.list_item, list);
       this.list =list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the iconList item for this position
        ListItem user = list.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.name = convertView.findViewById(R.id.list_item_title);
            viewHolder.home = convertView.findViewById(R.id.list_item_icon);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the iconList from the iconList object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(user.getTitle());
        viewHolder.home.setImageDrawable(user.getDrawable());
        // Return the completed view to render on screen
        return convertView;
    }
}

