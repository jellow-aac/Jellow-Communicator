package com.dsource.idc.jellowintl;

/**
 * Created by user on 5/25/2016.
 */

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
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
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
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

import se.simbio.encryption.Encryption;

import static com.dsource.idc.jellowintl.MainActivity.isAccessibilityTalkBackOn;
import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.maskNumber;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.updateSessionRef;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

public class ProfileFormActivity extends AppCompatActivity {
    private Button bSave;
    private EditText etName, etFatherContact, etFathername, etAddress, etEmailId;
    private SessionManager mSession;
    private String email, mUserGroup;
    private CountryCodePicker mCcp;
    private Spinner mBloodGroup;
    private FirebaseDatabase mDB;
    private DatabaseReference mRef;
    private String mDetailSaved, mCheckCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_profile_form);
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
        mBloodGroup = findViewById(R.id.bloodgroup);
        AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        boolean isAccessibilityEnabled = am.isEnabled();
        boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
        if(isAccessibilityEnabled && isExploreByTouchEnabled) {
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
                maskNumber(mSession.getCaregiverNumber().substring(1)));

        etName.setText(mSession.getName());
        mCcp = findViewById(R.id.ccp);
        mCcp.setCountryForPhoneCode(Integer.valueOf(mSession.getUserCountryCode()));
        mCcp.registerCarrierNumberEditText(etFatherContact);
        String contact = mSession.getCaregiverNumber().replace(
                "+".concat(mSession.getUserCountryCode()),"");
        //Extra 3 digits are taken out from contact.
        contact = contact.substring(0,contact.length()-3);
        etFatherContact.setText(contact);
        etFathername.setText(mSession.getCaregiverName());
        etAddress.setText(mSession.getAddress());
        etEmailId.setText(mSession.getEmailId());
        if(mSession.getBlood() != -1)
            mBloodGroup.setSelection(mSession.getBlood());

        if (!mSession.getUserGroup().isEmpty() &&
                mSession.getUserGroup().equals(getString(R.string.groupParentOnly)))
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
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.CREATE_ABOUT_ME_RECORDING_RES");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(mSession.getLanguage().equals(BN_IN))
            menu.findItem(R.id.keyboardinput).setVisible(false);
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            menu.findItem(R.id.closePopup).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.languageSelect:
                if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, LanguageSelectActivity.class));
                } else {
                    startActivity(new Intent(this, LanguageSelectTalkBackActivity.class));
                }
                finish(); break;
            case R.id.info:
                startActivity(new Intent(this, AboutJellowActivity.class));
                finish(); break;
            case R.id.usage:
                startActivity(new Intent(this, TutorialActivity.class));
                finish(); break;
            case R.id.keyboardinput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                finish(); break;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                finish(); break;
            case R.id.reset:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                finish(); break;
            case R.id.feedback:
                if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, FeedbackActivityTalkBack.class));
                }
                else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
                finish();
                break;
            case android.R.id.home:
                finish(); break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(mSession.getSessionCreatedAt());
        mSession.setSessionCreatedAt(sessionTime);

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
        if(!isAnalyticsActive()){
            resetAnalytics(this, mSession.getCaregiverNumber().substring(1));
        }
        if(!isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        startMeasuring();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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
        if(mSession.getEncryptedData().isEmpty()) {
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
            SecureKeys secureKey = new Gson().fromJson(mSession.getEncryptedData(), SecureKeys.class);
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
        mSession.setCaregiverName(etFathername.getText().toString());
        mSession.setAddress(etAddress.getText().toString());
        mSession.setName(etName.getText().toString());
        mSession.setCaregiverNumber(mCcp.getFullNumberWithPlus() + mSession.getExtraValToContact());
        mSession.setUserCountryCode(mCcp.getSelectedCountryCode());
        mSession.setEmailId(email);
        if(mBloodGroup.getSelectedItemPosition() > 0)
            mSession.setBlood(mBloodGroup.getSelectedItemPosition());
        else
            mSession.setBlood(-1);
        mSession.setUserGroup(userGroup);
        setUserProperty("userGroup", userGroup);
        mSession.setToastMessage(mDetailSaved);
        if(mSession.getLanguage().endsWith(MR_IN)) {
            sendBroadcast(new Intent("com.dsource.idc.jellowintl.CREATE_ABOUT_ME_RECORDING_REQ"));
            return;
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

                mSession.setCaregiverNumber(mCcp.getFullNumberWithPlus() +
                                    mSession.getExtraValToContact());
                mRef = mDB.getReference(BuildConfig.DB_TYPE + "/users/" +
                        maskNumber(mSession.getCaregiverNumber().substring(1)));
                mRef.removeEventListener(this);
                Crashlytics.setUserIdentifier(maskNumber(mSession.getCaregiverNumber().substring(1)));
                updateSessionRef(mSession.getCaregiverNumber().substring(1));
                encryptStoreUserInfo(etName.getText().toString(),
                        mSession.getCaregiverNumber().substring(1),
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

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellowintl.CREATE_ABOUT_ME_RECORDING_RES":
                    finish();
                    break;
            }
        }
    };

    private class NetworkConnectionTest extends AsyncTask<Void, Void, Boolean> {
        private Context mContext;
        private String mName, mContact, mEmailId, mCaregiverName, mAddress, mUserGroup;
        private SessionManager mSession;

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
            mSession = new SessionManager(mContext);
        }

        boolean isConnectedToNetwork(){
            final ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(!isConnectedToNetwork())
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
                    mSession.getExtraValToContact();
            //If user not changed the mobile number and encryption data is
            // available then directly encrypt the data and store it to firebase
            if(extraValContact.equals(mSession.getCaregiverNumber()) &&
                !mSession.getEncryptedData().isEmpty()) {
                encryptStoreUserInfo(etName.getText().toString(),
                        mSession.getCaregiverNumber().substring(1),
                        email,
                        etFathername.getText().toString(),
                        etAddress.getText().toString(),
                        getBloodGroup(), mUserGroup);
            //If user not changed the mobile number and encryption data is
            // unavailable and user is connected to Internet then
            // download encryption data and then encrypt the data and store it to firebase
            }else if(extraValContact.equals(mSession.getCaregiverNumber()) &&
                        mSession.getEncryptedData().isEmpty() && isConnected){
                encryptStoreUserInfo(etName.getText().toString(),
                        mSession.getCaregiverNumber().substring(1),
                        email,
                        etFathername.getText().toString(),
                        etAddress.getText().toString(),
                        getBloodGroup(), mUserGroup);
            //If user not changed the mobile number and encryption data is
            // unavailable and user is not connected to Internet then
            // show error message.
            }else if(extraValContact.equals(mSession.getCaregiverNumber()) &&
                    mSession.getEncryptedData().isEmpty() && !isConnected){
                bSave.setEnabled(true);
                Toast.makeText(mContext, mCheckCon, Toast.LENGTH_LONG).show();
            //If user changed the mobile number user is connected to Internet then
            // then encrypt the data and store it to firebase
            }else if(!extraValContact.equals(mSession.getCaregiverNumber()) && isConnected){
                mSession.setExtraValToContact(String.valueOf(new Random().nextInt(900) + 100));
                String newContact = mContact.concat(mSession.getExtraValToContact());
                DatabaseReference mNewRef = mDB.getReference(BuildConfig.DB_TYPE
                        + "/users/" + maskNumber(newContact)
                        + "/previousRecords/" + maskNumber(mSession.getCaregiverNumber().substring(1)));
                moveFirebaseRecord(mRef, mNewRef);
            //If user changed the mobile number user is not connected to Internet then
            // show error message.
            }else if(!extraValContact.equals(mSession.getCaregiverNumber()) && !isConnected){
                bSave.setEnabled(true);
                Toast.makeText(mContext, mCheckCon, Toast.LENGTH_LONG).show();
            }
        }
    }
}
