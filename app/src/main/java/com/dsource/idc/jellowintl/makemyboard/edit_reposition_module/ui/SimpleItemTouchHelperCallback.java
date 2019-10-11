package com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.ui;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.makemyboard.interfaces.DragAndDropListener;

class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private RecyclerView.ViewHolder previousVH = null;
    private DragAndDropListener callback;
    private int fromPosition = -1, toPosition = -1;

    public SimpleItemTouchHelperCallback(DragAndDropListener dragAndDropListener) {
        this.callback = dragAndDropListener;
    }

    @Override
    public int getBoundingBoxMargin() {
        return -20;
    }

    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return .05f;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //Don't move the first element
        if (viewHolder.getAdapterPosition() == 0 || viewHolder.getItemViewType() == DragAndDropAdapter.TYPE_CATEGORY)
            return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, 0);

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        if (previousVH != null) {
            if (previousVH instanceof DragAndDropAdapter.ViewHolder) {
                ((DragAndDropAdapter.ViewHolder) previousVH).setSelected(false);
            }
        }
        previousVH = target;

        ((DragAndDropAdapter.ViewHolder) previousVH).setSelected(true);
        if (fromPosition == -1) fromPosition = viewHolder.getAdapterPosition();
        toPosition = target.getAdapterPosition();
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
        if (target.getAdapterPosition() == 0) return false;
        return super.canDropOver(recyclerView, current, target);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof DragAndDropAdapter.ViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                DragAndDropAdapter.ViewHolder itemViewHolder = (DragAndDropAdapter.ViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
    }


    //Called when dragging is done and we need to bring everything back to normal
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof DragAndDropAdapter.ViewHolder) {
            // Tell the view holder it's time to restore the idle state
            DragAndDropAdapter.ViewHolder itemViewHolder = (DragAndDropAdapter.ViewHolder) viewHolder;
            itemViewHolder.onItemClear();
            if (previousVH != null)
                ((DragAndDropAdapter.ViewHolder) previousVH)
                        .setSelected(false);
        }

        if (fromPosition != -1 && toPosition != -1 && fromPosition != toPosition)
            callback.onDrop(fromPosition, toPosition);
        fromPosition = toPosition = -1;
    }

    @Override
    public int interpolateOutOfBoundsScroll(@NonNull RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
        Log.d("Scrolled out: ", "Scrolled out");
        return super.interpolateOutOfBoundsScroll(recyclerView, viewSize, viewSizeOutOfBounds, totalSize, msSinceStartScroll);
    }
}
