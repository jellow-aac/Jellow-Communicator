package com.dsource.idc.jellow;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Slider;

import java.util.Locale;

public class Setting extends AppCompatActivity {

    Spinner  spinner, spinner2, spinner3, spinner4;
    ArrayAdapter<CharSequence> adapter, adapter2, adapter3, adapter4;
    Button Save, Demo;
    TextToSpeech t1;
    private SessionManager session;
    String toSpeak;
    TextView speechspeed, voicepitch, tv1, tv2, tv3, tv4;
    int l, a;
    Slider speed, pitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>Settings</font>"));

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        if (dpHeight >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);

        adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        //adapter2= ArrayAdapter.createFromResource(this, R.array.accent, android.R.layout.simple_spinner_item);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.picture_view_mode, android.R.layout.simple_spinner_item);
        adapter4 = ArrayAdapter.createFromResource(this, R.array.grid_size, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);
        Save = (Button)findViewById(R.id.button4);
        Demo = (Button)findViewById(R.id.demo);
        speed = (Slider) findViewById(R.id.speed);
        pitch = (Slider)findViewById(R.id.pitch);
        speechspeed = (TextView)findViewById(R.id.speechspeed);
        voicepitch = (TextView)findViewById(R.id.voicepitch);
        tv1=(TextView)findViewById(R.id.tv1);
        //tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);

        session = new SessionManager(getApplicationContext());

        if (session.getLanguage()==1){
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>सेटिंग्स</font>"));
            tv1.setText("भाषा");
            //tv2.setText("उच्चारण");
            tv3.setText("प्रदर्शन प्रकार");
            tv4.setText("प्रति स्क्रीन आइकनों की गिनती");
            speechspeed.setText("भाषण गति");
            voicepitch.setText("आवाज का स्तर");
            Save.setText("रक्षित करें ");
            Demo.setText("डेमो");
            adapter = ArrayAdapter.createFromResource(this, R.array.languages_hindi, android.R.layout.simple_spinner_item);
            //adapter2= ArrayAdapter.createFromResource(this, R.array.accent_hindi, android.R.layout.simple_spinner_item);
            adapter3 = ArrayAdapter.createFromResource(this, R.array.picture_view_mode_hindi, android.R.layout.simple_spinner_item);
            adapter4 = ArrayAdapter.createFromResource(this, R.array.grid_size_hindi, android.R.layout.simple_spinner_item);
            spinner.setAdapter(adapter);
            //spinner2.setAdapter(adapter2);
            spinner3.setAdapter(adapter3);
            spinner4.setAdapter(adapter4);
        }

        spinner.setSelection(session.getLanguage());
        /*spinner2.setSelection(session.getAccent());
        if (session.getLanguage() == 1) {
            spinner2.setEnabled(false);
            spinner2.setAlpha(0.5f);
        }*/
        speed.setValue(session.getSpeed(),true);
        pitch.setValue(session.getPitch(),true);
        spinner3.setSelection(session.getPictureViewMode());
        spinner4.setSelection(session.getGridSize());

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setEngineByPackageName("com.google.android.mTts");
                }
            }
        });

        t1.setPitch((float) pitch.getValue()/50);

        if (session.getLanguage()==0){
            toSpeak = "My Name is Hello";
            t1.setLanguage(Locale.US);
        }

        Demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session.getLanguage()==1){
                    toSpeak ="मेरा नाम हेलो हैं";
                    t1.setLanguage(new Locale("hin", "IND"));
                }

                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position:" +position);
                l = position;

                if(position==0){
                    toSpeak = "My Name is Hello";
                    t1.setLanguage(new Locale("hin", "IND"));
                    /*spinner2.setEnabled(true);
                    spinner2.setAlpha(1f);*/
                }
                else if(position==1){
                    toSpeak ="मेरा नाम हेलो हैं";
                    t1.setLanguage(new Locale("hin", "IND"));
                    /*spinner2.setEnabled(false);
                    spinner2.setAlpha(0.5f);*/
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

       /* spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position:" +position);
                a = position;

                if(position==0){
                    mMenuItemBelowText.setLanguage(new Locale("hin", "IND"));
                }
                else if(position==1){
                    mMenuItemBelowText.setLanguage(new Locale("en", "IN"));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/


        speed.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                t1.setSpeechRate((float) newValue / 50);
                if (session.getLanguage() == 0) {
                    speechspeed.setText("Speech Speed: " + String.valueOf((float) newValue / 50));
                }
                if (session.getLanguage()==1){
                    speechspeed.setText("भाषण गति: " + String.valueOf((float) newValue / 50));
                }
            }
        });


        pitch.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                t1.setPitch((float) newValue / 50);
                if (session.getLanguage() == 0) {
                    voicepitch.setText("Voice Pitch: " + String.valueOf((float) newValue / 50));
                }
                if (session.getLanguage()==1){
                    voicepitch.setText("आवाज का स्तर: " + String.valueOf((float) newValue / 50));
                }
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            session.setLanguage(spinner.getSelectedItemPosition());
            // session.setAccent(spinner2.getSelectedItemPosition());
            session.setSpeed(speed.getValue());
            session.setPitch(pitch.getValue());
            session.setPictureViewMode(spinner3.getSelectedItemPosition());
            session.setGridSize(spinner4.getSelectedItemPosition());

            if (session.getLanguage()==1){
                Toast.makeText(Setting.this, "सेटिंग्स रक्षित हो गये हैं ", Toast.LENGTH_SHORT).show();
            }
            if (session.getLanguage()==0) {
                Toast.makeText(Setting.this, "Settings saved", Toast.LENGTH_SHORT).show();
            }

            Intent i = new Intent(Setting.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    });
}

    @Override
    protected void onPause() {
       finish();
        super.onPause();
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
            case R.id.info:
                Intent i = new Intent(Setting.this, About_Jellow.class);
                startActivity(i);
                finish();
                break;
            case R.id.profile:
                Intent intent1 = new Intent(Setting.this, Profile_form.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.feedback:
                Intent intent2 = new Intent(Setting.this, Feedback.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.usage:
                Intent intent3 = new Intent(Setting.this, Tutorial.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.reset:
                Intent intent4 = new Intent(Setting.this, Reset__preferences.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.keyboardinput:
                Intent intent6 = new Intent(Setting.this, Keyboard_Input.class);
                startActivity(intent6);
                finish();
                break;
            case android.R.id.home:
                Intent intent5 = new Intent(Setting.this, MainActivity.class);
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
            Intent i = new Intent(Setting.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}