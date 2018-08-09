package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_DropDownMenu;
import com.dsource.idc.jellowintl.models.SecureKeys;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
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

import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.getAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.maskNumber;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;

/**
 * Created by user on 5/25/2016.
 */
public class UserRegistrationActivity extends AppCompatActivity {
    public static final String LCODE = "lcode";
    public static final String TUTORIAL = "tutorial";

    final int GRID_3BY3 = 1;
    private Button bRegister;
    private EditText etName, etEmergencyContact, etEmailId;
    private SessionManager mSession;
    private FirebaseDatabase mDB;
    private DatabaseReference mRef;
    private CountryCodePicker mCcp;
    private String mUserGroup;
    Spinner languageSelect;
    String[] languagesCodes = new String[5], languageNames = new String[5];
    String selectedLanguage;
    String name, emergencyContact, eMailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
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
                if(!mSession.getUpdatedFirebase())
                    updateFirebaseDatabase();
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
        ViewCompat.setAccessibilityDelegate(mCcp, new TalkbackHints_DropDownMenu());
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
                R.layout.simple_spinner_item, shortLangNameForDisplay(languageNames));

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
                    Toast.makeText(UserRegistrationActivity.this,
                            getString(R.string.enterTheName), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Emergency contact is contact number of a caregiver/teacher/parent/therapist.
                // of a child. The contact with country code without preceding '+' and succeding
                // extra 3 random digits are added to emergency contact and is used as unique
                // identifier. Extra 3 digits are added to surpass Firebase behavior.
                // Random number generator logic:
                // max = 1000 and min = 100
                // num = new Random().nextInt((max - min)+1) + min
                // This will ensure 3 digit random number.
                mSession.setExtraValToContact(String.valueOf(new Random().nextInt(900) + 100));
                emergencyContact = mCcp.getFullNumber().concat(mSession.getExtraValToContact());
                if(etEmergencyContact.getText().toString().isEmpty() ||
                        !etEmergencyContact.getText().toString().matches("[0-9]*")){
                    bRegister.setEnabled(true);
                    Toast.makeText(UserRegistrationActivity.this,
                            getString(R.string.enternonemptycontact), Toast.LENGTH_SHORT).show();
                    return;
                }
                eMailId = etEmailId.getText().toString().trim();
                if (!isValidEmail(eMailId)){
                    bRegister.setEnabled(true);
                    Toast.makeText(UserRegistrationActivity.this,getString(R.string.invalid_emailId),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioGroup radioGroup = findViewById(R.id.radioUserGroup);
                if(radioGroup.getCheckedRadioButtonId() == -1){
                    bRegister.setEnabled(true);
                    Toast.makeText(UserRegistrationActivity.this,
                            getString(R.string.invalid_usergroup), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedLanguage == null)
                    return;

                mUserGroup = (radioGroup.getCheckedRadioButtonId() == R.id.radioParent) ?
                        getString(R.string.groupParentOnly): getString(R.string.groupTeacherOnly);
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

    private void updateFirebaseDatabase() {
        String userId = maskNumber(mSession.getCaregiverNumber().substring(1));
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(BuildConfig.DB_TYPE+"/users/" + userId);
        SecureKeys secureKey = new Gson().
                fromJson(mSession.getEncryptedData(), SecureKeys.class);
        if (secureKey == null)
            return;
        ref.child("email").setValue(encrypt(mSession.getEmailId(),secureKey));
        ref.child("emergencyContact").setValue(userId);
        ref.child("name").setValue(encrypt(mSession.getName(), secureKey));
        ref.child("userGroup").setValue(encrypt(mSession.getUserGroup(), secureKey));
        ref.child("bloodGroup").setValue(encrypt(getBloodGroup(mSession.getBlood()), secureKey));
        if(!mSession.getCaregiverNumber().isEmpty())
            ref.child("caregiverName").setValue(encrypt(mSession.getCaregiverName(), secureKey));

        if(!mSession.getAddress().isEmpty())
            ref.child("address").setValue(encrypt(mSession.getAddress(), secureKey));
        ref.child("updatedOn").setValue(ServerValue.TIMESTAMP);
        mSession.setUpdatedFirebase(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Crashlytics.log("Paused "+getLocalClassName());
    }

    private boolean isValidEmail(CharSequence target) {
        return target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
                Toast.makeText(mContext, getString(R.string.register_user), Toast.LENGTH_SHORT).show();
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

    private String[] shortLangNameForDisplay(String[] langNameToBeShorten) {
        String[] shortenLanguageNames = new String[langNameToBeShorten.length];
        for (int i=0; i < langNameToBeShorten.length; i++){
            switch (langNameToBeShorten[i]){
                case "English (India)":
                    shortenLanguageNames[i] = "English (IN)";
                    break;
                case "English (United Kingdom)":
                    shortenLanguageNames[i] = "English (UK)";
                    break;
                case "English (United States)":
                    shortenLanguageNames[i] = "English (US)";
                    break;
                default:
                    shortenLanguageNames[i] = langNameToBeShorten[i];
                    break;
            }
        }
        return shortenLanguageNames;
    }

    private String getBloodGroup(int bloodGroup) {
        switch(bloodGroup){
            case 1: return "A+ve";
            case 2: return "A-ve";
            case 3: return "B+ve";
            case 4: return "B-ve";
            case 5: return "AB+ve";
            case 6: return "AB-ve";
            case 7: return "O+ve";
            case 8: return "O-ve";
            default: return "not selected";
        }
    }
}
