package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class CustomGridLayoutManager extends GridLayoutManager {
    private static final float MILLISECONDS_PER_INCH_9_ICONS = 50f;
    private static final float MILLISECONDS_PER_INCH_3_ICONS=25f;
    private Context mContext;
    private int iconCount;

    public CustomGridLayoutManager(Context context, int spanCount, int iconCount) {
        super(context,spanCount);
        mContext = context;
        this.iconCount=iconCount;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state, final int position) {

        LinearSmoothScroller smoothScroller =
                new LinearSmoothScroller(mContext) {

                    //This controls the direction in which smoothScroll looks
                    //for your view
                    @Override
                    public PointF computeScrollVectorForPosition
                    (int targetPosition) {
                        return CustomGridLayoutManager.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    //This returns the milliseconds it takes to
                    //scroll one pixel.
                    @Override
                    protected float calculateSpeedPerPixel
                    (DisplayMetrics displayMetrics) {
                        //To decide the scroll speed
                        float speed;
                        speed=MILLISECONDS_PER_INCH_9_ICONS/displayMetrics.densityDpi;
                        if(iconCount==3)
                        speed=MILLISECONDS_PER_INCH_3_ICONS/displayMetrics.densityDpi;

                        return speed;
                    }
                };

        smoothScroller.setTargetPosition(position);
        Log.d("Ayaz: MyCustomScroller","In Custom scroller pos: "+position);
        startSmoothScroll(smoothScroller);
    }



}