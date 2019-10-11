package com.dsource.idc.jellowintl.makemyboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.MainActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.iActivity.BoardListActivity;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.iDataPresenter;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.SelectionManager;
import com.dsource.idc.jellowintl.makemyboard.interfaces.AddBoardListener;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.utility.BoardLanguageManager;
import com.dsource.idc.jellowintl.makemyboard.utility.DialogBox;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ADD_BOARD;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.DIALOG_TYPE;

public class BoardActivity extends BaseActivity {

    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(this,DemActivtiy.class));
        manager = new SessionManager(this);
        setContentView(R.layout.activity_my_boards);
       setActivityTitle(getString(R.string.menuMyBoards));
       setUpViews();
       updateBoardCount();
    }

    private void setUpViews() {
        findViewById(R.id.add_board).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in  = new Intent(getApplicationContext(), DialogBox.class);
                DialogBox.setAddBoardListener(addListener());
                in.putExtra(DIALOG_TYPE,ADD_BOARD);
                startActivity(in);
            }
        });

        findViewById(R.id.open_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BoardListActivity.class));
            }
        });

    }

    private AddBoardListener addListener() {
        return new AddBoardListener() {
            @Override
            public void onBoardCreated(final BoardModel board) {
                updateBoardCount();
                new BoardLanguageManager(board, BoardActivity.this, getAppDatabase()).checkLanguageAvailabilityInBoard();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onBoardUpdated(BoardModel board) {
            }
        };
    }


    private void updateBoardCount() {
        BoardDatabase database = new BoardDatabase(getAppDatabase());
        database.getAllBoards(new iDataPresenter<ArrayList<BoardModel>>() {
            @Override
            public void onSuccess(ArrayList<BoardModel> object) {
                ((TextView)findViewById(R.id.board_count)).setText(""+object.size());
            }

            @Override
            public void onFailure(String msg) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        switch (id){
            case android.R.id.home:startActivity(new Intent(this, MainActivity.class));finishAffinity();break;
        }

        return true;

    }
    @Override
    protected void onResume() {
        super.onResume();
        updateBoardCount();
        setVisibleAct(BoardActivity.class.getSimpleName());
        SelectionManager.getInstance().delete();
        if(manager!=null) manager.setCurrentBoardLanguage("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(manager!=null) manager.setCurrentBoardLanguage("");
    }


}
