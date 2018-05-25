package com.dsource.idc.jellowintl;

/**
 * Created by user on 5/25/2016.
 */

import android.app.ActivityManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.models.SecureKeys;
import com.dsource.idc.jellowintl.utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
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

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.maskNumber;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.updateSessionRef;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class ProfileFormActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private Button bSave;
    private EditText etName, etFatherContact, etFathername, etAddress, etEmailId;
    private SessionManager mSession;
    private String email, mUserGroup;
    private CountryCodePicker mCcp;
    private Spinner mBloodGroup;
    private FirebaseDatabase mDB;
    private DatabaseReference mRef;
    private boolean emergencyContactChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);
        new ChangeAppLocale(this).setLocale();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.menuProfile)+"</font>"));
        mSession = new SessionManager(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
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

        if (!mSession.getUserGroup().isEmpty() &&
                mSession.getUserGroup().equals(getString(R.string.groupParent).split("/")[0]))
            ((RadioButton)findViewById(R.id.radioParent)).setChecked(true);
        else
            ((RadioButton)findViewById(R.id.radioTherapist)).setChecked(true);

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
                Crashlytics.log("Profile Save");
                bSave.setEnabled(false);
                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), getString(R.string.enterTheName),
                            Toast.LENGTH_SHORT).show();
                    bSave.setEnabled(true);
                    return;
                }
                email = etEmailId.getText().toString().trim();
                if (!isValidEmail(email)) {
                    Toast.makeText(getBaseContext(), getString(R.string.invalid_emailId),
                            Toast.LENGTH_SHORT).show();
                    bSave.setEnabled(true);
                    return;
                }
                RadioGroup radioGroup = findViewById(R.id.radioUserGroup);
                mUserGroup = (radioGroup.getCheckedRadioButtonId() == R.id.radioParent) ?
                        getString(R.string.groupParent).split("/")[0] :
                        getString(R.string.groupTeacher).split("/")[0];

                new NetworkConnectionTest(ProfileFormActivity.this,
                        etName.getText().toString(),
                        mCcp.getFullNumber(),
                        email,
                        etFathername.getText().toString(),
                        etAddress.getText().toString(),
                        mUserGroup).execute();
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

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            throw new Error("unableToResume");
        }
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        startMeasuring();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String Permissions[], int[] grantResults){
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, R.string.granted_call_permission_req,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.rejected_call_permission_req,
                        Toast.LENGTH_SHORT).show();
            }
            finish();
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
                    try {
                        String jsonData = new String(bytes, "UTF-8");
                        SecureKeys secureKey = new Gson().fromJson(jsonData, SecureKeys.class);
                        encryptStoreUserInfoUsingSecureKey(name, contact, email,
                                caregiverName, address, bloodGroup, userGroup, secureKey);
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
        mSession.setCaregiverNumber(mCcp.getFullNumberWithPlus());
        mSession.setUserCountryCode(mCcp.getSelectedCountryCode());
        mSession.setEmailId(email);
        if(mBloodGroup.getSelectedItemPosition() > 0)
            mSession.setBlood(mBloodGroup.getSelectedItemPosition());
        else
            mSession.setBlood(-1);
        mSession.setUserGroup(userGroup);
        setUserProperty("userGroup", userGroup);

        Toast.makeText(this, getString(R.string.detailSaved), Toast.LENGTH_SHORT).show();

        // User device is above Lollipop and user changed, saved contact and app does not have call
        // permission then ask user for call permission.
        if(Build.VERSION.SDK_INT > 22 && emergencyContactChanged &&
                ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // If user device is not wifi-only device, only then request call permission.
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
                showPermissionRequestDialog();
                bSave.setEnabled(true);
            }else
                finish();
        }else
            finish();
    }

    public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath)
    {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // remove the listener to avoid any 'intermediate' updates while working in the same node
                mRef.removeEventListener(this);
                processPreviousRecordsNode(dataSnapshot,toPath);
                mRef.setValue(null);

                mSession.setCaregiverNumber(mCcp.getFullNumberWithPlus());
                mRef = mDB.getReference(BuildConfig.DB_TYPE + "/users/" +
                        maskNumber(mSession.getCaregiverNumber().substring(1)));
                mRef.removeEventListener(this);
                emergencyContactChanged = true;
                Crashlytics.setUserIdentifier(maskNumber(mSession.getCaregiverNumber().substring(1)));
                updateSessionRef(mCcp.getFullNumber());
                encryptStoreUserInfo(etName.getText().toString(),
                        mCcp.getFullNumber(),
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

    private void showPermissionRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(ProfileFormActivity.this, new String[]
                    {android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                dialog.dismiss();
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
            if(isConnected){
                if(!mCcp.getFullNumberWithPlus().equals(mSession.getCaregiverNumber())){
                    DatabaseReference mNewRef = mDB.getReference(BuildConfig.DB_TYPE
                            + "/users/" + maskNumber(mContact)
                            + "/previousRecords/" + maskNumber(mSession.getCaregiverNumber().substring(1)));
                    moveFirebaseRecord(mRef, mNewRef);
                }else {
                    encryptStoreUserInfo(mName, mContact, mEmailId, mCaregiverName, mAddress,
                            getBloodGroup(), mUserGroup);
                }
            }else{
                bSave.setEnabled(true);
                Toast.makeText(mContext, getString(R.string.checkConnectivity), Toast.LENGTH_LONG).show();
            }
        }
    }
}