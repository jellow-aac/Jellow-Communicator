package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dsource.idc.jellow.Utility.SessionManager;

/**
 * Created by ekalpa on 15-Jun-16.
 **/
public class ResetPreferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+ getString(R.string.menuResetPref) +"</font>"));
        final SessionManager session = new SessionManager(this);
        final DataBaseHelper myDbHelper = new DataBaseHelper(this);

        if (session.getScreenHeight() >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        ((Button) findViewById(R.id.no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((Button) findViewById(R.id.yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResetPreferences.this, getString(R.string.iconsHasBeenReset), Toast.LENGTH_SHORT).show();
                myDbHelper.delete();
                session.resetUserPeoplePlacesPreferences();
                Intent intentStartActivity = new Intent(ResetPreferences.this, Splash.class);
                intentStartActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentStartActivity);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(6).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile: startActivity(new Intent(ResetPreferences.this, ProfileForm.class)); finish(); break;
            case R.id.info: startActivity(new Intent(ResetPreferences.this, AboutJellow.class));   finish(); break;
            case R.id.usage: startActivity(new Intent(ResetPreferences.this, Tutorial.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(ResetPreferences.this, KeyboardInput.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(ResetPreferences.this, Feedback.class)); finish(); break;
            case R.id.settings: startActivity(new Intent(ResetPreferences.this, Setting.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(ResetPreferences.this, ResetPreferences.class)); finish(); break;
            case android.R.id.home: finish(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}