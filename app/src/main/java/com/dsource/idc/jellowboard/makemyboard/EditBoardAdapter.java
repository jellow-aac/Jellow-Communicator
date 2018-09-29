package com.dsource.idc.jellowboard.makemyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowboard.GlideApp;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.SessionManager;
import com.wonshinhyo.dragrecyclerview.DragAdapter;
import com.wonshinhyo.dragrecyclerview.DragHolder;
import com.wonshinhyo.dragrecyclerview.DragRecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;


/**
 * Created by Ayaz Alam 1/8/2018
 */
public class EditBoardAdapter extends DragAdapter {

    private final int mode;
    private final int gridSize;
    private Context context;
    private ArrayList<JellowIcon> dataSource;

    public final static int DELETE_MODE=112;
    public final static int NORMAL_MODE=113;
    private OnItemDeleteListener mItemDeleteListener;


    EditBoardAdapter(Context context, ArrayList<JellowIcon> data, int mode, int gridSize) {
        super(context, data);
        this.context  =context;
        this.dataSource = data;
        this.mode =mode;
        this.gridSize = gridSize;
    }


    private final class Holder extends DragHolder {
        TextView iconTitle;
        ImageView iconImage;
        ImageView removeIcon;
        View holder;

        Holder(View view, int viewType) {
            super(view);
            iconTitle = view.findViewById(R.id.icon_title);
            iconImage = view.findViewById(R.id.icon_image_view);
            removeIcon =itemView.findViewById(R.id.icon_remove_button);
            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos!=-1)
                        mItemDeleteListener.onItemDelete(v,pos);
                }
            });
            holder =view;
        }
    }

    @Override
    public DragRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(gridSize==2)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_layout_2x1_icons, parent, false);
        else if(gridSize<4)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_layout_3_icons, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_layout_9_icons, parent, false);
        Holder holder = new Holder(itemView,viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(DragRecyclerView.ViewHolder hol, int position) {
        super.onBindViewHolder(hol, position);
        Holder holder = (Holder) hol;

        JellowIcon thisIcon = dataSource.get(position);
        holder.iconTitle.setText(thisIcon.IconTitle);
        if(thisIcon.parent0==-1)
        {
            byte[] bitmapData=thisIcon.IconImage;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(context)
                    .asBitmap()
                    .load(stream.toByteArray())
                    .apply(RequestOptions.
                            circleCropTransform()).into(holder.iconImage);
            holder.iconImage.setBackground(context.getResources().getDrawable(R.drawable.icon_back_grey));
        }
        else if(thisIcon.parent1==-1)
        {
            TypedArray mArray=context.getResources().obtainTypedArray(R.array.arrLevelOneIconAdapter);
            holder.iconImage.setImageDrawable(mArray.getDrawable(thisIcon.parent0));
        }
        else
        {
            SessionManager mSession = new SessionManager(context);
            File en_dir = context.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/drawables";
            GlideApp.with(context)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
        }

    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if(mode==NORMAL_MODE)
            holder.itemView.findViewById(R.id.icon_remove_button).setVisibility(View.GONE);
        else if(mode==DELETE_MODE)
        {
            holder.itemView.findViewById(R.id.icon_remove_button).setVisibility(View.VISIBLE);
            Animation jiggle = AnimationUtils.loadAnimation(context, R.anim.jiggle);
            holder.itemView.findViewById(R.id.icon_image_view).startAnimation(jiggle);
        }
    }

    public void setOnItemDeleteListener(final OnItemDeleteListener mItemClickListener) { this.mItemDeleteListener = mItemClickListener; }

    public interface OnItemDeleteListener {
        void onItemDelete(View view, int position);
    }

}