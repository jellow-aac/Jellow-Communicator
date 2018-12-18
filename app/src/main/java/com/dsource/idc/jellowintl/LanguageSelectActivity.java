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
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

public class LanguageSelectActivity extends AppCompatActivity{

    public static final String FINISH = "finish";
    private final int ACT_CHECK_TTS_DATA = 1;
    SessionManager mSession;
    String[] offlineLanguages;
    String[] onlineLanguages;
    Spinner languageSelect;
    String selectedLanguage, sysTtsLang, mLangChanged;
    Button save,add,delete, changeTtsLang;
    ArrayAdapter<String> adapter_lan;
    boolean isOpenedTtsSett = false, isTtsLangChanged = false, shouldSaveLang = false;
    private int mSelectedItem = -1;
    // Variable hold strings from regional string.xml file.
    private String mStep1, mStep2, mStep3, mStep4, mTitleEditLang,
            mTitleChgLang, mRawStr4Step3, mCompleteStep2, mCompleteStep3, mRawStrStep2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_language_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F7F3C6'>"+getString(R.string.Language)+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);

        mSession = new SessionManager(this);
        mTitleEditLang = getString(R.string.editLanguage);
        mTitleChgLang = getString(R.string.txtChangeLanguage);
        mStep1 = getString(R.string.change_language_line2);
        mStep2 = getString(R.string.change_language_line5);
        mStep3 = getString(R.string.change_language_line4);
        mStep4 = getString(R.string.txtApplyChanges);
        mRawStr4Step3 = getString(R.string.step3);
        mCompleteStep2 = getString(R.string.txt_actLangSel_completestep2);
        mCompleteStep3 = getString(R.string.txt_actLangSel_completestep3);
        mRawStrStep2 = getString(R.string.txtStep2);

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
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        final String strDefaultLangEr = getString(R.string.txt_save_same_lang_def);
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
        final String strNoMoreLang2Add = getString(R.string.no_more_lang_2_add);
        final String strCheckConnectivity = getString(R.string.checkConnectivity);
        final String strDownloadableLang = getString(R.string.downloadableLang);
        final String strDownload = getString(R.string.download);
        final String strCancel = getString(R.string.cancel);
        final String strDisableWifiOnly =  getString(R.string.disable_wifi_only);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("LanguageSelect Add");
                if(!mSession.isWifiOnlyBtnPressedOnce()){
                    Toast.makeText(LanguageSelectActivity.this,
                            strDisableWifiOnly, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if (onlineLanguages.length == 0) {
                        Toast.makeText(LanguageSelectActivity.this, strNoMoreLang2Add,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    delete.setEnabled(false);
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        new MaterialDialog.Builder(LanguageSelectActivity.this)
                                .title(strDownloadableLang)
                                .items(populateCountryNameByUserType(onlineLanguages))
                                .itemsCallbackSingleChoice(
                                        0, new MaterialDialog.ListCallbackSingleChoice() {
                                            @Override
                                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                                showAddLangDialog(dialog, which, strCheckConnectivity);
                                                return true;
                                            }
                                        })
                                .positiveText(strDownload)
                                .negativeText(strCancel)
                                .cancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        delete.setEnabled(true);
                                    }
                                })
                                .show();
                    }else{
                        new MaterialDialog.Builder(LanguageSelectActivity.this)
                            .title(strDownloadableLang)
                            .items(populateCountryNameByUserType(onlineLanguages))
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                            showAddLangDialog(dialog, which, strCheckConnectivity);
                                        }
                                    })
                            .negativeText(strCancel)
                            .cancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    delete.setEnabled(true);
                                }
                            })
                            .show();
                    }
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
        final String strNoMoreLang2Del =  getString(R.string.no_more_lang_2_del);
        final String strLangRemoved = getString(R.string.languageRemoved);
        final String strDownloadedLang = getString(R.string.downloadedLang);
        final String strRemove = getString(R.string.remove);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crashlytics.log("LanguageSelect Delete");
                try {
                    if (offlineLanguages.length == 1) {
                        Toast.makeText(LanguageSelectActivity.this, strNoMoreLang2Del,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    add.setEnabled(false);
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        new MaterialDialog.Builder(LanguageSelectActivity.this)
                                .title(strDownloadedLang)
                                .items(populateCountryNameByUserType(removeCurrentLangFromList(offlineLanguages)))
                                .itemsCallbackSingleChoice(
                                        0, new MaterialDialog.ListCallbackSingleChoice() {
                                            @Override
                                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                                add.setEnabled(true);
                                                String locale = LangMap.get(offlineLanguages[which]);
                                                if (mSession.getLanguage().equals(locale)) {
                                                    Toast.makeText(LanguageSelectActivity.this, strLangCurrentlyInUse, Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    return true;
                                                }
                                                showDeleteLangDialog(dialog, itemView, which, text, strLangRemoved);
                                                return true;
                                            }
                                        })
                                .positiveText(strRemove)
                                .negativeText(strCancel)
                                .cancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        add.setEnabled(true);
                                    }
                                })
                                .show();
                    }else{
                        new MaterialDialog.Builder(LanguageSelectActivity.this)
                                .title(strDownloadedLang)
                                .items(populateCountryNameByUserType(removeCurrentLangFromList(offlineLanguages)))
                                .itemsCallback( new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                                add.setEnabled(true);
                                                String locale = LangMap.get(offlineLanguages[which]);
                                                if (mSession.getLanguage().equals(locale)) {
                                                    Toast.makeText(LanguageSelectActivity.this, strLangCurrentlyInUse, Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    return;
                                                }
                                                showDeleteLangDialog(dialog, itemView, which, text, strLangRemoved);
                                            }
                                        })
                                .negativeText(strCancel)
                                .cancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        add.setEnabled(true);
                                    }
                                })
                                .show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mLangChanged = getString(R.string.languageChanged);
    }

    private void showAddLangDialog(MaterialDialog dialog, int which, String strCheckConnectivity) {
        delete.setEnabled(true);
        ConnectivityManager cm =
                (ConnectivityManager) LanguageSelectActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

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

            Toast.makeText(LanguageSelectActivity.this, strCheckConnectivity, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteLangDialog(MaterialDialog dialog, View itemView, int which, CharSequence text, String strLangRemoved) {
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
        Toast.makeText(LanguageSelectActivity.this, strLangRemoved, Toast.LENGTH_SHORT).show();
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
        if(mSession.getLanguage().equals(SessionManager.BN_IN))subStrLen = 12;
        String stepStr = mRawStrStep2 +" "+ mStep4.substring(subStrLen);
        SpannableString spannedStr = new SpannableString(stepStr);
        int boldTxtLen = 7;
        if(mSession.getLanguage().equals(SessionManager.BN_IN))boldTxtLen = 14;
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
        ((TextView) findViewById(R.id.tv6)).setText(spannedStr);
    }

    private void updateViewsForNewLangSelect() {
        if(Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.tv5).setVisibility(View.GONE);
            findViewById(R.id.llImg).setVisibility(View.GONE);
            findViewById(R.id.changeTtsLangBut).setVisibility(View.GONE);
        }

        if(mSession.getLanguage().equals(BN_IN))
            boldTitleOnScreen();

        int boldTxtLen = 7;
        SpannableString spannedStr = new SpannableString(mStep1);
        if(mSession.getLanguage().equals(BN_IN)) boldTxtLen = 10;
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,boldTxtLen,0);
        ((TextView)findViewById(R.id.tv2)).setText(spannedStr);

        spannedStr = new SpannableString(mStep2.replace("_",getTTsLanguage()));
        if(mSession.getLanguage().equals(BN_IN)) boldTxtLen = 14;
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,boldTxtLen,0);
        ((TextView)findViewById(R.id.tv4)).setText(spannedStr);

        if(Build.VERSION.SDK_INT < 21) {
            spannedStr = new SpannableString(mStep3.replace("_", getTTsLanguage()));
            if (mSession.getLanguage().equals(BN_IN)) boldTxtLen = 12;
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
            ((TextView) findViewById(R.id.tv5)).setText(spannedStr);

            spannedStr = new SpannableString(mStep4);
            if (mSession.getLanguage().equals(BN_IN)) boldTxtLen = 12;
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
            ((TextView) findViewById(R.id.tv6)).setText(spannedStr);
        }else {
            int subStrLen = 8;
            if(mSession.getLanguage().equals(SessionManager.BN_IN))subStrLen = 12;
            else if(mSession.getLanguage().equals(SessionManager.HI_IN))subStrLen = 7;
            else if(mSession.getLanguage().equals(SessionManager.MR_IN))subStrLen = 6;
            String stepStr = mRawStr4Step3 +" "+ mStep4.substring(subStrLen);
            spannedStr = new SpannableString(stepStr);
            if (mSession.getLanguage().equals(BN_IN)) boldTxtLen = 12;
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
            ((TextView) findViewById(R.id.tv6)).setText(spannedStr);
        }
    }

    private void boldTitleOnScreen() {
        SpannableString spannedStr = new SpannableString(mTitleEditLang);
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,spannedStr.length(),0);
        ((TextView)findViewById(R.id.txt_title_editLang)).setText(spannedStr);

        spannedStr = new SpannableString(mTitleChgLang);
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,spannedStr.length(),0);
        ((TextView)findViewById(R.id.txt_title_chgLang)).setText(spannedStr);
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
        if( mSession.isDownloaded(SessionManager.ENG_IN) &&
                !current.equals(SessionManager.ENG_IN))
            lang.add(LangValueMap.get(SessionManager.ENG_IN));

        if( mSession.isDownloaded(SessionManager.ENG_US) &&
                !current.equals(SessionManager.ENG_US))
            lang.add(LangValueMap.get(SessionManager.ENG_US));

        if( mSession.isDownloaded(SessionManager.ENG_UK) &&
                !current.equals(SessionManager.ENG_UK))
            lang.add(LangValueMap.get(SessionManager.ENG_UK));

        if( mSession.isDownloaded(SessionManager.ENG_AU) &&
                !current.equals(SessionManager.ENG_AU))
            lang.add(LangValueMap.get(SessionManager.ENG_AU));

        if( mSession.isDownloaded(SessionManager.HI_IN) &&
                !current.equals(SessionManager.HI_IN))
            lang.add(LangValueMap.get(SessionManager.HI_IN));

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
        if( !mSession.isDownloaded(SessionManager.ENG_IN))
            lang.add(LangValueMap.get(SessionManager.ENG_IN));
        if( !mSession.isDownloaded(SessionManager.ENG_US))
            lang.add(LangValueMap.get(SessionManager.ENG_US));
        if( !mSession.isDownloaded(SessionManager.ENG_UK))
            lang.add(LangValueMap.get(SessionManager.ENG_UK));
        if( !mSession.isDownloaded(SessionManager.ENG_AU))
            lang.add(LangValueMap.get(SessionManager.ENG_AU));
        if( !mSession.isDownloaded(SessionManager.HI_IN))
            lang.add(LangValueMap.get(SessionManager.HI_IN));
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
                        Toast.makeText(context, mCompleteStep3, Toast.LENGTH_LONG).show();
                    if(isOpenedTtsSett && isTtsLangChanged && !mSession.getLangSettingIsCorrect())
                        if((!sysTtsLang.equals("-r")) &&
                            (mSession.getLanguage().equals(ENG_IN) && sysTtsLang.equals(HI_IN)) ||
                                (mSession.getLanguage().equals(BN_IN) && (sysTtsLang.equals(BN_IN) || (sysTtsLang.equals(BE_IN)))) ||
                                    (!mSession.getLanguage().equals(ENG_IN) && !mSession.getLanguage().equals(BN_IN)
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
                        Toast.makeText(context, mCompleteStep2, Toast.LENGTH_LONG).show();
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

    private String[] populateCountryNameByUserType(String[] langNameToBeShorten) {
        String[] shortenLanguageNames = new String[langNameToBeShorten.length];
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            for (int i = 0; i < langNameToBeShorten.length; i++) {
                switch (langNameToBeShorten[i]) {
                    case "मराठी":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_marathi);
                        break;
                    case "हिंदी":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_hindi);
                        break;
                    case "বাঙালি":
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
