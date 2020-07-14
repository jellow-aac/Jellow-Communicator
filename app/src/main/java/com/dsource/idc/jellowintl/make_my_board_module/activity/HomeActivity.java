package com.dsource.idc.jellowintl.make_my_board_module.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.activities.MainActivity;
import com.dsource.idc.jellowintl.activities.SpeechEngineBaseActivity;
import com.dsource.idc.jellowintl.make_my_board_module.adapters.HomeActivityAdapter;
import com.dsource.idc.jellowintl.make_my_board_module.datamodels.DragNDropDataProvider;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.GridSelectListener;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnItemClickListener;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnItemMoveListener;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnSelectionClearListener;
import com.dsource.idc.jellowintl.make_my_board_module.managers.ExpressiveIconManager;
import com.dsource.idc.jellowintl.make_my_board_module.managers.ModelManager;
import com.dsource.idc.jellowintl.make_my_board_module.managers.SearchScrollManager;
import com.dsource.idc.jellowintl.make_my_board_module.utility.Nomenclature;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.DialogKeyboardUtterance;
import com.dsource.idc.jellowintl.utility.LevelUiUtils;
import com.dsource.idc.jellowintl.utility.interfaces.TextToSpeechCallBacks;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.ENABLE_DROPDOWN_SPEAKER;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class HomeActivity extends SpeechEngineBaseActivity implements TextToSpeechCallBacks {

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
    private ImageView ivKeyboard;
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
        registerSpeechEngineErrorHandle(this);
        currentBoard = database.getBoardById(boardId);
        this.setTitle(currentBoard.getBoardName()+ " "+getString(R.string.board));
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
        ActivateView(ivBack, false);

        searchScrollManager = new SearchScrollManager(this, rvRecycler);
        manageKeyboard();
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            enableNavigationBack();
            setNavigationUiConditionally();
            setupActionBarTitle(View.VISIBLE, getString(R.string.home) + "/" +
                    getString(R.string.my_boards) + "/" +
                    currentBoard.getBoardName()+" "+getString(R.string.board));
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        }
        findViewById(R.id.iv_action_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitToBoardListScreen();
            }
        });
    }

    private void prepareRecyclerView() {
        rvRecycler = findViewById(R.id.recycler_view);
        adapter = new HomeActivityAdapter(new DragNDropDataProvider(displayList), this, getLevelXAdapterLayout());
        mLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setInitiateOnLongPress(mode == HomeActivityAdapter.REPOSITION_MODE);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(750);
        mRecyclerViewDragDropManager.setDragStartItemAnimationDuration(250);
        mRecyclerViewDragDropManager.setDraggingItemAlpha(0.8f);
        mRecyclerViewDragDropManager.setDraggingItemScale(1.3f);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(adapter);
        GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
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
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    showAccessibleDialog();
                }
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
            speakFromMMB(verbiage);
    }

    private void manageKeyboard() {
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogKeyboardUtterance().show(HomeActivity.this);
                speakFromMMB(getResources().getString(R.string.keyboard));
                ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
                ivKeyboard.setImageDrawable(getResources().getDrawable(R.drawable.keyboard_pressed));
            }
        });
    }

    private void prepareSpeech(JellowIcon jellowIcon) {
        selectedIconVerbiage = verbiageDatabase.getVerbiageById(jellowIcon.getVerbiageId());
        expIconManager.setAccordingVerbiage(selectedIconVerbiage);
        if (selectedIconVerbiage != null)
            speakFromMMB(selectedIconVerbiage.getSpeech_Label());
    }

    private void initViews() {
        ivKeyboard = findViewById(R.id.keyboard);
        ivKeyboard.setVisibility(View.VISIBLE);
        ivHome = findViewById(R.id.ivhome);
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivKeyboard.setImageDrawable(getResources().getDrawable(R.drawable.keyboard));
                selectedIconVerbiage = null;
                speakFromMMB(getString(R.string.home));
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
                speakFromMMB(getResources().getString(R.string.back));
                selectedIconVerbiage = null;
                onBackPressed();
            }
        });
        if(rvRecycler.getItemAnimator()!=null)
            ((SimpleItemAnimator) rvRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        int[] icons = {
                R.id.ivlike, R.id.ivyes, R.id.ivadd,
                R.id.ivdislike, R.id.ivno, R.id.ivminus,
                R.id.ivhome, R.id.ivback, R.id.keyboard
        };
        for (int icon: icons){
            ViewCompat.setAccessibilityDelegate(findViewById(icon), new TalkbackHints_SingleClick());
        }
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
            view.setAlpha(GlobalConstants.ENABLE_ALPHA);
            view.setClickable(true);
        } else {
            view.setAlpha(GlobalConstants.DISABLE_ALPHA);
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
        }, currentBoard.getGridSize());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                exitToBoardListScreen();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void exitToBoardListScreen() {
        if (getSession() != null) getSession().setCurrentBoardLanguage("");
        startActivities(new Intent[]{
                new Intent(getApplicationContext(), MainActivity.class),
                new Intent(getApplicationContext(), BoardListActivity.class)
        });
        stopSpeaking();
        finishAffinity();
    }

    private void disableLayout(boolean disable) {
        ActivateView(ivKeyboard,!disable);
        ActivateView(ivHome,!disable);
        expIconManager.disableExpressiveIcons(disable);
    }

    private void showAccessibleDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        Button enterCategory = mView.findViewById(R.id.enterCategory);
        final Button closeDialog = mView.findViewById(R.id.btnClose);
        ImageView ivLike = mView.findViewById(R.id.ivlike);
        ImageView ivYes = mView.findViewById(R.id.ivyes);
        ImageView ivAdd = mView.findViewById(R.id.ivadd);
        ImageView ivDisLike = mView.findViewById(R.id.ivdislike);
        ImageView ivNo = mView.findViewById(R.id.ivno);
        ImageView ivMinus = mView.findViewById(R.id.ivminus);
        final ImageView ivBack_ = mView.findViewById(R.id.back);
        ivBack_.setEnabled(false);
        ivBack_.setAlpha(GlobalConstants.DISABLE_ALPHA);
        final ImageView ivHome_ = mView.findViewById(R.id.home);
        final ImageView ivKeyboard_ = mView.findViewById(R.id.keyboard);
        ViewCompat.setAccessibilityDelegate(ivLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivYes, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivAdd, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivDisLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivNo, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivMinus, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivBack_, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivHome_, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivKeyboard_, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(enterCategory, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(closeDialog, new TalkbackHints_SingleClick());
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        enterCategory.setText(getString(R.string.speak));
        enterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(selectedIconVerbiage.getSpeech_Label());
            }
        });

        enterCategory.setAccessibilityDelegate(new View.AccessibilityDelegate(){
            @Override
            public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onPopulateAccessibilityEvent(host, event);
                if(event.getEventType() != AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                    mView.findViewById(R.id.txTitleHidden).
                            setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                }
            }
        });
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear all selection in current level
                clearSelectionAfterAccessibilityDialogClose();
                //dismiss dialog
                dialog.dismiss();
            }
        });

        final ImageView[] expressiveBtns = {ivLike, ivYes, ivAdd, ivDisLike, ivNo, ivMinus};
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ivlike).performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.LIKE);
            }
        });
        ivYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ivyes).performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.YES);
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ivadd).performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.MORE);
            }
        });
        ivDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ivdislike).performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.DONT_LIKE);
            }
        });
        ivNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ivno).performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.NO);
            }
        });
        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.ivminus).performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.LESS);
            }
        });
        ivBack_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ivHome_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHome.performClick();
                dialog.dismiss();
            }
        });
        ivKeyboard_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivKeyboard.performClick();
                dialog.dismiss();
            }
        });
        LevelUiUtils.setExpressiveIconConditionally(expressiveBtns, selectedIconVerbiage);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                clearSelectionAfterAccessibilityDialogClose();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2; //style id
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && isNotchDevice()) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void clearSelectionAfterAccessibilityDialogClose() {
        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home));
        expIconManager.resetSelection();
        LevelOneParent = -1;
        adapter.setSelectedPosition(-1);
        adapter.setExpIconPos(-1);
        Level = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board_home_menu, menu);
        menu.findItem(R.id.reposition_lock).setIcon(mode == HomeActivityAdapter.REPOSITION_MODE ? R.drawable.ic_unlocked : R.drawable.ic_locked);
        if(mode==HomeActivityAdapter.REPOSITION_MODE){
            adapter.setSelectedPosition(-1);
            selectedIconVerbiage =  null;
            expIconManager.resetSelection();
            expIconManager.disableExpressiveIcons(true);
            stopSpeaking();
            menu.findItem(R.id.reposition_lock).setTitle(getString(R.string.disable_reposition_icons));
        }else{
            expIconManager.disableExpressiveIcons(false);
            menu.findItem(R.id.reposition_lock).setTitle(getString(R.string.enable_reposition_icons));
        }
        return true;
    }

    private void searchInBoard() {
        Intent searchIntent = new Intent(this, BoardSearchActivity.class);
        searchIntent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.SEARCH_IN_BOARD);
        searchIntent.putExtra(BOARD_ID, currentBoard.getBoardId());
        searchIntent.putExtra(ENABLE_DROPDOWN_SPEAKER, true);
        startActivityForResult(searchIntent, SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH) {
            if (resultCode == RESULT_OK) {
                JellowIcon icon = (JellowIcon) data.getSerializableExtra(getString(R.string.search_result));
                ArrayList<Integer> iconPos = modelManager.getIconPositionInModel(icon);
                if (iconPos.size() >0) {
                    ActivateView(ivBack, Level > 0);
                    ivHome.setImageDrawable(getResources().getDrawable(R.drawable.home_pressed));
                    if (getNumberOfIconPerScreen() <= iconPos.get(0) || iconPos.get(0) < getLastVisibleItem())
                        searchScrollManager.scrollToPosition(iconPos.get(0));
                    adapter.tapSearchedItem(iconPos.get(0));
                    return;
                }
                selectedIconVerbiage = null;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
        }
        // Start measuring user app screen timer.
        startMeasuring();
    }

    @Override
    protected void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        stopSpeaking();
        super.onPause();
        // Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer.
        stopMeasuring(HomeActivity.class.getSimpleName());
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

    @Override
    public void sendSpeechEngineLanguageNotSetCorrectlyError() {}

    @Override
    public void speechEngineNotFoundError() {}

    @Override
    public void speechSynthesisCompleted() {}

    public void hideCustomKeyboardDialog() {
        ivKeyboard.setImageDrawable(getResources().getDrawable(R.drawable.keyboard));
    }
}
