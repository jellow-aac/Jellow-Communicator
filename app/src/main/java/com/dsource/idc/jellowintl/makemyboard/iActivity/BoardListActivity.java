package com.dsource.idc.jellowintl.makemyboard.iActivity;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.BoardActivity;
import com.dsource.idc.jellowintl.makemyboard.MyPair;
import com.dsource.idc.jellowintl.makemyboard.iAdapter.BoardAdapter;
import com.dsource.idc.jellowintl.makemyboard.iAdapter.LangSelectAdapter;
import com.dsource.idc.jellowintl.makemyboard.iModels.BoardListModel;
import com.dsource.idc.jellowintl.makemyboard.iView.IBoardListView;
import com.dsource.idc.jellowintl.makemyboard.iPresenter.IBoardListPresenter;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.activity.IconSelectActivity;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.SelectionManager;
import com.dsource.idc.jellowintl.makemyboard.interfaces.AddBoardListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.BoardClickListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.OnItemClickListener;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.utility.DialogBox;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ADD_BOARD;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.DIALOG_TYPE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.IS_EDIT_MODE;

public class BoardListActivity extends BaseBoardActivity<IBoardListView, IBoardListPresenter, BoardAdapter> implements IBoardListView, BoardClickListener, AddBoardListener {


    private LangSelectAdapter leftPaneAdapter;


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
        setUpItems();
        mPresenter.loadBoards("All");
        mAdapter.setOnItemClickListener(this);
        setActivityTitle(getString(R.string.menuMyBoards));
        setupLeftRecycler();
    }

    @Override
    public IBoardListPresenter createPresenter() {
        return new BoardListModel(getAppDatabase());
    }

    @Override
    public void setLayoutManager(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
    }

    //Instantiates all the views
    private void setUpItems() {
        findViewById(R.id.add_board).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), DialogBox.class);
                DialogBox.setAddBoardListener(BoardListActivity.this);
                in.putExtra(DIALOG_TYPE, ADD_BOARD);
                startActivity(in);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }


    @Override
    public void boardLoaded(ArrayList<BoardModel> boardList) {
        mAdapter.update(boardList);
    }

    @Override
    public void languageVsBoardCountLoaded(ArrayList<MyPair<String, Integer>> list) {
        leftPaneAdapter.update(list);
    }


    private void setupLeftRecycler() {
        final RecyclerView leftRV = findViewById(R.id.left_recycler_view);
        leftPaneAdapter = new LangSelectAdapter(mContext, R.layout.lang_list_item, new ArrayList<MyPair<String, Integer>>());
        leftRV.setLayoutManager(new LinearLayoutManager(mContext));
        leftRV.setAdapter(leftPaneAdapter);
        mPresenter.loadLanguageVsBoardCount(mContext);
        leftPaneAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                leftPaneAdapter.updateDataOnTouch(position);
                if (position == 0)
                    mPresenter.loadBoards("All");
                else mPresenter.loadBoards(leftPaneAdapter.getItem(position).getFirst());
            }
        });


    }

    @Override
    public void onItemClick(int position) {
        BoardModel board = mAdapter.getItem(position);
        mPresenter.openBoard(mContext, board);
    }

    @Override
    public void onItemDelete(final int position) {
        final CustomDialog dialog = new CustomDialog(mContext, CustomDialog.NORMAL, SessionManager.ENG_IN);
        dialog.setText("Are you sure you want to delete this board?");
        dialog.setOnNegativeClickListener(new CustomDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClickListener() {
                dialog.dismiss();
            }
        });
        dialog.setOnPositiveClickListener(new CustomDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClickListener() {
                mPresenter.deleteBoard(mAdapter.getItem(position));
                leftPaneAdapter.decreaseBoardCount();
                mAdapter.remove(position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBoardEdit(int position) {
        Intent in = new Intent(getApplicationContext(), DialogBox.class);
        DialogBox.setAddBoardListener(BoardListActivity.this);
        in.putExtra(DIALOG_TYPE, ADD_BOARD);
        in.putExtra(IS_EDIT_MODE, "YES");
        in.putExtra(BOARD_ID, mAdapter.getItem(position).getBoardId());
        startActivity(in);
    }

    @Override
    public void onBoardCreated(BoardModel board) {

        mAdapter.add(board);
        leftPaneAdapter.increaseBoardCount();
        mPresenter.openBoard(mContext, board);
        Toast.makeText(mContext, "Successfully created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(mContext, "Cancelled", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBoardUpdated(BoardModel board) {
        Intent in = new Intent(getApplicationContext(), IconSelectActivity.class);
        DialogBox.setAddBoardListener(BoardListActivity.this);
        in.putExtra(DIALOG_TYPE, ADD_BOARD);
        in.putExtra(IS_EDIT_MODE, "YES");
        in.putExtra(BOARD_ID, board.getBoardId());
        startActivity(in);
    }
    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(BoardListActivity.class.getSimpleName());
        SelectionManager.getInstance().delete();
        getSession().setCurrentBoardLanguage("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSession().setCurrentBoardLanguage("");
    }
}
