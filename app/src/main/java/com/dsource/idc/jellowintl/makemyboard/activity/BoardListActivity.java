package com.dsource.idc.jellowintl.makemyboard.activity;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.MainActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.adapters.BoardAdapter;
import com.dsource.idc.jellowintl.makemyboard.custom_dialogs.DialogAddBoard;
import com.dsource.idc.jellowintl.makemyboard.custom_dialogs.DialogCustom;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.interfaces.BoardClickListener;
import com.dsource.idc.jellowintl.makemyboard.managers.SelectionManager;
import com.dsource.idc.jellowintl.makemyboard.models.BoardListModel;
import com.dsource.idc.jellowintl.makemyboard.presenter_interfaces.IBoardListPresenter;
import com.dsource.idc.jellowintl.makemyboard.view_interfaces.IBoardListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

public class BoardListActivity extends BaseBoardActivity<IBoardListView, IBoardListPresenter, BoardAdapter> implements IBoardListView, BoardClickListener {

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
        mAdapter.update(boardList);
    }

    @Override
    public void onItemClick(int position) {
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
        dialog.setText(getString(R.string.delete_board)+" ?");
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
                mAdapter.remove(position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBoardEdit(int position) {
        Intent intent = new Intent(this, IconSelectActivity.class);
        intent.putExtra(BOARD_ID,mAdapter.getList().get(position).getBoardId());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(BoardListActivity.class.getSimpleName());
        SelectionManager.getInstance().delete();
        getSession().setCurrentBoardLanguage("");
        initViewsAndEvents();
    }

    @Override
    public void onBackPressed() {
        if(getSession()!=null)
            getSession().setCurrentBoardLanguage("");
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
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
}
