package com.dsource.idc.jellowintl.make_my_board_module.activity;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.adapters.BoardAdapter;
import com.dsource.idc.jellowintl.make_my_board_module.custom_dialogs.DialogAddBoard;
import com.dsource.idc.jellowintl.make_my_board_module.custom_dialogs.DialogCustom;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.BoardClickListener;
import com.dsource.idc.jellowintl.make_my_board_module.managers.SelectionManager;
import com.dsource.idc.jellowintl.make_my_board_module.models.BoardListModel;
import com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces.IBoardListPresenter;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.IBoardListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class BoardListActivity extends BaseBoardActivity<IBoardListView, IBoardListPresenter, BoardAdapter> implements IBoardListView, BoardClickListener {
    public static final boolean EDIT_ENABLED = true;
    public static final boolean EDIT_DISABLED = false;
    private boolean editMode = EDIT_DISABLED;
    private MenuItem editMenu;

    @Override
    public int getLayoutId() {
        return R.layout.activity_board_list;
    }

    @Override
    public BoardAdapter getAdapter() {
        return new BoardAdapter(mContext, R.layout.my_board_card, new ArrayList<BoardModel>());
    }

    @Override
    public void initViewsAndEvents() {
        mPresenter.loadBoards("All");
        mAdapter.setOnItemClickListener(this);
        enableNavigationBack();
        setupActionBarTitle(View.VISIBLE, getString(R.string.home) + "/ "+ getString(R.string.menuMyBoards));
        setNavigationUiConditionally();
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        findViewById(R.id.iv_action_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public IBoardListPresenter createPresenter() {
        return new BoardListModel(getAppDatabase());
    }

    @Override
    public void setLayoutManager(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
    }

    @Override
    public void boardLoaded(ArrayList<BoardModel> boardList) {
        if(boardList.size() > 1)
            findViewById(R.id.place_holder_text).setVisibility(View.GONE);
        mAdapter.update(boardList);
    }

    @Override
    public void onItemClick(int position) {
        if (editMode == EDIT_ENABLED)
            return;
        if (position == 0){
            startActivity(new Intent(getApplicationContext(), DialogAddBoard.class));
            return;
        }
        BoardModel board = mAdapter.getItem(position);
        mPresenter.openBoard(mContext, board);
        getSession().setCurrentBoardLanguage(board.getLanguage());
    }

    @Override
    public void onItemDelete(final int position) {
        final DialogCustom dialog = new DialogCustom(mContext);
        dialog.setText(getString(R.string.delete_board).replace("-",
                mAdapter.getItem(position).getBoardName()));
        dialog.setOnNegativeClickListener(new DialogCustom.OnNegativeClickListener() {
            @Override
            public void onNegativeClickListener() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new DialogCustom.OnPositiveClickListener() {
            @Override
            public void onPositiveClickListener() {
                mPresenter.deleteBoard(mContext,mAdapter.getItem(position));
                Toast.makeText(mContext,
                        getString(R.string.board_deleted).
                                replace("_", mAdapter.getItem(position).getBoardName()),
                        Toast.LENGTH_SHORT).show();
                mAdapter.remove(position);
                if(mAdapter.getItemCount()==1) {
                    findViewById(R.id.place_holder_text).setVisibility(View.VISIBLE);
                    mAdapter.setEditMode(EDIT_DISABLED);
                    editMode = EDIT_DISABLED;
                    editMenu.setIcon(R.drawable.ic_edit_icon_disabled);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBoardEdit(int position) {
        getSession().setCurrentBoardLanguage(mAdapter.getList().get(position).getLanguage());
        Intent intent = new Intent(this, IconSelectActivity.class);
        intent.putExtra(BOARD_ID,mAdapter.getList().get(position).getBoardId());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(BoardListActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
        }
        // Start measuring user app screen timer.
        startMeasuring();
        SelectionManager.getInstance().delete();
        getSession().setCurrentBoardLanguage("");
        initViewsAndEvents();
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
        stopMeasuring(BoardListActivity.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        if(getSession()!=null)
            getSession().setCurrentBoardLanguage("");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Integer.parseInt(getString(R.string.search_board)) && resultCode == RESULT_OK) {
            final String boardName = data.getStringExtra(getString(R.string.search_result));
            for (int i = 0; i < mAdapter.getList().size(); i++) {
                if (mAdapter.getList().get(i).getBoardName().equals(boardName)){
                    mAdapter.highlightSearchedBoard(i);
                    Timer timer = new Timer();
                    final int finalI = i;
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            speakFromMMB(boardName);
                            mRecyclerView.smoothScrollToPosition(finalI);
                        }
                    },1000);
                    return;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.enable_edit){
            this.editMenu = item;
            if(editMode==EDIT_DISABLED){
                item.setTitle("Enabled board edit");
                mAdapter.setEditMode(EDIT_ENABLED);
                editMode = EDIT_ENABLED;
                item.setIcon(R.drawable.ic_edit_icon_enabled);
            }else{
                item.setTitle("Disabled board edit");
                mAdapter.setEditMode(EDIT_DISABLED);
                editMode = EDIT_DISABLED;
                item.setIcon(R.drawable.ic_edit_icon_disabled);
            }
            mAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}
