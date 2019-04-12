package com.dsource.idc.jellowintl;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by HP on 12/03/2017.
 */
class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    RecyclerTouchListener(Context applicationContext, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(applicationContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if (child != null && clickListener != null) {
            clickListener.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override   public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override   public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
}