package com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.makemyboard.adapters.LevelSelectorAdapter;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;

import java.util.ArrayList;

public class LevelManager {
    private RecyclerView recyclerView;
    private LevelSelectorAdapter adapter;
    private Context context;
    private GenCallback<Integer> positionCallback;
    private ArrayList<String> list;

    public LevelManager(@NonNull RecyclerView recyclerView,@NonNull Context context,@NonNull GenCallback<Integer> positionCallback) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.positionCallback = positionCallback;
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        setUpAdapter();
    }
    @NonNull
    public LevelManager setList(@NonNull ArrayList<String> list){
        this.list =list;
        setUpAdapter();
        return this;
    }

    private void setUpAdapter() {
        adapter = new LevelSelectorAdapter(context,list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListner(new LevelSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(positionCallback!=null)
                    positionCallback.callBack(position);
            }
        });
    }
    public boolean hideFilter(){
        return adapter.selectedPosition==0||adapter.selectedPosition==6||adapter.selectedPosition==9;
    }
    public void updateSelection(int position){
        this.adapter.selectedPosition = position;
        adapter.notifyDataSetChanged();
    }
    public int getSelectedPosition(){
        return adapter.selectedPosition;
    }

    public int getLevel(){
        return (adapter.selectedPosition-1);
    }

    public RecyclerView getRecycler(){return recyclerView;}
}
