package com.dsource.idc.jellowintl.makemyboard.utility;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.activity.IconSelectActivity;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.DatabaseCreator;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.databases.TextDatabase;
import com.dsource.idc.jellowintl.makemyboard.interfaces.SuccessCallBack;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
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
        if(getSupportActionBar()!=null) getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        mSession = new SessionManager(this);
        progressBar = findViewById(R.id.pg);
        progressBar.setMax(1);
        progressText = findViewById(R.id.progress_text);
        if(getIntent().getStringExtra(LCODE)!=null){
            langCode = getIntent().getStringExtra(LCODE);
            boardId = getIntent().getStringExtra(BOARD_ID);
        }
        else{
            Toast.makeText(this,"Some error occured please try again",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if(!new TextDatabase(this,langCode, getAppDatabase()).checkForTableExists())
        {
            progressBar.setProgress(.3f);
            progressText.setText("Setting things up, Please wait...");
            createDatabase();
        }
        else
        {
            Intent intent = new Intent(this, IconSelectActivity.class);
            intent.putExtra(LCODE,langCode);
            intent.putExtra(BOARD_ID,boardId);
            startActivity(intent);
            finish();
        }

    }

    private void createDatabase() {
        TextDatabase databaseHelper = new TextDatabase(this,langCode, getAppDatabase());
        DatabaseCreator<TextDatabase> creater =  new DatabaseCreator<>(databaseHelper, new SuccessCallBack() {
           @Override
           public void onSuccess(Object object) {
               progressBar.setProgress(.7f);
               progressText.setText("Finalizing setup, please wait...");
               createIconDatabase();
           }
       });
       creater.execute();
    }

    private void createIconDatabase() {
        progressBar.setProgress(1f);
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


