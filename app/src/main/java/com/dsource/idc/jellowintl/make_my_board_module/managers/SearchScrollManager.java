package com.dsource.idc.jellowintl.make_my_board_module.managers;

import android.content.Context;
import android.os.Handler;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.make_my_board_module.interfaces.PositionCallback;

public class SearchScrollManager {

    private RecyclerView recyclerView;
    private PositionCallback positionCallback;
    private Context context;
    private RecyclerView.OnScrollListener scrollListener;

    public SearchScrollManager(Context context, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.context = context;
    }

    public void scrollToPosition(int position) {
        highlightIcon(position);
        Handler handler = new Handler();
        //Remove the listeners after 1500 secs
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clearListener();
            }
        },1500);
    }

    public void onCompleteCallback(PositionCallback callback) {
        positionCallback = callback;
    }

    private void highlightIcon(int position) {
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };
        recyclerView.addOnScrollListener(getListener(position));
        smoothScroller.setTargetPosition(position);
        recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);

    }

    private RecyclerView.OnScrollListener getListener(final int index) {
        final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };

        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (itemDisplayed(index)) {
                        if (positionCallback != null)
                            positionCallback.position(index);
                    } else {
                        smoothScroller.setTargetPosition(index);
                        if (recyclerView.getLayoutManager() != null)
                            recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
                    }
                }
            }
        };

        return scrollListener;
    }

    private boolean itemDisplayed(int index) {
        int firstVisiblePos = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePos = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        if (lastVisiblePos == (index - 1))
            return true;
        return index >= firstVisiblePos && index <= lastVisiblePos;
    }

    public void clearListener() {
        recyclerView.clearOnScrollListeners();
    }


}
