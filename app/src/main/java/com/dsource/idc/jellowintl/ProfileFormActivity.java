package com.dsource.idc.jellowintl;

/**
 * Created by user on 5/25/2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.models.SecureKeys;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import androidx.annotation.NonNull;
import se.simbio.encryption.Encryption;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.maskNumber;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.updateSessionRef;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

public class ProfileFormActivity extends SpeechEngineBaseActivity {
    private Button bSave;
    private EditText etName, etFatherContact, etFathername, etAddress, etEmailId;
    private String email, mUserGroup;
    private CountryCodePicker mCcp;
    private Spinner mBloodGroup;
    private FirebaseDatabase mDB;
    private DatabaseReference mRef;
    private String mDetailSaved, mCheckCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);
        enableNavigationBack();
        setActivityTitle(getString(R.string.menuProfile));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etName = findViewById(R.id.etName);
        etFathername = findViewById(R.id.etFathername);
        etFatherContact = findViewById(R.id.etFathercontact);
        etAddress = findViewById(R.id.etAddress);
        etEmailId = findViewById(R.id.etEmailId);
        mBloodGroup = findViewById(R.id.bloodgroup);
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.bloodgroup_talkback, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mBloodGroup.setAdapter(adapter);
        }
        else{
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.bloodgroup, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodGroup.setAdapter(adapter);
        }
        bSave = findViewById(R.id.bSave);

        mDB = FirebaseDatabase.getInstance();
        mRef = mDB.getReference(BuildConfig.DB_TYPE+"/users/" +
                maskNumber(getSession().getCaregiverNumber().substring(1)));

        etName.setText(getSession().getName());
        mCcp = findViewById(R.id.ccp);
        mCcp.setCountryForPhoneCode(Integer.valueOf(getSession().getUserCountryCode()));
        mCcp.registerCarrierNumberEditText(etFatherContact);
        String contact = getSession().getCaregiverNumber().replace(
                "+".concat(getSession().getUserCountryCode()),"");
        //Extra 3 digits are taken out from contact.
        contact = contact.substring(0,contact.length()-3);
        etFatherContact.setText(contact);
        etFathername.setText(getSession().getCaregiverName());
        etAddress.setText(getSession().getAddress());
        etEmailId.setText(getSession().getEmailId());
        if(getSession().getBlood() != -1)
            mBloodGroup.setSelection(getSession().getBlood());

        if (!getSession().getUserGroup().isEmpty() &&
                getSession().getUserGroup().equals(getString(R.string.groupParentOnly)))
            ((RadioButton)findViewById(R.id.radioParent)).setChecked(true);
        else
            ((RadioButton)findViewById(R.id.radioTherapist)).setChecked(true);

        final String strEnterName = getString(R.string.enterTheName);
        final String strInvalidEmail = getString(R.string.invalid_emailId);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("Profile Save");
                bSave.setEnabled(false);
                if (etName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ProfileFormActivity.this,
                            strEnterName, Toast.LENGTH_SHORT).show();
                    bSave.setEnabled(true);
                    return;
                }
                if(!etFatherContact.getText().toString().matches("[0-9]+")){
                    bSave.setEnabled(true);
                    Toast.makeText(ProfileFormActivity.this,
                            getString(R.string.enternonemptycontact), Toast.LENGTH_SHORT).show();
                    return;
                }
                email = etEmailId.getText().toString().trim();
                if (!isValidEmail(email)) {
                    Toast.makeText(ProfileFormActivity.this,
                            strInvalidEmail, Toast.LENGTH_SHORT).show();
                    bSave.setEnabled(true);
                    return;
                }
                RadioGroup radioGroup = findViewById(R.id.radioUserGroup);
                mUserGroup = (radioGroup.getCheckedRadioButtonId() == R.id.radioParent) ?
                        getString(R.string.groupParentOnly): getString(R.string.groupTeacherOnly);

                new NetworkConnectionTest(ProfileFormActivity.this,
                        etName.getText().toString(),
                        mCcp.getFullNumber(),
                        email,
                        etFathername.getText().toString(),
                        etAddress.getText().toString(),
                        mUserGroup).execute();
            }
        });
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        mDetailSaved = getString(R.string.detailSaved);
        mCheckCon = getString(R.string.checkConnectivity);
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
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
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
                        bSave.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                    }
                }
            });
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private String getBloodGroup() {
        switch(mBloodGroup.getSelectedItem().toString()){
            case "A+ve":
            case "अ +":
                return "A+ve";
            case "A-ve":
            case "अ -":
                return "A-ve";
            case "B+ve":
            case "ब +":
                return "B+ve";
            case "B-ve":
            case "ब -":
                return "B-ve";
            case "AB+ve":
            case "अब +":
                return "AB+ve";
            case "AB-ve":
            case "अब -":
                return "AB-ve";
            case "O+ve":
            case "ओ +":
                return "O+ve";
            case "O-ve":
            case "ओ -":
                return "O-ve";
            default:
                return "not selected";
        }
    }

    private void encryptStoreUserInfo(final String name, final String contact,
                                      final String email, final String caregiverName,
                                      final String address, final String bloodGroup,
                                      final String userGroup) {
        if(getSession().getEncryptedData().isEmpty()) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference pathReference = storageRef.child("jellow-json.json");
            final long ONE_MEGABYTE = 1024 * 1024;
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    String jsonData = new String(bytes, StandardCharsets.UTF_8);
                    SecureKeys secureKey = new Gson().fromJson(jsonData, SecureKeys.class);
                    encryptStoreUserInfoUsingSecureKey(name, contact, email,
                            caregiverName, address, bloodGroup, userGroup, secureKey);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Crashlytics.logException(exception);
                }
            });
        }else {
            SecureKeys secureKey = new Gson().fromJson(getSession().getEncryptedData(), SecureKeys.class);
            encryptStoreUserInfoUsingSecureKey(name, contact, email,
                        caregiverName, address, bloodGroup, userGroup, secureKey);
        }
    }

    private void encryptStoreUserInfoUsingSecureKey(String name, String contact, String email,
                String caregiverName, String address, String bloodGroup,
                        String userGroup, SecureKeys secureKey) {
        mRef.child("emergencyContact").setValue(maskNumber(contact));
        mRef.child("name").setValue(encrypt(name, secureKey));
        mRef.child("email").setValue(encrypt(email, secureKey));
        if(!caregiverName.isEmpty())
            mRef.child("caregiverName").setValue(encrypt(caregiverName, secureKey));
        if(!address.isEmpty())
            mRef.child("address").setValue(encrypt(address, secureKey));
        mRef.child("bloodGroup").setValue(encrypt(bloodGroup, secureKey));
        mRef.child("userGroup").setValue(encrypt(userGroup, secureKey));
        mRef.child("updatedOn").setValue(ServerValue.TIMESTAMP);
        mRef.child("versionCode").setValue(BuildConfig.VERSION_CODE);
        savedProfileDetails(userGroup);
    }

    private String encrypt(String plainText, SecureKeys secureKey) {
        Encryption encryption = Encryption.getDefault(secureKey.getKey(),
                secureKey.getSalt(), new byte[16]);
        return encryption.encryptOrNull(plainText).trim();
    }

    private void savedProfileDetails(String userGroup) {
        getSession().setCaregiverName(etFathername.getText().toString());
        getSession().setAddress(etAddress.getText().toString());
        getSession().setName(etName.getText().toString());
        getSession().setCaregiverNumber(mCcp.getFullNumberWithPlus() + getSession().getExtraValToContact());
        getSession().setUserCountryCode(mCcp.getSelectedCountryCode());
        getSession().setEmailId(email);
        if(mBloodGroup.getSelectedItemPosition() > 0)
            getSession().setBlood(mBloodGroup.getSelectedItemPosition());
        else
            getSession().setBlood(-1);
        getSession().setUserGroup(userGroup);
        setUserProperty("userGroup", userGroup);
        getSession().setToastMessage(mDetailSaved);
        if(getSession().getLanguage().endsWith(MR_IN)) {
            createUserProfileRecordingsUsingTTS();
        }
        finish();
    }

    public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath)
    {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // remove the scrollListener to avoid any 'intermediate' updates while working in the same node
                mRef.removeEventListener(this);
                processPreviousRecordsNode(dataSnapshot,toPath);
                mRef.setValue(null);

                getSession().setCaregiverNumber(mCcp.getFullNumberWithPlus() +
                                    getSession().getExtraValToContact());
                mRef = mDB.getReference(BuildConfig.DB_TYPE + "/users/" +
                        maskNumber(getSession().getCaregiverNumber().substring(1)));
                mRef.removeEventListener(this);
                Crashlytics.setUserIdentifier(maskNumber(getSession().getCaregiverNumber().substring(1)));
                updateSessionRef(getSession().getCaregiverNumber().substring(1));
                encryptStoreUserInfo(etName.getText().toString(),
                        getSession().getCaregiverNumber().substring(1),
                        email,
                        etFathername.getText().toString(),
                        etAddress.getText().toString(),
                        getBloodGroup(), mUserGroup);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                }
        });
    }

    public void processPreviousRecordsNode(DataSnapshot snapshot, DatabaseReference mNewRef){

        if(snapshot.hasChild("previousRecords")){

            // get the previous records node of the old number
            Object o = snapshot.child("previousRecords").getValue();
            // set the entire 'old number -> previous records node' value to 'new number -> prevrecords node'
            mNewRef.getParent().setValue(o);
            // set the entire value of 'old number' node to 'new number -> prevrecords node'
            mNewRef.setValue(snapshot.getValue());
            // remove the 'prevRecords' node from the copied 'old number' node in the 'new number -> prevrecords -> old number'
            mNewRef.child("previousRecords").setValue(null);

        } else {

            // if old number doesn't have previous records then just copy it into 'new number -> prevrecords node'
            mNewRef.setValue(snapshot.getValue());
        }
    }

    private class NetworkConnectionTest extends AsyncTask<Void, Void, Boolean> {
        private Context mContext;
        private String mName, mContact, mEmailId, mCaregiverName, mAddress, mUserGroup;

        public NetworkConnectionTest(Context context, String name, String contact,
                                     String email, String caregiverName, String address,
                                     String userGroup) {
            mContext = context;
            mName = name;
            mContact = contact;
            mEmailId = email;
            mCaregiverName = caregiverName;
            mAddress = address;
            mUserGroup = userGroup;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (!isConnectedToNetwork((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)))
                return false;
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(3000);
                urlc.connect();
                if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK)
                    return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            super.onPostExecute(isConnected);
            String extraValContact = mCcp.getFullNumberWithPlus() +
                    getSession().getExtraValToContact();
            //If user not changed the mobile number and encryption data is
            // available then directly encrypt the data and store it to firebase
            if(extraValContact.equals(getSession().getCaregiverNumber()) &&
                !getSession().getEncryptedData().isEmpty()) {
                encryptStoreUserInfo(etName.getText().toString(),
                        getSession().getCaregiverNumber().substring(1),
                        email,
                        etFathername.getText().toString(),
                        etAddress.getText().toString(),
                        getBloodGroup(), mUserGroup);
            //If user not changed the mobile number and encryption data is
            // unavailable and user is connected to Internet then
            // download encryption data and then encrypt the data and store it to firebase
            }else if(extraValContact.equals(getSession().getCaregiverNumber()) &&
                        getSession().getEncryptedData().isEmpty() && isConnected){
                encryptStoreUserInfo(etName.getText().toString(),
                        getSession().getCaregiverNumber().substring(1),
                        email,
                        etFathername.getText().toString(),
                        etAddress.getText().toString(),
                        getBloodGroup(), mUserGroup);
            //If user not changed the mobile number and encryption data is
            // unavailable and user is not connected to Internet then
            // show error message.
            }else if(extraValContact.equals(getSession().getCaregiverNumber()) &&
                    getSession().getEncryptedData().isEmpty() && !isConnected){
                bSave.setEnabled(true);
                Toast.makeText(mContext, mCheckCon, Toast.LENGTH_LONG).show();
            //If user changed the mobile number user is connected to Internet then
            // then encrypt the data and store it to firebase
            }else if(!extraValContact.equals(getSession().getCaregiverNumber()) && isConnected){
                getSession().setExtraValToContact(String.valueOf(new Random().nextInt(900) + 100));
                String newContact = mContact.concat(getSession().getExtraValToContact());
                DatabaseReference mNewRef = mDB.getReference(BuildConfig.DB_TYPE
                        + "/users/" + maskNumber(newContact)
                        + "/previousRecords/" + maskNumber(getSession().getCaregiverNumber().substring(1)));
                moveFirebaseRecord(mRef, mNewRef);
            //If user changed the mobile number user is not connected to Internet then
            // show error message.
            }else if(!extraValContact.equals(getSession().getCaregiverNumber()) && !isConnected){
                bSave.setEnabled(true);
                Toast.makeText(mContext, mCheckCon, Toast.LENGTH_LONG).show();
            }
        }
    }
}
