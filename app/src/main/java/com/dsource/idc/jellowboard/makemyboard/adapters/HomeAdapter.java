package com.dsource.idc.jellowboard.makemyboard.adapters;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowboard.GlideApp;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.SessionManager;

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
    public int expIconPos = -1;

    public HomeAdapter(List<JellowIcon> data, Context context, int gridSize) {
        this.data = data;
        mContext=context;
        this.GridSize=gridSize;
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {

        JellowIcon thisIcon = data.get(position);
        holder.iconTitle.setText(thisIcon.IconTitle);
        setMenuImageBorder(holder.backGround,false,-1);
        if(position==this.selectedPosition)
        setMenuImageBorder(holder.backGround,true,expIconPos);

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;  switch (GridSize){
            case 1: //1 by 1
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_layout_1x1_icons, parent, false);
                break;
            case 2: // 1 by 2
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_layout_1x2_icons, parent, false);
                break;
            case 3: // 1 by 3
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_layout_3_icons, parent, false);break;
            case 6:// 3 by 3
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_layout_9_icons, parent, false);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.custom_layout_9_icons, parent, false);
        }
        return new ViewHolder(itemView);
    }

    private void setMenuImageBorder(GradientDrawable gd, boolean setBorder,int pos) {

        if(setBorder){
            // mActionBtnClickCount = 0, brown color border is set.
                // mFlgImage define color of border.
            if(pos==-1) gd.setColor(ContextCompat.getColor(mContext,R.color.colorSelect));
            else switch (pos) {
                    case 0: gd.setColor(ContextCompat.getColor(mContext,R.color.colorLike)); break;
                    case 1: gd.setColor(ContextCompat.getColor(mContext,R.color.colorYes)); break;
                    case 2: gd.setColor(ContextCompat.getColor(mContext,R.color.colorMore)); break;
                    case 3: gd.setColor(ContextCompat.getColor(mContext,R.color.colorDontLike)); break;
                    case 4: gd.setColor(ContextCompat.getColor(mContext,R.color.colorNo)); break;
                    case 5: gd.setColor(ContextCompat.getColor(mContext,R.color.colorLess)); break;
                }
        } else
            gd.setColor(ContextCompat.getColor(mContext,android.R.color.transparent));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView iconTitle;
        public ImageView iconImage;
        public GradientDrawable backGround;

        public ViewHolder(final View v) {
            super(v);
            iconImage=v.findViewById(R.id.icon_image_view);
            iconTitle=v.findViewById(R.id.icon_title);
            backGround = (GradientDrawable)v.findViewById(R.id.borderView).getBackground();
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(getAdapterPosition()!=selectedPosition)
                onItemSelectListener.onItemSelected(view,getAdapterPosition());
            else
                onDoubleTapListener.onItemDoubleTap(view,getAdapterPosition());

        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }



}
