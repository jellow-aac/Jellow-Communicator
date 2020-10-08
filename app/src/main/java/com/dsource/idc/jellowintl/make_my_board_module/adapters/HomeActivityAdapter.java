package com.dsource.idc.jellowintl.make_my_board_module.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.AbstractDataProvider;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnItemClickListener;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnItemMoveListener;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnSelectionClearListener;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.GlideApp;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.io.File;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;

public class HomeActivityAdapter extends RecyclerView.Adapter<HomeActivityAdapter.MyViewHolder> implements DraggableItemAdapter<HomeActivityAdapter.MyViewHolder> {

    public static final int REPOSITION_MODE = 221;
    public final static int NORMAL_MODE = 113;
    private static final int[] EMPTY_STATE = new int[] {};
    private Context mContext;
    private int layoutResId;
    private OnItemClickListener onItemClickListener;
    private OnItemMoveListener mOnItemMoveListener;
    private int expIconPos = -1;
    private int selectedPosition = -1;
    private AbstractDataProvider mProvider;
    private OnSelectionClearListener resetVerbiageCallback;

    public HomeActivityAdapter(AbstractDataProvider dataProvider, Context context, int layoutResId) {
        mProvider = dataProvider;
        mContext = context;
        this.layoutResId = layoutResId;
        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final JellowIcon thisIcon = (JellowIcon) mProvider.getItem(position);

        String iconTitle = thisIcon.getIconTitle().length() <= 24 ?
                thisIcon.getIconTitle() :
                thisIcon.getIconTitle().substring(0,24)+ mContext.getString(R.string.limiter);
        holder.iconTitle.setText(iconTitle);
        holder.mContainer.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        holder.parent.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        if (thisIcon.isCustomIcon())
            setImageFromBoard(holder.iconImage, thisIcon.getIconDrawable());
        else
            setImageFromLibrary(holder.iconImage, thisIcon.getIconDrawable());

        final int dragState = holder.getDragStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.flag_transparent;
                // need to clear drawable state here to get correct appearance of the dragging item.
                if (holder.mContainer.getForeground() != null) {
                    holder.mContainer.getForeground().setState(EMPTY_STATE);
                }
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.flag_transparent;
            } else {
                bgResId = R.drawable.flag_transparent;
            }
            holder.mContainer.setBackgroundResource(bgResId);
        }

        //Reset the border
        setMenuImageBorder(holder, false, -1);

        //Set the selected position colour
        if (selectedPosition == position) {
            setMenuImageBorder(holder, true, expIconPos);
        }  else setMenuImageBorder(holder,false, -1);
    }

    public void setSelectedPosition(int selectedPosition) {
        //Reset the last position
        if (this.selectedPosition != -1 && selectedPosition != -1) {
            int lastPosition = this.selectedPosition;
            this.selectedPosition = selectedPosition;
            //Resetting last position
            notifyItemChanged(lastPosition);
        } else if(selectedPosition==-1&&this.selectedPosition!=-1){
            selectedPosition = this.selectedPosition;
            this.selectedPosition = -1;
            notifyItemChanged(selectedPosition);
        }else
            this.selectedPosition = selectedPosition;

        if (selectedPosition != -1) notifyItemChanged(selectedPosition);
    }

    private void setMenuImageBorder(MyViewHolder viewHolder, boolean setBorder, int pos) {

        GradientDrawable gd = viewHolder.backGround;
        if (gd == null)
            return;
        if (setBorder) {
            switch (pos) {
                case -1:
                    gd.setColor(ContextCompat.getColor(mContext, R.color.colorSelect));
                    break;
                case 0:
                    gd.setColor(ContextCompat.getColor(mContext, R.color.colorLike));
                    break;
                case 1:
                    gd.setColor(ContextCompat.getColor(mContext, R.color.colorYes));
                    break;
                case 2:
                    gd.setColor(ContextCompat.getColor(mContext, R.color.colorMore));
                    break;
                case 3:
                    gd.setColor(ContextCompat.getColor(mContext, R.color.colorDontLike));
                    break;
                case 4:
                    gd.setColor(ContextCompat.getColor(mContext, R.color.colorNo));
                    break;
                case 5:
                    gd.setColor(ContextCompat.getColor(mContext, R.color.colorLess));
                    break;
            }
        } else
            gd.setColor(ContextCompat.getColor(mContext, android.R.color.transparent));
    }

    private void setImageFromBoard(ImageView imageView, String imageURL) {
        File en_dir = mContext.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath();
        GlideApp.with(mContext)
                .load(path + "/" + imageURL + ".png")
                .placeholder(R.drawable.ic_icon_placeholder)
                .into(imageView);
    }

    private void setImageFromLibrary(ImageView imageView, String drawableId) {
        GlideApp.with(mContext).load(getIconPath(mContext, drawableId + EXTENSION))
                .placeholder(R.drawable.ic_icon_placeholder)
                .into(imageView);
    }

    public void setExpIconPos(int expIconPos) {
        this.expIconPos = expIconPos;
    }

    public void setSelectionClearListener(OnSelectionClearListener selectionClearListener){
        this.resetVerbiageCallback = selectionClearListener;
    }

    public void tapSearchedItem(int iconPos) {
        onItemClickListener.onItemClick(iconPos);
    }

    @NonNull @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new MyViewHolder(itemView);
    }

    class MyViewHolder extends AbstractDraggableItemViewHolder {
        FrameLayout mContainer;
        TextView iconTitle;
        ImageView iconImage;
        LinearLayout parent;
        View holder;
        GradientDrawable backGround;


        MyViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.frame_layout);
            iconTitle = v.findViewById(R.id.te1);
            iconImage = v.findViewById(R.id.icon1);
            backGround = (GradientDrawable) v.findViewById(R.id.borderView).getBackground();
            parent = v.findViewById(R.id.linearlayout_icon1);
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });

            holder = v;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemMoveListener(OnItemMoveListener itemMoveListener) {
        this.mOnItemMoveListener = itemMoveListener;
    }

    private interface Draggable extends DraggableItemConstants {
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
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        mProvider.moveItem(fromPosition, toPosition);
        mOnItemMoveListener.onItemMove(toPosition, fromPosition);
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
        selectedPosition = -1;
        expIconPos = -1;
        if(resetVerbiageCallback !=null)
            resetVerbiageCallback.onSelectionCleared();
        notifyDataSetChanged();
    }

    @Override
    public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
        notifyDataSetChanged();
    }
}
