package com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.bean.LevelChild;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.bean.LevelParent;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;

import java.util.List;

public class LevelAdapter extends ExpandableRecyclerAdapter<LevelParent, LevelChild, ViewHolderParent,LevelChildViewHolder> {

    private int selectedParentPosition =0;
    private int selectedChildPosition = -1;
    private onLevelClickListener callback;
    private Context context;
    private List<LevelParent> items;

    public LevelAdapter(List<LevelParent> groups) {
        super(groups);
        items = groups;
    }

    @NonNull
    @Override
    public ViewHolderParent onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view = LayoutInflater.from(parentViewGroup.getContext()).inflate(R.layout.level_parent_select_card, parentViewGroup, false);
        final GenCallback<Integer> collapseCallback  = new GenCallback<Integer>() {
            @Override
            public void callBack(Integer object) {
                if(selectedParentPosition!=object)
                {
                    collapseAllParents();
                    expandParent(object);
                    selectedParentPosition=object;
                    notifyDataSetChanged();
                    onClick();
                }
            }
        };
        GenCallback<Integer> positionCallback  = new GenCallback<Integer>() {
            @Override
            public void callBack(Integer object) {
                collapseAllParents();
                notifyDataSetChanged();
                selectedParentPosition = object;
                selectedChildPosition = -1;
                onClick();
            }
        };
        return new ViewHolderParent(view,positionCallback,collapseCallback,context);
    }

    public LevelAdapter setContext(Context context){
        this.context = context;
        return this;
    }


    @Override
    public LevelChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select_card, parent, false);
        return new LevelChildViewHolder(view, new GenCallback<Integer>() {
            @Override
            public void callBack(Integer object) {
                selectedChildPosition = object;
                onClick();
            }
        });
    }

    @Override
    public void onBindParentViewHolder(@NonNull ViewHolderParent parentViewHolder, int parentPosition, @NonNull LevelParent parent) {
        parentViewHolder.setParentTitle(parent);
        if(parent.getChildList().size()==0) parentViewHolder.disableCollapse(true);
        else parentViewHolder.disableCollapse(false);
        try {
            if (parent == items.get(selectedParentPosition)) parentViewHolder.setSelected(true);
            else parentViewHolder.setSelected(false);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBindChildViewHolder(@NonNull LevelChildViewHolder childViewHolder, int parentPosition, int childPosition, @NonNull LevelChild child) {
        final LevelChild artist = items.get(parentPosition).getChildList().get(childPosition);
        childViewHolder.onBind(artist);
    }

    private void onClick(){
        notifyDataSetChanged();
        if(callback!=null)
            callback.onClick(selectedParentPosition,selectedChildPosition);
        else Log.d("ERROR","Callback not implemented");
    }

    public int getSelectedPosition() {
        return selectedParentPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        collapseAllParents();
        this.selectedParentPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public interface onLevelClickListener{
       void onClick(int parent,int child);
    }

    public void setOnClickListener(onLevelClickListener callback){
        this.callback = callback;
    }

}