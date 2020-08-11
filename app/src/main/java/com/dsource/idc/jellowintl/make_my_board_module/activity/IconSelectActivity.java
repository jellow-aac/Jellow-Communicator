package com.dsource.idc.jellowintl.make_my_board_module.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.activities.MainActivity;
import com.dsource.idc.jellowintl.make_my_board_module.adapters.SelectIconAdapter;
import com.dsource.idc.jellowintl.make_my_board_module.custom_dialogs.DialogAddBoard;
import com.dsource.idc.jellowintl.make_my_board_module.custom_dialogs.DialogCustom;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.LevelAdapter;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.datamodels.LevelParent;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnItemClickListener;
import com.dsource.idc.jellowintl.make_my_board_module.managers.LevelManager;
import com.dsource.idc.jellowintl.make_my_board_module.managers.SearchManager;
import com.dsource.idc.jellowintl.make_my_board_module.managers.SelectionManager;
import com.dsource.idc.jellowintl.make_my_board_module.models.SelectIconModel;
import com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces.ISelectPresenter;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.ISelectIconView;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.CustomGridLayoutManager;
import com.dsource.idc.jellowintl.utility.GlideApp;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.SEARCH_CODE;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class IconSelectActivity extends BaseBoardActivity<ISelectIconView, ISelectPresenter, SelectIconAdapter> implements ISelectIconView {


    private static final String LIST_OF_ICON = "list_of_icons";
    private static final String CURRENT_POSITION = "current_position";
    private LevelManager levelManager;
    private CheckBox cbSelectAll;
    private SearchManager searchScrollManager;

    @Override
    public void initViewsAndEvents() {
        searchScrollManager = new SearchManager(mRecyclerView);

        RecyclerView levelSelectRecycler = findViewById(R.id.rv_level_select);

        levelManager = new LevelManager(levelSelectRecycler, mContext, new LevelAdapter.onLevelClickListener() {
            @Override
            public void onClick(int parent, int child) {
                //Disable the checkbox if current board
                if (parent == 0) {
                    mAdapter.setCheckBoxMode(false);
                    //Call the presenter to load the data from current board
                    mPresenter.loadLevels(currentBoard);
                    Toast.makeText(mContext, getString(R.string.press_next_to_select_icons),
                            Toast.LENGTH_SHORT).show();
                } else {
                    //Enable the checkbox mode
                    mAdapter.setCheckBoxMode(true);
                    //Call the presenter to load new data
                    mPresenter.loadLevels(parent, child);
                }
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Don't do anything if current board element is pressed
                if (mPresenter.getLevel() == 0) return;
                //Check if item is already available, if yes then remove it. otherwise clear
                if (SelectionManager.getInstance().isPresent(mAdapter.getItem(position)))
                    //Clear the item from the list
                    SelectionManager.getInstance().removeIconFromList(mAdapter.getItem(position));
                else
                    //Add the item to the list
                    SelectionManager.getInstance().addIconToList(mAdapter.getItem(position));

                manageSelection();
                mAdapter.setCheckedPosition(position);
                mAdapter.notifyItemChanged(position);
            }
        });

        cbSelectAll = findViewById(R.id.cb_selectAll);

        //Select all check box
        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Select or deselect the current list according to checkbox state
                SelectionManager.getInstance().selectAll(cbSelectAll.isChecked(), mAdapter.getList());
                manageSelection();
                mAdapter.notifyDataSetChanged();
            }
        });

        //Reset button
        getView(R.id.btn_reset_selection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the view
                SelectionManager.getInstance().selectAll(false, mAdapter.getList());

                mAdapter.notifyDataSetChanged();

                manageSelection();
            }
        });

        //Next button
        getView(R.id.btn_next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addListToBoard(currentBoard, SelectionManager.getInstance().getList());
            }
        });

        getView(R.id.ll_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), DialogAddBoard.class);
                in.putExtra(BOARD_ID, currentBoard.getBoardId());
                startActivity(in);
            }
        });

        mPresenter.loadLevels(currentBoard);
        mPresenter.loadSubLevels();

    }

    private void setupHeader() {
        String boardName= currentBoard.getBoardName().length() <= 24 ?
                currentBoard.getBoardName()+" " + getString(R.string.board) :
                currentBoard.getBoardName().substring(0,24) +
                        getString(R.string.limiter)+"\n"+
                        getString(R.string.board);
        ((TextView) findViewById(R.id.board_name)).setText(boardName);
        File en_dir = mContext.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath();
        GlideApp.with(mContext)
                .load(path +"/" + currentBoard.getBoardId() + EXTENSION)
                .placeholder(R.drawable.ic_board_person)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .transform(new CircleCrop())
                .dontAnimate()
                .into(((ImageView) findViewById(R.id.board_icon)));
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private void manageSelection() {

        StringBuilder builder = new StringBuilder("(");
        builder.append(SelectionManager.getInstance().getList().size());
        builder.append(")");

        ((TextView) getView(R.id.icon_count)).setText(builder.toString());
        getView(R.id.icon_count).setContentDescription(
                SelectionManager.getInstance().getList().size()+ getString(R.string.icons_selected));

        //Disable the checkbox by default
        cbSelectAll.setChecked(false);

        if (mPresenter.getLevel() != 0) {

            setVisibility(R.id.btn_reset_selection, true);

            disableView(R.id.btn_reset_selection, true);

            //We have to enable the reset button if any of the icon of this list is present in the @SelectionManager list
            boolean enableReset = SelectionManager.getInstance().containsAny(mAdapter.getList());

            if (enableReset) {
                disableView(R.id.btn_reset_selection, false);
                //We need to check the select all checkbox if current list is sublist of the @SelectionManager list
                //and we need to do this only when reset is enabled, i.e at least one icon is present in the list already
                boolean selectAll = SelectionManager.getInstance().isSublist(mAdapter.getList());
                cbSelectAll.setChecked(selectAll);

            } else {
                disableView(R.id.btn_reset_selection, true);
                cbSelectAll.setChecked(false);
            }


        } else setVisibility(R.id.btn_reset_selection, false);

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_icon_select;
    }

    @Override
    public SelectIconAdapter getAdapter() {
        return new SelectIconAdapter(this, new ArrayList<JellowIcon>(), false);
    }

    @Override
    public ISelectPresenter createPresenter() {
        return new SelectIconModel(this, getAppDatabase(), currentBoard.getLanguage());
    }

    @Override
    public void setLayoutManager(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new CustomGridLayoutManager(mContext, gridSize(),gridSize()));
    }

    /**
     * Callback from the model once the list is loaded
     * @param list List
     */
    @Override
    public void onLevelLoaded(ArrayList<JellowIcon> list) {
        mAdapter.update(list);

        setVisibility(R.id.place_holder_text, list.isEmpty());

        if (mPresenter.getLevel() == 0) {
            setVisibility(R.id.btn_reset_selection, false);
            setVisibility(R.id.cb_selectAll, false);
            setVisibility(R.id.icon_count, false);
        } else {
            setVisibility(R.id.btn_reset_selection, true);
            setVisibility(R.id.cb_selectAll, true);
            setVisibility(R.id.icon_count, true);
        }

        if (searchScrollManager.isUserSearchedTheIcon()) {
            searchScrollManager.scrollToIcon(getPosition(searchScrollManager.getSearchedIcon()));
        }
        manageSelection();
    }

    private int getPosition(JellowIcon iconToBeSearched) {
        if(iconToBeSearched==null) return -1;
        for(int i=0;i<mAdapter.getList().size();i++)
            if(mAdapter.getList().get(i).isEqual(iconToBeSearched))
                return i;
        return -1;
    }

    @Override
    public void onSublevelLoaded(ArrayList<LevelParent> list) {
        levelManager.setList(list);
    }

    @Override
    public void onBoardSaved() {
        Intent intent = new Intent(this, AddEditActivity.class);
        intent.putExtra(BOARD_ID, currentBoard.getBoardId());
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(String msg) {
        Log.d(this.getLocalClassName(), msg);
    }

    private int gridSize() {
        int gridSize = 6;
        if ((mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            gridSize = 10;
        } else if ((mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            gridSize = 9;
        } else if ((mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            gridSize = 4;
        }

        return gridSize;
    }


    /**
     * This function saves the current state of the application, including list of Icon Selected and previous position,
     * this prevents the loss of iconList during screen orientation change and device lock.
     *
     * @param outState current state of the activity
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(LIST_OF_ICON, SelectionManager.getInstance().getList());
        outState.putInt(CURRENT_POSITION, levelManager.getSelectedPosition());
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Restore the position in case of rotation
        if (savedInstanceState != null) {
            ArrayList<JellowIcon> selectedIconList = (ArrayList<JellowIcon>) savedInstanceState.getSerializable(LIST_OF_ICON);
            SelectionManager.getInstance().setList(selectedIconList);
            levelManager.updateSelection(savedInstanceState.getInt(CURRENT_POSITION), -1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
        }
        // Start measuring user app screen timer.
        startMeasuring();
        refreshBoard();
        setupHeader();
        setupToolBar(R.string.select_icon_title);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer.
        stopMeasuring(IconSelectActivity.class.getSimpleName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_board_select_icon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent searchIntent = new Intent(this, BoardSearchActivity.class);
            searchIntent.putExtra(BOARD_ID, currentBoard.getBoardId());
            searchIntent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.NORMAL_SEARCH);
            startActivityForResult(searchIntent, SEARCH_CODE);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                JellowIcon icon = (JellowIcon) data.getExtras().getSerializable(getString(R.string.search_result));
                if (icon != null && !SelectionManager.getInstance().isPresent(icon)) {
                    SelectionManager.getInstance().addIconToList(icon);
                    searchScrollManager.setSearchedIcon(icon);
                    /*If user searched an icon from level two*/
                    if(icon.getVerbiageId().substring(6,10).equals("0000")){
                        levelManager.updateSelection((icon.getParent0() + 1), icon.getParent2());
                        levelManager.highlightSelection((icon.getParent0() + 1), icon.getParent2());
                    }else {
                        levelManager.updateSelection((icon.getParent0() + 1), icon.getParent1());
                        levelManager.highlightSelection((icon.getParent0() + 1), icon.getParent1());
                    }
                } else
                    Toast.makeText(this, getResources().getString(R.string.icon_already_present), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        final DialogCustom dialog = new DialogCustom(this);
        dialog.setText(getString(R.string.icon_select_exit_warning));
        dialog.setOnPositiveClickListener(new DialogCustom.OnPositiveClickListener() {
            @Override
            public void onPositiveClickListener() {
                SelectionManager.getInstance().delete();
                if(getSession()!=null) getSession().setCurrentBoardLanguage("");
                startActivities(new Intent[]{
                        new Intent(getApplicationContext(), MainActivity.class),
                        new Intent(getApplicationContext(), BoardListActivity.class)
                });
                finishAffinity();
            }
        });
        dialog.setOnNegativeClickListener(new DialogCustom.OnNegativeClickListener() {
            @Override
            public void onNegativeClickListener() {
                dialog.cancel();
            }
        });
        dialog.show();
    }

}
