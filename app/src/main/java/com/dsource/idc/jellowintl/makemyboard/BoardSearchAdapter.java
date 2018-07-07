package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class BoardSearchAdapter extends RecyclerView.Adapter<BoardSearchAdapter.ViewHolder>{

private Context mContext;
// private LayoutInflater mInflater;
private ArrayList<JellowIcon> mDataSource;
        BoardSearchAdapter.OnItemClickListener mItemClickListener=null;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView iconTitle;
    public ImageView iconImage;
    public TextView iconDir;
    public ImageView speakIcon;
    public ViewHolder(View v) {
        super(v);
        iconImage =v.findViewById(R.id.search_icon_drawable);
        iconTitle = v.findViewById(R.id.search_icon_title);
        iconDir = v.findViewById(R.id.parent_directory);
        speakIcon=v.findViewById(R.id.speak_button);
        speakIcon.setVisibility(View.GONE);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mItemClickListener.onItemClick(view,getAdapterPosition());
    }
}



public interface OnItemClickListener {
    void onItemClick(View view, int position);
}

    public void setOnItemClickListner(final BoardSearchAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * public constructor
     * @param context
     * @param items
     */
    public BoardSearchAdapter(Context context, ArrayList<JellowIcon> items) {
        mContext = context;
        mDataSource = items;
    }


    @Override
    public BoardSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_search_list_item, parent, false);


        return new BoardSearchAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(BoardSearchAdapter.ViewHolder holder, int position) {
        JellowIcon icon = mDataSource.get(position);


    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
