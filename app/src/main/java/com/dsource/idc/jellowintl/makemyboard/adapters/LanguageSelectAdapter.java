package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.iDataPresenter;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class LanguageSelectAdapter {
    private Context context;
    private RecyclerView recyclerView;
    //Stores the language code for the database transactions
    private ArrayList<String> langList;
    private ArrayList<Integer> boardCount;
    private LanguageAdapter adapter;
    private GenCallback<String> callback;

    public LanguageSelectAdapter(Context context,RecyclerView recyclerView) {
        this.context  =context;
        this.recyclerView = recyclerView;
        //Retrieving all the language codes from the SessionManager Class

        ArrayList<String> mLanguageList = new ArrayList<>(Arrays.asList(LanguageFactory.getAvailableLanguages()));
        //Removing codes of non tts language
        for(String lang:SessionManager.NoTTSLang)
            mLanguageList.remove(SessionManager.LangValueMap.get(lang));
        langList = new ArrayList<>();
        for(String lang: mLanguageList)
            langList.add(SessionManager.LangMap.get(lang));


        boardCount = new ArrayList<>(langList.size()+1);
        for (int i = 0; i <= langList.size(); i++) {
            boardCount.add(0);
        }
        setUp();
        updateBoardCount();
    }

    public void updateBoardCount(){
        final BoardDatabase database = new BoardDatabase(context);
        database.getAllBoards(new iDataPresenter<ArrayList<BoardModel>>() {
            @Override
            public void onSuccess(ArrayList<BoardModel> object) {
                boardCount.set(0,object.size());
            }

            @Override
            public void onFailure(String msg) {

            }
        });

        for(int i=0;i<langList.size();i++){
            final int finalI = i;
            database.getAllBoards(langList.get(i), new iDataPresenter<ArrayList<BoardModel>>() {
                @Override
                public void onSuccess(ArrayList<BoardModel> object) {
                    boardCount.set(finalI+1,object.size());
                }

                @Override
                public void onFailure(String msg) {

                }
            });
        }
        adapter.updateBoardCount(boardCount);
        setUp();
    }
    private void setUp(){
        adapter = new LanguageAdapter(context);
        adapter.updateBoardCount(boardCount);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListner(new LevelSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(callback!=null) {
                    if (position == 0) callback.callBack("All");
                    else callback.callBack(langList.get(position - 1));
                }
            }
        });
    }

    public void setCallback(GenCallback<String> callback){
        this.callback=callback;
    }
}


class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder>{

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<String> mLanguageList;
    private ArrayList<Integer> mBoardCount;
    private LevelSelectorAdapter.OnItemClickListener mItemClickListener=null;
    private int selectedPosition = 0 ;

    public void updateBoardCount(ArrayList<Integer> boardCount) {
        mBoardCount =boardCount;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Each iconList item is just a string in this case
        TextView levelTitle;
        TextView boardCount;
        View holder;
        public ViewHolder(View v) {
            super(v);
            levelTitle = v.findViewById(R.id.icon_title);
            boardCount = v.findViewById(R.id.board_count);
            holder=v;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectedPosition = getAdapterPosition();
            mItemClickListener.onItemClick(view,getAdapterPosition());
            notifyDataSetChanged();
        }
    }



    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListner(final LevelSelectorAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    /**
     * public constructor
     * @param context
     */
    public LanguageAdapter(Context context) {
        mContext = context;
        mLanguageList = new ArrayList<>();
        mLanguageList.add(context.getResources().getString(R.string.all_boards));
        mLanguageList.addAll(Arrays.asList(LanguageFactory.getAvailableLanguages()));
        for(String lang:SessionManager.NoTTSLang)
            mLanguageList.remove(SessionManager.LangValueMap.get(lang));
    }


    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lang_list_item, parent, false);
        return new LanguageAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LanguageAdapter.ViewHolder holder, int position) {

        String title = mLanguageList.get(position);
        holder.levelTitle.setText(title);

        holder.boardCount.setText(mBoardCount.get(position).toString());
        if(position==selectedPosition)
        {
            holder.levelTitle.setTextColor(mContext.getResources().getColor(R.color.app_background));
            holder.holder.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.boardCount.setTextColor(mContext.getResources().getColor(R.color.app_background));

        }
        else
        {

            holder.boardCount.setTextColor(mContext.getResources().getColor(R.color.level_select_text_color));
            holder.levelTitle.setTextColor(mContext.getResources().getColor(R.color.level_select_text_color));
            holder.holder.setBackgroundColor(mContext.getResources().getColor(R.color.app_background));
        }
    }

    @Override
    public int getItemCount() {
        return mLanguageList.size();
    }
}
