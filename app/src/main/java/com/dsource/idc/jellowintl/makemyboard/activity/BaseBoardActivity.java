package com.dsource.idc.jellowintl.makemyboard.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.iPresenter.IBasePresenter;
import com.dsource.idc.jellowintl.makemyboard.iView.IBaseView;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

public abstract class BaseBoardActivity<V extends IBaseView, P extends IBasePresenter<V>, A extends RecyclerView.Adapter> extends BaseActivity {

    public P mPresenter;
    public A mAdapter;
    public RecyclerView mRecyclerView;
    public BoardModel currentBoard;
    public Context mContext;
    private SparseArray<View> mViewList;

    public abstract int getLayoutId();

    public abstract A getAdapter();

    public abstract void initViewsAndEvents();

    public abstract P createPresenter();

    public abstract void setLayoutManager(RecyclerView recyclerView);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        mViewList = new SparseArray<>();

        mContext = this;

        getCurrentBoard();

        mPresenter = createPresenter();

        mPresenter.attachView((V) this);

        mAdapter = getAdapter();

        mRecyclerView = findViewById(R.id.recycler_view);

        setLayoutManager(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);

        initViewsAndEvents();
    }

    private void getCurrentBoard() {
        try {
            String boardId = "";

            if (getIntent().getExtras() != null)
                boardId = getIntent().getExtras().getString(BOARD_ID);
            BoardDatabase database = new BoardDatabase(getAppDatabase());
            currentBoard = database.getBoardById(boardId);
        } catch (NullPointerException e) {
            Toast.makeText(this, "Some error occurred", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(BaseBoardActivity.class.getSimpleName());
    }

    public View getView(int resId) {
        if (mViewList.get(resId) == null)
            mViewList.append(resId, findViewById(resId));
        return mViewList.get(resId);
    }

    public void setVisibility(int resId, boolean isVisible) {
        if (getView(resId) != null)
            getView(resId).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void disableView(int resId, boolean isDisabled) {
        if (getView(resId) != null) {
            if (isDisabled) {
                getView(resId).setEnabled(false);
                getView(resId).setAlpha(.6f);
            } else {
                getView(resId).setEnabled(true);
                getView(resId).setAlpha(1.0f);
            }
        }
    }

    public void setupToolBar(int stringResId){

        if(getSupportActionBar()!=null) {
            //enableNavigationBack();
            getSupportActionBar().setTitle(stringResId);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        }
    }
    public int getNumberOfIconPerScreen() {
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
