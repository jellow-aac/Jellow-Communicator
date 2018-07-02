package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class MyBoardSearchAdapter extends RecyclerView.Adapter<MyBoardSearchAdapter.ViewHolder>{

private Context mContext;
// private LayoutInflater mInflater;
private ArrayList<JellowIcon> mDataSource;
        MyBoardSearchAdapter.OnItemClickListener mItemClickListener=null;

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
    }
}



public interface OnItemClickListener {
    void onItemClick(View view, int position);
}

    public void setOnItemClickListner(final MyBoardSearchAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * public constructor
     * @param context
     * @param items
     */
    public MyBoardSearchAdapter(Context context, ArrayList<JellowIcon> items) {
        mContext = context;
        mDataSource = items;
    }


    @Override
    public MyBoardSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_search_list_item, parent, false);


        return new MyBoardSearchAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyBoardSearchAdapter.ViewHolder holder, int position) {
        JellowIcon icon = mDataSource.get(position);

    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
