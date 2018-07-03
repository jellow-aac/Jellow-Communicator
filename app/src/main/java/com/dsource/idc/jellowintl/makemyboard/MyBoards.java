package com.dsource.idc.jellowintl.makemyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

public class MyBoards extends AppCompatActivity {

    RecyclerView mRecyclerView;
    BoardAdapter adapter;
    ArrayList<Board> boardList;
    ImageView addBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_boards);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));


        initFields();
        prepareBoardList();


    }

    /**
     * A function to instantiate all the fields
     */
    private void initFields() {
        boardList=new ArrayList<>();
        mRecyclerView=findViewById(R.id.board_recycer_view);
        adapter=new BoardAdapter(this,boardList);
        adapter.SetOnItemClickListner(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                if(Position==(boardList.size()-1))
                {
                    boardList.add(new Board("Board 1",1,5));
                    adapter.notifyDataSetChanged();
                }
                else
                Toast.makeText(MyBoards.this,"Open "+boardList.get(Position),Toast.LENGTH_SHORT).show();

            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        notifyGridSizeChange(boardList.size());
        mRecyclerView.setAdapter(adapter);


    }
    void notifyGridSizeChange(int listSize)
    {
        if(listSize<4)
            listSize=3;
        else if(listSize>3)
            listSize=4;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,listSize));
    }

    /**
     * prepares the board list
     */
    private void prepareBoardList() {
        boardList.add(new Board("Board 1",1,5));

        boardList.add(new Board("NULL",-1,-1));
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main_with_search, menu);
        return true;
    }
}
