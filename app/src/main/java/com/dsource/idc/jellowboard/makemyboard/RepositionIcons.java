package com.dsource.idc.jellowboard.makemyboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.makemyboard.utility.BoardDatabase;
import com.dsource.idc.jellowboard.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowboard.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowboard.makemyboard.adapters.RepositionIconAdapter;
import com.dsource.idc.jellowboard.makemyboard.interfaces.onRecyclerItemClick;
import com.dsource.idc.jellowboard.makemyboard.models.Board;
import com.dsource.idc.jellowboard.makemyboard.models.DataProvider;
import com.dsource.idc.jellowboard.makemyboard.utility.SaveBoardOnline;
import com.dsource.idc.jellowboard.utility.CustomGridLayoutManager;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.SessionManager;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowboard.makemyboard.adapters.RepositionIconAdapter.DELETE_MODE;
import static com.dsource.idc.jellowboard.makemyboard.adapters.RepositionIconAdapter.NORMAL_MODE;

public class RepositionIcons extends AppCompatActivity {

    private static final int SEARCH = 123;
    ArrayList<JellowIcon> displayList;
    //RecyclerView mRecyclerView;
    private ModelManager modelManager;
    private String boardId;
    public static final String BOARD_ID="Board_Id";
    private BoardDatabase database;
    private int Level=0;
    private int LevelOneParent=-1;
    private int LevelTwoParent=-1;
    private Board currentBoard;
    private RecyclerView.OnScrollListener scrollListener;
    private RelativeLayout.LayoutParams defaultRecyclerParams;
    private int previousSelection = -1;
    private int currentMode;
    RecyclerView mRecyclerView;
    RepositionIconAdapter adapter;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_board_layout);
        //Disable Expressive Icons for this activity
        findViewById(R.id.expressiveOne).setAlpha(.5f);
        findViewById(R.id.expressiveTwo).setAlpha(.5f);
        //Instantiating the board database object
        database=new BoardDatabase(this);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        try{
            boardId =getIntent().getExtras().getString(BOARD_ID);
        }
        catch (NullPointerException e)
        {
            Log.d("No board id found", boardId);
        }

        currentMode = NORMAL_MODE;
        currentBoard=database.getBoardById(boardId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+"Reposition icons"+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);
        modelManager =new ModelManager(this,currentBoard.getBoardIconModel());
        displayList= modelManager.getLevelOneFromModel();
        initFields();
        defaultRecyclerParams =(RelativeLayout.LayoutParams)findViewById(R.id.recycler_view).getLayoutParams();
        updateList(NORMAL_MODE);
    }


    private void updateList(int Mode) {
        invalidateOptionsMenu();
        adapter=new RepositionIconAdapter(new DataProvider(displayList),this,Mode,currentBoard.getGridSize());
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(750);
        mRecyclerViewDragDropManager.setDragStartItemAnimationDuration(250);
        mRecyclerViewDragDropManager.setDraggingItemAlpha(0.8f);
        mRecyclerViewDragDropManager.setDraggingItemScale(1.3f);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(adapter);
        GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();//DraggableItemAnimator();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mRecyclerView.setItemAnimator(animator);
        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
        //Parameters for centering the recycler view in the layout.
        RelativeLayout.LayoutParams centeredRecyclerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        centeredRecyclerParams.addRule(RelativeLayout.ABOVE,findViewById(R.id.relativeLayoutNavigation).getId());
        centeredRecyclerParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        centeredRecyclerParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);

        int GridSize  = currentBoard.getGridSize();
        if(GridSize<4)
        {

            switch (GridSize)
            {
                case 1:
                    mRecyclerView.setLayoutParams(centeredRecyclerParams);break;
                case 2:
                    mRecyclerView.setLayoutParams(centeredRecyclerParams);break;
                case 3:
                    mRecyclerView.setLayoutParams(defaultRecyclerParams);break;
            }
            mRecyclerView.setLayoutManager(new CustomGridLayoutManager(this,currentBoard.getGridSize(),3));
        }
        else
        {
            mRecyclerView.setLayoutParams(defaultRecyclerParams);
            mRecyclerView.setLayoutManager(new CustomGridLayoutManager(this,3,9));
        }
        adapter.setOnItemClickListener(new onRecyclerItemClick() {
            @Override
            public void onClick(int pos) {
                if(previousSelection==pos) {
                    notifyItemClicked(pos,currentMode);
                }
                else previousSelection =pos;
            }
        });

        adapter.setOnItemMoveListener(
                new RepositionIconAdapter.onItemMoveListener() {
                    @Override
                    public void onItemMove(int to, int from) {
                        modelManager.updateItemMoved(Level, LevelOneParent, LevelTwoParent, from, to, currentBoard);
                    }
        });

        adapter.setOnItemDeleteListener(new RepositionIconAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(View view, int position) {
                modelManager.deleteIconFromModel(Level,LevelOneParent,LevelTwoParent,position,currentBoard);
                if(displayList.get(position).isCustomIcon())
                    deleteImageFromStorage(displayList.get(position).IconDrawable);
                displayList.remove(position);
                if(displayList.size()<1&&Level!=0)
                    onBackPressed();
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void deleteImageFromStorage(String fileID) {
        File en_dir = this.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath() + "/boardicon";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File root = new File(path);
            File file = new File(root, fileID+ ".png");
            if(file.exists())
                //noinspection ResultOfMethodCallIgnored
                file.delete();//Delete the previous image
        }

    }

    private void initFields(){

        (findViewById(R.id.save_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBoard.setBoardCompleted();

                new SaveBoardOnline(new SessionManager(RepositionIcons.this).getCaregiverNumber())
                        .upload(currentBoard, new SaveBoardOnline.onUploadedListener() {
                    @Override
                    public void onUploadComplete(int code, String msg) {
                        if(code==1) ;
                        else ;
                    }
                });
                database.updateBoardIntoDatabase(currentBoard);
                Intent intent =new Intent(RepositionIcons.this,Home.class);
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
                    updateList(currentMode);
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
            else Toast.makeText(RepositionIcons.this,"No sub category",Toast.LENGTH_SHORT).show();
        }
        else if(Level==1){

            if(LevelOneParent!=-1) {
                ArrayList<JellowIcon> temp = modelManager.getLevelThreeFromModel(LevelOneParent, position);
                if (temp.size() > 0) {
                    displayList = temp;
                    Level++;
                    updateList(mode);
                    LevelTwoParent=position;
                } else Toast.makeText(RepositionIcons.this, "No sub category", Toast.LENGTH_SHORT).show();

            } else Log.d("LevelOneParentNotSet","Icon"+LevelOneParent+" "+position);
        }
        else if(Level==2)
        {
            Toast.makeText(RepositionIcons.this,"No sub category",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {

        if(Level==2)
        {
            if(LevelOneParent!=-1) {
                displayList = modelManager.getLevelTwoFromModel(LevelOneParent);
                LevelTwoParent=-1;
                updateList(currentMode);
                Level--;
            }

        }
        else if(Level==1)
        {
            displayList= modelManager.getLevelOneFromModel();
            LevelOneParent=-1;
            updateList(currentMode);
            Level--;
        }
        else if(Level==0)
        {
            CustomDialog dialog=new CustomDialog(this,CustomDialog.NORMAL);
            dialog.setText(getString(R.string.exit_warning));
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

        if(isDeleteModeOn&&displayList.size()>0)
        {
            MenuItem item = menu.findItem(R.id.delete_icons);
            item.setIcon(R.drawable.ic_done);
        }
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
                showGridDialog();break;
            case android.R.id.home:
                CustomDialog dialog=new CustomDialog(this,CustomDialog.NORMAL);
                dialog.setText(getString(R.string.exit_warning));
                dialog.setOnPositiveClickListener(new CustomDialog.onPositiveClickListener() {
                    @Override
                    public void onPositiveClickListener() {
                        startActivity(new Intent(RepositionIcons.this,MyBoards.class));finish();
                    }
                });
                dialog.show();
                break;
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
        if(displayList.size()>0) {
            if (!isDeleteModeOn)
            {
                updateList(DELETE_MODE);
                currentMode = DELETE_MODE;
            }
            else {
                updateList(NORMAL_MODE);
                currentMode =NORMAL_MODE;
            }
            isDeleteModeOn = !isDeleteModeOn;
        }
    }

    private void showGridDialog() {
        CustomDialog dialog=new CustomDialog(this,CustomDialog.GRID_SIZE);
        dialog.setGridSelectListener(new CustomDialog.GridSelectListener() {
            @Override
            public void onGridSelectListener(int size) {
                currentBoard.setGridSize(size);
                if(isDeleteModeOn)
                updateList(DELETE_MODE);
                else updateList(NORMAL_MODE);
            }
        });
        dialog.show();
        dialog.setCancelable(true);
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

    private void highlightIcon(final ArrayList<Integer> iconPos) {
        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };
        if(iconPos.get(1)==-1)
        {
            //Level One
            if(Level==0) {
                Level = 0;
                displayList.clear();
                displayList = modelManager.getLevelOneFromModel();
                updateList(NORMAL_MODE);
            }

            mRecyclerView.addOnScrollListener(getListener(iconPos.get(0)));
            smoothScroller.setTargetPosition(iconPos.get(0));
            mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
            adapter.highlightIcon = iconPos.get(0);
        }
        else if(iconPos.get(2)==-1)
        {
            //Level Two
            Level = 1;
            displayList.clear();
            displayList =modelManager.getLevelTwoFromModel(iconPos.get(0));
            updateList(NORMAL_MODE);
            mRecyclerView.addOnScrollListener(getListener(iconPos.get(1)));
            smoothScroller.setTargetPosition(iconPos.get(1));
            mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
            adapter.highlightIcon = iconPos.get(1);

        }
        else {
           // LevelThree
            Level = 2;
            displayList.clear();
            displayList =modelManager.getLevelThreeFromModel(iconPos.get(0),iconPos.get(1));
            updateList(NORMAL_MODE);
            mRecyclerView.addOnScrollListener(getListener(iconPos.get(2)));
            smoothScroller.setTargetPosition(iconPos.get(2));
            mRecyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
            adapter.highlightIcon = iconPos.get(2);

        }
    }
    private RecyclerView.OnScrollListener getListener(final int index) {
        final RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_END;
            }
        };

        scrollListener =new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE)
                {
                    if(itemDisplayed(index)){
                        mRecyclerView.removeOnScrollListener(scrollListener);
                        adapter.highlightIcon = -1;
                    }
                    else {smoothScroller.setTargetPosition(index); recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);}
                }
            }};

        return scrollListener;
    }
    private boolean itemDisplayed(int index) {
        int firstVisiblePos = ((CustomGridLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePos = ((CustomGridLayoutManager) mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        if(lastVisiblePos==(index-1))
            return true;
        return index >= firstVisiblePos && index <= lastVisiblePos;
    }



    @Override
    protected void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }

        mLayoutManager = null;

        super.onDestroy();
    }



}

