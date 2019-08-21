package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

public class SimpleArrayAdapter extends ArrayAdapter<String> {
    // View lookup cache
    private class ViewHolder {
        TextView name;
    }

    public SimpleArrayAdapter(Context context, ArrayList<String> StringList) {
        super(context, R.layout.level_select_card, StringList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.level_select_card, parent, false);
            viewHolder.name = convertView.findViewById(R.id.icon_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(title.replaceAll("…",""));
        return convertView;
    }

}