package com.dsource.idc.jellowintl.makemyboard;


import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.makeramen.dragsortadapter.DragSortAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {


    public final List<JellowIcon> data;
    public Context mContext;
    public OnItemClickListener mItemClickListener;
    private View parent;
    private RecyclerView mRecyclerView;
    public HomeAdapter(List<JellowIcon> data, Context context) {

        this.data = data;
        mContext=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //each data item is just a string in this case
        public TextView iconTitle;
        public ImageView iconImage;



        public ViewHolder(View v) {
            super(v);
            iconImage=v.findViewById(R.id.icon_image_view);
            iconTitle=v.findViewById(R.id.icon_title);
            v.findViewById(R.id.icon_remove_button).setVisibility(View.GONE);
            v.setOnClickListener(this);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemClickListener.onItemClick(v,getAdapterPosition());
                    return false;
                }
            });

        }

        @Override
        public void onClick(View view) {
            onItemSelectListener.onItemClick(view,getAdapterPosition());
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemSelectListener {
        void onItemClick(View view, int position);
    }
    OnItemSelectListener onItemSelectListener;


    public void setOnItemClickListner(final HomeAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setOnItemSelectListner(final HomeAdapter.OnItemSelectListener mItemClickListener) {
        this.onItemSelectListener = mItemClickListener;
    }
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_edit_pain_card, parent, false);
        return new HomeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {

        JellowIcon thisIcon = data.get(position);
        holder.iconTitle.setText(thisIcon.IconTitle);

        if(thisIcon.parent1==-1)
        {
            TypedArray mArray=mContext.getResources().obtainTypedArray(R.array.arrLevelOneIconAdapter);
            holder.iconImage.setImageDrawable(mArray.getDrawable(thisIcon.parent0));
        }
        else
        {
            SessionManager mSession = new SessionManager(mContext);
            File en_dir = mContext.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/drawables";
            GlideApp.with(mContext)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }



}
