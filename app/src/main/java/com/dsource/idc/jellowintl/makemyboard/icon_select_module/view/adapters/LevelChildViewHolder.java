package com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.adapters;


import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.bean.LevelChild;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;

public class LevelChildViewHolder extends ChildViewHolder {

    private TextView artistName;

    public LevelChildViewHolder(View itemView, final GenCallback<Integer> callback) {
        super(itemView);
        artistName = itemView.findViewById(R.id.icon_title);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callBack(getChildAdapterPosition());
            }
        });
    }

    public void onBind(LevelChild artist) {
        artistName.setText(artist.getName().replaceAll("…",""));
    }
}