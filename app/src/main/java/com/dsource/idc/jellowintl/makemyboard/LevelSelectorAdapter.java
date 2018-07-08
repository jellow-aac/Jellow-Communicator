package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

public class LevelSelectorAdapter extends RecyclerView.Adapter<LevelSelectorAdapter.ViewHolder>{

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;
    LevelSelectorAdapter.OnItemClickListener mItemClickListener=null;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //each data item is just a string in this case
        public TextView levelTitle;

        View holder;
        public ViewHolder(View v) {
            super(v);
            levelTitle =v.findViewById(R.id.icon_title);
            holder=v;
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

    public void setOnItemClickListner(final LevelSelectorAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * public constructor
     * @param context
     * @param items
     */
    public LevelSelectorAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mDataSource = items;
    }


    @Override
    public LevelSelectorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_select_card, parent, false);
        return new LevelSelectorAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LevelSelectorAdapter.ViewHolder holder, int position) {

        String title = mDataSource.get(position);
        holder.levelTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
