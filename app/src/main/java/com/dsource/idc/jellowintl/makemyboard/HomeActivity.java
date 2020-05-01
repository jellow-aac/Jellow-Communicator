package com.dsource.idc.jellowintl.makemyboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.SpeechEngineBaseActivity;
import com.dsource.idc.jellowintl.makemyboard.activity.BoardListActivity;
import com.dsource.idc.jellowintl.makemyboard.adapters.HomeActivityAdapter;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.makemyboard.interfaces.GridSelectListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.OnItemClickListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.OnItemMoveListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.OnSelectionClearListener;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.models.DataProvider;
import com.dsource.idc.jellowintl.makemyboard.utility.ExpressiveIconManager;
import com.dsource.idc.jellowintl.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowintl.makemyboard.utility.Nomenclature;
import com.dsource.idc.jellowintl.makemyboard.utility.SearchScrollManager;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

public class HomeActivity extends SpeechEngineBaseActivity {

    private static final int SEARCH = 1221;
    private RecyclerView rvRecycler;
    private ImageView ivHome, ivBack;
    private ModelManager modelManager;
    private ArrayList<JellowIcon> displayList;
    private int Level = 0;
    private String boardId;
    private BoardDatabase database;
    private BoardModel currentBoard;
    private HomeActivityAdapter adapter;
    private int LevelOneParent = -1;
    private Icon selectedIconVerbiage;
    private TextDatabase verbiageDatabase;
    private ExpressiveIconManager expIconManager;
    private ArrayList<ExpressiveIcon> expIconVerbiage;
    private EditText etSpeechTextInput;
    private ImageView ivSpeakerButton;
    private ImageView ivKeyboard;
    private boolean isKeyboardVisible = false;
    private SearchScrollManager searchScrollManager;
    private GridLayoutManager mLayoutManager;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private int mode = HomeActivityAdapter.NORMAL_MODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);

        database = new BoardDatabase(getAppDatabase());
        try {
            if (getIntent().getExtras() != null)
                boardId = getIntent().getExtras().getString(BOARD_ID);
        } catch (NullPointerException e) {
            Log.d("No board id found", boardId);
        }

        currentBoard = database.getBoardById(boardId);
        setupToolbar();
        verbiageDatabase = new TextDatabase(this, currentBoard.getLanguage(), getAppDatabase());
        modelManager = new ModelManager(currentBoard.getIconModel());
        displayList = modelManager.getLevelOneFromModel();
        prepareRecyclerView();
        initViews();
        expIconManager = new ExpressiveIconManager(this, findViewById(R.id.parent));
        expIconManager.setClickListener(new ExpressiveIconManager.expIconClickListener() {
            @Override
            public void expressiveIconClicked(int expIconPos, int time) {
                adapter.setExpIconPos(expIconPos);
                adapter.notifyDataSetChanged();
                speakVerbiage(expIconPos, time);
            }
        });
        loadExpressiveIconVerbiage();
        //ActivateView(ivHome,false);
        ActivateView(ivBack, false);

        searchScrollManager = new SearchScrollManager(this, rvRecycler);
        manageKeyboard();
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            enableNavigationBack();
            setActivityTitle(getString(R.string.home) + "/ " +
                    getString(R.string.my_boards) + "/ " +
                    currentBoard.getBoardName());
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        }
    }

    private void prepareRecyclerView() {
        rvRecycler = findViewById(R.id.recycler_view);
        adapter = new HomeActivityAdapter(new DataProvider(displayList), this, getLevelXAdapterLayout());
        mLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setInitiateOnLongPress(mode == HomeActivityAdapter.REPOSITION_MODE);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(750);
        mRecyclerViewDragDropManager.setDragStartItemAnimationDuration(250);
        mRecyclerViewDragDropManager.setDraggingItemAlpha(0.8f);
        mRecyclerViewDragDropManager.setDraggingItemScale(1.3f);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(adapter);
        GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();//DraggableItemAnimator();
        rvRecycler.setLayoutManager(mLayoutManager);
        rvRecycler.setAdapter(mWrappedAdapter);
        rvRecycler.setItemAnimator(animator);
        mRecyclerViewDragDropManager.attachRecyclerView(rvRecycler);

        //Setting the grid layout span according to the grid size
        switch (currentBoard.getGridSize()) {
            case 0:
                rvRecycler.setLayoutManager(new GridLayoutManager(this, 1));
                break;
            case 1:
            case 3:
                rvRecycler.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            default:
                rvRecycler.setLayoutManager(new GridLayoutManager(this, 3));
                break;
        }

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                adapter.setExpIconPos(-1);
                ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
                adapter.setSelectedPosition(position);
                prepareSpeech(displayList.get(position));
            }
        });
        adapter.setOnItemMoveListener(
                new OnItemMoveListener() {
                    @Override
                    public void onItemMove(int to, int from) {
                        moveIcon(to, from);
                    }
                });

        adapter.setSelectionClearListener(new OnSelectionClearListener() {
            @Override
            public void onSelectionCleared() {
                selectedIconVerbiage = null;
                expIconManager.resetSelection();
                if(mode==HomeActivityAdapter.REPOSITION_MODE) {
                    expIconManager.disableExpressiveIcons(true);
                }
            }
        });


    }

    private void moveIcon(int to, int from) {
        currentBoard.getIconModel().move(to, from);
        database.updateBoardIntoDatabase(currentBoard);
    }

    private void loadExpressiveIconVerbiage() {
        expIconVerbiage = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            expIconVerbiage.add(verbiageDatabase.getExpressiveIconsById(Nomenclature.getNameForExpressiveIcons(i, currentBoard.getLanguage())));

    }

    private void speakVerbiage(int expIconPos, int time) {
        String verbiage = "";
        if (selectedIconVerbiage != null)
            switch (expIconPos) {
                case 0:
                    if (time == 0) verbiage = selectedIconVerbiage.getL();
                    else verbiage = selectedIconVerbiage.getLL();
                    break;
                case 1:
                    if (time == 0) verbiage = selectedIconVerbiage.getY();
                    else verbiage = selectedIconVerbiage.getYY();
                    break;
                case 2:
                    if (time == 0) verbiage = selectedIconVerbiage.getM();
                    else verbiage = selectedIconVerbiage.getMM();
                    break;
                case 3:
                    if (time == 0) verbiage = selectedIconVerbiage.getD();
                    else verbiage = selectedIconVerbiage.getDD();
                    break;
                case 4:
                    if (time == 0) verbiage = selectedIconVerbiage.getN();
                    else verbiage = selectedIconVerbiage.getNN();
                    break;
                case 5:
                    if (time == 0) verbiage = selectedIconVerbiage.getS();
                    else verbiage = selectedIconVerbiage.getSS();
                    break;

            }
        else if (time == 0) verbiage = expIconVerbiage.get(expIconPos).getL();
        else verbiage = expIconVerbiage.get(expIconPos).getLL();

        if (!verbiage.equals("NA"))
            speak(verbiage);
    }

    private void manageKeyboard() {
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(getResources().getString(R.string.keyboard));
                isKeyboardVisible = !isKeyboardVisible;
                enableKeyboardView();
            }
        });
    }

    private void enableKeyboardView() {
        if (isKeyboardVisible) {
            ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
            ivKeyboard.setImageDrawable(getResources().getDrawable(R.drawable.keyboard_pressed));
            rvRecycler.setVisibility(View.GONE);
            etSpeechTextInput.setVisibility(View.VISIBLE);
            etSpeechTextInput.requestFocus();
            ivSpeakerButton.setVisibility(View.VISIBLE);
            expIconManager.disableExpressiveIcons(true);
            selectedIconVerbiage = null;
            ActivateView(ivBack, true);
        } else {
            ivKeyboard.setImageDrawable(getResources().getDrawable(R.drawable.keyboard));
            rvRecycler.setVisibility(View.VISIBLE);
            etSpeechTextInput.setVisibility(View.GONE);
            ivSpeakerButton.setVisibility(View.GONE);
            expIconManager.disableExpressiveIcons(false);
            ActivateView(ivBack, false);
        }
        invalidateOptionsMenu();
    }

    private void prepareSpeech(JellowIcon jellowIcon) {
        selectedIconVerbiage = verbiageDatabase.getVerbiageById(jellowIcon.getVerbiageId());
        expIconManager.setAccordingVerbiage(selectedIconVerbiage);
        if (selectedIconVerbiage != null)
            speak(selectedIconVerbiage.getSpeech_Label());
    }

    private void initViews() {
        etSpeechTextInput = findViewById(R.id.et);
        etSpeechTextInput.setVisibility(View.GONE);
        ivSpeakerButton = findViewById(R.id.ttsbutton);
        ivSpeakerButton.setVisibility(View.GONE);
        ivKeyboard = findViewById(R.id.keyboard);
        ivKeyboard.setVisibility(View.VISIBLE);
        ivSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSpeechTextInput.getText().toString().equals("")) {
                    etSpeechTextInput.requestFocus();
                } else speak(etSpeechTextInput.getText().toString());
            }
        });
        ivHome = findViewById(R.id.ivhome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isKeyboardVisible = false;
                enableKeyboardView();
                selectedIconVerbiage = null;
                speak(getString(R.string.home));
                ActivateView(ivHome, true);
                ActivateView(ivBack, false);
                expIconManager.resetSelection();
                LevelOneParent = -1;
                adapter.setSelectedPosition(-1);
                adapter.setExpIconPos(-1);
                Level = 0;
                if(rvRecycler.getLayoutManager()!=null)
                    rvRecycler.getLayoutManager().smoothScrollToPosition(rvRecycler,null,0);
                ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
            }
        });
        ivBack = findViewById(R.id.ivback);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(getResources().getString(R.string.back));
                selectedIconVerbiage = null;
                if (isKeyboardVisible) {
                    isKeyboardVisible = false;
                    enableKeyboardView();
                    if (Level == 0) ActivateView(ivBack, false);
                } else onBackPressed();
            }
        });
        if(rvRecycler.getItemAnimator()!=null)
            ((SimpleItemAnimator) rvRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private int getLevelXAdapterLayout() {
        switch (currentBoard.getGridSize()) {
            case 0:
                return R.layout.layout_level_xadapter_1_icon;
            case 1:
                return R.layout.layout_level_xadapter_2_icons;
            case 2:
                return R.layout.layout_level_xadapter_3_icons;
            case 3:
                return R.layout.layout_level_xadapter_4_icons;
            default:
                return R.layout.layout_level_xadapter_9_icons;
        }
    }

    @Override
    public void onBackPressed() {

        expIconManager.resetSelection();
        adapter.setSelectedPosition(-1);
        adapter.setExpIconPos(-1);
        selectedIconVerbiage = null;
        if (Level == 2) {
            if (LevelOneParent != -1) {
                displayList = modelManager.getLevelTwoFromModel(LevelOneParent);
                adapter.setSelectedPosition(LevelOneParent);
                prepareRecyclerView();
                Level--;
            }

        } else if (Level == 1) {
            displayList = modelManager.getLevelOneFromModel();
            LevelOneParent = -1;
            prepareRecyclerView();
            Level--;
            ActivateView(ivBack, false);
        } else if (Level == 0) {
            ActivateView(ivBack, false);
            ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
        }

    }

    private void ActivateView(ImageView view, boolean activate) {
        if (activate) {
            view.setAlpha(1f);
            view.setClickable(true);
        } else {
            view.setAlpha(.5f);
            view.setClickable(false);
        }
    }

    private void showGridDialog() {
        showGridDialog(new GridSelectListener() {
            @Override
            public void onGridSelectListener(int size) {
                currentBoard.setGridSize(size);
                database.updateBoardIntoDatabase(currentBoard);
                prepareRecyclerView();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSession() != null) getSession().setCurrentBoardLanguage("");
                startActivity(new Intent(getApplicationContext(), BoardListActivity.class));
                stopSpeaking();
                finishAffinity();
                break;
            case R.id.grid_size:
                showGridDialog();
                break;
            case R.id.action_search:
                searchInBoard();
                break;
            case R.id.reposition_lock:
                mode = mode == HomeActivityAdapter.NORMAL_MODE ? HomeActivityAdapter.REPOSITION_MODE : HomeActivityAdapter.NORMAL_MODE;
                if (mode == HomeActivityAdapter.REPOSITION_MODE) {
                    Toast.makeText(this, getString(R.string.reposition_text), Toast.LENGTH_SHORT).show();
                    mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
                    disableLayout(true);
                }
                else {
                    Toast.makeText(this, getString(R.string.reposition_complete_msg), Toast.LENGTH_LONG).show();
                    mRecyclerViewDragDropManager.setInitiateOnLongPress(false);
                    disableLayout(false);
                }
                invalidateOptionsMenu();
                break;
            case R.id.action_home_app:
                startActivity(new Intent(getApplicationContext(), BoardListActivity.class));
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void disableLayout(boolean disable) {
        ActivateView(ivKeyboard,!disable);
        ActivateView(ivHome,!disable);
        expIconManager.disableExpressiveIcons(disable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isKeyboardVisible) {
            getMenuInflater().inflate(R.menu.board_home_menu, menu);
            menu.findItem(R.id.reposition_lock).setIcon(mode == HomeActivityAdapter.REPOSITION_MODE ? R.drawable.ic_unlocked : R.drawable.ic_locked);
            if(mode==HomeActivityAdapter.REPOSITION_MODE){
                adapter.setSelectedPosition(-1);
                selectedIconVerbiage =  null;
                expIconManager.resetSelection();
                expIconManager.disableExpressiveIcons(true);
                stopSpeaking();
            }else{
                expIconManager.disableExpressiveIcons(false);
            }
        }
        return true;
    }

    private void searchInBoard() {
        Intent searchIntent = new Intent(this, BoardSearchActivity.class);
        searchIntent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.SEARCH_IN_BOARD);
        searchIntent.putExtra(BOARD_ID, currentBoard.getBoardId());
        startActivityForResult(searchIntent, SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH) {
            if (resultCode == RESULT_OK) {
                JellowIcon icon = (JellowIcon) data.getSerializableExtra(getString(R.string.search_result));
                ArrayList<Integer> iconPos = modelManager.getIconPositionInModel(icon);
                if (iconPos.size() >0)
                    highlightIcon(iconPos);
                selectedIconVerbiage = null;

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void highlightIcon(final ArrayList<Integer> iconPos) {
        ActivateView(ivBack, Level > 0);
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
        adapter.setHighlightedIcon(iconPos.get(0));
        if (getNumberOfIconPerScreen() <= iconPos.get(0) || iconPos.get(0) < getLastVisibleItem())
            searchScrollManager.scrollToPosition(iconPos.get(0));
        else
            adapter.notifyDataSetChanged();
    }

    private Integer getLastVisibleItem() {
        if (rvRecycler.getLayoutManager() != null)
            return ((GridLayoutManager) rvRecycler.getLayoutManager()).findFirstVisibleItemPosition();
        return -1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpeechEngineLanguage(getSession().getCurrentBoardLanguage());
        setVisibleAct(HomeActivity.class.getSimpleName());
        if (!getSession().getToastMessage().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    getSession().getToastMessage(), Toast.LENGTH_SHORT).show();
            getSession().setToastMessage("");
        }
    }

    @Override
    protected void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        stopSpeaking();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (rvRecycler != null) {
            rvRecycler.setItemAnimator(null);
            rvRecycler.setAdapter(null);
            rvRecycler = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }

        mLayoutManager = null;

        super.onDestroy();
    }

    private int getNumberOfIconPerScreen() {
        switch (currentBoard.getGridSize()) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 8;
        }
        return 9;
    }


}
