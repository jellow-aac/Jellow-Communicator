package com.dsource.idc.jellowintl.makemyboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.ModelManager;
import com.dsource.idc.jellowintl.utility.CustomGridLayoutManager;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class EditBoard extends AppCompatActivity {

    private static final int SEARCH = 123;
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
    private RecyclerView.OnScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_board_layout);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+"Reposition icons"+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);
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
                notifyItemClicked(position,AdapterEditBoard.NORMAL_MODE);
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
                        modelManager.deleteIconFromModel(Level,LevelOneParent,LevelTwoParent,position,currentBoard);
                        displayList.remove(position);
                        adapterRight.notifyDataSetChanged();
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
        mRecyclerView=findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new CustomGridLayoutManager(this,3,3));

        (findViewById(R.id.save_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBoard.setBoardCompleted();
                database.updateBoardIntoDatabase(new DataBaseHelper(EditBoard.this).getWritableDatabase(),currentBoard);
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
                if(Level!=0) {
                    displayList = modelManager.getLevelOneFromModel();
                    updateList(AdapterEditBoard.NORMAL_MODE);
                    Level = 0;
                    LevelOneParent = -1;
                }
            }
        });




    }

    private void notifyItemClicked(int position,int mode) {
        if(Level==0)//Level one Item clicked
        {
            ArrayList<JellowIcon> temp= modelManager.getLevelTwoFromModel(position);
            if(temp.size()>0) {
                displayList = temp;
                LevelOneParent = position;
                Level++;
                updateList(mode);
            }
            //else Toast.makeText(EditBoard.this,"No sub category",Toast.LENGTH_SHORT).show();
        }
        else if(Level==1){

            if(LevelOneParent!=-1) {
                ArrayList<JellowIcon> temp = modelManager.getLevelThreeFromModel(LevelOneParent, position);
                if (temp.size() > 0) {
                    displayList = temp;
                    Level++;
                    updateList(mode);
                    LevelTwoParent=position;
                } //else Toast.makeText(EditBoard.this, "No sub category", Toast.LENGTH_SHORT).show();

            } else Log.d("LevelOneParentNotSet","Icon"+LevelOneParent+" "+position);
        }
        else if(Level==2)
        {
           // Toast.makeText(EditBoard.this,"No sub category",Toast.LENGTH_SHORT).show();
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
            case R.id.delete_icons:
                prepareIconDeleteMode();
                break;
            case R.id.search:
                searchInBoard();
                break;
            case R.id.grid_size:
                Toast.makeText(this,"Have some issue",Toast.LENGTH_SHORT).show();
                showGridDialog();break;
            case android.R.id.home: finish(); break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void searchInBoard() {
        Intent searchIntent = new Intent(this,BoardSearch.class);
        searchIntent.putExtra(BoardSearch.SEARCH_MODE,BoardSearch.SEARCH_IN_BOARD);
        searchIntent.putExtra(BOARD_ID,currentBoard.getBoardID());
        startActivityForResult(searchIntent,SEARCH);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==SEARCH)
        {
            if(resultCode==RESULT_OK)
            {
                JellowIcon icon  = (JellowIcon)data.getSerializableExtra(getString(R.string.search_result));
                ArrayList<Integer> iconPos = modelManager.getIconPositionInModel(icon);
                if(!(iconPos.size()<1))
                    highlightIcon(iconPos);

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    ViewTreeObserver.OnGlobalLayoutListener tempListener;
    private void highlightIcon(final ArrayList<Integer> iconPos) {
        if(iconPos.get(1)==-1)
        {//Level One
            Level = 0;
            displayList.clear();
            displayList =modelManager.getLevelOneFromModel();
            updateList(AdapterEditBoard.NORMAL_MODE);
           // mRecyclerView.addOnScrollListener(getListener(iconPos.get(0)));
            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,iconPos.get(0));
           /*  tempListener= new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    View v = mRecyclerView.getChildAt(iconPos.get(0));
                    if (v != null) {
                        Animation wiggle = AnimationUtils.loadAnimation(EditBoard.this,R.anim.jiggle_determinate);
                        v.startAnimation(wiggle);
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(tempListener);
                    }
                    else
                    {
                        mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(tempListener);

                    }
                }
            };
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(tempListener);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
*/


        }
        else if(iconPos.get(2)==-1)
        {
            //Level Two
            Level = 1;
            displayList.clear();
            displayList =modelManager.getLevelTwoFromModel(iconPos.get(0));
            updateList(AdapterEditBoard.NORMAL_MODE);
            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,iconPos.get(1));

        }
        else {
           // LevelThree
            Level = 2;
            displayList.clear();
            displayList =modelManager.getLevelThreeFromModel(iconPos.get(0),iconPos.get(1));
            updateList(AdapterEditBoard.NORMAL_MODE);
            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,iconPos.get(2));

        }
    }

    /**
     * This functions returns a scroll scrollListener which triggers the setHighlight function
     * when the scrolling is done
     * @Author Ayaz Alam
     * */
    private RecyclerView.OnScrollListener getListener(final int index) {
        scrollListener =new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                    ;//Wait untill scrolling
                else if(newState==RecyclerView.SCROLL_STATE_IDLE)
                    setSearchHighlight(index);//Try highlighting the view after scrolling
            }};
        return scrollListener;
    }

    /**
     * This function is responsible for highlighting the view
     * @param pos is the postion of view to be highlighted
     * */
    ViewTreeObserver.OnGlobalLayoutListener populationDoneListener;
    public void setSearchHighlight(final int pos)
    {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        populationDoneListener=new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View searchedView = mRecyclerView.getChildAt(pos);
                if(searchedView==null) {
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,pos );
                    return;
                }


                Animation wiggle = AnimationUtils.loadAnimation(EditBoard.this,R.anim.jiggle_determinate);
                searchedView.startAnimation(wiggle);
                /*
                GradientDrawable gd = (GradientDrawable) searchedView.findViewById(R.id.borderView).getBackground();
                gd.setColor(ContextCompat.getColor(getApplicationContext(), R.color.search_highlight));*/
                Log.d("Ayaz", "Step 4: Background is set and removing the scrollListener");
                mRecyclerView.removeOnScrollListener(scrollListener);
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
            }
        };
        //Adding the scrollListener to the mRecycler to listen onPopulated callBack
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(populationDoneListener);
    }


}

