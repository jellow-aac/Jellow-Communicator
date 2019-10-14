package com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.ui;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.makemyboard.MyPair;
import com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.presentors.EditAdapterCallback;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;
import com.dsource.idc.jellowintl.makemyboard.interfaces.DragAndDropListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.EditIconCallback;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.NORMAL_MODE;


public class EditRecyclerManager implements EditAdapterCallback {
    private Context context;
    private RecyclerView recycler;
    private BoardModel currentBoard;
    private ArrayList<JellowIcon> iconList;
    private GenCallback<Void> initDialog;
    private DragAndDropAdapter adapter;
    private EditIconCallback onIconEditCallback;
    private int levelOneParent = -1;
    private int Level = 0;
    private RecyclerView.OnScrollListener scrollListener;


    public EditRecyclerManager(Context context, RecyclerView recyclerView, BoardModel currentBoard){
        this.context = context;
        this.recycler = recyclerView;
        this.currentBoard=currentBoard;
        initiate();
    }

    private void initiate(){
        switch (currentBoard.getGridSize()){
            case 0:
                recycler.setLayoutManager(new GridLayoutManager(context, 1));
                break;
            case 1:
            case 3:
                recycler.setLayoutManager(new GridLayoutManager(context, 2));
                break;
            default :
                recycler.setLayoutManager(new GridLayoutManager(context, 3));
                break;
        }
        iconList = fetchIcons();
        adapter = new DragAndDropAdapter(iconList,context,currentBoard.getGridSize());
        adapter.setCallbacks(this);
/*

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new DragAndDropListener() {
            @Override
            public void onDrop(int fromPosition, int toPosition) {
                adapter.removeItem(fromPosition);
            }
        });
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recycler);*/

        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private ArrayList<JellowIcon> fetchIcons(){
        //TODO Please take a look at this function for editing purpose
        return currentBoard.getIconModel().getAllIcons();
    }

    @Override
    public void onIconClicked(int adapterPosition) {
        if(adapterPosition==0){
            initDialog.callBack(null);
        }else{
           /* TODO levelOneParent = adapterPosition;
            iconList = currentBoard.getIconModel().getChildren().get(levelOneParent).getAllIcons();
            initiate();*/
        }
    }

    public boolean goBack(){
        if(levelOneParent!=-1) {
            levelOneParent = -1;
            iconList = fetchIcons();
            initiate();
            return true;
        }
        return false;
    }

    @Override
    public void onIconEdit(int adapterPosition) {
       JellowIcon icon = iconList.get(adapterPosition);
       onIconEditCallback.onIconEditClicked(icon,adapterPosition-1);
    }

    @Override
    public void onIconRemove(int adapterPosition) {
        currentBoard.getIconModel().removeIcon(levelOneParent,adapterPosition);
        iconList.remove(adapterPosition);
        adapter.notifyItemRemoved(adapterPosition);
    }

    public void setAddIconCategoryListener(GenCallback<Void> initDialog) {
        this.initDialog = initDialog;
    }

    public void refresh() {
        initiate();
    }

    public void scrollDown() {
        recycler.smoothScrollToPosition(iconList.size()-1);
    }

    public void changeGridSize(int gridSize) {
        this.currentBoard.setGridSize(gridSize);
        initiate();
    }

    public void setOnIconEditCallback(EditIconCallback onIconGenCallback) {
        this.onIconEditCallback = onIconGenCallback;
    }

    public int remove(JellowIcon thisIcon) {
        return adapter.removeItem(thisIcon);
    }

    public void scrollToPosition(int positionInTheList) {
        recycler.smoothScrollToPosition(positionInTheList);
    }

    public void openLevel(int level) {
        if(Level!=level){
            //TODO, when category is added, change list accordingly
        }

    }

    public void highlightIcon(final MyPair<Integer, Integer>  iconPos) {
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };
        if(iconPos.getSecond()==-1)
        {
            //Level One
            if(Level!=0) {
                Level = 0;
                openLevel(Level);
            }
            recycler.addOnScrollListener(getListener(iconPos.getFirst()+1));
            smoothScroller.setTargetPosition(iconPos.getFirst()+1);
            recycler.getLayoutManager().startSmoothScroll(smoothScroller);
            adapter.setHighlightedIcon(iconPos.getFirst()+1);
        }
        else
        {
            //Level Two
            if(Level!=1) {
                Level = 1;
                openLevel(Level);
            }
            recycler.addOnScrollListener(getListener(iconPos.getSecond()+1));
            smoothScroller.setTargetPosition(iconPos.getSecond()+1);
            recycler.getLayoutManager().startSmoothScroll(smoothScroller);
            adapter.setHighlightedIcon(iconPos.getSecond()+1);

        }
    }
    private RecyclerView.OnScrollListener getListener(final int index) {
        final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };

        scrollListener =new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE)
                {
                    if(itemDisplayed(index)){
                        recycler.removeOnScrollListener(scrollListener);
                        adapter.setHighlightedIcon(-1);
                    }
                    else {smoothScroller.setTargetPosition(index); recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);}
                }
            }};

        return scrollListener;
    }
    private boolean itemDisplayed(int index) {
        int firstVisiblePos = ((GridLayoutManager) recycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePos = ((GridLayoutManager) recycler.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        if(lastVisiblePos==(index-1))
            return true;
        return index >= firstVisiblePos && index <= lastVisiblePos;
    }

}
