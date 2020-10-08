package com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.datamodels.LevelChild;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.GenCallback;

public class LevelChildViewHolder extends ChildViewHolder {

    private TextView artistName;
    private Context context;

    public LevelChildViewHolder(View itemView, final GenCallback<Integer> callback, Context context) {
        super(itemView);
        artistName = itemView.findViewById(R.id.icon_title);
        this.context = context;
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