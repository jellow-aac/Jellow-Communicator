package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

public class LevelSelecterAdapter extends RecyclerView.Adapter<LevelSelecterAdapter.ViewHolder>{

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;
    LevelSelecterAdapter.OnItemClickListener mItemClickListener=null;
    int selectedPosition=-1;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //each data item is just a string in this case
        public TextView levelTitle;

        public ViewHolder(View v) {
            super(v);
            levelTitle =v.findViewById(R.id.icon_title);
            if(getAdapterPosition()==0)
                v.setBackground(mContext.getResources().getDrawable(R.color.colorIntro));
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           mItemClickListener.onItemClick(view,getAdapterPosition());
           selectedPosition=getAdapterPosition();
        }
    }



    public interface OnItemClickListener {
         void onItemClick(View view, int position);
    }

    public void setOnItemClickListner(final LevelSelecterAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * public constructor
     * @param context
     * @param items
     */
    public LevelSelecterAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mDataSource = items;
    }


    @Override
    public LevelSelecterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_select_card, parent, false);


        return new LevelSelecterAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(LevelSelecterAdapter.ViewHolder holder, int position) {
        String title = mDataSource.get(position);
        holder.levelTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
