package com.dsource.idc.jellowintl.make_my_board_module.managers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.LevelAdapter;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.datamodels.LevelParent;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private RecyclerView recyclerView;

    private LevelAdapter adapter;

    private Context context;

    private LevelAdapter.onLevelClickListener  positionCallback;

    private ArrayList<LevelParent> list;

    public LevelManager(@NonNull RecyclerView recyclerView,@NonNull Context context,@NonNull LevelAdapter.onLevelClickListener positionCallback) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.positionCallback = positionCallback;
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        setUpAdapter();
    }
    @NonNull
    public LevelManager setList(@NonNull ArrayList<LevelParent> list){
        this.list =list;
        setUpAdapter();
        return this;
    }

    private void setUpAdapter() {
        List<LevelParent> genres = list;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        //instantiate your adapter with the list of genres
        adapter = new LevelAdapter(genres)
                            .setContext(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new LevelAdapter.onLevelClickListener() {
            @Override
            public void onClick(int parent, int child) {
                if(positionCallback!=null)
                    positionCallback.onClick(parent,child);
            }
        });
    }

    public void updateSelection(int parent0, int parent1){
        this.adapter.setSelectedPosition(parent0);
        if(positionCallback!=null)
            positionCallback.onClick(parent0, parent1);
    }
    public void highlightSelection(int parent0, int parent1) {
        adapter.expandTheListAtPosition(parent0, parent1);
    }

    public int getSelectedPosition(){
        return adapter.getSelectedPosition();
    }

    public int getLevel(){
        return (adapter.getSelectedPosition()-1);
    }

    public RecyclerView getRecycler(){return recyclerView;}
}
