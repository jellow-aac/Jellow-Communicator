package com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.datamodels.LevelChild;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.datamodels.LevelParent;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.GenCallback;

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
                    selectedChildPosition = -1;
                    notifyDataSetChanged();
                    onClick();
                }
            }
        };
        return new ViewHolderParent(view, collapseCallback,context);
    }

    @Override
    public LevelChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select_card, parent, false);
        return new LevelChildViewHolder(view, new GenCallback<Integer>() {
            @Override
            public void callBack(Integer object) {
                selectedChildPosition = object;
                onClick();
            }
        }, context);
    }

    @Override
    public void onBindParentViewHolder(@NonNull ViewHolderParent parentViewHolder, int parentPosition, @NonNull LevelParent parent) {
        parentViewHolder.setParentTitle(parent);
        if (parentPosition > 0){
            parentViewHolder.makeTextBold(false);
        }else{
            parentViewHolder.makeTextBold(true);
        }
        if(parent.getChildList().size()==0){
            parentViewHolder.disableCollapse(true);
        }
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
        TextView artistName = childViewHolder.itemView.findViewById(R.id.icon_title);
        RelativeLayout rl = childViewHolder.itemView.findViewById(R.id.relative_layout);
        if (childPosition != selectedChildPosition) {
            rl.setBackgroundColor(context.getResources().getColor(R.color.app_background));
            artistName.setTextColor(context.getResources().getColor(R.color.level_select_text_color));
        }else{
            rl.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            artistName.setTextColor(context.getResources().getColor(R.color.app_background));
        }
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

    public void expandTheListAtPosition(int parent0, int parent1){
        expandParent(parent0);
        selectedChildPosition = parent1;
    }

    public LevelAdapter setContext(Context context){
        this.context = context;
        return this;
    }

}