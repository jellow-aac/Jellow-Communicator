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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dsource.idc.jellow.Utility.SessionManager;

public class Profile_form extends AppCompatActivity {
    private Button bSave;
    private EditText etName, etFatherContact, etFathername, etAddress, etEmailId;
    private SessionManager mSession;
    private String email;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.menuProfile)+"</font>"));
        mSession = new SessionManager(this);
        if (mSession.getScreenHeight() >= 600)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back_600);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etName = (EditText) findViewById(R.id.etName);
        etFathername = (EditText) findViewById(R.id.etFathername);
        etFatherContact = (EditText) findViewById(R.id.etFathercontact);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etEmailId = (EditText) findViewById(R.id.etEmailId);
        Spinner bloodgroup = (Spinner) findViewById(R.id.bloodgroup);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bloodgroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodgroup.setAdapter(adapter);
        bSave = (Button) findViewById(R.id.bSave);
        mSession = new SessionManager(getApplicationContext());

        etName.setText(mSession.getName());
        etFatherContact.setText(mSession.getFather_no());
        etFathername.setText(mSession.getFather_name());
        etAddress.setText(mSession.getAddress());
        etEmailId.setText(mSession.getEmailId());
        bloodgroup.setSelection(mSession.getBlood());

        etName.addTextChangedListener(new TextWatcher() {
            @Override   public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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

            @Override public void afterTextChanged(Editable s) {}
        });

        bloodgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSession.setBlood(position);
            }

            @Override   public void onNothingSelected(AdapterView<?> parent) {  }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmailId.getText().toString().trim();
                if (etName.getText().toString().length() < 0) {
                    if (etFatherContact.getText().toString().trim().length() == 10) {
                        if (isValidEmail(email)) {
                            mSession.setFather_name(etFathername.getText().toString());
                            mSession.setAddress(etAddress.getText().toString());
                            mSession.setName(etName.getText().toString());
                            mSession.setFather_no(etFatherContact.getText().toString());
                            mSession.setEmailId(email);
                            Toast.makeText(Profile_form.this, getString(R.string.detailSaved), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Profile_form.this, MainActivity.class));
                        } else
                            Toast.makeText(getApplicationContext(), getString(R.string.invalid_emailId), Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.invalidContactNumber), Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(Profile_form.this, getString(R.string.enterTheName), Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(Profile_form.this, Setting.class));
                finish();
                break;
            case R.id.info:
                startActivity(new Intent(Profile_form.this, About_Jellow.class));
                finish();
                break;
            case R.id.feedback:
                startActivity(new Intent(Profile_form.this, Feedback.class));
                finish();
                break;
            case R.id.usage:
                startActivity(new Intent(Profile_form.this, Tutorial.class));
                finish();
                break;
            case R.id.reset:
                startActivity(new Intent(Profile_form.this, Reset__preferences.class));
                finish();
                break;
            case android.R.id.home:
                startActivity(new Intent(Profile_form.this, MainActivity.class));
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(Profile_form.this, Keyboard_Input.class));
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
            startActivity(new Intent(Profile_form.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

