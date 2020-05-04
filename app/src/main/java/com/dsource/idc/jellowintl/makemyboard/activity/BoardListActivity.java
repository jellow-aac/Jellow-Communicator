package com.dsource.idc.jellowintl.makemyboard.activity;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.MainActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.Dialogs.DialogAddBoard;
import com.dsource.idc.jellowintl.makemyboard.Dialogs.DialogCustom;
import com.dsource.idc.jellowintl.makemyboard.adapters.BoardAdapter;
import com.dsource.idc.jellowintl.makemyboard.iModels.BoardListModel;
import com.dsource.idc.jellowintl.makemyboard.iPresenter.IBoardListPresenter;
import com.dsource.idc.jellowintl.makemyboard.iView.IBoardListView;
import com.dsource.idc.jellowintl.makemyboard.interfaces.BoardClickListener;
import com.dsource.idc.jellowintl.makemyboard.managers.SelectionManager;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

import java.util.ArrayList;

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
        setActivityTitle(getString(R.string.home) + "/ "+ getString(R.string.menuMyBoards));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
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
        Intent in = new Intent(getApplicationContext(), DialogAddBoard.class);
        in.putExtra(BOARD_ID, mAdapter.getItem(position).getBoardId());
        startActivity(in);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SelectionManager.getInstance().delete();
        getSession().setCurrentBoardLanguage("");
        initViewsAndEvents();
    }

    @Override
    public void onBackPressed() {
        if(getSession()!=null) getSession().setCurrentBoardLanguage("");
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }
}
