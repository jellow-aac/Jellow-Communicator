package com.dsource.idc.jellowintl;

/**
 * Created by user on 5/25/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dsource.idc.jellowintl.utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;

public class ProfileFormActivity extends AppCompatActivity {
    private Button bSave;
    private EditText etName, etFatherContact, etFathername, etAddress, etEmailId;
    private SessionManager mSession;
    private String email;
    private CountryCodePicker mCcp;
    FirebaseDatabase mDB;
    DatabaseReference mRef;
    private boolean mAutoSetBGSpinner = true, mUserSetTheBloodGroup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);
        new ChangeAppLocale(this).setLocale();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.menuProfile)+"</font>"));
        mSession = new SessionManager(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etName = findViewById(R.id.etName);
        etFathername = findViewById(R.id.etFathername);
        etFatherContact = findViewById(R.id.etFathercontact);
        etAddress = findViewById(R.id.etAddress);
        etEmailId = findViewById(R.id.etEmailId);
        final Spinner bloodgroup = findViewById(R.id.bloodgroup);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bloodgroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodgroup.setAdapter(adapter);
        bSave = findViewById(R.id.bSave);

        mDB = FirebaseDatabase.getInstance();
        mRef = mDB.getReference(BuildConfig.DB_TYPE+"/users");


        etName.setText(mSession.getName());
        mCcp = findViewById(R.id.ccp);
        mCcp.setCountryForPhoneCode(Integer.valueOf(mSession.getUserCountryCode()));
        mCcp.registerCarrierNumberEditText(etFatherContact);
        etFatherContact.setText(mSession.getCaregiverNumber().replace("+".concat(mSession.getUserCountryCode()),""));
        etFathername.setText(mSession.getCaregiverName());
        etAddress.setText(mSession.getAddress());
        etEmailId.setText(mSession.getEmailId());
        if(mSession.getBlood() != -1) bloodgroup.setSelection(mSession.getBlood());

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

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmailId.getText().toString().trim();
                if (etName.getText().toString().length() > 0) {
                        if (isValidEmail(email)) {
                            if(mCcp.getFullNumberWithPlus().equals(mSession.getCaregiverNumber()))
                            {
                                String emergencyContact = mSession.getCaregiverNumber();
                                mRef.child(emergencyContact).child("email").setValue(email);
                                mRef.child(emergencyContact).child("name").setValue(etName.getText().toString());

                            }else {

                                String emergencyContact = mCcp.getFullNumberWithPlus();
                                mRef.child(emergencyContact).child("emergencyContact").setValue(emergencyContact);
                                mRef.child(emergencyContact).child("email").setValue(email);
                                mRef.child(emergencyContact).child("name").setValue(etName.getText().toString());
                            }

                            mSession.setCaregiverName(etFathername.getText().toString());
                            mSession.setAddress(etAddress.getText().toString());
                            mSession.setName(etName.getText().toString());
                            mSession.setCaregiverNumber(mCcp.getFullNumberWithPlus());
                            mSession.setUserCountryCode(mCcp.getSelectedCountryCode());
                            mSession.setEmailId(email);
                            if(bloodgroup.getSelectedItemPosition() > 0)
                                mSession.setBlood(bloodgroup.getSelectedItemPosition());
                            else
                                mSession.setBlood(-1);

                            Toast.makeText(ProfileFormActivity.this, getString(R.string.detailSaved), Toast.LENGTH_SHORT).show();
                            finish();

                        } else
                            Toast.makeText(getApplicationContext(), getString(R.string.invalid_emailId), Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(ProfileFormActivity.this, getString(R.string.enterTheName), Toast.LENGTH_SHORT).show();
            }
        });
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
            case R.id.languageSelect: startActivity(new Intent(this, LanguageSelectActivity.class)); finish(); break;
            case R.id.info: startActivity(new Intent(this, AboutJellowActivity.class)); finish(); break;
            case R.id.usage: startActivity(new Intent(this, TutorialActivity.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(this, KeyboardInputActivity.class)); finish(); break;
            case R.id.settings: startActivity(new Intent(getApplication(), SettingActivity.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(this, ResetPreferencesActivity.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(this, FeedbackActivity.class)); finish(); break;
            case android.R.id.home: finish(); break;
            default: return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMeasuring("ProfileFormActivity");
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
         startMeasuring();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}