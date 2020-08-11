package com.dsource.idc.jellowintl.make_my_board_module.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.activities.BaseActivity;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.helper_classes.GeneralDatabaseCreator;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.SuccessCallBack;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.activities.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.BOARD_ID;

public class SetupMMB extends BaseActivity {
    RoundCornerProgressBar progressBar;
    private SessionManager mSession;
    private String langCode;
    private String boardId;
    TextView progressText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_download);
        setNavigationUiConditionally();
        if(getSupportActionBar()!=null) getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        mSession = new SessionManager(this);
        progressBar = findViewById(R.id.pg);
        progressText = findViewById(R.id.progress_text);
        progressBar.setContentDescription(getString(R.string.setting_up_the_language));
        if(getIntent().getStringExtra(LCODE)!=null){
            langCode = getIntent().getStringExtra(LCODE);
            boardId = getIntent().getStringExtra(BOARD_ID);
        }else{
            Toast.makeText(this, R.string.unable_to_create_board,Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if(!new TextDatabase(this,langCode, getAppDatabase()).checkForTableExists())
        {
            progressText.setText(R.string.setting_up_the_language);
            createDatabase(this);
        }else
        {
            Intent intent = new Intent(this, IconSelectActivity.class);
            intent.putExtra(LCODE,langCode);
            intent.putExtra(BOARD_ID,boardId);
            startActivity(intent);
            finish();
        }

    }

    private void createDatabase(final SetupMMB setupMMB) {
        TextDatabase databaseHelper = new TextDatabase(this,langCode, getAppDatabase());
        GeneralDatabaseCreator<TextDatabase> creator =  new GeneralDatabaseCreator<>(databaseHelper, new SuccessCallBack() {
            @Override
            public void setProgressSize(final int progressSize) {
                setupMMB.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           progressBar.setMax(progressSize);
                       }
                   });
            }

            @Override
            public void updateProgress(final int progress) {
                setupMMB.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                    }
                });
            }

            @Override
           public void onSuccess(Object object) {
               createIconDatabase();
           }
       });
       creator.execute();
    }

    private void createIconDatabase() {
        progressText.setText(R.string.completed_process);
        Intent intent = new Intent(SetupMMB.this, IconSelectActivity.class);
        intent.putExtra(LCODE,langCode);
        intent.putExtra(BOARD_ID,boardId);
        startActivity(intent);
        mSession.setLanguageDataUpdateState(langCode, GlobalConstants.LANGUAGE_STATE_NO_CHANGE);
        mSession.setCurrentBoardLanguage(langCode);
        mSession.setBoardDatabaseStatus(true,langCode);
        finish();
    }
}


