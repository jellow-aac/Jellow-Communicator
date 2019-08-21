package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.adapters.BoardAdapter;
import com.dsource.idc.jellowintl.makemyboard.adapters.LanguageSelectAdapter;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.iDataPresenter;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.activity.IconSelectActivity;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.SelectionManager;
import com.dsource.idc.jellowintl.makemyboard.interfaces.AddBoardListener;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.utility.BoardLanguageManager;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.utility.DialogBox;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ADD_BOARD;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.DIALOG_TYPE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.IS_EDIT_MODE;

public class BoardListActivity extends BaseActivity {

    private BoardDatabase database;
    private ImageView addBoard;
    private ArrayList<BoardModel> boardList;
    private RecyclerView recyclerView;
    private BoardAdapter adapter;
    private Context ctx;
    private SessionManager manager;
    private LanguageSelectAdapter selector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list_acitivty);
        database =new BoardDatabase(this);
        boardList =new ArrayList<>();
        recyclerView =findViewById(R.id.board_recycer_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        manager = new SessionManager(this);
        setUpItems();
        setSelection("All");
        updateBoardCount();
        ctx =this;
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.menuMyBoards));
            //TODO Check color to keep or remove.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        }


    }

    //Instantiates all the views
    private void setUpItems() {
        selector = new LanguageSelectAdapter(this,((RecyclerView)findViewById(R.id.recycler_view)));
        selector.setCallback(new GenCallback<String>() {
            @Override
            public void callBack(String object) {
                setSelection(object);
            }
        });


        addBoard=findViewById(R.id.add_board);
        addBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in  = new Intent(getApplicationContext(), DialogBox.class);
                DialogBox.mAddBoardListener =addListener();
                in.putExtra(DIALOG_TYPE,ADD_BOARD);
                startActivity(in);
            }
        });
    }

    private AddBoardListener addListener() {
        return new AddBoardListener() {
            @Override
            public void onBoardCreated(BoardModel board) {
                updateBoardCount();
                setUpBoardList();
                new BoardLanguageManager(board,BoardListActivity.this, getAppDatabase()).checkLanguageAvailabilityInBoard();
                Toast.makeText(BoardListActivity.this,"Successfully created",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(BoardListActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onBoardUpdated(BoardModel board) {
                Intent in  = new Intent(getApplicationContext(), IconSelectActivity.class);
                DialogBox.mAddBoardListener =addListener();
                in.putExtra(DIALOG_TYPE,ADD_BOARD);
                in.putExtra(IS_EDIT_MODE,"YES");
                in.putExtra(BOARD_ID,board.getBoardId());
                startActivity(in);

            }
        };
    }

    private void updateBoardCount() {
        selector.updateBoardCount();
    }

    private void setSelection(String language){
        if(language.equals("All"))
            database.getAllBoards(new iDataPresenter<ArrayList<BoardModel>>() {
                @Override
                public void onSuccess(ArrayList<BoardModel> object) {
                    boardList = object;
                }

                @Override
                public void onFailure(String msg) {

                }
            });
        else database.getAllBoards(language,new iDataPresenter<ArrayList<BoardModel>>() {
                @Override
                public void onSuccess(ArrayList<BoardModel> object) {
                    boardList = object;
                }

                @Override
                public void onFailure(String msg) {

                }
            });
        setUpBoardList();
    }

    private void setUpBoardList() {
        adapter =new BoardAdapter(this,boardList);
        adapter.setOnItemClickListener(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position, int code) {
                BoardModel board = boardList.get(Position);
                new BoardLanguageManager(board,ctx, getAppDatabase()).checkLanguageAvailabilityInBoard();
            }

            @Override
            public void onItemDelete(final int position) {
                final CustomDialog dialog =new CustomDialog(BoardListActivity.this,CustomDialog.NORMAL, SessionManager.ENG_IN);
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
                        database.deleteBoard(boardList.get(position));
                        boardList.remove(position);
                        updateBoardCount();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            @Override
            public void onBoardEdit(int position) {
                Intent in  = new Intent(getApplicationContext(), DialogBox.class);
                DialogBox.mAddBoardListener =addListener();
                in.putExtra(DIALOG_TYPE,ADD_BOARD);
                in.putExtra(IS_EDIT_MODE,"YES");
                in.putExtra(BOARD_ID,boardList.get(position).getBoardId());
                startActivity(in);
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        switch (id){
            case android.R.id.home:finish();break;
        }

        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(BoardListActivity.class.getSimpleName());
        SelectionManager.getInstance().delete();
        if(manager!=null) manager.setCurrentBoardLanguage("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(manager!=null) manager.setCurrentBoardLanguage("");
    }
}
