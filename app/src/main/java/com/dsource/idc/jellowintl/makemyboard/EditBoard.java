package com.dsource.idc.jellowintl.makemyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class EditBoard extends AppCompatActivity {

    ArrayList<JellowIcon> icons;
    EditBoardAdapter adapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_board);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        icons=(ArrayList<JellowIcon>)getIntent().getSerializableExtra("IconList");
        leftRec();
    }

    ArrayList<LeftIconPane> list;
    private void tempRec() {
    }


    private void leftRec(){
        mRecyclerView=findViewById(R.id.icon_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        RightPainIconAdapter adapterRight=new RightPainIconAdapter(mRecyclerView,icons,this);
        mRecyclerView.setAdapter(adapterRight);
        adapterRight.notifyDataSetChanged();


        (findViewById(R.id.ivback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        (findViewById(R.id.ivhome)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

//TODO Now what we need to do is think what we need to do