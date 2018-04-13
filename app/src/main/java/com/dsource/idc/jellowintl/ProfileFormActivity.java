package com.dsource.idc.jellowintl;

/**
 * Created by user on 5/25/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.dsource.idc.jellowintl.models.SecureKeys;
import com.dsource.idc.jellowintl.utility.ChangeAppLocale;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import se.simbio.encryption.Encryption;

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.maskNumber;
import static com.dsource.idc.jellowintl.utility.Analytics.reportException;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.updateSessionRef;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class ProfileFormActivity extends AppCompatActivity {
    private Button bSave;
    private EditText etName, etFatherContact, etFathername, etAddress, etEmailId;
    private SessionManager mSession;
    private String email;
    private CountryCodePicker mCcp;
    private Spinner mBloodGroup;
    FirebaseDatabase mDB;
    DatabaseReference mRef;

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
        mBloodGroup = findViewById(R.id.bloodgroup);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bloodgroup, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodGroup.setAdapter(adapter);
        bSave = findViewById(R.id.bSave);

        mDB = FirebaseDatabase.getInstance();
        mRef = mDB.getReference(BuildConfig.DB_TYPE+"/users/" + maskNumber(mSession.getCaregiverNumber().substring(1)));


        etName.setText(mSession.getName());
        mCcp = findViewById(R.id.ccp);
        mCcp.setCountryForPhoneCode(Integer.valueOf(mSession.getUserCountryCode()));
        mCcp.registerCarrierNumberEditText(etFatherContact);
        etFatherContact.setText(mSession.getCaregiverNumber().replace("+".concat(mSession.getUserCountryCode()),""));
        etFathername.setText(mSession.getCaregiverName());
        etAddress.setText(mSession.getAddress());
        etEmailId.setText(mSession.getEmailId());
        if(mSession.getBlood() != -1)
            mBloodGroup.setSelection(mSession.getBlood());

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
                            new NetworkConnectionTest(ProfileFormActivity.this,
                                    etName.getText().toString(),
                                    mCcp.getFullNumber(),
                                    email,
                                    etFathername.getText().toString(),
                                    etAddress.getText().toString()).execute();
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
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(mSession.getSessionCreatedAt());
        mSession.setSessionCreatedAt(sessionTime);

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

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            throw new Error("unableToResume");
        }
         startMeasuring();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void encryptStoreUserInfo(final String name, final String contact,
                                      final String email, final String caregiverName,
                                      final String address, final String bloodGroup) {
        if(mSession.getEncryptedData().isEmpty()) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference pathReference = storageRef.child("jellow-json.json");
            final long ONE_MEGABYTE = 1024 * 1024;
            pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try {
                        String jsonData = new String(bytes, "UTF-8");
                        SecureKeys secureKey = new Gson().fromJson(jsonData, SecureKeys.class);
                        encryptStoreUserInfoUsingSecureKey(name, contact, email,
                                caregiverName, address, bloodGroup, secureKey);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    reportException(exception);
                }
            });
        }else {
            SecureKeys secureKey = new Gson().fromJson(mSession.getEncryptedData(), SecureKeys.class);
            encryptStoreUserInfoUsingSecureKey(name, contact, email,
                        caregiverName, address, bloodGroup, secureKey);
        }
    }

    private void encryptStoreUserInfoUsingSecureKey(String name, String contact, String email,
                String caregiverName, String address, String bloodGroup, SecureKeys secureKey) {
        mRef.child("emergencyContact").setValue(maskNumber(contact));
        mRef.child("name").setValue(encrypt(name, secureKey));
        mRef.child("email").setValue(encrypt(email, secureKey));
        if(!caregiverName.isEmpty())
            mRef.child("caregiverName").setValue(encrypt(caregiverName, secureKey));
        if(!address.isEmpty())
            mRef.child("address").setValue(encrypt(address, secureKey));
        mRef.child("bloodGroup").setValue(encrypt(bloodGroup, secureKey));
        mRef.child("updatedOn").setValue(ServerValue.TIMESTAMP);
        savedProfileDetails();
    }

    private String encrypt(String plainText, SecureKeys secureKey) {
        Encryption encryption = Encryption.getDefault(secureKey.getKey(),
                secureKey.getSalt(), new byte[16]);
        return encryption.encryptOrNull(plainText).trim();
    }

    private void savedProfileDetails() {
        mSession.setCaregiverName(etFathername.getText().toString());
        mSession.setAddress(etAddress.getText().toString());
        mSession.setName(etName.getText().toString());
        mSession.setCaregiverNumber(mCcp.getFullNumberWithPlus());
        mSession.setUserCountryCode(mCcp.getSelectedCountryCode());
        mSession.setEmailId(email);
        if(mBloodGroup.getSelectedItemPosition() > 0)
            mSession.setBlood(mBloodGroup.getSelectedItemPosition());
        else
            mSession.setBlood(-1);

        Toast.makeText(this, getString(R.string.detailSaved), Toast.LENGTH_SHORT).show();
        finish();
    }

    public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath)
    {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        mRef.setValue(null);
                        mSession.setCaregiverNumber(mCcp.getFullNumberWithPlus());
                        mRef = mDB.getReference(BuildConfig.DB_TYPE + "/users/"+
                                maskNumber(mSession.getCaregiverNumber().substring(1)));
                        updateSessionRef(mCcp.getFullNumber());
                        encryptStoreUserInfo(etName.getText().toString(),
                                mCcp.getFullNumber(),
                                email,
                                etFathername.getText().toString(),
                                etAddress.getText().toString(),
                                getBloodGroup());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class NetworkConnectionTest extends AsyncTask<Void, Void, Boolean> {
        private Context mContext;
        private String mName, mContact, mEmailId, mCaregiverName, mAddress;
        private SessionManager mSession;

        public NetworkConnectionTest(Context context, String name, String contact,
                                     String email, String caregiverName, String address) {
            mContext = context;
            mName = name;
            mContact = contact;
            mEmailId = email;
            mCaregiverName = caregiverName;
            mAddress = address;
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
                if(!mCcp.getFullNumberWithPlus().equals(mSession.getCaregiverNumber())){
                    DatabaseReference mNewRef = mDB.getReference(BuildConfig.DB_TYPE
                            + "/users/" + maskNumber(mContact)
                            + "/previousRecords/" + maskNumber(mSession.getCaregiverNumber().substring(1)));
                    moveFirebaseRecord(mRef, mNewRef);
                }else {
                    encryptStoreUserInfo(mName, mContact, mEmailId, mCaregiverName, mAddress,
                            getBloodGroup());
                }
            }else{
                Toast.makeText(mContext, getString(R.string.checkConnectivity), Toast.LENGTH_LONG).show();
            }
        }
    }
}