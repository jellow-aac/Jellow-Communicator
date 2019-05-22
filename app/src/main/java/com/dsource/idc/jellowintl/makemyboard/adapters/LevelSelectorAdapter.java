package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class LevelSelectorAdapter extends RecyclerView.Adapter<LevelSelectorAdapter.ViewHolder>{

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;
    LevelSelectorAdapter.OnItemClickListener mItemClickListener=null;
    public int selectedPosition = 0 ;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Each data item is just a string in this case
        TextView levelTitle;
        View holder;
        public ViewHolder(View v) {
            super(v);
            levelTitle = v.findViewById(R.id.icon_title);
            holder=v;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onItemClick(view,getAdapterPosition());
            selectedPosition = getAdapterPosition();
            notifyDataSetChanged();
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
        if(position==selectedPosition)
        {
            ((TextView)holder.holder.findViewById(R.id.icon_title)).setTextColor(mContext.getResources().getColor(R.color.app_background));
            holder.holder.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            ((TextView)holder.holder.findViewById(R.id.icon_title)).setTextColor(mContext.getResources().getColor(R.color.level_select_text_color));
            holder.holder.setBackgroundColor(mContext.getResources().getColor(R.color.app_background));
        }
        holder.levelTitle.setText(title.replaceAll("…",""));
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
