/*package com.dsource.idc.jellow;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

*//**
 * Created by ekalpa on 5/22/2017.
 *//*

class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    GridSpacingItemDecoration() { }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        ((GridLayoutManager.LayoutParams) view.getLayoutParams()).leftMargin = 30;
        ((GridLayoutManager.LayoutParams) view.getLayoutParams()).rightMargin = 0;
        ((GridLayoutManager.LayoutParams) view.getLayoutParams()).topMargin = -30;
        ((GridLayoutManager.LayoutParams) view.getLayoutParams()).bottomMargin= -50;
    }
}*/
package com.dsource.idc.jellow;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ekalpa on 5/22/2017.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }

        /*((GridLayoutManager.LayoutParams) view.getLayoutParams()).leftMargin = 0;
        ((GridLayoutManager.LayoutParams) view.getLayoutParams()).rightMargin = 0;
        ((GridLayoutManager.LayoutParams) view.getLayoutParams()).topMargin = 0;
        ((GridLayoutManager.LayoutParams) view.getLayoutParams()).bottomMargin= 0;*/
    }
}