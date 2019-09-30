package com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.presentors.EditAdapterCallback;
import com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.ui.activity.AddEditIconAndCategoryActivity;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;

public class DragAndDropAdapter extends RecyclerView.Adapter<DragAndDropAdapter.ViewHolder>{
    static final int TYPE_ADD_BUTTON = 33;
    static final int TYPE_CATEGORY = 11;
    static final int TYPE_NORMAL =22;

    public ArrayList<JellowIcon> icons;
    protected Context context;
    private AddEditIconAndCategoryActivity mAct;
    private final String addIconCategoryTag ="add_icon_cat";
    private int gridSize;
    private int highlightPosition=-1;
    private EditAdapterCallback callBacks;

    public DragAndDropAdapter(ArrayList<JellowIcon> icons, Context context,int gridSize) {
        this.icons = icons;
        mAct =(AddEditIconAndCategoryActivity)context;
        icons.add(0,new JellowIcon("Add Icon/Folder",addIconCategoryTag,-1,-1,-1));
        this.context = context;
        this.gridSize =gridSize;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.iconTitle.setTextColor(Color.rgb(64, 64, 64));
        final JellowIcon item = icons.get(position);
        holder.iconTitle.setText(item.getIconTitle());
        //Disable edit and remove from the add icon
        if(position==0) {
            holder.removeIcon.setVisibility(View.GONE);
            holder.editIcon.setVisibility(View.GONE);
        }else{
            holder.removeIcon.setVisibility(View.VISIBLE);
            holder.editIcon.setVisibility(View.VISIBLE);
        }
        holder.backGround.setColor(ContextCompat.getColor(mAct,R.color.transparent));

        JellowIcon thisIcon = icons.get(position);
        holder.iconTitle.setText(thisIcon.getIconTitle());
        if(thisIcon.getIconDrawable().equals(addIconCategoryTag)){
            holder.iconImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_plus));
        }else if(thisIcon.getParent0()==-1) {
            File en_dir = mAct.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath();
            GlideApp.with(mAct)
                    .load(path+"/"+ thisIcon.getIconDrawable()+".png")
                    .apply(RequestOptions.
                            circleCropTransform())
                    .placeholder(R.drawable.person_circular)
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

    @Override
    public int getItemViewType(int position) {
        if(position==0) return TYPE_ADD_BUTTON;
        else if(icons.get(position).isCategory())
            return TYPE_CATEGORY;
        else return TYPE_NORMAL;
    }

    @Override
    public DragAndDropAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int GRID_1BY1 = 1, GRID_1BY2 = 2, GRID_1BY3 = 3,GRID_2BY2=4;
        View rowView;
        if(mAct.isNotchDevice() && gridSize == GRID_1BY1) {
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_1_icon_notch, parent, false);
        }else if(!mAct.isNotchDevice() && gridSize == GRID_1BY1){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_1_icon, parent, false);
        }else if(mAct.isNotchDevice() && gridSize == GRID_1BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_2_icons_notch, parent, false);
        }else if(!mAct.isNotchDevice() && gridSize == GRID_1BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_2_icons, parent, false);
        }else if(mAct.isNotchDevice() && gridSize == GRID_2BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_4_icons_notch, parent, false);
        }else if(!mAct.isNotchDevice() && gridSize == GRID_2BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_4_icons, parent, false);
        }
        else if(mAct.isNotchDevice() && gridSize == GRID_1BY3){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons_notch, parent, false);
        }else if(!mAct.isNotchDevice() && gridSize == GRID_1BY3){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        }else if(mAct.isNotchDevice()){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons_notch, parent, false);
        }else{
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        }
        return new DragAndDropAdapter.ViewHolder(rowView);
    }

    void setCallbacks(EditAdapterCallback callBacks) {
        this.callBacks = callBacks;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FrameLayout mContainer;
        TextView iconTitle;
        ImageView iconImage;
        ImageView removeIcon;
        ImageView editIcon;
        View holder;
        GradientDrawable backGround;
        public ViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.frame_layout);
            iconTitle = v.findViewById(R.id.te1);
            iconImage = v.findViewById(R.id.icon1);
            removeIcon =itemView.findViewById(R.id.delete_icons);

            editIcon =itemView.findViewById(R.id.edit_icons);
            v.findViewById(R.id.edit_remove_container).setVisibility(View.VISIBLE);
            removeIcon.setVisibility(View.VISIBLE);
            editIcon.setVisibility(View.VISIBLE);
            backGround = (GradientDrawable)v.findViewById(R.id.borderView).getBackground();
            holder =v;

            editIcon.setOnClickListener(this);
            removeIcon.setOnClickListener(this);
            iconImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view==iconImage) callBacks.onIconClicked(getAdapterPosition());
            else if(view==editIcon) callBacks.onIconEdit(getAdapterPosition());
            else if(view==removeIcon) callBacks.onIconRemove(getAdapterPosition());

        }

        void onItemSelected() {

        }

        void onItemClear() {

        }
    }


    @Override
    public int getItemCount() {
        return icons.size();
    }



}
