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

/**
 * Created by ekalpa on 15-Jun-16.
 **/

public class Reset__preferences extends AppCompatActivity {
    Button No;
    Button Yes;
    TextView tv1, tv2, tv3;
    private SessionManager session;

    DataBaseHelper myDbHelper;
    public static int flag = 0;

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
        No = (Button)findViewById(R.id.no);
        Yes = (Button)findViewById(R.id.yes);
        session = new SessionManager(getApplicationContext());
        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);

        if (session.getLanguage()==1){
            tv1.setText("प्रयोगकर्ता के द्वारा सबसे अधिक इस्तेमाल किये गए पसंदीदा आइकॉन को जेलो अॅप्लिकेशन स्मरण में रखता हैं, और उन्हें स्क्रीन पर मुख्य पसंद के रूप में प्रदर्शित करता हैं।");
            tv2.setText("आइकॉन्स को रीसेट करके , सभी आइकॉन मूल क्रम में आ जायेंगे जैसे वे पहली बार अॅप्लिकेशन में प्रदर्शित किये गये थे।");
            tv3.setText("आप आइकॉन्स को रीसेट करना चाहते हैं?");
            Yes.setText("हाँ");
            No.setText("नहीं");
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>आइकॉन रीसेट करें</font>"));
        }

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                finish();
                Intent i = new Intent(Reset__preferences.this, MainActivity.class);
                startActivity(i);
            }
        });

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getLanguage()==1){
                    Toast.makeText(Reset__preferences.this, "आयकॉन रीसेट हो गये हैं", Toast.LENGTH_SHORT).show();
                }
                if (session.getLanguage()==0) {
                    Toast.makeText(Reset__preferences.this, "Preferences have been resetted", Toast.LENGTH_SHORT).show();
                }
                myDbHelper.delete();
                flag = 1;
                Intent i = new Intent(Reset__preferences.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (session.getLanguage()==1){
            MenuInflater blowUp = getMenuInflater();
            blowUp.inflate(R.menu.menu_main, menu);
        }
        if (session.getLanguage()==0) {
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
            Intent i = new Intent(Reset__preferences.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
