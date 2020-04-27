package com.dsource.idc.jellowintl.makemyboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.MainActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.Dialogs.DialogAddBoard;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.interfaces.IDataCallback;
import com.dsource.idc.jellowintl.makemyboard.managers.SelectionManager;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

import java.util.ArrayList;

public class BoardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_boards);
        setActivityTitle(getString(R.string.menuMyBoards));
        findViewById(R.id.iv_action_bar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setUpViews();
        updateBoardCount();
    }

    private void setUpViews() {
        findViewById(R.id.add_board).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), DialogAddBoard.class);
                startActivity(in);
            }
        });

        findViewById(R.id.open_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((TextView) findViewById(R.id.board_count)).getText().equals("0"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_board_created), Toast.LENGTH_LONG).show();
                else
                    startActivity(new Intent(getApplicationContext(), BoardListActivity.class));
            }
        });

    }

    private void updateBoardCount() {
        BoardDatabase database = new BoardDatabase(getAppDatabase());
        database.getAllBoards(new IDataCallback<ArrayList<BoardModel>>() {
            @Override
            public void onSuccess(ArrayList<BoardModel> object) {
                ((TextView) findViewById(R.id.board_count)).setText("" + object.size());
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
                break;
        }

        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBoardCount();
        setVisibleAct(BoardActivity.class.getSimpleName());
        SelectionManager.getInstance().delete();
        if (getSession() != null) getSession().setCurrentBoardLanguage("");
    }

    @Override
    public void onBackPressed() {
        if(getSession()!=null) getSession().setCurrentBoardLanguage("");
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finishAffinity();
    }


}
