package com.dsource.idc.jellow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dsource.idc.jellow.Utility.SessionManager;

/**
 * Created by ekalpa on 15-Jun-16.
 **/

public class Reset__preferences extends AppCompatActivity {
    private final int LANG_ENG = 0, LANG_HINDI = 1;
    private Button mNo, mYes;
    private TextView mTextView1, mTextView2, mTextView3;
    private SessionManager mSession;
    private DataBaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>Reset Preferences</font>"));
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        if (dpHeight >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);


        myDbHelper = new DataBaseHelper(Reset__preferences.this);
        myDbHelper = new DataBaseHelper(this);
        mNo = (Button)findViewById(R.id.no);
        mYes = (Button)findViewById(R.id.yes);
        mSession = new SessionManager(getApplicationContext());
        mTextView1 =(TextView)findViewById(R.id.tv1);
        mTextView2 =(TextView)findViewById(R.id.tv2);
        mTextView3 =(TextView)findViewById(R.id.tv3);

        if (mSession.getLanguage()==1){
            mTextView1.setText("प्रयोगकर्ता के द्वारा सबसे अधिक इस्तेमाल किये गए पसंदीदा आइकॉन को जेलो अॅप्लिकेशन स्मरण में रखता हैं, और उन्हें स्क्रीन पर मुख्य पसंद के रूप में प्रदर्शित करता हैं।");
            mTextView2.setText("आइकॉन्स को रीसेट करके , सभी आइकॉन मूल क्रम में आ जायेंगे जैसे वे पहली बार अॅप्लिकेशन में प्रदर्शित किये गये थे।");
            mTextView3.setText("आप आइकॉन्स को रीसेट करना चाहते हैं?");
            mYes.setText("हाँ");
            mNo.setText("नहीं");
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>आइकॉन रीसेट करें</font>"));
        }

        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Reset__preferences.this, MainActivity.class));
                finish();
            }
        });

        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSession.getLanguage()== LANG_ENG){
                    Toast.makeText(Reset__preferences.this, "आयकॉन रीसेट हो गये हैं", Toast.LENGTH_SHORT).show();
                }
                if (mSession.getLanguage()== LANG_HINDI) {
                    Toast.makeText(Reset__preferences.this, "Preferences have been resetted", Toast.LENGTH_SHORT).show();
                }
                myDbHelper.delete();
                mSession.resetUserPeoplePlacesPreferences();
                startActivity(new Intent(Reset__preferences.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (mSession.getLanguage() == LANG_ENG){
            MenuInflater blowUp = getMenuInflater();
            blowUp.inflate(R.menu.menu_main, menu);
        }
        if (mSession.getLanguage() == LANG_HINDI) {
            MenuInflater blowUp = getMenuInflater();
            blowUp.inflate(R.menu.menu_1, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(Reset__preferences.this, Setting.class);
                startActivity(intent);
                finish();
                break;
            case R.id.info:
                Intent i = new Intent(Reset__preferences.this, About_Jellow.class);
                startActivity(i);
                finish();
                break;
            case R.id.profile:
                Intent intent1 = new Intent(Reset__preferences.this, Profile_form.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.feedback:
                Intent intent2 = new Intent(Reset__preferences.this, Feedback.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.usage:
                Intent intent3 = new Intent(Reset__preferences.this, Tutorial.class);
                startActivity(intent3);
                finish();
                break;
            case android.R.id.home:
                Intent intent5 = new Intent(Reset__preferences.this, MainActivity.class);
                startActivity(intent5);
                break;
            case R.id.keyboardinput:
                Intent intent6 = new Intent(Reset__preferences.this, Keyboard_Input.class);
                startActivity(intent6);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(Reset__preferences.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
