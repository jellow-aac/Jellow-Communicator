package com.dsource.idc.jellowintl.makemyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.JsonDatabase.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.JsonDatabase.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.JsonDatabase.Sorter;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class EditBoard extends AppCompatActivity {

    ArrayList<JellowIcon> mainList;
    ArrayList<JellowIcon> displayList;
    RecyclerView mRecyclerView;
    private Sorter sorter;
    private String boardId;
    public static final String BOARD_ID="Board_Id";
    private BoardDatabase database;
    AdapterEditBoard adapterRight;
    private int Level=0;
    private int LevelOneParent=-1;

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
        mainList =thisBoard.getIconList();
        mainList =sortList(mainList);
        sorter=new Sorter(mainList,EditBoard.this);
        displayList=sorter.getLevelOneFromModel();
        initFields();
        updateList();
    }



    ArrayList<JellowIcon> sortList(ArrayList<JellowIcon> iconsList)
    {
        ArrayList<JellowIcon> listOfAllIcon=new IconDataBaseHelper(this).getAllIcons();
        ArrayList<JellowIcon> sortedList=new ArrayList<>();

        for(int i=0;i<listOfAllIcon.size();i++)
        {
            if(listContainsIcon(listOfAllIcon.get(i),iconsList))
                sortedList.add(listOfAllIcon.get(i));
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

    private void updateList()
    {
        adapterRight=new AdapterEditBoard(mRecyclerView,displayList,this);
        mRecyclerView.setAdapter(adapterRight);
        adapterRight.notifyDataSetChanged();
        adapterRight.setOnItemClickListener(new AdapterEditBoard.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("ItemClicked","Item: "+position);
                notifyItemClicked(position);
            }
        });
    }
    private void initFields(){
        mRecyclerView=findViewById(R.id.icon_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        (findViewById(R.id.ivback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        (findViewById(R.id.ivhome)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayList=sorter.getLevelOneFromModel();
                updateList();
                Level=0;
                LevelOneParent=-1;
            }
        });




    }

    private void notifyItemClicked(int position) {
        if(Level==0)//Level one Item clicked
        {
            ArrayList<JellowIcon> temp=sorter.getLevelTwoFromModel(position);
            if(temp.size()>0) {
                displayList = temp;
                LevelOneParent = position;
                Level++;
                updateList();
            }
            else Toast.makeText(EditBoard.this,"No sub category",Toast.LENGTH_SHORT).show();
        }
        else if(Level==1){

            if(LevelOneParent!=-1) {
                ArrayList<JellowIcon> temp = sorter.getLevelThreeFromModel(LevelOneParent, position);
                if (temp.size() > 0) {
                    displayList = temp;
                    Level++;
                    updateList();
                } else Toast.makeText(EditBoard.this, "No sub category", Toast.LENGTH_SHORT).show();

            } else Log.d("LevelOneParentNotSet","Icon"+LevelOneParent+" "+position);
        }
        else if(Level==2)
        {
            Toast.makeText(EditBoard.this,"No sub category",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {

        if(Level==2)
        {
            if(LevelOneParent!=-1) {
                displayList = sorter.getLevelTwoFromModel(LevelOneParent);
                updateList();
                Level--;
            }

        }
        else if(Level==1)
        {
            displayList=sorter.getLevelOneFromModel();
            LevelOneParent=-1;
            updateList();
            Level--;
        }
        else if(Level==0)
        {
            CustomDialog dialog=new CustomDialog(this);
            dialog.setText("Are you sure you want to exit");
            dialog.setOnPositiveClickListener(new CustomDialog.onPositiveClickListener() {
                @Override
                public void onPositiveClickListener() {
                    finish();
                }
            });
            dialog.show();
        }

    }

}

//TODO Now what we need to do is think what we need to do