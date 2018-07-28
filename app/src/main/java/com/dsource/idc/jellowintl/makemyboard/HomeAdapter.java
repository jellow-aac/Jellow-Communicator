package com.dsource.idc.jellowintl.makemyboard;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {


    public List<JellowIcon> data;
    public Context mContext;
    private onDoubleTapListener onDoubleTapListener;
    private OnItemSelectListener onItemSelectListener;
    int GridSize;
    public int selectedPosition=-1;

    public HomeAdapter(List<JellowIcon> data, Context context, int gridSize) {
        this.data = data;
        mContext=context;
        this.GridSize=gridSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView iconTitle;
        public ImageView iconImage;

        public ViewHolder(final View v) {
            super(v);
            iconImage=v.findViewById(R.id.icon1);
            iconTitle=v.findViewById(R.id.te1);
            v.setOnClickListener(this);
            v.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        onDoubleTapListener.onItemDoubleTap(v,getAdapterPosition());
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        onItemSelectListener.onItemSelected(v,getAdapterPosition());
                        return super.onSingleTapUp(e);
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });

        }

        @Override
        public void onClick(View view) {
            onItemSelectListener.onItemSelected(view,getAdapterPosition());
        }
    }


    public interface onDoubleTapListener {
        void onItemDoubleTap(View view, int position);
    }
    public interface OnItemSelectListener {
        void onItemSelected(View view, int position);
    }



    public void setOnDoubleTapListner(final onDoubleTapListener mItemClickListener) {
        this.onDoubleTapListener = mItemClickListener;
    }

    public void setOnItemSelectListner(final HomeAdapter.OnItemSelectListener mItemClickListener) {
        this.onItemSelectListener = mItemClickListener;
    }
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(GridSize<4)
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        return new HomeAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {

        JellowIcon thisIcon = data.get(position);
        holder.iconTitle.setText(thisIcon.IconTitle);
        //TODO Highlight the icon.

        if(thisIcon.parent0==-1)
        {
            byte[] bitmapData=thisIcon.IconImage;//.getBytes(Charset.defaultCharset());
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
            holder.iconImage.setBackground(mContext.getResources().getDrawable(R.drawable.icon_back_grey));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(mContext)
                    .asBitmap()
                    .load(stream.toByteArray())
                    .apply(RequestOptions.
                            circleCropTransform()).into(holder.iconImage);

        }
        else
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
