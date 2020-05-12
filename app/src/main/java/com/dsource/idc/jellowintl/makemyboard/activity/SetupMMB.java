package com.dsource.idc.jellowintl.makemyboard.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.helper_classes.GeneralDatabaseCreator;
import com.dsource.idc.jellowintl.makemyboard.interfaces.SuccessCallBack;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

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
        if(getIntent().getStringExtra(LCODE)!=null){
            langCode = getIntent().getStringExtra(LCODE);
            boardId = getIntent().getStringExtra(BOARD_ID);
        }else{
            Toast.makeText(this,"Some error appeared please try again",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if(!new TextDatabase(this,langCode, getAppDatabase()).checkForTableExists())
        {
            progressText.setText("Setting things up, Please wait...");
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
               progressText.setText("Finalizing setup, please wait...");
               createIconDatabase();
           }
       });
       creator.execute();
    }

    private void createIconDatabase() {
        progressText.setText("Completed");
        Intent intent = new Intent(SetupMMB.this, IconSelectActivity.class);
        intent.putExtra(LCODE,langCode);
        intent.putExtra(BOARD_ID,boardId);
        startActivity(intent);
        mSession.setCurrentBoardLanguage(langCode);
        mSession.setBoardDatabaseStatus(true,langCode);
        finish();
    }
}


