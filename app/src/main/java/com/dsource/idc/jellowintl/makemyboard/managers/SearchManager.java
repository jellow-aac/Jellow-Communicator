package com.dsource.idc.jellowintl.makemyboard.managers;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.makemyboard.adapters.SelectIconAdapter;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.CustomGridLayoutManager;

public class SearchManager {

    private RecyclerView.OnScrollListener scrollListener;
    private RecyclerView iconRecycler;
    private int scrollCount = 0;
    private Context context;
    private ViewTreeObserver.OnGlobalLayoutListener scrollingPopulationListener;
    private boolean fromSearch = false;
    private JellowIcon iconToBeSearched;


    public SearchManager(@NonNull RecyclerView iconRecycler, @NonNull Context context) {
        this.iconRecycler = iconRecycler;
        this.context = context;
    }

    public void clear() {
        //Remove listener if already present
        iconRecycler.clearOnScrollListeners();
        if (scrollListener != null)
            iconRecycler.removeOnScrollListener(scrollListener);
        scrollListener = null;
        if (scrollingPopulationListener != null)
            iconRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(scrollingPopulationListener);
        scrollingPopulationListener = null;
        fromSearch = false;
        iconToBeSearched = null;
        scrollCount = 0;
    }

    public void addSearchedIcon(int positionInTheList) {
        clear();
        scrollCount = 0;
        Log.d("PositionToScroll", positionInTheList + " GRID: " + (gridSize() * numberOfRows()));

        if (positionInTheList > (gridSize() * numberOfRows())||positionInTheList<getFirstVisiblePosition()) {
            scrollListener = getListener(positionInTheList);
            iconRecycler.addOnScrollListener(scrollListener);
            iconRecycler.getLayoutManager().smoothScrollToPosition(iconRecycler, null, positionInTheList);
        }
        scrollListener = null;
    }

    private int getFirstVisiblePosition() {
        if (iconRecycler.getLayoutManager() != null)
            return ((GridLayoutManager) iconRecycler.getLayoutManager()).findFirstVisibleItemPosition();
        return -1;
    }

    private void setSearchHighlight(final int index) {

        scrollingPopulationListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                iconRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(scrollingPopulationListener);
                scrollingPopulationListener = null;

                if (itemDisplayed(index)) {
                    if (iconRecycler.getAdapter() instanceof SelectIconAdapter)
                        ((SelectIconAdapter) iconRecycler.getAdapter()).setHighlightedIconPos(index);
                    clear();
                } else {

                    if (scrollCount < 10) {
                        iconRecycler.getLayoutManager().smoothScrollToPosition(iconRecycler, null, index);
                        scrollCount++;
                    } else {
                        clear();
                    }
                }
            }
        };
        iconRecycler.getViewTreeObserver().addOnGlobalLayoutListener(scrollingPopulationListener);
    }

    /**
     * This function checks whether the searched item is present on the current screen,
     * for this we're just using current and last visible item and returning true and false regarding the position
     */
    private boolean itemDisplayed(int index) {
        int firstVisiblePos = ((CustomGridLayoutManager) iconRecycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePos = ((CustomGridLayoutManager) iconRecycler.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        if (lastVisiblePos == (index - 1))
            return true;

        return index >= firstVisiblePos && index <= lastVisiblePos;
    }

    private int numberOfRows() {
        return 2;
    }

    private int gridSize() {
        int gridSize = 6;
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            gridSize = 10;
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            gridSize = 9;
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            gridSize = 4;
        }

        return gridSize;
    }

    private RecyclerView.OnScrollListener getListener(final int index) {

        scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    setSearchHighlight(index);//Try highlighting the view after scrolling

            }
        };

        return scrollListener;
    }

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(JellowIcon icon) {
        clear();
        if (icon == null) {
            fromSearch = false;
            iconToBeSearched = null;
        } else {
            fromSearch = true;
            this.iconToBeSearched = icon;
        }
    }

    @NonNull
    public JellowIcon getIconToBeSearched() {
        return iconToBeSearched;
    }
}
