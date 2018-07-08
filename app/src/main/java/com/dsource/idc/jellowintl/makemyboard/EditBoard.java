package com.dsource.idc.jellowintl.makemyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.JsonDatabase.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.JsonDatabase.Sorter;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class EditBoard extends AppCompatActivity {

    ArrayList<JellowIcon> icons;
    EditBoardAdapter adapter;
    RecyclerView mRecyclerView;
    private Sorter sorter;
    private String boardId;
    public static final String BOARD_ID="Board_Id";
    private BoardDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_board);
        database=new BoardDatabase(this);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        try{
            boardId =getIntent().getExtras().getString(BOARD_ID);
        }
        catch (NullPointerException e)
        {
            Log.d("No board id found", boardId);
        }
        Board thisBoard=database.getBoardById(boardId);
        icons=thisBoard.getIconList();


        icons=sortList(icons);
        sorter=new Sorter(icons);
        leftRec();
    }

    ArrayList<JellowIcon> sortList(ArrayList<JellowIcon> iconsList)
    {
        ArrayList<JellowIcon> listOfAllIcon=new IconDataBaseHelper(this).getAllIcons();
        ArrayList<JellowIcon> sortedList=new ArrayList<>();

        for(int i=0;i<listOfAllIcon.size();i++)
        {
            if(listContainsIcon(listOfAllIcon.get(i),iconsList)) {
                sortedList.add(listOfAllIcon.get(i));
                Log.d("List:","List has "+listOfAllIcon.get(i).IconTitle);
            }

            if(sortedList.size()==iconsList.size())
                break;
        }

        return sortedList;
    }

    /***
     * This function checks whether a icon is present in the list or not
     * @param icon
     * @param list
     * @return boolean
     */
    public boolean listContainsIcon(JellowIcon icon, ArrayList<JellowIcon> list)
    {
        boolean present=false;
        for(int i=0;i<list.size();i++)
            if(list.get(i).isEqual(icon))
                present=true;
        Log.d("Selection: ","Present "+present);
        return present;
    }



    private void leftRec(){
        mRecyclerView=findViewById(R.id.icon_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        for(int i=0;i<icons.size();i++)
            Log.d("List:","FinalList "+icons.get(i).IconTitle);
        RightPainIconAdapter adapterRight=new RightPainIconAdapter(mRecyclerView,sorter.getLevelOneIcons(),this);
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