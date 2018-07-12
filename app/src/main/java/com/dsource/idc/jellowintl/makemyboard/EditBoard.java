package com.dsource.idc.jellowintl.makemyboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.ModelManager;
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
    private View parent;
    AdapterEditBoard adapterRight;
    private int Level=0;
    private int LevelOneParent=-1;
    private int LevelTwoParent=-1;
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
        parent=findViewById(R.id.parent);
        currentBoard=database.getBoardById(boardId);
        getSupportActionBar().setTitle(currentBoard.boardTitle+"'s Board");
        modelManager =new ModelManager(this,currentBoard.getBoardIconModel());
        displayList= modelManager.getLevelOneFromModel();
        initFields();
        updateList(AdapterEditBoard.NORMAL_MODE);
    }

    void updateListFromDatabase()
    {
        if(Level==0)
        {
            displayList=modelManager.getLevelOneFromModel();
        }
        else if(Level==1)
        {
            displayList=modelManager.getLevelTwoFromModel(levelOneParent);
        }
        updateList(AdapterEditBoard.NORMAL_MODE);
        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,displayList.size()-1);
    }

    boolean draggedOut=false;
    int levelOneParent,levelTwoParent,levelThreeParent, fromLevel;
    private void updateList(int Mode) {
        adapterRight=new AdapterEditBoard(mRecyclerView,displayList,this,Mode,parent);
        mRecyclerView.setAdapter(adapterRight);
        adapterRight.notifyDataSetChanged();
        adapterRight.setOnItemClickListener(new AdapterEditBoard.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("ItemClicked","Item: "+position);
                notifyItemClicked(position);
            }
        });
        adapterRight.setOnItemMovedListener(new AdapterEditBoard.OnItemMovedListener() {
            @Override
            public void onItemDelete(int fromPosition, int toPosition) {
                modelManager.updateItemMoved(Level,LevelOneParent,LevelTwoParent,fromPosition,toPosition,currentBoard);
            }
        });

        if(Mode==AdapterEditBoard.DELETE_MODE)
        adapterRight.setOnItemDeleteListener(new AdapterEditBoard.OnItemDeleteListener() {
            @Override
            public void onItemDelete(View view, final int position) {
                        Log.d("Delete Mode","Callback");
                        modelManager.deleteIconFromModel(Level,LevelOneParent,LevelTwoParent,position,currentBoard);
                        displayList.remove(position);
                        updateList(AdapterEditBoard.DELETE_MODE);


            }
        });

        adapterRight.setmOnItemDraggedOutListener(new AdapterEditBoard.onItemDraggedOut() {


            @Override
            public void onIconDraggedOut(int position) {
                Log.d("Item Moved",""+position);
                draggedOut=true;
                if(Level==1)
                {
                    levelOneParent=LevelOneParent;
                    levelTwoParent=position;
                    levelThreeParent=-1;
                    fromLevel =Level;

                }
                else if(Level==2)
                {
                    levelOneParent=LevelOneParent;
                    levelTwoParent=LevelTwoParent;
                    levelThreeParent=position;
                    fromLevel =Level;

                }
                if(Level!=0)
                onBackPressed();
            }

            @Override
            public void onIconDropped() {
                if(draggedOut)
                {
                    Log.d("ItemDroped","Item Droped "+fromLevel+" "+Level+" "+levelOneParent+" "+levelTwoParent+" "+levelThreeParent);
                    modelManager.moveIconToFrom(fromLevel,Level,levelOneParent,levelTwoParent,levelThreeParent,currentBoard);
                    draggedOut=!draggedOut;
                }
                updateListFromDatabase();

            }
        });
    }

    private void initFields(){
        mRecyclerView=findViewById(R.id.icon_recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,currentBoard.getGridSize()));

        (findViewById(R.id.save_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBoard.setBoardCompleted();
                Intent intent =new Intent(EditBoard.this,BoardHome.class);
                intent.putExtra(BOARD_ID,boardId);
                startActivity(intent);
                finish();

            }
        });

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
                updateList(AdapterEditBoard.NORMAL_MODE);
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
                updateList(AdapterEditBoard.NORMAL_MODE);
            }
            else Toast.makeText(EditBoard.this,"No sub category",Toast.LENGTH_SHORT).show();
        }
        else if(Level==1){

            if(LevelOneParent!=-1) {
                ArrayList<JellowIcon> temp = modelManager.getLevelThreeFromModel(LevelOneParent, position);
                if (temp.size() > 0) {
                    displayList = temp;
                    Level++;
                    updateList(AdapterEditBoard.NORMAL_MODE);
                    LevelTwoParent=position;
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
                LevelTwoParent=-1;
                updateList(AdapterEditBoard.NORMAL_MODE);
                Level--;
            }

        }
        else if(Level==1)
        {
            displayList= modelManager.getLevelOneFromModel();
            LevelOneParent=-1;
            updateList(AdapterEditBoard.NORMAL_MODE);
            Level--;
        }
        else if(Level==0)
        {
            CustomDialog dialog=new CustomDialog(this);
            dialog.setText("Are you sure you want to exit ?");
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
            case R.id.delete_icons:
                prepareIconDeleteMode();
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

    boolean isDeleteModeOn=false;
    private void prepareIconDeleteMode() {
        if(!isDeleteModeOn)
        updateList(AdapterEditBoard.DELETE_MODE);
        else updateList(AdapterEditBoard.NORMAL_MODE);
        isDeleteModeOn=!isDeleteModeOn;
    }

    private void showGridDialog() {
        CustomDialog dialog=new CustomDialog(this,CustomDialog.GRID_SIZE);
        dialog.setGridSelectListener(new CustomDialog.GridSelectListener() {
            @Override
            public void onGridSelectListener(int size) {
                currentBoard.setGridSize(size);
                new BoardDatabase(EditBoard.this).updateBoardIntoDatabase(new DataBaseHelper(EditBoard.this).getWritableDatabase(),currentBoard);
                initFields();
            }
        });
        dialog.show();
        dialog.setCancelable(true);

        //TODO Add some codes to resize the icons
    }
}

