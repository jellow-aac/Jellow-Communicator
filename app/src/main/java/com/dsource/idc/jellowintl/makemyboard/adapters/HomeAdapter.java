package com.dsource.idc.jellowintl.makemyboard.adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.HomeActivity;
import com.dsource.idc.jellowintl.makemyboard.interfaces.onRecyclerItemClick;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.List;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {


    public List<JellowIcon> iconList;
    private onRecyclerItemClick clickListener;
    private HomeActivity mAct;
    private int gridSize;
    public int selectedPosition=-1;
    public int expIconPos = -1;
    public int highlightedIcon = -1;
    private String langCode;

    public HomeAdapter(List<JellowIcon> list, Context context, int gridSize,String langCode) {
        this.iconList = list;
        mAct = (HomeActivity) context;
        this.gridSize =gridSize;
        this.langCode = langCode;
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {

        holder.iconTitle.setTextColor(Color.rgb(64, 64, 64));
        JellowIcon thisIcon = iconList.get(position);
        holder.iconTitle.setText(thisIcon.getIconTitle());
        setMenuImageBorder(holder.backGround,false,-1);
        if(selectedPosition!=-1) highlightedIcon=-1;
        if(position==this.selectedPosition) setMenuImageBorder(holder.backGround,true,expIconPos);
        else if(highlightedIcon==position) setMenuImageBorder(holder.backGround,true,100);

        if(thisIcon.getParent0()==-1)
        {
            File en_dir = mAct.getDir(SessionManager.BOARD_ICON_LOCATION,Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath();
            GlideApp.with(mAct)
                    .load(path+"/"+ thisIcon.getIconDrawable()+".png")
                    .apply(RequestOptions.
                            circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
            holder.iconImage.setBackground(mAct.getResources().getDrawable(R.drawable.icon_back_grey));
        }
        else
        {
            GlideApp.with(mAct).load(getIconPath(mAct, thisIcon.getIconDrawable()+EXTENSION))
                    .into(holder.iconImage);
        }





    }

    public void setOnItemSelectListener(final onRecyclerItemClick mItemClickListener) {
        this.clickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final int GRID_1BY1 = 1, GRID_1BY2 = 2, GRID_1BY3 = 3,GRID_2BY2 =4;
        View rowView;
        if(mAct.isNotchDevice() && gridSize == GRID_1BY1) {
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_1_icon_notch, parent, false);
        }else if(!mAct.isNotchDevice() && gridSize == GRID_1BY1){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_1_icon, parent, false);
        }else if(mAct.isNotchDevice() && gridSize == GRID_1BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_2_icons_notch, parent, false);
        }else if(!mAct.isNotchDevice() && gridSize == GRID_1BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_2_icons, parent, false);
        }else if(mAct.isNotchDevice() && gridSize == GRID_1BY3){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons_notch, parent, false);
        }else if(mAct.isNotchDevice() && gridSize == GRID_2BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_4_icons_notch, parent, false);
        }else if(!mAct.isNotchDevice() && gridSize == GRID_2BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_4_icons, parent, false);
        }
        else if(!mAct.isNotchDevice() && gridSize == GRID_1BY3){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        }else if(mAct.isNotchDevice()){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons_notch, parent, false);
        }else{
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        }
        return new ViewHolder(rowView);
    }

    private void setMenuImageBorder(GradientDrawable gd, boolean setBorder,int pos) {

        if(setBorder){
            // mActionBtnClickCount = 0, brown color border is set.
                // mFlgImage define color of border.
            if(pos==-1) gd.setColor(ContextCompat.getColor(mAct,R.color.colorSelect));
            else switch (pos) {
                    case 0: gd.setColor(ContextCompat.getColor(mAct,R.color.colorLike)); break;
                    case 1: gd.setColor(ContextCompat.getColor(mAct,R.color.colorYes)); break;
                    case 2: gd.setColor(ContextCompat.getColor(mAct,R.color.colorMore)); break;
                    case 3: gd.setColor(ContextCompat.getColor(mAct,R.color.colorDontLike)); break;
                    case 4: gd.setColor(ContextCompat.getColor(mAct,R.color.colorNo)); break;
                    case 5: gd.setColor(ContextCompat.getColor(mAct,R.color.colorLess)); break;
                case 100:   gd.setColor(ContextCompat.getColor(mAct,R.color.search_highlight)); break;
                }
        } else
            gd.setColor(ContextCompat.getColor(mAct,android.R.color.transparent));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView iconTitle;
        ImageView iconImage;
        GradientDrawable backGround;

        public ViewHolder(final View v) {
            super(v);
            iconImage=v.findViewById(R.id.icon1);
            iconTitle=v.findViewById(R.id.te1);
            backGround = (GradientDrawable)v.findViewById(R.id.borderView).getBackground();
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(getAdapterPosition()!=selectedPosition)
                clickListener.onClick(getAdapterPosition());
            else
                clickListener.onDoubleTap(getAdapterPosition());

        }
    }


    @Override
    public int getItemCount() {
        return iconList.size();
    }



}
