package com.dsource.idc.jellowintl;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
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
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static com.dsource.idc.jellowintl.LanguageDownloadActivity.CLOSE;
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
import static com.dsource.idc.jellowintl.utility.SessionManager.ES_ES;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangValueMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.SP_ES;

public class LanguageSelectActivity extends SpeechEngineBaseActivity {
    String[] availableLanguages;
    Spinner languageSelect;
    String selectedLanguage, mLangChanged;
    Button save, changeTtsLang;
    ArrayAdapter<String> adapter_lan;
    private int mSelectedItem = -1;
    // Variable hold strings from regional string.xml file.
    private String mStep1, mStep2, mStep3, mStep4, mTitleChgLang, mRawStr4Step3,
            mCompleteStep2, mCompleteStep3, mRawStrStep2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);
        enableNavigationBack();
        setActivityTitle(getString(R.string.Language));
        LanguageFactory.deleteOldLanguagePackagesInBackground(this);
        mTitleChgLang = getString(R.string.txtChangeLanguage);
        mStep1 = getString(R.string.change_language_line2);
        mStep2 = getString(R.string.change_language_line5);
        mStep3 = getString(R.string.change_language_line4);
        mStep4 = getString(R.string.txtApplyChanges);
        mRawStr4Step3 = getString(R.string.step3);
        mCompleteStep2 = getString(R.string.txt_actLangSel_completestep2);
        mCompleteStep3 = getString(R.string.txt_actLangSel_completestep3);
        mRawStrStep2 = getString(R.string.txtStep2);
        mLangChanged = getString(R.string.languageChanged);

        setImageUsingGlide(R.drawable.tts_wifi_1, ((ImageView) findViewById(R.id.ivAddLang1)));
        setImageUsingGlide(R.drawable.tts_wifi_2, ((ImageView) findViewById(R.id.ivAddLang2)));
        setImageUsingGlide(R.drawable.tts_wifi_3, ((ImageView) findViewById(R.id.ivAddLang3)));
        setImageUsingGlide(R.drawable.gtts3, ((ImageView) findViewById(R.id.ivTtsVoiceDat)));
        setImageUsingGlide(R.drawable.arrow, ((ImageView) findViewById(R.id.ivArrow1)));
        setImageUsingGlide(R.drawable.arrow, ((ImageView) findViewById(R.id.ivArrow2)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.tv5).setVisibility(View.GONE);
            findViewById(R.id.llImg).setVisibility(View.GONE);
            findViewById(R.id.changeTtsLangBut).setVisibility(View.GONE);
        } else {
            setImageUsingGlide(R.drawable.gtts1, ((ImageView) findViewById(R.id.ivTtsSetting1)));
            setImageUsingGlide(R.drawable.gtts2, ((ImageView) findViewById(R.id.ivTtsSetting2)));
            setImageUsingGlide(R.drawable.gtts4, ((ImageView) findViewById(R.id.ivTtsSetting3)));
            setImageUsingGlide(R.drawable.arrow, ((ImageView) findViewById(R.id.ivArrow3)));
            setImageUsingGlide(R.drawable.arrow, ((ImageView) findViewById(R.id.ivArrow4)));
        }

        availableLanguages = LanguageFactory.getAvailableLanguages(this);
        languageSelect = findViewById(R.id.selectDownloadedLanguageSpinner);

        adapter_lan = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, populateCountryNameByUserType(availableLanguages));
        adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languageSelect.setAdapter(adapter_lan);
        languageSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLanguage = availableLanguages[i];
                mSelectedItem = i;
                if (selectedLanguage.equals(LangValueMap.get(MR_IN))) {
                    hideViewsForNonTtsLang(true);
                    setViewsForNonTtsLang();
                } else {
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

                if(selectedLanguage.equals(LangValueMap.get(MR_IN)) && !LanguageFactory.isMarathiPackageAvailable
                        (LanguageSelectActivity.this)){
                    startActivity(new Intent(LanguageSelectActivity.this,
                            LanguageDownloadActivity.class)
                            .putExtra(LCODE, MR_IN).putExtra(CLOSE, true));
                    return;
                }
                /* If the current app language is same as new selected language then
                 *   Show Toast message string 'strDefaultLangEr'*/
                if (getSession().getLanguage().equals(LangMap.get(selectedLanguage))) {
                    Toast.makeText(LanguageSelectActivity.this,
                            strDefaultLangEr, Toast.LENGTH_SHORT).show();
                    return;
                }

                //if current user language is not marathi and user want to change current language
                // to marathi.
                if (!getSession().getLanguage().equals(MR_IN)
                        && selectedLanguage != null &&
                        selectedLanguage.equals(LangValueMap.get(MR_IN))) {
                    saveLanguage();
                } else if (selectedLanguage != null) {
                    /* If device is Lollipop or above AND
                     *   current app language is same as new selected language THEN
                     *   Show Toast message string 'strDefaultLangEr'*/
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (isVoiceAvailableForLanguage(LangMap.get(selectedLanguage)))
                            saveLanguage();
                        else
                            Toast.makeText(LanguageSelectActivity.this,
                                    mCompleteStep2, Toast.LENGTH_LONG).show();
                        /* If Speech Engine language is same as selected language OR
                         *  Selected language is Bengali and Speech Engine language is
                         *  either BN_IN or BE_IN form THEN OR
                         *  Selected language is Spanish and Speech Engine language is
                         *  either ES_ES or SP_ES form THEN
                         *  save the language and set language setting to true.
                         *  else
                         *  showDialog toast about Complete step 3.*/
                    } else if (getSpeechEngineLanguage().equals(LangMap.get(selectedLanguage)) ||
                            (LangMap.get(selectedLanguage).equals(BN_IN) &&
                                    (getSpeechEngineLanguage().equals(BN_IN) ||
                                            getSpeechEngineLanguage().equals(BE_IN))) ||
                            (LangMap.get(selectedLanguage).equals(ES_ES) &&
                                    (getSpeechEngineLanguage().equals(ES_ES) ||
                                            getSpeechEngineLanguage().equals(SP_ES))) ) {
                        saveLanguage();
                    } else {
                        Toast.makeText(LanguageSelectActivity.this,
                                mCompleteStep3, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
        if(getSession().getLanguage().equals(SessionManager.BN_IN))subStrLen = 12;
        String stepStr = mRawStrStep2 +" "+ mStep4.substring(subStrLen);
        SpannableString spannedStr = new SpannableString(stepStr);
        int boldTxtLen = 7;
        if(getSession().getLanguage().equals(SessionManager.BN_IN))boldTxtLen = 14;
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
        ((TextView) findViewById(R.id.tv6)).setText(spannedStr);
    }

    private void updateViewsForNewLangSelect() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.tv5).setVisibility(View.GONE);
            findViewById(R.id.llImg).setVisibility(View.GONE);
            findViewById(R.id.changeTtsLangBut).setVisibility(View.GONE);
        }

        if(getSession().getLanguage().equals(BN_IN))
            boldTitleOnScreen();

        int boldTxtLen = 7;
        SpannableString spannedStr = new SpannableString(mStep1);
        if(getSession().getLanguage().equals(BN_IN)) boldTxtLen = 10;
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,boldTxtLen,0);
        ((TextView)findViewById(R.id.tv2)).setText(spannedStr);

        spannedStr = new SpannableString(mStep2.replace("_",getTTsLanguage()));
        if(getSession().getLanguage().equals(BN_IN)) boldTxtLen = 14;
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0,boldTxtLen,0);
        ((TextView)findViewById(R.id.tv4)).setText(spannedStr);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            spannedStr = new SpannableString(mStep3.replace("_", getTTsLanguage()));
            if (getSession().getLanguage().equals(BN_IN)) boldTxtLen = 12;
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
            ((TextView) findViewById(R.id.tv5)).setText(spannedStr);

            spannedStr = new SpannableString(mStep4);
            if (getSession().getLanguage().equals(BN_IN)) boldTxtLen = 12;
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
            ((TextView) findViewById(R.id.tv6)).setText(spannedStr);
        }else {
            int subStrLen = 8;
            if(getSession().getLanguage().equals(SessionManager.BN_IN))subStrLen = 12;
            else if(getSession().getLanguage().equals(HI_IN))subStrLen = 7;
            else if(getSession().getLanguage().equals(SessionManager.MR_IN))subStrLen = 6;
            String stepStr = mRawStr4Step3 +" "+ mStep4.substring(subStrLen);
            spannedStr = new SpannableString(stepStr);
            if (getSession().getLanguage().equals(BN_IN)) boldTxtLen = 12;
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, boldTxtLen, 0);
            ((TextView) findViewById(R.id.tv6)).setText(spannedStr);
        }
    }

    private void boldTitleOnScreen() {
        SpannableString spannedStr = new SpannableString(mTitleChgLang);
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

    public void openSpeechDataSetting(View view){
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openSpeechSetting(View view){
        startActivity(new Intent().setAction("com.android.settings.TTS_SETTINGS"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        stopMeasuring("ChangeLanguageActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(LanguageSelectActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
        startMeasuring();
        availableLanguages = LanguageFactory.getAvailableLanguages(this);
        adapter_lan = new ArrayAdapter<String>(this, R.layout.simple_spinner_item,
                populateCountryNameByUserType(availableLanguages));
        adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSelect.setAdapter(adapter_lan);
        if(mSelectedItem != -1)
            languageSelect.setSelection(mSelectedItem);

        if(!getSession().getToastMessage().isEmpty()) {
            Toast.makeText(this, getSession().getToastMessage(), Toast.LENGTH_SHORT).show();
            getSession().setToastMessage("");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    private void saveLanguage() {
        getSession().setLanguage(LangMap.get(selectedLanguage));
        Bundle bundle = new Bundle();
        bundle.putString("LanguageSet", "Switched to "+ LangMap.get(selectedLanguage));
        bundleEvent("Language",bundle);
        setUserProperty("UserLanguage", LangMap.get(selectedLanguage));
        setCrashlyticsCustomKey("UserLanguage",  LangMap.get(selectedLanguage));
        Toast.makeText(this, mLangChanged, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        getSession().setLanguageChange(GlobalConstants.LANGUAGE_STATE_CHANGED);
        startActivity(intent);
        finishAffinity();
    }

    private String getTTsLanguage() {
        String language = selectedLanguage;
        if(language.equals("English (India)"))
            return  "English (India)";
        else if(language.equals("हिंदी"))
            return  "Hindi (India)";
        else if(language.equals("বাঙালি"))
            return "Bengali (India)";
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
                    case "Spanish (Spain)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_span_span);
                        break;
                    case "தமிழ்":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_tamil_in);
                        break;
                    case "Deutsch (Deutschland)":
                        shortenLanguageNames[i] = getString(R.string.acc_lang_german_ger);
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
                    case "Spanish (Spain)":
                        shortenLanguageNames[i] = "Spanish (ES)";
                        break;
                    case "Tamil (India)":
                        shortenLanguageNames[i] = "Tamil (IN)";
                        break;
                    case "Deutsch (Deutschland)":
                        shortenLanguageNames[i] = "Deutsch (DE)";
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
}
