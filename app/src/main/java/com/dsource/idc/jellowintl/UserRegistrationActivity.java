package com.dsource.idc.jellowintl;

/**
 * Created by user on 5/25/2016.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.models.SecureKeys;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Random;

import se.simbio.encryption.Encryption;

import static com.dsource.idc.jellowintl.MainActivity.isDeviceReadyToCall;
import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.getAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.maskNumber;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;

public class UserRegistrationActivity extends AppCompatActivity {
    public static final String LCODE = "lcode";
    public static final String TUTORIAL = "tutorial";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;

    final int GRID_3BY3 = 1;
    private Button bRegister;
    private EditText etName, etEmergencyContact, etEmailId;
    private SessionManager mSession;
    private FirebaseDatabase mDB;
    private DatabaseReference mRef;
    private CountryCodePicker mCcp;
    private String mUserGroup;
    Spinner languageSelect;
    String[] languagesCodes = new String[4], languageNames = new String[4];
    String selectedLanguage;
    String name, emergencyContact, eMailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        FirebaseMessaging.getInstance().subscribeToTopic("jellow_aac");
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.app_name)+"</font>"));

        mSession = new SessionManager(this);
        if(!mSession.getCaregiverNumber().equals("")) {
            getAnalytics(this, mSession.getCaregiverNumber().substring(1));
            mSession.setSessionCreatedAt(new Date().getTime());
            Crashlytics.setUserIdentifier(maskNumber(mSession.getCaregiverNumber().substring(1)));
        }
        if (mSession.isUserLoggedIn() && !mSession.getLanguage().isEmpty())
        {
            if(mSession.isDownloaded(mSession.getLanguage()) && mSession.isCompletedIntro()) {
                startActivity(new Intent(this, SplashActivity.class));
            }else if(mSession.isDownloaded(mSession.getLanguage()) && !mSession.isCompletedIntro()){
                startActivity(new Intent(this, Intro.class));
            }else {
                startActivity(new Intent(UserRegistrationActivity.this,
                        LanguageDownloadActivity.class)
                        .putExtra(LCODE,mSession.getLanguage()).putExtra(TUTORIAL,true));
            }
            finish();
        }else {
            mSession.setBlood(-1);
        }

        mDB = FirebaseDatabase.getInstance();
        mRef = mDB.getReference(BuildConfig.DB_TYPE+"/users");

        LangMap.keySet().toArray(languagesCodes);
        LangMap.keySet().toArray(languageNames);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etName = findViewById(R.id.etName);
        etEmergencyContact = findViewById(R.id.etEmergencyContact);
        mCcp = findViewById(R.id.ccp);
        mCcp.registerCarrierNumberEditText(etEmergencyContact);
        etEmailId= findViewById(R.id.etEmailId);
        bRegister = findViewById(R.id.bRegister);
        bRegister.setAlpha(0.5f);
        bRegister.setEnabled(false);

        etName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etName.getText().toString().isEmpty()){
                    bRegister.setAlpha(0.5f);
                    bRegister.setEnabled(false);
                }else{
                    bRegister.setAlpha(1f);
                    bRegister.setEnabled(true);
                }
            }
        });

        languageSelect = findViewById(R.id.langSelectSpinner);

        ArrayAdapter<String> adapter_lan = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, languageNames);

        adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languageSelect.setAdapter(adapter_lan);
        languageSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLanguage = languagesCodes[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedLanguage = null;
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bRegister.setEnabled(false);

                name  = etName.getText().toString();
                if (etName.getText().toString().equals("")) {
                    bRegister.setEnabled(true);
                    Toast.makeText(getBaseContext(), getString(R.string.enterTheName),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                // Emergency contact is contact number of a caregiver/teacher/parent/therapist.
                // of a child. The contact with country code without preceding '+' and succeding
                // extra 5 random digits are added to emergency contact and is used as unique
                // identifier. Extra 5 digits are added to surpass Firebase behavior.
                // Random number generator logic:
                // max = 1000 and min = 100
                // num = new Random().nextInt((max - min)+1) + min
                // This will ensure 3 digit random number.
                mSession.setExtraValToContact(String.valueOf(new Random().nextInt(900) + 100));
                emergencyContact = mCcp.getFullNumber().concat(mSession.getExtraValToContact());
                if(etEmergencyContact.getText().toString().isEmpty()){
                    bRegister.setEnabled(true);
                    Toast.makeText(getBaseContext(), getString(R.string.enternonemptycontact),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                eMailId = etEmailId.getText().toString().trim();
                if (!isValidEmail(eMailId)){
                    bRegister.setEnabled(true);
                    Toast.makeText(getBaseContext(),getString(R.string.invalid_emailId),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioGroup radioGroup = findViewById(R.id.radioUserGroup);
                if(radioGroup.getCheckedRadioButtonId() == -1){
                    bRegister.setEnabled(true);
                    Toast.makeText(getBaseContext(),getString(R.string.invalid_usergroup),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedLanguage == null)
                    return;

                mUserGroup = (radioGroup.getCheckedRadioButtonId() == R.id.radioParent) ?
                        getString(R.string.groupParentOnly): getString(R.string.groupTeacherOnly);

                // If user device Android os is above Lollipop and user is not denied Call
                // permission once and app do not have the call permission and
                // if user have sim device and ready to call, then show Dialog about
                // Call permission usage.
                if(Build.VERSION.SDK_INT > 22 &&
                        !ActivityCompat.shouldShowRequestPermissionRationale
                                (UserRegistrationActivity.this,
                                    android.Manifest.permission.CALL_PHONE) &&
                        ActivityCompat.checkSelfPermission(UserRegistrationActivity.this,
                                android.Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED &&
                        (isDeviceReadyToCall((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE))))
                    showPermissionRequestDialog();
                else
                    new NetworkConnectionTest(UserRegistrationActivity.this, name,
                            emergencyContact, eMailId, mUserGroup).execute();
            }
        });

        if(!mSession.getName().isEmpty()){
            etName.setText(mSession.getName());
            etEmergencyContact.setText(mSession.getCaregiverNumber());
            etEmailId.setText(mSession.getEmailId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Crashlytics.log("Paused "+getLocalClassName());
    }

    /*private void checkNetworkConnection() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class)) {
                    mSession.setName(name);
                    mSession.setCaregiverNumber(emergencyContact);
                    mSession.setUserCountryCode(mCcp.getSelectedCountryCode());
                    mSession.setEmailId(eMailId);
                    createUser(name, emergencyContact, eMailId,
                            mCcp.getSelectedCountryEnglishName(), selectedLanguage,
                             mUserGroup, mUserGroup);
                    return;
                }
                Toast.makeText(UserRegistrationActivity.this, getString(R.string.checkConnectivity), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }*/

    private void showPermissionRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                showCallPreview();
            }
        });
        // Set other dialog properties
        builder.setCancelable(false);
        builder.setMessage(getString(R.string.call_permission_info));
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        // Show the AlertDialog
        dialog.show();
    }

    private void showCallPreview(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED){
            new NetworkConnectionTest(UserRegistrationActivity.this,
                    name, emergencyContact, eMailId, mUserGroup).execute();
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String Permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(UserRegistrationActivity.this,
                        getString(R.string.granted_call_permission_req), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserRegistrationActivity.this,
                        R.string.rejected_call_permission_req, Toast.LENGTH_SHORT).show();
            }
            new NetworkConnectionTest(UserRegistrationActivity.this,
                    name, emergencyContact, eMailId, mUserGroup).execute();

        }
    }

    private class NetworkConnectionTest extends AsyncTask<Void, Void, Boolean>{
        private Context mContext;
        private String mName, mEmergencyContact, mEmailId, mUserGroup;
        private SessionManager mSession;

        public NetworkConnectionTest(Context context, String name, String emergencyContact,
                                     String eMailId, String userGroup) {
            mContext = context;
            mName = name;
            mEmergencyContact = emergencyContact;
            mEmailId = eMailId;
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
            if(isConnected){
                mSession.setName(mName);
                mSession.setCaregiverNumber("+".concat(emergencyContact));
                mSession.setUserCountryCode(mCcp.getSelectedCountryCode());
                mSession.setEmailId(mEmailId);
                encryptStoreUserInfo(mName, emergencyContact, eMailId, mUserGroup);
            }else{
                bRegister.setEnabled(true);
                Toast.makeText(mContext, getString(R.string.checkConnectivity), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void encryptStoreUserInfo(final String name, final String contact,
                                      final String email, final String userGroup) {
        FirebaseStorage storage =  FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("jellow-json.json");
        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                try {
                    String jsonData = new String(bytes, "UTF-8");
                    mSession.setEncryptionData(jsonData);
                    SecureKeys secureKey = new Gson().
                            fromJson(mSession.getEncryptedData(), SecureKeys.class);
                    Crashlytics.log("Created secure key.");
                    createUser(encrypt(name, secureKey), contact,
                            encrypt(email, secureKey),
                            encrypt(mCcp.getSelectedCountryEnglishName(), secureKey),
                            encrypt(selectedLanguage, secureKey),
                            encrypt(userGroup, secureKey), userGroup);
                } catch (UnsupportedEncodingException e) {
                    Crashlytics.logException(e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Crashlytics.logException(exception);
            }
        });
    }

    private String encrypt(String plainText, SecureKeys secureKey) {
        Encryption encryption = Encryption.getDefault(secureKey.getKey(), secureKey.getSalt(), new byte[16]);
        return encryption.encryptOrNull(plainText).trim();
    }

    private void createUser(final String name, final String emergencyContact, String eMailId,
                            String country, String firstLang, final String userGroup,
                            final String decUserGroup)
    {
        try {
            final String userId = maskNumber(emergencyContact);
            getAnalytics(UserRegistrationActivity.this, emergencyContact);
            mSession.setSessionCreatedAt(new Date().getTime());
            mRef.child(userId).child("email").setValue(eMailId);
            mRef.child(userId).child("emergencyContact").setValue(userId);
            mRef.child(userId).child("name").setValue(name);
            mRef.child(userId).child("country").setValue(country);
            mRef.child(userId).child("firstLanguage").setValue(firstLang);
            mRef.child(userId).child("userGroup").setValue(userGroup);
            mRef.child(userId).child("joinedOn").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mSession.setUserLoggedIn(true);
                        mSession.setLanguage(LangMap.get(selectedLanguage));
                        mSession.setGridSize(GRID_3BY3);
                        mSession.setUserGroup(decUserGroup);
                        Bundle bundle = new Bundle();
                        bundle.putString("LanguageSet", "First time "+ LangMap.get(selectedLanguage));
                        setUserProperty("UserId", userId);
                        setUserProperty("UserLanguage", LangMap.get(selectedLanguage));
                        setUserProperty("UserGroup", decUserGroup);
                        bundleEvent("Language", bundle);
                        Crashlytics.setUserIdentifier(userId);
                        bundle.clear();
                        bundle.putString(LCODE,LangMap.get(selectedLanguage));
                        bundle.putBoolean(TUTORIAL,true);
                        startActivity(new Intent(UserRegistrationActivity.this,
                                LanguageDownloadActivity.class).putExtras(bundle));
                        finish();
                    } else {
                        bRegister.setEnabled(true);
                        Crashlytics.log("User data not added.");
                        Toast.makeText(UserRegistrationActivity.this, getString(R.string.checkConnectivity), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}