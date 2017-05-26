package com.dsource.idc.jellow;

/**
 * Created by user on 5/25/2016.
 */

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Profile_form extends AppCompatActivity {

    Button bSave;
    EditText etName, etFathercontact, etFathername, etAddress, etEmailId;
    TextView tvName, tvCaregiverName, tvCaregiverNo, tvHomeAddress, tvEmailAddress, tvBloodGrp;
    Spinner bloodgroup;
    ArrayAdapter<CharSequence> adapter;
    private SessionManager session;
    String email;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>Profile</font>"));
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;

        if (dpHeight >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etName = (EditText) findViewById(R.id.etName);
        tvName = (TextView)findViewById(R.id.tvName);
        tvCaregiverName=(TextView)findViewById(R.id.tvCaregiverName);
        tvCaregiverNo=(TextView)findViewById(R.id.tvCaregiverNo);
        tvHomeAddress=(TextView)findViewById(R.id.tvHomeAddress);
        tvEmailAddress=(TextView)findViewById(R.id.tvEmailAddress);
        tvHomeAddress=(TextView)findViewById(R.id.tvHomeAddress);
        tvBloodGrp=(TextView)findViewById(R.id.tvBloodGrp);
        etFathername = (EditText) findViewById(R.id.etFathername);
        etFathercontact = (EditText) findViewById(R.id.etFathercontact);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etEmailId = (EditText) findViewById(R.id.etEmailId);
        bloodgroup = (Spinner) findViewById(R.id.bloodgroup);
        adapter = ArrayAdapter.createFromResource(this, R.array.bloodgroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodgroup.setAdapter(adapter);
        bSave = (Button) findViewById(R.id.bSave);

        session = new SessionManager(getApplicationContext());

        etName.setText(session.getName());
        etFathercontact.setText(session.getFather_no());
        etFathername.setText(session.getFather_name());
        etAddress.setText(session.getAddress());
        etEmailId.setText(session.getEmailId());
        bloodgroup.setSelection(session.getBlood());

        if (session.getLanguage()==1){
            tvName.setText("बच्चे का नाम");
            tvCaregiverName.setText("केयर गिवर का नाम ");
            tvCaregiverNo.setText("केयर गिवर का संपर्क नंबर ");
            tvHomeAddress.setText("घर का पता ");
            tvEmailAddress.setText("ई- मेल आयडी");
            tvBloodGrp.setText("रक्त वर्ग");
            bSave.setText("रक्षित करें ");
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>प्रोफाइल</font>"));
        }

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etName.getText().toString().isEmpty()) {
                    bSave.setAlpha(0.5f);
                    bSave.setEnabled(false);
                } else {
                    bSave.setAlpha(1f);
                    bSave.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bloodgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Position_bg:" + position);

                session.setBlood(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String n = etName.getText().toString();
                String fc = etFathercontact.getText().toString();
                String fn = etFathername.getText().toString();
                String ad = etAddress.getText().toString();
                email = etEmailId.getText().toString().trim();

                if (etName.getText().length() > 0) {

                    if (etFathercontact.getText().toString().trim()
                            .length() == 10) {

                        if (isValidEmail(email))
                        {
                            session.setFather_name(fn);
                            session.setAddress(ad);
                            session.setName(n);
                            session.setFather_name(fn);
                            session.setFather_no(fc);
                            session.setEmailId(email);

                            if (session.getLanguage()==1){
                                Toast.makeText(Profile_form.this, "जानकारी रक्षित हो गई हैं ", Toast.LENGTH_SHORT).show();
                            }
                            if (session.getLanguage()==0) {
                                Toast.makeText(Profile_form.this, "Details saved", Toast.LENGTH_SHORT).show();
                            }
                            Intent i = new Intent(Profile_form.this, MainActivity.class);

                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(getApplicationContext(),"Invalid contact number  ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Profile_form.this, "Please Enter the Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
            case R.id.settings:
                Intent intent = new Intent(Profile_form.this, Setting.class);
                startActivity(intent);
                finish();
                break;
            case R.id.info:
                Intent i = new Intent(Profile_form.this, About_Jellow.class);
                startActivity(i);
                finish();
                break;
            case R.id.feedback:
                Intent intent2 = new Intent(Profile_form.this, Feedback.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.usage:
                Intent intent3 = new Intent(Profile_form.this, Tutorial.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.reset:
                Intent intent4 = new Intent(Profile_form.this, Reset__preferences.class);
                startActivity(intent4);
                finish();
                break;
            case android.R.id.home:
                Intent intent5 = new Intent(Profile_form.this, MainActivity.class);
                startActivity(intent5);
                break;
            case R.id.keyboardinput:
                Intent intent6 = new Intent(Profile_form.this, Keyboard_Input.class);
                startActivity(intent6);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent i = new Intent(Profile_form.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

