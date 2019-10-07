package com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.ui;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.presentors.EditAdapterCallback;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;
import com.dsource.idc.jellowintl.makemyboard.interfaces.DragAndDropListener;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;


public class EditRecyclerManager implements EditAdapterCallback {
    private Context context;
    private RecyclerView recycler;
    private BoardModel currentBoard;
    private ArrayList<JellowIcon> iconList;
    private GenCallback<Void> initDialog;
    private DragAndDropAdapter adapter;
    private GenCallback<JellowIcon> onIconEditCallback;
    private int levelOneParent = -1;


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


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new DragAndDropListener() {
            @Override
            public void onDrop(int fromPosition, int toPosition) {
                adapter.removeItem(fromPosition);
            }
        });
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recycler);

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
       onIconEditCallback.callBack(icon);
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

    public void setOnIconEditCallback(GenCallback<JellowIcon> onIconGenCallback) {
        this.onIconEditCallback = onIconGenCallback;
    }

    public void remove(JellowIcon thisIcon) {
        //TODO remove the edited Icon from the list
    }
}
