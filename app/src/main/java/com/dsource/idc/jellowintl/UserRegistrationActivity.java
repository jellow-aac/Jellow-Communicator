package com.dsource.idc.jellowintl;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_DropDownMenu;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.AppUpdateUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.getAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.UNIVERSAL_PACKAGE;

/**
 * Created by Rahul on 12 Nov, 2019.
 */
public class UserRegistrationActivity extends BaseActivity implements CheckNetworkStatus{
    public static final String LCODE = "lcode";
    public static final String TUTORIAL = "tutorial";

    enum REQUEST_NEED{
        APP_UPDATE, REGISTER, OTHER
    }

    private Button bRegister;
    private EditText etName, etEmergencyContact, etAddress;
    private DatabaseReference mRef;
    private CountryCodePicker mCcp;
    private String[] languagesCodes = new String[LangMap.size()];
    private String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("jellow_aac");
        getSession().changePreferencesFile(this);
        //Reset Board Language
        getSession().setCurrentBoardLanguage("");

        if (!getSession().getUserId().equals("")) {
            getAnalytics(this, getSession().getUserId());
            getSession().setSessionCreatedAt(new Date().getTime());
            Crashlytics.setUserIdentifier(getSession().getUserId());
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            new NetworkConnectionTest(
                    UserRegistrationActivity.this,
                    REQUEST_NEED.APP_UPDATE).execute();
        }else{
            continueLoadingRegistrationScreen();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            new AppUpdateUtil().
                    executeUpdateFlow(
                            AppUpdateUtil.UpdateStatus.RUNNING,
                            UserRegistrationActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AppUpdateUtil.UPDATE_REQUEST_CODE && resultCode == RESULT_OK){
            Intent mStartActivity = new Intent(UserRegistrationActivity.this, UserRegistrationActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId,
                    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }else if(requestCode == AppUpdateUtil.UPDATE_REQUEST_CODE){
            Toast.makeText(this, "Resumed on RESULT_OK not.", Toast.LENGTH_SHORT).show();
            continueLoadingRegistrationScreen();
        }
    }

    @Override
    public void onReceiveNetworkState(int state, REQUEST_NEED requestNeed) {
         if(requestNeed == REQUEST_NEED.APP_UPDATE && state == GlobalConstants.NETWORK_CONNECTED){
            AppUpdateUtil updateUtil = new AppUpdateUtil();
            updateUtil.executeUpdateFlow(AppUpdateUtil.UpdateStatus.INIT, UserRegistrationActivity.this);
        }else if(requestNeed == REQUEST_NEED.APP_UPDATE && state == GlobalConstants.NETWORK_DISCONNECTED){
            continueLoadingRegistrationScreen();
        }else if(requestNeed == REQUEST_NEED.REGISTER && state == GlobalConstants.NETWORK_CONNECTED){
            Toast.makeText(UserRegistrationActivity.this,
                    getString(R.string.register_user), Toast.LENGTH_SHORT).show();
            autoLoginAndSetupUser();
        }else if(requestNeed == REQUEST_NEED.REGISTER && state == GlobalConstants.NETWORK_DISCONNECTED) {
            bRegister.setEnabled(true);
            Toast.makeText(UserRegistrationActivity.this,
                    getString(R.string.checkConnectivity), Toast.LENGTH_LONG).show();
        }
    }

    private void continueLoadingRegistrationScreen() {
        if (getSession().isUserLoggedIn()) {
            if (LanguageFactory.isLanguageDataAvailable(this) && getSession().isCompletedIntro()) {
                startActivity(new Intent(this, SplashActivity.class));
            }else if(!LanguageFactory.isLanguageDataAvailable(this)){
                startActivity(new Intent(UserRegistrationActivity.this,
                        LanguageDownloadActivity.class)
                        .putExtra(LCODE, UNIVERSAL_PACKAGE).putExtra(TUTORIAL, true));
            }else if(getSession().getLanguage().equals(MR_IN) && !LanguageFactory.
                    isMarathiPackageAvailable(this)){
                startActivity(new Intent(UserRegistrationActivity.this,
                        LanguageDownloadActivity.class)
                        .putExtra(LCODE, MR_IN).putExtra(TUTORIAL, true));
            } else if (LanguageFactory.isLanguageDataAvailable(this) &&
                    !getSession().isCompletedIntro()) {
                startActivity(new Intent(this, Intro.class));
            }
            finish();
        } else {
            getSession().setBlood(-1);
            initializeScreenViewsAndListeners();
        }
    }

    private void initializeScreenViewsAndListeners() {
        setContentView(R.layout.activity_user_registration);
        setupActionBarTitle(View.GONE, getString(R.string.menuUserRegistration));
        setNavigationUiConditionally();
        findViewById(R.id.iv_action_bar_back).setVisibility(View.GONE);
        languagesCodes = LanguageFactory.getAvailableLanguages();
        String[] languageNames = LanguageFactory.getAvailableLanguages();
        etName = findViewById(R.id.etName);
        ((TextView)findViewById(R.id.tv_pivacy_link)).setText(Html.fromHtml(getString(R.string.privacy_link_info)));
        etEmergencyContact = findViewById(R.id.etEmergencyContact);
        mCcp = findViewById(R.id.ccp);
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE)))
            mCcp.setCountryPreference(null);
        ViewCompat.setAccessibilityDelegate(mCcp, new TalkbackHints_DropDownMenu());
        mCcp.registerCarrierNumberEditText(etEmergencyContact);
        //This listener is useful only when TalkBack accessibility is "ON".
        mCcp.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override public void onCcpDialogOpen(Dialog dialog) {}

            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
                etEmergencyContact.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
            }
            @Override public void onCcpDialogCancel(DialogInterface dialogInterface) {}
        });
        etAddress = findViewById(R.id.etAddress);
        bRegister = findViewById(R.id.bRegister);
        findViewById(R.id.childName).setFocusableInTouchMode(true);
        findViewById(R.id.childName).setFocusable(true);
        findViewById(R.id.cb_privacy_consent).setContentDescription(
                ((TextView)findViewById(R.id.tv_pivacy_link)).getText().toString());
        Spinner languageSelect = findViewById(R.id.langSelectSpinner);
        ArrayAdapter<String> adapter_lan = new ArrayAdapter<>(this,
                R.layout.simple_spinner_item, populateCountryNameByUserType(languageNames));
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

                if (etName.getText().toString().trim().isEmpty()) {
                    bRegister.setEnabled(true);
                    Toast.makeText(UserRegistrationActivity.this,
                            getString(R.string.enterTheName), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!etEmergencyContact.getText().toString().matches("[0-9]+")){
                    bRegister.setEnabled(true);
                    Toast.makeText(UserRegistrationActivity.this,
                            getString(R.string.enternonemptycontact), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etAddress.getText().toString().trim().isEmpty()){
                    bRegister.setEnabled(true);
                    Toast.makeText(UserRegistrationActivity.this,getString(R.string.invalid_address),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                CheckBox cb = findViewById(R.id.cb_privacy_consent);
                if (!cb.isChecked()){
                    bRegister.setEnabled(true);
                    Toast.makeText(UserRegistrationActivity.this,
                            getString(R.string.consent_privacy), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedLanguage == null)
                    return;
                new NetworkConnectionTest(
                        UserRegistrationActivity.this,
                        REQUEST_NEED.REGISTER).execute();
            }
        });

        if(!getSession().getName().isEmpty()){
            etName.setText(getSession().getName());
            etEmergencyContact.setText(getSession().getCaregiverNumber());
            etAddress.setText(getSession().getAddress());
        }
    }

    public void continueLoadingTheApp() {
        continueLoadingRegistrationScreen();
    }

    private String[] populateCountryNameByUserType(String[] langNameToBeShorten) {
        String[] shortenLanguageNames = new String[langNameToBeShorten.length];
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            for (int i = 0; i < langNameToBeShorten.length; i++) {
                switch (langNameToBeShorten[i]) {
                    case "मराठी (Marathi)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_marathi);
                        break;
                    case "हिंदी (Hindi)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_hindi);
                        break;
                    case "বাংলা (Bengali)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_bengali);
                        break;
                    case "English (India)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_eng_in);
                        break;
                    case "English (United Kingdom)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_eng_gb);
                        break;
                    case "English (United States)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_eng_us);
                        break;
                    case "English (Australia)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_eng_au);
                        break;
                    case "Spanish (Spain)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_span_span);
                        break;
                    case "தமிழ் (Tamil)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_tamil_in);
                        break;
                    case "German (Deutschland)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_german_ger);
                        break;
                    default:
                        shortenLanguageNames[i] = langNameToBeShorten[i];
                        break;
                }
            }
        }else{
            for (int i = 0; i < langNameToBeShorten.length; i++) {
                switch (langNameToBeShorten[i]) {
                    case "English (India)":
                        shortenLanguageNames[i] = "English (IN)";
                        break;
                    case "English (United Kingdom)":
                        shortenLanguageNames[i] = "English (UK)";
                        break;
                    case "English (United States)":
                        shortenLanguageNames[i] = "English (US)";
                        break;
                    case "English (Australia)":
                        shortenLanguageNames[i] = "English (AU)";
                        break;
                    case "Spanish (Spain)":
                        shortenLanguageNames[i] = "Spanish (ES)";
                        break;
                    case "Tamil (India)":
                        shortenLanguageNames[i] = "Tamil (IN)";
                        break;
                    case "German (Deutschland)":
                        shortenLanguageNames[i] = "German (DE)";
                        break;
                    case "French (France)":
                        shortenLanguageNames[i] = "French (FR)";
                        break;
                    default:
                        shortenLanguageNames[i] = langNameToBeShorten[i];
                        break;
                }
            }
        }
        return shortenLanguageNames;
    }

    private void autoLoginAndSetupUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Crashlytics.log("User logged in");
                            getSession().setName(etName.getText().toString().trim());
                            getSession().setCaregiverNumber(mCcp.getFullNumberWithPlus());
                            getSession().setUserCountryCode(mCcp.getSelectedCountryCode());
                            getSession().setAddress(etAddress.getText().toString().trim());
                            getSession().setUserId(UUID.randomUUID().toString());

                            getAnalytics(UserRegistrationActivity.this, getSession().getUserId());
                            getSession().setSessionCreatedAt(new Date().getTime());
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("firstLanguage", selectedLanguage);
                            map.put("versionCode", BuildConfig.VERSION_CODE);
                            map.put("joinedOn", ServerValue.TIMESTAMP);
                            FirebaseDatabase mDB = FirebaseDatabase.getInstance();
                            mRef = mDB.getReference(BuildConfig.DB_TYPE + "/users");
                            mRef.child(getSession().getUserId()).setValue(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        getSession().setUserLoggedIn(true);
                                        getSession().setLanguage(LangMap.get(selectedLanguage));
                                        getSession().setGridSize(GlobalConstants.NINE_ICONS_PER_SCREEN);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("LanguageSet", "First time "+ LangMap.get(selectedLanguage));
                                        setUserProperty("UserId", getSession().getUserId());
                                        setUserProperty("UserLanguage", LangMap.get(selectedLanguage));
                                        setUserProperty("GridSize", "9");
                                        setUserProperty("PictureViewMode", "PictureText");
                                        bundleEvent("Language", bundle);
                                        Crashlytics.setUserIdentifier(getSession().getUserId());
                                        setCrashlyticsCustomKey("GridSize", "9");
                                        setCrashlyticsCustomKey("PictureViewMode", "PictureText");
                                        bundle.clear();
                                        bundle.putString(LCODE, UNIVERSAL_PACKAGE);
                                        bundle.putBoolean(TUTORIAL, true);
                                        startActivity(new Intent(UserRegistrationActivity.this,
                                                LanguageDownloadActivity.class).putExtras(bundle));
                                        finish();
                                    } else {
                                        bRegister.setEnabled(true);
                                        Crashlytics.log("User data not added.");
                                        Toast.makeText(UserRegistrationActivity.this,
                                                getString(R.string.checkConnectivity), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    bRegister.setEnabled(true);
                                    Crashlytics.log("User data not added.");
                                    Crashlytics.logException(e);
                                    Toast.makeText(UserRegistrationActivity.this,
                                            getString(R.string.error_in_registration), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else
                            Toast.makeText(UserRegistrationActivity.this,
                                    getString(R.string.error_in_registration), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserRegistrationActivity.this,
                        e.getMessage(), Toast.LENGTH_SHORT).show();
                bRegister.setEnabled(true);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class NetworkConnectionTest extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private CheckNetworkStatus listener;
        private UserRegistrationActivity.REQUEST_NEED requestNeed;

        NetworkConnectionTest(Context context, UserRegistrationActivity.REQUEST_NEED requestNeed) {
            this.context = context;
            this.listener = (CheckNetworkStatus) context;
            this.requestNeed = requestNeed;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(!isConnectedToNetwork((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)))
                return false;
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    urlc.disconnect();
                    return true;
                }
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
                listener.onReceiveNetworkState(GlobalConstants.NETWORK_CONNECTED, requestNeed);
            }else{
                listener.onReceiveNetworkState(GlobalConstants.NETWORK_DISCONNECTED, requestNeed);
            }
        }
    }
}