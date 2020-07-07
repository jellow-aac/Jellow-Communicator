package com.dsource.idc.jellowintl.activities;

/**
 * Created by Rahul on 12 Nov, 2019.
 */

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.hbb20.CountryCodePicker;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

public class ProfileFormActivity extends SpeechEngineBaseActivity {
    private Button btnSave;
    private EditText etChildName, etCaregiverContact, etCaregiverName, etAddress, etEmailId;
    private String mDetailSaved;
    private CountryCodePicker mCcp;
    private Spinner mBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);
        setupActionBarTitle(View.VISIBLE, getString(R.string.home)+"/ "+getString(R.string.menuProfile));
        setNavigationUiConditionally();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etChildName = findViewById(R.id.etName);
        etCaregiverName = findViewById(R.id.etFathername);
        etCaregiverContact = findViewById(R.id.etFathercontact);
        etAddress = findViewById(R.id.etAddress);
        etEmailId = findViewById(R.id.etEmailAddress);
        mBloodGroup = findViewById(R.id.bloodgroup);
        ((TextView)findViewById(R.id.view_privacy_policy)).setText(Html.fromHtml(getString(R.string.txt_view_privacy_policy)));
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.bloodgroup_talkback, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mBloodGroup.setAdapter(adapter);
        }else{
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this, R.array.bloodgroup, R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mBloodGroup.setAdapter(adapter);
        }
        btnSave = findViewById(R.id.bSave);

        etChildName.setText(getSession().getName());

        mCcp = findViewById(R.id.ccp);
        mCcp.setCountryForPhoneCode(Integer.valueOf(getSession().getUserCountryCode()));
        mCcp.registerCarrierNumberEditText(etCaregiverContact);
        String contact = getSession().getCaregiverNumber().replace(
                "+".concat(getSession().getUserCountryCode()),"");
        etCaregiverContact.setText(contact);

        etCaregiverName.setText(getSession().getCaregiverName());

        etAddress.setText(getSession().getAddress());

        etEmailId.setText(getSession().getEmailId());

        if(getSession().getBlood() != -1)
            mBloodGroup.setSelection(getSession().getBlood());

        final String strEnterName = getString(R.string.enterTheName);
        final String strInvalidEmail = getString(R.string.invalid_emailId);
        final String strInvalidAddress = getString(R.string.invalid_address);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("Profile Save");
                btnSave.setEnabled(false);
                if (etChildName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ProfileFormActivity.this,
                            strEnterName, Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    return;
                }
                if(!etCaregiverContact.getText().toString().matches("[0-9]+")){
                    btnSave.setEnabled(true);
                    Toast.makeText(ProfileFormActivity.this,
                            getString(R.string.enternonemptycontact), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etAddress.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ProfileFormActivity.this,
                            strInvalidAddress, Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    return;
                }
                if (!isValidEmail(etEmailId.getText().toString().trim())) {
                    Toast.makeText(ProfileFormActivity.this,
                            strInvalidEmail, Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    return;
                }
                savedProfileDetails();
            }
        });

        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        mDetailSaved = getString(R.string.detailSaved);
        if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            findViewById(R.id.tvName).setFocusableInTouchMode(true);
            findViewById(R.id.tvName).setFocusable(true);
            mCcp.setCountryPreference(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        stopMeasuring("ProfileFormActivity");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(ProfileFormActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
        }
        startMeasuring();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        addAccessibilityDelegateToSpinners();
    }

    private void addAccessibilityDelegateToSpinners() {
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            mBloodGroup.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                @Override
                public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
                    super.onInitializeAccessibilityEvent(host, event);
                    if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                        btnSave.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                    }
                }
            });
        }
    }

    private void savedProfileDetails() {
        getSession().setCaregiverName(etCaregiverName.getText().toString());
        getSession().setAddress(etAddress.getText().toString());
        getSession().setName(etChildName.getText().toString());
        getSession().setCaregiverNumber(mCcp.getFullNumberWithPlus());
        getSession().setUserCountryCode(mCcp.getSelectedCountryCode());
        getSession().setEmailId(etEmailId.getText().toString().trim());
        if(mBloodGroup.getSelectedItemPosition() > 0)
            getSession().setBlood(mBloodGroup.getSelectedItemPosition());
        else
            getSession().setBlood(-1);
        getSession().setToastMessage(mDetailSaved);
        if(getSession().getLanguage().endsWith(MR_IN)) {
            createUserProfileRecordingsUsingTTS();
        }
        FirebaseDatabase mDB = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDB.getReference(BuildConfig.DB_TYPE+"/users/"+getSession().getUserId());
        mRef.child("updatedOn").setValue(ServerValue.TIMESTAMP);
        mRef.child("versionCode").setValue(BuildConfig.VERSION_CODE);
        finish();
    }
}