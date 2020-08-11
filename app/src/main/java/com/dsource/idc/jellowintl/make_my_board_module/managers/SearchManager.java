package com.dsource.idc.jellowintl.make_my_board_module.managers;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.make_my_board_module.adapters.SelectIconAdapter;
import com.dsource.idc.jellowintl.models.JellowIcon;

public class SearchManager {

    private RecyclerView iconRecycler;
    private boolean userSearchedTheIcon = false;
    private JellowIcon iconToBeSearched;


    public SearchManager(@NonNull RecyclerView iconRecycler) {
        this.iconRecycler = iconRecycler;
    }

    public void scrollToIcon(final int positionInTheList) {
        clear();
        try{
            iconRecycler.getLayoutManager().smoothScrollToPosition(iconRecycler, null, positionInTheList);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((SelectIconAdapter) iconRecycler.getAdapter()).setHighlightedIconPos(positionInTheList);
                    clear();
                }
            }, 1500);
        }catch(Exception e){
            e.printStackTrace();
            clear();
        }
    }

    public boolean isUserSearchedTheIcon() {
        return userSearchedTheIcon;
    }

    public void setSearchedIcon(JellowIcon icon) {
        if (icon == null)
            iconToBeSearched = null;
        iconToBeSearched = icon;
        userSearchedTheIcon = true;
    }

    @NonNull
    public JellowIcon getSearchedIcon() {
        return iconToBeSearched;
    }

    private void clear() {
        userSearchedTheIcon = false;
        iconToBeSearched = null;
    }
}
