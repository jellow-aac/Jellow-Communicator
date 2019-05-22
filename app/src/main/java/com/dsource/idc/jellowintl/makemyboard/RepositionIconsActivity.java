package com.dsource.idc.jellowintl.makemyboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.adapters.RepositionIconAdapter;
import com.dsource.idc.jellowintl.makemyboard.interfaces.onRecyclerItemClick;
import com.dsource.idc.jellowintl.makemyboard.models.Board;
import com.dsource.idc.jellowintl.makemyboard.models.DataProvider;
import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.makemyboard.utility.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.DELETE_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.NORMAL_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.ImageStorageHelper.deleteAllCustomImage;

public class RepositionIconsActivity extends BaseActivity {

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
    private ImageView home;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNotchDevice())
            setContentView(R.layout.activity_levelx_layout_notch);
        else
            setContentView(R.layout.activity_levelx_layout);
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

        enableNavigationBack();
        setActivityTitle("Reposition icons");

        //TODO Check color to keep or remove.
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+"Reposition icons"+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);*/
        modelManager =new ModelManager(this,currentBoard.getBoardIconModel());
        displayList= modelManager.getLevelOneFromModel();
        initFields();
        defaultRecyclerParams =(RelativeLayout.LayoutParams)findViewById(R.id.recycler_view).getLayoutParams();
        updateList(NORMAL_MODE);
        ActivateView(home,true);
        ActivateView(back,false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(RepositionIconsActivity.class.getSimpleName());
    }


    private void updateList(int Mode) {
        invalidateOptionsMenu();
        adapter=new RepositionIconAdapter(new DataProvider(displayList),this,Mode,currentBoard.getGridSize());
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
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
        switch (currentBoard.getGridSize()){
            case 1:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                break;
            case 2:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case 4:mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
                break;
            case 3:
            default :
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
        }
        adapter.setOnItemClickListener(new onRecyclerItemClick() {
            @Override
            public void onClick(int pos) {
                home.setImageDrawable(getResources().getDrawable(R.drawable.home));
                ActivateView(back,true);
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
                IconModel level=modelManager.getModel();
                if(Level==0)
                    level = modelManager.getModel().getChildren().get(position);
                else if(Level==1)
                    level = modelManager.getModel().getChildren().get(LevelOneParent).getChildren().get(position);
                else if(Level==2)
                    level = modelManager.getModel().getChildren().get(LevelOneParent).getChildren()
                            .get(LevelTwoParent).getChildren().get(position);
                deleteAllCustomImage(RepositionIconsActivity.this,level);
                modelManager.deleteIconFromModel(Level,LevelOneParent,LevelTwoParent,position,currentBoard);
                displayList.remove(position);
                if(displayList.size()<1&&Level!=0)
                    onBackPressed();
                if(displayList.size()<1&&Level==0)
                {
                    Toast.makeText(getApplicationContext(),"No icons left",Toast.LENGTH_LONG).show();
                    invalidateOptionsMenu();
                }


                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initFields(){

        findViewById(R.id.save_button).setVisibility(View.VISIBLE);
        findViewById(R.id.keyboard).setAlpha(.5f);
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBoard.setBoardCompleted();
                database.updateBoardIntoDatabase(currentBoard);
                Intent intent =new Intent(RepositionIconsActivity.this, HomeActivity.class);
                intent.putExtra(BOARD_ID,boardId);
                startActivity(intent);
                finish();

            }
        });

        findViewById(R.id.et).setVisibility(View.GONE);
        findViewById(R.id.ttsbutton).setVisibility(View.GONE);


        back = findViewById(R.id.ivback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        home = findViewById(R.id.ivhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivateView(back,false);
                home.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
                if(Level!=0) {
                    displayList = modelManager.getLevelOneFromModel();
                    updateList(currentMode);
                    Level = 0;
                    LevelOneParent = -1;
                }
            }
        });


    }

    private void ActivateView(ImageView view, boolean activate) {
        if(activate)
        {
            view.setAlpha(1f);
            view.setClickable(true);
        }
        else
        {
            view.setAlpha(.5f);
            view.setClickable(false);
        }
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
            else Toast.makeText(RepositionIconsActivity.this,"No sub category",Toast.LENGTH_SHORT).show();
        }
        else if(Level==1){

            if(LevelOneParent!=-1) {
                ArrayList<JellowIcon> temp = modelManager.getLevelThreeFromModel(LevelOneParent, position);
                if (temp.size() > 0) {
                    displayList = temp;
                    Level++;
                    updateList(mode);
                    LevelTwoParent=position;
                } else Toast.makeText(RepositionIconsActivity.this, "No sub category", Toast.LENGTH_SHORT).show();

            } else Log.d("LevelOneParentNotSet","Icon"+LevelOneParent+" "+position);
        }
        else if(Level==2)
        {
            Toast.makeText(RepositionIconsActivity.this,"No sub category",Toast.LENGTH_SHORT).show();
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
            ActivateView(back,false);
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
                        startActivity(new Intent(RepositionIconsActivity.this, MyBoardsActivity.class));finish();
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
        Intent searchIntent = new Intent(this, BoardSearchActivity.class);
        searchIntent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.SEARCH_IN_BOARD);
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
        new CustomDialog(this, new CustomDialog.GridSelectListener() {
            @Override
            public void onGridSelectListener(int size) {
                currentBoard.setGridSize(size);
                if(isDeleteModeOn)
                    updateList(DELETE_MODE);
                else updateList(NORMAL_MODE);
            }
        });
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
        int firstVisiblePos = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePos = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
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

