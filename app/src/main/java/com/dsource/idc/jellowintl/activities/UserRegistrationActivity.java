package com.dsource.idc.jellowintl.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_DropDownMenu;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.async.InternetTest;
import com.dsource.idc.jellowintl.utility.interfaces.CheckNetworkStatus;
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
public class UserRegistrationActivity extends BaseActivity implements CheckNetworkStatus {
    public static final String LCODE = "lcode";
    public static final String TUTORIAL = "tutorial";
    private Button bRegister;
    private EditText etName, etEmergencyContact, etAddress;
    private DatabaseReference mRef;
    private CountryCodePicker mCcp;
    private String[] languagesCodes = new String[LangMap.size()];
    private String selectedLanguage;
    private InternetTest internetTest;
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

        if (getSession().isUserLoggedIn()) {

            if (LanguageFactory.isLanguageDataAvailable(this) && getSession().isCompletedIntro()) {
                startActivity(new Intent(this, SplashActivity.class));
            }else if(!LanguageFactory.isLanguageDataAvailable(this)){
                startActivity(new Intent(UserRegistrationActivity.this,
                        LanguageDownloadActivity.class)
                        .putExtra(LCODE, UNIVERSAL_PACKAGE).putExtra(TUTORIAL, true));
                /* 0 represents old value of one by three config*/
                if(getSession().getGridSize() == 0)
                    getSession().setGridSize(GlobalConstants.THREE_ICONS_PER_SCREEN);
                else
                    getSession().setGridSize(GlobalConstants.NINE_ICONS_PER_SCREEN);
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

    @Override
    public void onReceiveNetworkState(int state) {
        internetTest.unRegisterReceiver();
         if(state == GlobalConstants.NETWORK_CONNECTED){
            Toast.makeText(UserRegistrationActivity.this,
                    getString(R.string.register_user), Toast.LENGTH_SHORT).show();
            autoLoginAndSetupUser();
        }else{
            bRegister.setEnabled(true);
            Toast.makeText(UserRegistrationActivity.this,
                    getString(R.string.checkConnectivity), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeScreenViewsAndListeners() {
        setContentView(R.layout.activity_user_registration);
        setupActionBarTitle(View.GONE, getString(R.string.menuUserRegistration));
        setNavigationUiConditionally();
        findViewById(R.id.iv_action_bar_back).setVisibility(View.GONE);
        languagesCodes = LanguageFactory.getAvailableLanguages();
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
        ((Button)findViewById(R.id.btn_lang_select)).setText(languagesCodes[1]);
        selectedLanguage = languagesCodes[1];

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
                internetTest = new InternetTest();
                internetTest.registerReceiver(UserRegistrationActivity.this);
                internetTest.execute(UserRegistrationActivity.this);
            }
        });

        if(!getSession().getName().isEmpty()){
            etName.setText(getSession().getName());
            etEmergencyContact.setText(getSession().getCaregiverNumber());
            etAddress.setText(getSession().getAddress());
        }
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

    public void showAvailableLanguageDialog(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(LanguageFactory.getAvailableLanguages(), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedLanguage = languagesCodes[which];
                ((Button)findViewById(R.id.btn_lang_select)).setText(languagesCodes[which]);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}