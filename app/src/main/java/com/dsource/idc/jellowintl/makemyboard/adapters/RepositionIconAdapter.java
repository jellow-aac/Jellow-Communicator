package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.RepositionIconsActivity;
import com.dsource.idc.jellowintl.makemyboard.interfaces.AbstractDataProvider;
import com.dsource.idc.jellowintl.makemyboard.interfaces.onRecyclerItemClick;
import com.dsource.idc.jellowintl.makemyboard.utility.DrawableUtils;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.DELETE_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.NORMAL_MODE;


public class RepositionIconAdapter extends RecyclerView.Adapter<RepositionIconAdapter.MyViewHolder> implements DraggableItemAdapter<RepositionIconAdapter.MyViewHolder> {
    private static final String TAG = "MyDraggableItemAdapter";
    private RepositionIconsActivity mAct;
    private final int mode;
    private final int gridSize;
    private int mItemMoveMode = RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;
    private RepositionIconAdapter.OnItemDeleteListener mItemDeleteListener;
    private onRecyclerItemClick onItemClickListener;
    private onItemMoveListener mOnItemMoveListener;
    public int highlightIcon = -1;

    public void setOnItemClickListener(onRecyclerItemClick onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemMoveListener
    {
        void onItemMove(int to, int from);
    }

    public void setOnItemMoveListener(onItemMoveListener itemMoveListener)
    {
        this.mOnItemMoveListener = itemMoveListener;
    }

    private interface Draggable extends DraggableItemConstants {
    }

    private AbstractDataProvider mProvider;

    public class MyViewHolder extends AbstractDraggableItemViewHolder {
        public FrameLayout mContainer;
        TextView iconTitle;
        ImageView iconImage;
        ImageView removeIcon;
        View holder;
        GradientDrawable backGround;


        public MyViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.frame_layout);
            iconTitle = v.findViewById(R.id.te1);
            iconImage = v.findViewById(R.id.icon1);
            removeIcon =itemView.findViewById(R.id.delete_icons);
            backGround = (GradientDrawable)v.findViewById(R.id.borderView).getBackground();
            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos!=-1)
                        mItemDeleteListener.onItemDelete(v,pos);
                }
            });
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(getAdapterPosition());
                }
            });
            holder =v;
        }
    }

    public RepositionIconAdapter(AbstractDataProvider dataProvider, Context context, int mode, int gridSize) {
        mProvider = dataProvider;
        mAct = (RepositionIconsActivity) context;
        this.mode =mode;
        this.gridSize = gridSize;

        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    public void setItemMoveMode(int itemMoveMode) {
        mItemMoveMode = itemMoveMode;
    }

    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mProvider.getItem(position).getViewType();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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
        return new MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.iconTitle.setTextColor(Color.rgb(64, 64, 64));
        final AbstractDataProvider.Data item = mProvider.getItem(position);
        holder.iconTitle.setText(mProvider.getItem(position).getText());
        holder.backGround.setColor(ContextCompat.getColor(mAct,R.color.transparent));
        if(highlightIcon==position)
            holder.backGround.setColor(ContextCompat.getColor(mAct,R.color.search_highlight));


        JellowIcon thisIcon = (JellowIcon)mProvider.getItem(position);
        holder.iconTitle.setText(thisIcon.IconTitle);
        if(thisIcon.parent0==-1)
        {
            File en_dir = mAct.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/boardicon";
            GlideApp.with(mAct)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
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
            File en_dir = mAct.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/drawables";
            GlideApp.with(mAct)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
        }


        final int dragState = holder.getDragStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.flag_transparent;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.flag_transparent;
            } else {
                bgResId = R.drawable.flag_transparent;
            }
            holder.mContainer.setBackgroundResource(bgResId);
        }

    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if(mode==NORMAL_MODE)
            holder.removeIcon.setVisibility(View.GONE);
        else if(mode==DELETE_MODE)
        {
            holder.removeIcon.setVisibility(View.VISIBLE);
            holder.removeIcon.bringToFront();
            Animation jiggle = AnimationUtils.loadAnimation(mAct, R.anim.jiggle);
            holder.iconImage.startAnimation(jiggle);
        }
    }
    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (mItemMoveMode == RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT) {
        mProvider.moveItem(fromPosition, toPosition);
        mOnItemMoveListener.onItemMove(toPosition,fromPosition);
        }
        else{
        mProvider.swapItem(fromPosition, toPosition);
        }

    }

    @Override
    public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
        return null;
    }

    @Override
    public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {
        return true;
    }

    @Override
    public void onItemDragStarted(int position) {
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }

    public void setOnItemDeleteListener(final OnItemDeleteListener mItemClickListener) { this.mItemDeleteListener = mItemClickListener; }

    public interface OnItemDeleteListener {
        void onItemDelete(View view, int position);
    }
}
