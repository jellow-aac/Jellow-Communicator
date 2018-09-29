package com.dsource.idc.jellowboard.makemyboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsource.idc.jellowboard.R;

import java.util.ArrayList;

public class SimpleListAdapter extends ArrayAdapter<ListItem> {
    private Context context;
    private ArrayList<ListItem> list;
    // View lookup cache
    private static class ViewHolder {
        TextView name;
        ImageView home;
    }

    public SimpleListAdapter(Context context, ArrayList<ListItem> list) {
        super(context, R.layout.list_item, list);
       this.context=context;
       this.list =list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
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
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(user.title);
        viewHolder.home.setImageDrawable(user.drawable);
        // Return the completed view to render on screen
        return convertView;
    }
}

class ListItem
{
    public String title;
    public android.graphics.drawable.Drawable drawable;

    public ListItem(String title, Drawable drawable) {
        this.title = title;
        this.drawable = drawable;
    }
}