package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import static com.dsource.idc.jellowintl.MainActivity.isAccessibilityTalkBackOn;
import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;
import static com.dsource.idc.jellowintl.utility.SessionManager.BE_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangValueMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

public class LanguageSelectTalkBackActivity extends AppCompatActivity{

    public static final String FINISH = "finish";
    private final int ACT_CHECK_TTS_DATA = 1;
    SessionManager mSession;
    String[] offlineLanguages, onlineLanguages;
    Spinner languageSelect;
    String selectedLanguage, sysTtsLang;
    Button save,add,delete, changeTtsLang;
    ArrayAdapter<String> adapter_lan;
    boolean isOpenedTtsSett = false, isTtsLangChanged = false, shouldSaveLang = false;
    private int mSelectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_language_select_accessible);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>Language</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        mSession = new SessionManager(this);

        setImageUsingGlide(R.drawable.tts_wifi_1, ((ImageView)findViewById(R.id.ivAddLang1)));
        setImageUsingGlide(R.drawable.tts_wifi_2, ((ImageView)findViewById(R.id.ivAddLang2)));
        setImageUsingGlide(R.drawable.tts_wifi_3, ((ImageView)findViewById(R.id.ivAddLang3)));
        setImageUsingGlide(R.drawable.gtts3, ((ImageView)findViewById(R.id.ivTtsVoiceDat)));
        setImageUsingGlide(R.drawable.arrow, ((ImageView)findViewById(R.id.ivArrow1)));
        setImageUsingGlide(R.drawable.arrow, ((ImageView)findViewById(R.id.ivArrow2)));
        if(Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.tv5).setVisibility(View.GONE);
            findViewById(R.id.llImg).setVisibility(View.GONE);
            findViewById(R.id.changeTtsLangBut).setVisibility(View.GONE);
        }else{
            setImageUsingGlide(R.drawable.gtts1, ((ImageView)findViewById(R.id.ivTtsSetting1)));
            setImageUsingGlide(R.drawable.gtts2, ((ImageView)findViewById(R.id.ivTtsSetting2)));
            setImageUsingGlide(R.drawable.gtts4, ((ImageView)findViewById(R.id.ivTtsSetting3)));
            setImageUsingGlide(R.drawable.arrow, ((ImageView)findViewById(R.id.ivArrow3)));
            setImageUsingGlide(R.drawable.arrow, ((ImageView)findViewById(R.id.ivArrow4)));
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_RES");
        registerReceiver(receiver, filter);

        getSpeechLanguage("");
        offlineLanguages = getOfflineLanguages();
        onlineLanguages = getOnlineLanguages();
        languageSelect = findViewById(R.id.selectDownloadedLanguageSpinner);

        adapter_lan = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, populateCountryNameByUserType(offlineLanguages));
        adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languageSelect.setAdapter(adapter_lan);
        languageSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLanguage = offlineLanguages[i];
                mSelectedItem = i;
                if(selectedLanguage.equals(LangValueMap.get(MR_IN))) {
                    hideViewsForNonTtsLang(true);
                    setViewsForNonTtsLang();
                }else {
                    hideViewsForNonTtsLang(false);
                    updateViewsForNewLangSelect();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedLanguage = null;
            }
        });

        changeTtsLang = findViewById(R.id.changeTtsLangBut);
        changeTtsLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("LanguageSelect SetTTsEng");
                isOpenedTtsSett = true;
                Intent intent = new Intent();
                intent.setAction("com.android.settings.TTS_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        save = findViewById(R.id.saveBut);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("LanguageSelect Apply");
                //if current user language is not marathi and user want to change current language
                // to marathi.
                if (!mSession.getLanguage().equals(LangMap.get("मराठी"))
                        && selectedLanguage != null && selectedLanguage.equals("मराठी")){
                    saveLanguage();
                    mSession.setLangSettingIsCorrect(true);
                }else if(selectedLanguage != null)
                {
                    if(Build.VERSION.SDK_INT >= 21 &&
                            mSession.getLanguage().equals(LangMap.get(selectedLanguage))) {
                        Toast.makeText(LanguageSelectTalkBackActivity.this,
                                "Selected language is already default language", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(Build.VERSION.SDK_INT >= 21){
                        checkIfVoiceAvail(LangMap.get(selectedLanguage));
                        return;
                    }else if(shouldSaveLang) {
                        saveLanguage();
                        mSession.setLangSettingIsCorrect(true);
                        return;
                    }
                    else if (mSession.getLanguage().equals(LangMap.get(selectedLanguage)) && !shouldSaveLang){
                        Toast.makeText(LanguageSelectTalkBackActivity.this,
                                "Selected language is already default language", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getSpeechLanguage(LangMap.get(selectedLanguage));
                }
            }
        });

        add = findViewById(R.id.addBut);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("LanguageSelect Add");
                if(!mSession.isWifiOnlyBtnPressedOnce()){
                    Toast.makeText(LanguageSelectTalkBackActivity.this,
                            "Please complete previous step to turn off \'Use Wi-Fi only\' option",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if (onlineLanguages.length == 0) {
                        Toast.makeText(LanguageSelectTalkBackActivity.this, "No more languages to add",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    delete.setEnabled(false);
                    SpannableString spannedDlStr = new SpannableString("Downloadable Languages");
                    spannedDlStr.setSpan(new StyleSpan(Typeface.BOLD),0, spannedDlStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString spannedCnlStr = new SpannableString("CANCEL");
                    spannedCnlStr.setSpan(new StyleSpan(Typeface.BOLD),0, spannedCnlStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MaterialAlertDialogBuilder builder =
                            new MaterialAlertDialogBuilder(LanguageSelectTalkBackActivity.this);
                    AlertDialog addLangDialog = builder
                            .setTitle(spannedDlStr)
                            .setSingleChoiceItems(populateCountryNameByUserType(onlineLanguages), 0, new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delete.setEnabled(true);
                                    showAddLangDialog(dialog, which,
                                            "You are not connected to the Internet. Please turn on your Internet.");
                                }
                            })
                            .setNegativeButton(spannedCnlStr, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delete.setEnabled(true);
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    delete.setEnabled(true);
                                }
                            })
                            .create();
                    addLangDialog.show();
                    customizeLanguageDialog(addLangDialog);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        delete = findViewById(R.id.delBut);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("LanguageSelect Delete");
                try {
                    if (offlineLanguages.length == 1) {
                        Toast.makeText(LanguageSelectTalkBackActivity.this, "No more languages to delete",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    add.setEnabled(false);
                    SpannableString spannedDlStr = new SpannableString("Downloaded Languages");
                    spannedDlStr.setSpan(new StyleSpan(Typeface.BOLD),0, spannedDlStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString spannedCnlStr = new SpannableString("CANCEL");
                    spannedCnlStr.setSpan(new StyleSpan(Typeface.BOLD),0, spannedCnlStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LanguageSelectTalkBackActivity.this);
                    AlertDialog delLangDialog = builder
                        .setTitle(spannedDlStr)
                        .setSingleChoiceItems(populateCountryNameByUserType(removeCurrentLangFromList(offlineLanguages))
                                , 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        add.setEnabled(true);
                                        String locale = LangMap.get(offlineLanguages[which]);
                                        if (mSession.getLanguage().equals(locale)) {
                                            Toast.makeText(LanguageSelectTalkBackActivity.this,
                                                    "Language is currently in use", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            return ;
                                        }
                                        showDeleteLangDialog(dialog, which, "Language removed");
                                    }
                                })
                        .setNegativeButton(spannedCnlStr, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                add.setEnabled(true);
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                add.setEnabled(true);
                            }
                        })
                        .create();
                    delLangDialog.show();
                    customizeLanguageDialog(delLangDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void customizeLanguageDialog(AlertDialog dialog) {
        Button btnPos = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Typeface tf = ResourcesCompat.getFont
                (LanguageSelectTalkBackActivity.this, R.font.mukta_semibold);
        btnPos.setTypeface(tf);
        btnPos.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        //TODO Add this part of code to mobile only device
        /*Rect displayRectangle = new Rect();
        Window window = dialog.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);   */
    }

    private void showAddLangDialog(DialogInterface dialog, int which, String strCheckConnectivity) {
        delete.setEnabled(true);
        ConnectivityManager cm =
                (ConnectivityManager) LanguageSelectTalkBackActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            Bundle bundle = new Bundle();
            bundle.putString(LCODE, LangMap.get(onlineLanguages[which]));
            bundle.putBoolean(FINISH, false);
            setSpeechLanguage(LangMap.get(onlineLanguages[which])); //To start TTS voice package download automatically.
            setSpeechLanguage(mSession.getLanguage());              //To switch TTS voice package back.
            speakSpeech("");                              // Send empty string to TTS Engine to eliminate voice lag after user goes back without changing the language.
            startActivity(new Intent(getBaseContext(), LanguageDownloadActivity.class).putExtras(bundle));
            dialog.dismiss();
        } else {
            Toast.makeText(LanguageSelectTalkBackActivity.this, strCheckConnectivity, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteLangDialog(DialogInterface dialog, int which, String strLangRemoved) {
        String locale = LangMap.get(offlineLanguages[which]);
        File file = getBaseContext().getDir(locale, Context.MODE_PRIVATE);
        if (file.exists()) {
            deleteRecursive(file);
        }
        file.delete();
        mSession.setRemoved(locale);
        onlineLanguages = getOnlineLanguages();
        offlineLanguages = getOfflineLanguages();
        adapter_lan = new ArrayAdapter<String>(getBaseContext(),
                R.layout.simple_spinner_item, populateCountryNameByUserType(offlineLanguages));
        adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSelect.setAdapter(adapter_lan);
        dialog.dismiss();
        Toast.makeText(LanguageSelectTalkBackActivity.this, strLangRemoved, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    private void hideViewsForNonTtsLang(boolean disableViews) {
        //TODO Hide views to when user selected non tts language.
        if(disableViews) {
            findViewById(R.id.tv4).setVisibility(View.GONE);
            findViewById(R.id.ivTtsVoiceDat).setVisibility(View.GONE);
            findViewById(R.id.btnDownloadVoiceData).setVisibility(View.GONE);
            findViewById(R.id.tv5).setVisibility(View.GONE);
            findViewById(R.id.llImg).setVisibility(View.GONE);
            findViewById(R.id.changeTtsLangBut).setVisibility(View.GONE);
        }else{
            findViewById(R.id.tv4).setVisibility(View.VISIBLE);
            findViewById(R.id.ivTtsVoiceDat).setVisibility(View.VISIBLE);
            findViewById(R.id.btnDownloadVoiceData).setVisibility(View.VISIBLE);
            findViewById(R.id.tv5).setVisibility(View.VISIBLE);
            findViewById(R.id.llImg).setVisibility(View.VISIBLE);
            findViewById(R.id.changeTtsLangBut).setVisibility(View.VISIBLE);
        }
    }

    private void setViewsForNonTtsLang() {
        int subStrLen = 7;
        String stepStr = "Step 2: "+ ((TextView) findViewById(R.id.tv6))
                .getText().toString().substring(subStrLen);
        SpannableString spannedStr = new SpannableString(stepStr);
        int boldTxtLen = 7;
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
        ((TextView) findViewById(R.id.tv6)).setText(spannedStr);
    }

    private void updateViewsForNewLangSelect() {
        if(Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.tv5).setVisibility(View.GONE);
            findViewById(R.id.llImg).setVisibility(View.GONE);
            findViewById(R.id.changeTtsLangBut).setVisibility(View.GONE);
        }

        int boldTxtLen = 7;
        SpannableString spannedStr = new SpannableString(((TextView)findViewById(R.id.tv2))
                .getText().toString());
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,boldTxtLen,0);
        ((TextView)findViewById(R.id.tv2)).setText(spannedStr);
        String str = "Step 2: Check if voice data for _ is available on your device. If not please turn on internet and download voice data for selected language. In case process is stalled please check your internet connection and retry";
        spannedStr = new SpannableString(str.replace("_",getLangShortenName(selectedLanguage)));
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,boldTxtLen,0);
        ((TextView)findViewById(R.id.tv4)).setText(spannedStr);

        if(Build.VERSION.SDK_INT < 21) {
            str = "Step 3: Set \'Google Text-to-speech (TTS)\' as default TTS engine and set _ as default language of the engine.";
            spannedStr = new SpannableString(str.replace("_", getLangShortenName(selectedLanguage)));
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
            ((TextView) findViewById(R.id.tv5)).setText(spannedStr);

            spannedStr = new SpannableString(((TextView) findViewById(R.id.tv6))
                    .getText().toString());
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
            ((TextView) findViewById(R.id.tv6)).setText(spannedStr);
        }else {
            int subStrLen = 8;
            String stepStr = "Step 3: "+ ((TextView) findViewById(R.id.tv6)).
                    getText().toString().substring(subStrLen);
            spannedStr = new SpannableString(stepStr);
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
            ((TextView) findViewById(R.id.tv6)).setText(spannedStr);
        }
    }

    private CharSequence getLangShortenName(String langName) {
        switch (langName){
            case "हिंदी":
                return "Hindi (India)";
            case "বাঙালি":
                return "Bengali (India)";
            default:
                return langName;
        }
    }

    private void setImageUsingGlide(int image, ImageView imgView) {
        GlideApp.with(this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(imgView);
    }

    public void checkTTSVoiceDataAvailable(View view){
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openTextToSpeechSetting(View view){
        startActivity(new Intent().setAction("com.android.settings.TTS_SETTINGS"));
        mSession.setWifiOnlyBtnPressedOnce(true);
    }

    private void deleteRecursive(File fileObj) {
        if (fileObj.isDirectory())
            for (File child : fileObj.listFiles())
                deleteRecursive(child);

        fileObj.delete();
    }

    private String[] getOfflineLanguages()
    {
        List<String> lang = new ArrayList<>();

        String current = mSession.getLanguage();
        if( mSession.isDownloaded(ENG_IN) &&
                !current.equals(ENG_IN))
            lang.add(LangValueMap.get(ENG_IN));

        if( mSession.isDownloaded(SessionManager.ENG_US) &&
                !current.equals(SessionManager.ENG_US))
            lang.add(LangValueMap.get(SessionManager.ENG_US));

        if( mSession.isDownloaded(SessionManager.ENG_UK) &&
                !current.equals(SessionManager.ENG_UK))
            lang.add(LangValueMap.get(SessionManager.ENG_UK));

        if( mSession.isDownloaded(SessionManager.ENG_AU) &&
                !current.equals(SessionManager.ENG_AU))
            lang.add(LangValueMap.get(SessionManager.ENG_AU));

        if( mSession.isDownloaded(HI_IN) &&
                !current.equals(HI_IN))
            lang.add(LangValueMap.get(HI_IN));

        if(( mSession.isDownloaded(SessionManager.BN_IN) &&
                !current.equals(SessionManager.BN_IN)))
            lang.add(LangValueMap.get(SessionManager.BN_IN));

        if( mSession.isDownloaded(SessionManager.MR_IN) &&
                !current.equals(SessionManager.MR_IN))
            lang.add(LangValueMap.get(SessionManager.MR_IN));

        lang.add(LangValueMap.get(current));

        return lang.toArray(new String[lang.size()]);
    }

    private String[] getOnlineLanguages()
    {
        List<String> lang = new ArrayList<>();
        if( !mSession.isDownloaded(ENG_IN))
            lang.add(LangValueMap.get(ENG_IN));
        if( !mSession.isDownloaded(SessionManager.ENG_US))
            lang.add(LangValueMap.get(SessionManager.ENG_US));
        if( !mSession.isDownloaded(SessionManager.ENG_UK))
            lang.add(LangValueMap.get(SessionManager.ENG_UK));
        if( !mSession.isDownloaded(SessionManager.ENG_AU))
            lang.add(LangValueMap.get(SessionManager.ENG_AU));
        if( !mSession.isDownloaded(HI_IN))
            lang.add(LangValueMap.get(HI_IN));
        if( !mSession.isDownloaded(SessionManager.BN_IN))
            lang.add(LangValueMap.get(SessionManager.BN_IN));
        if( !mSession.isDownloaded(SessionManager.MR_IN))
            lang.add(LangValueMap.get(SessionManager.MR_IN));
        return lang.toArray(new String[lang.size()]);
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

        stopMeasuring("ChangeLanguageActivity");
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
        onlineLanguages = getOnlineLanguages();
        offlineLanguages = getOfflineLanguages();
        adapter_lan = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, populateCountryNameByUserType(offlineLanguages));
        adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSelect.setAdapter(adapter_lan);
        if(isOpenedTtsSett)
            getSpeechLanguage("");
        if(mSelectedItem != -1)
            languageSelect.setSelection(mSelectedItem);

        if(!mSession.getToastMessage().isEmpty()) {
            Toast.makeText(this, mSession.getToastMessage(), Toast.LENGTH_SHORT).show();
            mSession.setToastMessage("");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                finish(); break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
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
            case R.id.reset:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                finish(); break;
            case R.id.feedback:
                if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, FeedbackActivityTalkBack.class));
                } else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
                finish();
                break;
            case android.R.id.home:
                onBackPressed(); break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACT_CHECK_TTS_DATA)
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL)
                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Pass", Toast.LENGTH_SHORT).show();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES":
                    isTtsLangChanged = (sysTtsLang != null &&
                            !sysTtsLang.equals(intent.getStringExtra("systemTtsRegion")));

                    sysTtsLang = intent.getStringExtra("systemTtsRegion");
                    if(intent.getBooleanExtra("saveUserLanguage",false)){
                        saveLanguage();
                    }else if(intent.getBooleanExtra("showError",false))
                        //Change Toast here
                        Toast.makeText(context, "Please turn on internet and complete Step 3", Toast.LENGTH_LONG).show();
                    if(isOpenedTtsSett && isTtsLangChanged && !mSession.getLangSettingIsCorrect())
                        if((!sysTtsLang.equals("-r")) &&
                            (mSession.getLanguage().equals(BN_IN) && (sysTtsLang.equals(BN_IN) || (sysTtsLang.equals(BE_IN)))) ||
                                (!mSession.getLanguage().equals(BN_IN)
                                        && mSession.getLanguage().equals(sysTtsLang)))
                            shouldSaveLang = true;
                    isOpenedTtsSett = isTtsLangChanged = false;
                    break;
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_RES":
                    // Only for 21 and above device
                    if(intent.hasExtra("isVoiceAvail") &&
                            intent.getBooleanExtra("isVoiceAvail", false))
                        saveLanguage();
                    else {
                        Toast.makeText(context, "Voice data not available. Please turn on internet and complete Step 2", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        addAccessibilityDelegateToSpinners();
    }

    private void addAccessibilityDelegateToSpinners() {
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
            languageSelect.setAccessibilityDelegate(new View.AccessibilityDelegate() {
                @Override
                public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
                    super.onInitializeAccessibilityEvent(host, event);
                    if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                        findViewById(R.id.tv4).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
                    }
                }
            });
        }
    }

    private void saveLanguage() {
        mSession.setLanguage(LangMap.get(selectedLanguage));
        Bundle bundle = new Bundle();
        bundle.putString("LanguageSet", "Switched to "+ LangMap.get(selectedLanguage));
        bundleEvent("Language",bundle);
        setUserProperty("UserLanguage", LangMap.get(selectedLanguage));
        setCrashlyticsCustomKey("UserLanguage",  LangMap.get(selectedLanguage));
        Toast.makeText(this, "Language Changed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        mSession.setLanguageChange(1);
        startActivity(intent);
        finishAffinity();
    }

    private void setSpeechLanguage(String speechLang){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_LANG");
        intent.putExtra("speechLanguage", speechLang);
        sendBroadcast(intent);
    }

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText);
        sendBroadcast(intent);
    }

    private void getSpeechLanguage(String saveLang){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_REQ");
        intent.putExtra("saveSelectedLanguage", saveLang);
        sendBroadcast(intent);
    }

    private void checkIfVoiceAvail(String language) {
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_REQ");
        intent.putExtra("language", language);
        sendBroadcast(intent);
    }

    private String[] populateCountryNameByUserType(String[] langNameToBeShorten) {
        String[] shortenLanguageNames = new String[langNameToBeShorten.length];
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            for (int i = 0; i < langNameToBeShorten.length; i++) {
                switch (langNameToBeShorten[i]) {
                    case "मराठी":
                        shortenLanguageNames[i] = "Marathi (India)";
                        break;
                    case "हिंदी":
                        shortenLanguageNames[i] = "Hindi (India)";
                        break;
                    case "বাঙালি":
                        shortenLanguageNames[i] = "Bengali (India)";
                        break;
                    case "English (India)":
                        shortenLanguageNames[i] = "English (India)";
                        break;
                    case "English (United Kingdom)":
                        shortenLanguageNames[i] = "English (United Kingdom)";
                        break;
                    case "English (United States)":
                        shortenLanguageNames[i] = "English (United States)";
                        break;
                    case "English (Australia)":
                        shortenLanguageNames[i] = "English (Australia)";
                        break;
                    default:
                        shortenLanguageNames[i] = langNameToBeShorten[i];
                        break;
                }
            }
        }else {
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
                    default:
                        shortenLanguageNames[i] = langNameToBeShorten[i];
                        break;
                }
            }
        }
        return shortenLanguageNames;
    }

    private String[] removeCurrentLangFromList(String[] currentList) {
        String[] filteredList = new String[currentList.length-1];
        int j=-1;
        for (int i = 0; i < currentList.length; i++) {
            if (currentList[i].equals(LangValueMap.get(mSession.getLanguage()))) {
            }else
                filteredList[++j] = currentList[i];
        }
        return filteredList;
    }
}
