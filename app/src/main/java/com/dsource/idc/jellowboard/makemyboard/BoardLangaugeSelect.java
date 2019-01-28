package com.dsource.idc.jellowboard.makemyboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.makemyboard.adapters.LanguageSelectAdapter;
import com.dsource.idc.jellowboard.makemyboard.models.Board;
import com.dsource.idc.jellowboard.utility.LanguageHelper;
import com.dsource.idc.jellowboard.utility.SessionManager;

import java.util.ArrayList;

public class BoardLangaugeSelect extends AppCompatActivity {


    ArrayList<String> LanguageList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LanguageSelectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_langauge_select);


        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+getString(R.string.icon_select_text)+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        initViews();


    }

    private void initViews(){
        mRecyclerView = findViewById(R.id.recycler_view);
        adapter  = new LanguageSelectAdapter(this);
        adapter.setOnItemClickListner(new LanguageSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),adapter.flagCountry.get(position),Toast.LENGTH_SHORT).show();;
            }
        });

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListner(new LanguageSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LanguageHelper.onAttachCustom(
                        BoardLangaugeSelect.this,
                        SessionManager.LangMap.get(adapter.mDataSource.get(position)));
                Intent i = new Intent(BoardLangaugeSelect.this,MyBoards.class);
                i.putExtra("lang",SessionManager.LangMap.get(adapter.mDataSource.get(position)));
                startActivity(i);
            }
        });
        adapter.notifyDataSetChanged();

    }

}
