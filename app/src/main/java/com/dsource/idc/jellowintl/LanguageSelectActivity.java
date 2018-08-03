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
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangValueMap;

public class LanguageSelectActivity extends AppCompatActivity{

    public static final String FINISH = "finish";
    private final int ACT_CHECK_TTS_DATA = 1;
    SessionManager mSession;
    String[] offlineLanguages;
    String[] onlineLanguages;
    Spinner languageSelect;
    String selectedLanguage, systemTtsLang, mLangChanged;
    Button save,add,delete, changeTtsLang;
    ArrayAdapter<String> adapter_lan;
    boolean isOpenedTtsSett = false, isTtsLangChanged = false, shouldSaveLang = false;
    private int mSelectedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.Language)+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        mSession = new SessionManager(this);
        if(Build.VERSION.SDK_INT >= 21)
            setupViewsForAboveKitkatDevices();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_RES");
        registerReceiver(receiver, filter);

        getSpeechLanguage("");
        offlineLanguages = getOfflineLanguages();
        onlineLanguages = getOnlineLanguages();
        languageSelect = findViewById(R.id.selectDownloadedLanguageSpinner);

        adapter_lan = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, shortLangNameForDisplay(offlineLanguages));

        adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languageSelect.setAdapter(adapter_lan);
        languageSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLanguage = offlineLanguages[i];
                mSelectedItem = i;
                if(Build.VERSION.SDK_INT < 21)
                    setupViewsForBelowLollipopDevices();
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
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        final String strDefaultLangEr = getString(R.string.txt_save_same_lang_def);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("LanguageSelect Apply");
                if(selectedLanguage != null)
                {
                    if(Build.VERSION.SDK_INT >= 21 &&
                            mSession.getLanguage().equals(LangMap.get(selectedLanguage))) {
                        Toast.makeText(LanguageSelectActivity.this,
                                strDefaultLangEr, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LanguageSelectActivity.this,
                                strDefaultLangEr, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getSpeechLanguage(LangMap.get(selectedLanguage));
                }
            }
        });

        add = findViewById(R.id.addBut);
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        final String strLangLimitExceeded = getString(R.string.languageLimitExceeded);
        final String strCheckConnectivity = getString(R.string.checkConnectivity);
        final String strDownloadableLang = getString(R.string.downloadableLang);
        final String strDownload = getString(R.string.download);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("LanguageSelect Add");
                try {
                    delete.setEnabled(false);
                     new MaterialDialog.Builder(LanguageSelectActivity.this)
                            .title(strDownloadableLang)
                            .items(shortLangNameForDisplay(onlineLanguages))
                            .itemsCallbackSingleChoice(
                                    0, new MaterialDialog.ListCallbackSingleChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                            delete.setEnabled(true);
                                            ConnectivityManager cm =
                                                    (ConnectivityManager)LanguageSelectActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

                                            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                            boolean isConnected = activeNetwork != null &&
                                                    activeNetwork.isConnectedOrConnecting();

                                            if(isConnected)
                                            {
                                                if(offlineLanguages.length < 3)
                                                {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString(LCODE,LangMap.get(onlineLanguages[which]));
                                                    bundle.putBoolean(FINISH,false);
                                                    setSpeechLanguage(LangMap.get(onlineLanguages[which])); //To start TTS voice package download automatically.
                                                    setSpeechLanguage(mSession.getLanguage());              //To switch TTS voice package back.
                                                    speakSpeech("");                              // Send empty string to TTS Engine to eliminate voice lag after user goes back without changing the language.
                                                    startActivity(new Intent(getBaseContext(),LanguageDownloadActivity.class).putExtras(bundle));
                                                    dialog.dismiss();
                                                } else {
                                                    Toast.makeText(LanguageSelectActivity.this, strLangLimitExceeded,Toast.LENGTH_SHORT).show();
                                                }

                                            }else {

                                                Toast.makeText(LanguageSelectActivity.this, strCheckConnectivity,Toast.LENGTH_SHORT).show();
                                            }

                                            return true;
                                        }
                                    })
                            .positiveText(strDownload)
                            .cancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    delete.setEnabled(true);
                                }
                            })
                            .show();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        delete = findViewById(R.id.delBut);
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        final String strLangCurrentlyInUse = getString(R.string.languageCurrentlyInUse);
        final String strLangRemoved = getString(R.string.languageRemoved);
        final String strDownloadedLang = getString(R.string.downloadedLang);
        final String strRemove = getString(R.string.remove);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("LanguageSelect Delete");
                try {
                    add.setEnabled(false);
                    new MaterialDialog.Builder(LanguageSelectActivity.this)
                            .title(strDownloadedLang)
                            .items(shortLangNameForDisplay(offlineLanguages))
                            .itemsCallbackSingleChoice(
                                    0, new MaterialDialog.ListCallbackSingleChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                            add.setEnabled(true);
                                            String locale = LangMap.get(offlineLanguages[which]);
                                            if(mSession.getLanguage().equals(locale))
                                            {
                                                Toast.makeText(LanguageSelectActivity.this, strLangCurrentlyInUse,Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                return true;
                                            }
                                            File file = getBaseContext().getDir(locale, Context.MODE_PRIVATE);
                                            if(file.exists())
                                            {
                                                deleteRecursive(file);
                                            }
                                                file.delete();
                                            mSession.setRemoved(locale);
                                            onlineLanguages = getOnlineLanguages();
                                            offlineLanguages = getOfflineLanguages();
                                            adapter_lan = new ArrayAdapter<String>(getBaseContext(),
                                                    R.layout.simple_spinner_item, shortLangNameForDisplay(offlineLanguages));
                                            adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            languageSelect.setAdapter(adapter_lan);
                                            dialog.dismiss();
                                            Toast.makeText(LanguageSelectActivity.this,strLangRemoved,Toast.LENGTH_SHORT).show();
                                            return true;
                                        }
                                    })
                            .positiveText(strRemove)
                            .cancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    add.setEnabled(true);
                                }
                            })
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mLangChanged = getString(R.string.languageChanged);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    private void setupViewsForBelowLollipopDevices() {
        SpannableString spannedStr = new SpannableString(getString(R.string.change_language_line5).replace("_", getTTsLanguage()));
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,7,0);
        ((TextView)findViewById(R.id.tv4)).setText(spannedStr);
        spannedStr = new SpannableString(getString(R.string.change_language_line4).replace("_", getTTsLanguage()));
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,7,0);
        ((TextView)findViewById(R.id.tv5)).setText(spannedStr);
        findViewById(R.id.btnDownloadVoiceData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        spannedStr = new SpannableString(getString(R.string.change_language_line2));
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,7,0);
        ((TextView)findViewById(R.id.tv2)).setText(spannedStr);
        spannedStr = new SpannableString(getString(R.string.txtApplyChanges));
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,7,0);
        ((TextView)findViewById(R.id.tv6)).setText(spannedStr);
        spannedStr = null;
    }

    private void setupViewsForAboveKitkatDevices() {
        findViewById(R.id.llFollwStep).setVisibility(View.GONE);
        findViewById(R.id.llStep2).setVisibility(View.GONE);
        findViewById(R.id.ivTtsVoiceDat).setVisibility(View.GONE);
        findViewById(R.id.btnDownloadVoiceData).setVisibility(View.GONE);
        findViewById(R.id.llStep3).setVisibility(View.GONE);
        findViewById(R.id.llImg).setVisibility(View.GONE);
        findViewById(R.id.changeTtsLangBut).setVisibility(View.GONE);
        int subStrLen = 8;
        if(mSession.getLanguage().equals(SessionManager.HI_IN))subStrLen = 7;
        ((TextView)findViewById(R.id.tv2)).setText(getString(R.string.change_language_line2).substring(subStrLen));
        ((TextView)findViewById(R.id.tv6)).setText(getString(R.string.txtApplyChanges).substring(subStrLen));
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

        lang.add(LangValueMap.get(current));

        if( mSession.isDownloaded(SessionManager.ENG_IN) &&
                !current.equals(SessionManager.ENG_IN))
            lang.add(LangValueMap.get(SessionManager.ENG_IN));

        if( mSession.isDownloaded(SessionManager.ENG_US) &&
                !current.equals(SessionManager.ENG_US))
            lang.add(LangValueMap.get(SessionManager.ENG_US));

        if( mSession.isDownloaded(SessionManager.ENG_UK) &&
                !current.equals(SessionManager.ENG_UK))
            lang.add(LangValueMap.get(SessionManager.ENG_UK));

        if( mSession.isDownloaded(SessionManager.HI_IN) &&
                !current.equals(SessionManager.HI_IN))
            lang.add(LangValueMap.get(SessionManager.HI_IN));
        return lang.toArray(new String[lang.size()]);
    }

    private String[] getOnlineLanguages()
    {
        List<String> lang = new ArrayList<>();


        if( !mSession.isDownloaded(SessionManager.ENG_IN))
            lang.add(LangValueMap.get(SessionManager.ENG_IN));
        if( !mSession.isDownloaded(SessionManager.ENG_US))
            lang.add(LangValueMap.get(SessionManager.ENG_US));
        if( !mSession.isDownloaded(SessionManager.ENG_UK))
            lang.add(LangValueMap.get(SessionManager.ENG_UK));
        if( !mSession.isDownloaded(SessionManager.HI_IN))
            lang.add(LangValueMap.get(SessionManager.HI_IN));
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
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        startMeasuring();
        onlineLanguages = getOnlineLanguages();
        offlineLanguages = getOfflineLanguages();
        adapter_lan = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, shortLangNameForDisplay(offlineLanguages));
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings: startActivity(new Intent(getApplication(), SettingActivity.class)); finish(); break;
            case R.id.profile: startActivity(new Intent(this, ProfileFormActivity.class)); finish(); break;
            case R.id.info: startActivity(new Intent(this, AboutJellowActivity.class)); finish(); break;
            case R.id.usage: startActivity(new Intent(this, TutorialActivity.class)); finish(); break;
            case R.id.keyboardinput: startActivity(new Intent(this, KeyboardInputActivity.class)); finish(); break;
            case R.id.reset: startActivity(new Intent(this, ResetPreferencesActivity.class)); finish(); break;
            case R.id.feedback: startActivity(new Intent(this, FeedbackActivity.class)); finish(); break;
            case android.R.id.home: onBackPressed(); break;
            default: return super.onOptionsItemSelected(item);
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
                    isTtsLangChanged = (systemTtsLang != null &&
                            !systemTtsLang.equals(intent.getStringExtra("systemTtsRegion")));

                    systemTtsLang = intent.getStringExtra("systemTtsRegion");
                    if(intent.getBooleanExtra("saveUserLanguage",false)){
                        saveLanguage();
                    }else if(intent.getBooleanExtra("showError",false))
                        Toast.makeText(context, getString(R.string.set_engine_language),
                                Toast.LENGTH_LONG).show();

                    if(isOpenedTtsSett && isTtsLangChanged && !mSession.getLangSettingIsCorrect())
                        if((mSession.getLanguage().equals("en-rIN") && systemTtsLang.equals("hi-rIN")) ||
                                (!mSession.getLanguage().equals("en-rIN") && mSession.getLanguage().equals(systemTtsLang)))
                            shouldSaveLang = true;
                    isOpenedTtsSett = isTtsLangChanged = false;
                    break;
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_RES":
                    if(intent.hasExtra("isVoiceAvail") &&
                            intent.getBooleanExtra("isVoiceAvail", false))
                        saveLanguage();
                    else {
                        setSpeechLanguage(mSession.getLanguage());
                        Toast.makeText(context, getString(R.string.txt_actLangSel_completestep2),
                                Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };

    private void saveLanguage() {
        mSession.setLanguage(LangMap.get(selectedLanguage));
        Bundle bundle = new Bundle();
        bundle.putString("LanguageSet", "Switched to "+ LangMap.get(selectedLanguage));
        bundleEvent("Language",bundle);
        setUserProperty("UserLanguage", LangMap.get(selectedLanguage));
        setCrashlyticsCustomKey("UserLanguage",  LangMap.get(selectedLanguage));
        Toast.makeText(this, mLangChanged, Toast.LENGTH_SHORT).show();
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

    private String getTTsLanguage() {
        String language = selectedLanguage;
        if(language.equals("English (India)") || language.equals("हिंदी"))
            return  "Hindi (India)";
        return selectedLanguage;
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
}