package com.dsource.idc.jellowintl.makemyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.ModelManager;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class EditBoard extends AppCompatActivity {

    ArrayList<JellowIcon> mainList;
    ArrayList<JellowIcon> displayList;
    RecyclerView mRecyclerView;
    private ModelManager modelManager;
    private String boardId;
    public static final String BOARD_ID="Board_Id";
    private BoardDatabase database;
    AdapterEditBoard adapterRight;
    private int Level=0;
    private int LevelOneParent=-1;
    private Board currentBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_board);
        database=new BoardDatabase(this);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        try{
            boardId =getIntent().getExtras().getString(BOARD_ID);
        }
        catch (NullPointerException e)
        {
            Log.d("No board id found", boardId);
        }

        currentBoard=database.getBoardById(boardId);
        modelManager =new ModelManager(this,currentBoard.getBoardIconModel());
        displayList= modelManager.getLevelOneFromModel();
        initFields();
        updateList();
    }

    /**
     * This function sorts the Icon list as they are present in the hierarchy
     * @param iconsList
     * @return
     */
    ArrayList<JellowIcon> sortList(ArrayList<JellowIcon> iconsList) {
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
    public boolean listContainsIcon(JellowIcon icon, ArrayList<JellowIcon> list) {
        boolean present=false;
        for(int i=0;i<list.size();i++)
            if(list.get(i).isEqual(icon))
                present=true;
        Log.d("Selection: ","Present "+present);
        return present;
    }

    private void updateList() {
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,currentBoard.getGridSize()));
        (findViewById(R.id.ivback)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        (findViewById(R.id.ivhome)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayList= modelManager.getLevelOneFromModel();
                updateList();
                Level=0;
                LevelOneParent=-1;
            }
        });




    }

    private void notifyItemClicked(int position) {
        if(Level==0)//Level one Item clicked
        {
            ArrayList<JellowIcon> temp= modelManager.getLevelTwoFromModel(position);
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
                ArrayList<JellowIcon> temp = modelManager.getLevelThreeFromModel(LevelOneParent, position);
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
                displayList = modelManager.getLevelTwoFromModel(LevelOneParent);
                updateList();
                Level--;
            }

        }
        else if(Level==1)
        {
            displayList= modelManager.getLevelOneFromModel();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_board_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO add action
            case R.id.delete_boards:
                break;
            case R.id.search:
                break;
            case R.id.grid_size:
                showGridDialog();break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showGridDialog() {
        CustomDialog dialog=new CustomDialog(this,CustomDialog.GRID_SIZE);
        dialog.setGridSelectListener(new CustomDialog.GridSelectListener() {
            @Override
            public void onGridSelectListener(int size) {
                currentBoard.setGridSize(size);
                initFields();
            }
        });
        dialog.show();

        //TODO Add some codes to resize the icons
    }
}

