package com.dsource.idc.jellow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.dsource.idc.jellow.Utility.SessionManager;

/**
 * Created by user on 5/27/2016.
 */

public class Keyboard_Input extends AppCompatActivity {

    private SessionManager session;
    Button SaveAsDefault, ABC, QWERTY;
    String id;
    TextView t1, t2, t3, vc, tvtop, tvtop1, tvtop2, tvkc;
    Button tvbottom, tvbottom1, tvbottom2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keyboardinput);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>Voice and Keyboard Control</font>"));
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        if (dpHeight >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);


        session = new SessionManager(getApplicationContext());
        SaveAsDefault = (Button)findViewById(R.id.default_button);
        ABC = (Button) findViewById(R.id.abc);
        QWERTY = (Button)findViewById(R.id.qwerty);
        t1 = (TextView)findViewById(R.id.t1);
        t2 = (TextView)findViewById(R.id.t2);
        t3 = (TextView)findViewById(R.id.t3);
        vc = (TextView)findViewById(R.id.tvvc);
        tvtop = (TextView)findViewById(R.id.tvtop);
        tvtop1 = (TextView)findViewById(R.id.tvtop1);
        tvtop2 = (TextView)findViewById(R.id.tvtop2);
        tvbottom = (Button) findViewById(R.id.tvbottom);
        tvbottom1 = (Button) findViewById(R.id.tvbottom1);
        tvbottom2 = (Button) findViewById(R.id.tvbottom2);
        tvkc = (TextView)findViewById(R.id.tvkc);

        if (session.getLanguage()==1){
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>आवाज़ एवं कीबोर्ड नियंत्रण</font>"));
            t1.setText("अपनी पसंद का कीबोर्ड  चुनने के लिए और उसे डिफ़ॉल्ट बनाने के लिए आपको २ चरण पूरे करने होंगे:");
            t2.setText("चरण १: टैबलेट/मोबाइल के सेटिंग्स से कीबोर्ड को सक्रिय करें");
            t3.setText("चरण २: चुने हुए कीबोर्ड को डिफ़ॉल्ट बनाएँ");
            vc.setText("आवाज़ नियंत्रण");
            tvtop.setText("गूगल टेक्सट टू स्पीच चुनें");
            tvtop1.setText("हिंदी भाषा को  डिफ़ॉल्ट बनाएँ ");
            tvtop2.setText("हिंदी आवाज़ डाउनलोड करें");
            tvbottom.setText("सक्रिय करें");
            tvbottom1.setText("डिफ़ॉल्ट बनाएँ");
            tvbottom2.setText("डाउनलोड करें");
            tvkc.setText("कीबोर्ड नियंत्रण");
            ABC.setText("क्रमिक 'कखग' कीबोर्ड");
            QWERTY.setText("डिफ़ॉल्ट देवनागरी कीबोर्ड");
            SaveAsDefault.setText("रक्षित करें");
        }

        tvbottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction("com.android.settings.TTS_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        tvbottom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction("com.android.settings.TTS_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        tvbottom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                installIntent.setPackage("com.google.android.mTts");
                startActivity(installIntent);
            }
        });

        ABC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                startActivity(intent);

            }
        });

        QWERTY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
                startActivity(intent);

            }
        });

        SaveAsDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showInputMethodPicker();
                Intent intent5 = new Intent(Keyboard_Input.this, MainActivity.class);
                startActivity(intent5);
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
                Intent intent = new Intent(Keyboard_Input.this, Setting.class);
                startActivity(intent);
                finish();
                break;
            case R.id.info:
                Intent i = new Intent(Keyboard_Input.this, About_Jellow.class);
                startActivity(i);
                finish();
                break;
            case R.id.profile:
                Intent intent1 = new Intent(Keyboard_Input.this, Profile_form.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.feedback:
                Intent intent2 = new Intent(Keyboard_Input.this, Feedback.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.usage:
                Intent intent3 = new Intent(Keyboard_Input.this, Tutorial.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.reset:
                Intent intent4 = new Intent(Keyboard_Input.this, Reset__preferences.class);
                startActivity(intent4);
                finish();
                break;
            case android.R.id.home:
                Intent intent5 = new Intent(Keyboard_Input.this, MainActivity.class);
                startActivity(intent5);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(Keyboard_Input.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}