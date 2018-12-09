package com.dsource.idc.jellowboard.makemyboard.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowboard.GlideApp;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.makemyboard.interfaces.AbstractDataProvider;
import com.dsource.idc.jellowboard.makemyboard.interfaces.onRecyclerItemClick;
import com.dsource.idc.jellowboard.makemyboard.utility.DrawableUtils;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.SessionManager;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class RepositionIconAdapter extends RecyclerView.Adapter<RepositionIconAdapter.MyViewHolder> implements DraggableItemAdapter<RepositionIconAdapter.MyViewHolder> {
    private static final String TAG = "MyDraggableItemAdapter";
    private final Context mContext;
    private final int mode;
    private final int gridSize;
    private int mItemMoveMode = RecyclerViewDragDropManager.ITEM_MOVE_MODE_DEFAULT;
    private RepositionIconAdapter.OnItemDeleteListener mItemDeleteListener;

    public final static int DELETE_MODE=112;
    public final static int NORMAL_MODE=113;
    private onRecyclerItemClick onItemClickListener;
    private onItemMoveListener mOnItemMoveListener;

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

        public MyViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.frame_layout);
            iconTitle = v.findViewById(R.id.icon_title);
            iconImage = v.findViewById(R.id.icon_image_view);
            removeIcon =itemView.findViewById(R.id.icon_remove_button);
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
        mContext = context;
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
        View itemView;
        switch (gridSize){
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
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AbstractDataProvider.Data item = mProvider.getItem(position);
        holder.iconTitle.setText(mProvider.getItem(position).getText());

        JellowIcon thisIcon = (JellowIcon)mProvider.getItem(position);
        holder.iconTitle.setText(thisIcon.IconTitle);
        if(thisIcon.parent0==-1)
        {
            SessionManager mSession = new SessionManager(mContext);
            File en_dir = mContext.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/boardicon";
            GlideApp.with(mContext)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .apply(RequestOptions.
                            circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
            holder.iconImage.setBackground(mContext.getResources().getDrawable(R.drawable.icon_back_grey));
        }
        else if(thisIcon.parent1==-1)
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
            holder.itemView.findViewById(R.id.icon_remove_button).setVisibility(View.GONE);
        else if(mode==DELETE_MODE)
        {
            holder.itemView.findViewById(R.id.icon_remove_button).setVisibility(View.VISIBLE);
            Animation jiggle = AnimationUtils.loadAnimation(mContext, R.anim.jiggle);
            holder.itemView.findViewById(R.id.icon_image_view).startAnimation(jiggle);
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
