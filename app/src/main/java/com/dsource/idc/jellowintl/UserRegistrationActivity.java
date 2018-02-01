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
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dsource.idc.jellowintl.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.Utility.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.dsource.idc.jellowintl.Utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.Utility.Analytics.getAnalytics;
import static com.dsource.idc.jellowintl.Utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.Utility.SessionManager.LangMap;

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
    Spinner languageSelect;
    String[] languagesCodes = new String[4], languageNames = new String[4];
    String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.app_name)+"</font>"));
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        mSession = new SessionManager(this);

        if(!mSession.getFather_no().equals(""))
        getAnalytics(this,mSession.getFather_no());
        mSession.setBlood(-1);



        if (mSession.isUserLoggedIn())
        {
            if(mSession.isDownloaded(mSession.getLanguage()) && mSession.isCompletedIntro()) {
                startActivity(new Intent(this, SplashActivity.class));
            }else if(mSession.isDownloaded(mSession.getLanguage()) && !mSession.isCompletedIntro()){
                startActivity(new Intent(this, Intro.class));
            }else {
                startActivity(new Intent(UserRegistrationActivity.this,
                        LanguageDownloadActivity.class).putExtra(LCODE,mSession.getLanguage()).putExtra(TUTORIAL,true));
            }
            finish();
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
        bRegister.setEnabled(true);


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
                String name, emergencyContact, eMailId, formattedDate;
                name  = etName.getText().toString();
                emergencyContact = mCcp.getFullNumberWithPlus();
                bRegister.setEnabled(false);
                if (etName.getText().toString().equals("")) {
                    bRegister.setEnabled(true);
                    Toast.makeText(getBaseContext(), getString(R.string.enterTheName), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etEmergencyContact.getText().toString().equals("")){
                    bRegister.setEnabled(true);
                    Toast.makeText(getBaseContext(), getString(R.string.enternonemptycontact), Toast.LENGTH_SHORT).show();
                    return;
                }
                eMailId = etEmailId.getText().toString().trim();
                if (!isValidEmail(eMailId)){
                    bRegister.setEnabled(true);
                    Toast.makeText(getBaseContext(),getString(R.string.invalid_emailId), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(selectedLanguage == null) return;
                Calendar ca = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                formattedDate = df.format(ca.getTime());
                new NetworkConnectionTest(UserRegistrationActivity.this, name, emergencyContact, eMailId, formattedDate).execute();
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void createUser(final String name,final String emergencyContact, String eMailId, String formattedDate)
    {
        try {
            getAnalytics(UserRegistrationActivity.this,emergencyContact);
            mRef.child(emergencyContact).child("email").setValue(eMailId);
            mRef.child(emergencyContact).child("emergencyContact").setValue(emergencyContact);
            mRef.child(emergencyContact).child("name").setValue(name);
            mRef.child(emergencyContact).child("joinedOn").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mSession.setUserLoggedIn(true);
                        mSession.setLanguage(LangMap.get(selectedLanguage));
                        mSession.setGridSize(GRID_3BY3);
                        Bundle bundle = new Bundle();
                        bundle.putString("First Run Selected Language",LangMap.get(selectedLanguage));
                        setUserProperty("UserId",emergencyContact);
                        bundleEvent("Language",bundle);
                        bundle.clear();
                        bundle.putString(LCODE,LangMap.get(selectedLanguage));
                        bundle.putBoolean(TUTORIAL,true);
                        startActivity(new Intent(UserRegistrationActivity.this,
                                LanguageDownloadActivity.class).putExtras(bundle));
                        finish();
                    } else {
                        bRegister.setEnabled(true);
                        Toast.makeText(UserRegistrationActivity.this, getString(R.string.checkInternetConn), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class NetworkConnectionTest extends AsyncTask<Void, Void, Boolean>{
        private Context mContext;
        private String mName, mEmergencyContact, mEmailId, mFormattedDate;
        private SessionManager mSession;

        public NetworkConnectionTest(Context context, String name, String emergencyContact, String eMailId, String formattedDate) {
            mContext = context;
            mName = name;
            mEmergencyContact = emergencyContact;
            mEmailId = eMailId;
            mFormattedDate = formattedDate;
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
                mSession.setFather_no(mEmergencyContact);
                mSession.setUserCountryCode(mCcp.getSelectedCountryCode());
                mSession.setEmailId(mEmailId);
                Calendar ca = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mFormattedDate = df.format(ca.getTime());
                createUser(mName, mEmergencyContact, mEmailId, mFormattedDate);
            }else{
                bRegister.setEnabled(true);
                Toast.makeText(UserRegistrationActivity.this, getString(R.string.checkInternetConn), Toast.LENGTH_LONG).show();
            }
        }
    }
}