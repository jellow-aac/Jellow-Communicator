package com.dsource.idc.jellowintl.makemyboard.iActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.SelectionManager;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.iPresenter.IBasePresenter;
import com.dsource.idc.jellowintl.makemyboard.iView.IBaseView;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

public abstract class BaseBoardActivity<V extends IBaseView,P extends IBasePresenter<V>,A extends RecyclerView.Adapter> extends BaseActivity {

    public P mPresenter;
    public A mAdapter;
    public RecyclerView mRecyclerView;

    public abstract int getLayoutId();

    public abstract A getAdapter();

    public abstract void initViewsAndEvents();

    public abstract P createPresenter();

    public abstract void setLayoutManager(RecyclerView recyclerView);

    public BoardModel currentBoard;

    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        mContext = this;

        getCurrentBoard();

        mPresenter = createPresenter();

        mPresenter.attachView((V)this);

        mAdapter = getAdapter();

        mRecyclerView = findViewById(R.id.recycler_view);

        setLayoutManager(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);

        initViewsAndEvents();
    }

    private void getCurrentBoard() {
        try{
            String boardId="";

            if(getIntent().getExtras()!=null)
                boardId =getIntent().getExtras().getString(BOARD_ID);
            BoardDatabase database=new BoardDatabase(getAppDatabase());
            currentBoard=database.getBoardById(boardId);
        }
        catch (NullPointerException e)
        {
            Toast.makeText(this,"Some error occured",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO once all activity inherit base class, then uncomment this line,setVisibleAct(BaseBoardActivity.class.getSimpleName());
    }
}
