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
public class Reset__preferences extends AppCompatActivity {
    private SessionManager mSession;
    private DataBaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+ getString(R.string.menuResetPref) +"</font>"));
        mSession = new SessionManager(this);

        if (mSession.getScreenHeight() >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        myDbHelper = new DataBaseHelper(this);
        Button mNo = (Button) findViewById(R.id.no);
        Button mYes = (Button) findViewById(R.id.yes);
        /*TextView mTextView1 = (TextView) findViewById(R.id.tv1);
            mTextView2 =(TextView)findViewById(R.id.tv2);
            mTextView3 =(TextView)findViewById(R.id.tv3);*/

        /*if (mSession.getLanguage()==1){
            mTextView1.setText("प्रयोगकर्ता के द्वारा सबसे अधिक इस्तेमाल किये गए पसंदीदा आइकॉन को जेलो अॅप्लिकेशन स्मरण में रखता हैं, और उन्हें स्क्रीन पर मुख्य पसंद के रूप में प्रदर्शित करता हैं।");
            mTextView2.setText("आइकॉन्स को रीसेट करके , सभी आइकॉन मूल क्रम में आ जायेंगे जैसे वे पहली बार अॅप्लिकेशन में प्रदर्शित किये गये थे।");
            mTextView3.setText("आप आइकॉन्स को रीसेट करना चाहते हैं?");
            mYes.setText("हाँ");
            mNo.setText("नहीं");
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>आइकॉन रीसेट करें</font>"));
        }*/

        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(Reset__preferences.this, getString(R.string.iconsHasBeenReset), Toast.LENGTH_SHORT).show();
            myDbHelper.delete();
            mSession.resetUserPeoplePlacesPreferences();
            Intent intent = new Intent(Reset__preferences.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(7).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile: startActivity(new Intent(Reset__preferences.this, Profile_form.class)); finish(); break;
            case R.id.info: startActivity(new Intent(Reset__preferences.this, About_Jellow.class));   finish(); break;
            case R.id.usage: startActivity(new Intent(Reset__preferences.this, Tutorial.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(Reset__preferences.this, Keyboard_Input.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(Reset__preferences.this, Feedback.class)); finish(); break;
            case R.id.settings: startActivity(new Intent(Reset__preferences.this, Setting.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(Reset__preferences.this, Reset__preferences.class)); finish(); break;
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