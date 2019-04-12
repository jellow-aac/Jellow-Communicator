package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
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
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

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

public class LanguageSelectActivity extends SpeechEngineBaseActivity {
    public static final String FINISH = "finish";
    String[] offlineLanguages;
    String[] onlineLanguages;
    Spinner languageSelect;
    String selectedLanguage, mLangChanged;
    Button save,add,delete, changeTtsLang;
    ArrayAdapter<String> adapter_lan;
    private int mSelectedItem = -1;
    // Variable hold strings from regional string.xml file.
    private String mStep1, mStep2, mStep3, mStep4, mTitleEditLang,
            mTitleChgLang, mRawStr4Step3, mCompleteStep2, mCompleteStep3, mRawStrStep2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);
        enableNavigationBack();
        setActivityTitle(getString(R.string.Language));

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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                /* If the current app language is same as new selected language then
                 *   Show Toast message string 'strDefaultLangEr'*/
                if(getSession().getLanguage().equals(LangMap.get(selectedLanguage))){
                    Toast.makeText(LanguageSelectActivity.this,
                            strDefaultLangEr, Toast.LENGTH_SHORT).show();
                    return;
                }

                //if current user language is not marathi and user want to change current language
                // to marathi.
                if (!getSession().getLanguage().equals(MR_IN)
                    && selectedLanguage != null &&
                        selectedLanguage.equals(LangValueMap.get(MR_IN))){
                    saveLanguage();
                }else if(selectedLanguage != null) {
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
                     *  either BN_IN or BE_IN form THEN
                     *  save the language and set language setting to true.
                     *  else
                     *  show toast about Complete step 3.*/
                    } else if (getSpeechEngineLanguage().equals(LangMap.get(selectedLanguage)) ||
                        (LangMap.get(selectedLanguage).equals(BN_IN) &&
                            (getSpeechEngineLanguage().equals(BN_IN) ||
                                getSpeechEngineLanguage().equals(BE_IN)))) {
                        saveLanguage();
                    } else {
                        Toast.makeText(LanguageSelectActivity.this,
                                mCompleteStep3, Toast.LENGTH_SHORT).show();
                    }
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
                if(!getSession().isWifiOnlyBtnPressedOnce()){
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
                    final int[] item = {0};
                    SpannableString spannedDlStr = new SpannableString(strDownloadableLang);
                    spannedDlStr.setSpan(new StyleSpan(Typeface.BOLD),0, spannedDlStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString spannedCnlStr = new SpannableString(strCancel);
                    spannedCnlStr.setSpan(new StyleSpan(Typeface.BOLD),0, spannedCnlStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LanguageSelectActivity.this);
                    AlertDialog addLangDialog = builder
                    .setTitle(spannedDlStr)
                    .setSingleChoiceItems(populateCountryNameByUserType(onlineLanguages), 0, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            item[0] = which;
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            delete.setEnabled(true);
                        }
                    })
                    .setPositiveButton(strDownload, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete.setEnabled(true);
                            showAddLangDialog(dialog, item[0], strCheckConnectivity);
                        }
                    })
                    .setNegativeButton(spannedCnlStr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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

                    final int[] item = {0};
                    SpannableString spannedDlStr = new SpannableString(strDownloadedLang);
                    spannedDlStr.setSpan(new StyleSpan(Typeface.BOLD),0, spannedDlStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    SpannableString spannedCnlStr = new SpannableString(strCancel);
                    spannedCnlStr.setSpan(new StyleSpan(Typeface.BOLD),0, spannedCnlStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LanguageSelectActivity.this);
                    AlertDialog delLangDialog = builder
                            .setTitle(spannedDlStr)
                            .setSingleChoiceItems(populateCountryNameByUserType(removeCurrentLangFromList(offlineLanguages))
                                    , 0, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            item[0] = which;
                                        }
                                    })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    add.setEnabled(true);
                                }
                            })
                            .setPositiveButton(strRemove, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    add.setEnabled(true);
                                    String locale = LangMap.get(offlineLanguages[item[0]]);
                                    if (getSession().getLanguage().equals(locale)) {
                                        Toast.makeText(LanguageSelectActivity.this, strLangCurrentlyInUse, Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        return ;
                                    }
                                    showDeleteLangDialog(dialog, item[0], strLangRemoved);
                                }
                            })
                            .setNegativeButton(spannedCnlStr,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
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
        mLangChanged = getString(R.string.languageChanged);
    }

    private void customizeLanguageDialog(AlertDialog dialog) {
        Button btnPos = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNeg = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Typeface tf = ResourcesCompat.getFont(LanguageSelectActivity.this, R.font.mukta_semibold);
        btnPos.setTypeface(tf);
        btnNeg.setTypeface(tf);
        btnPos.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        btnNeg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        //TODO Add this part of code to mobile only device
        /*Rect displayRectangle = new Rect();
        Window window = dialog.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);   */
    }

    private void showAddLangDialog(DialogInterface dialog, int which, String strCheckConnectivity) {
        delete.setEnabled(true);

        if (isConnectedToNetwork((ConnectivityManager) LanguageSelectActivity.this.
                getSystemService(Context.CONNECTIVITY_SERVICE))) {
            Bundle bundle = new Bundle();
            bundle.putString(LCODE, LangMap.get(onlineLanguages[which]));
            bundle.putBoolean(FINISH, false);
            //To start TTS voice package download automatically.
            setSpeechEngineLanguage(LangMap.get(onlineLanguages[which]));
            //To switch TTS voice package back.
            setSpeechEngineLanguage(getSession().getLanguage());
            // Send empty string to TTS Engine to eliminate voice
            // lag after user goes back without changing the language.
            speak("");
            startActivity(new Intent(getBaseContext(), LanguageDownloadActivity.class).putExtras(bundle));
            dialog.dismiss();
        } else {

            Toast.makeText(LanguageSelectActivity.this, strCheckConnectivity, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteLangDialog(DialogInterface dialog, int which, String strLangRemoved) {
        String locale = LangMap.get(offlineLanguages[which]);
        File file = getBaseContext().getDir(locale, Context.MODE_PRIVATE);
        if (file.exists()) {
            deleteRecursive(file);
        }
        file.delete();
        getSession().setRemoved(locale);
        onlineLanguages = getOnlineLanguages();
        offlineLanguages = getOfflineLanguages();
        adapter_lan = new ArrayAdapter<String>(getBaseContext(),
                R.layout.simple_spinner_item, populateCountryNameByUserType(offlineLanguages));
        adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSelect.setAdapter(adapter_lan);
        dialog.dismiss();
        Toast.makeText(LanguageSelectActivity.this, strLangRemoved, Toast.LENGTH_SHORT).show();
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

    public void openSpeechDataSetting(View view){
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openSpeechSetting(View view){
        startActivity(new Intent().setAction("com.android.settings.TTS_SETTINGS"));
        getSession().setWifiOnlyBtnPressedOnce(true);
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

        String current = getSession().getLanguage();
        if( getSession().isDownloaded(ENG_IN) &&
                !current.equals(ENG_IN))
            lang.add(LangValueMap.get(ENG_IN));

        if( getSession().isDownloaded(SessionManager.ENG_US) &&
                !current.equals(SessionManager.ENG_US))
            lang.add(LangValueMap.get(SessionManager.ENG_US));

        if( getSession().isDownloaded(SessionManager.ENG_UK) &&
                !current.equals(SessionManager.ENG_UK))
            lang.add(LangValueMap.get(SessionManager.ENG_UK));

        if( getSession().isDownloaded(SessionManager.ENG_AU) &&
                !current.equals(SessionManager.ENG_AU))
            lang.add(LangValueMap.get(SessionManager.ENG_AU));

        if( getSession().isDownloaded(HI_IN) &&
                !current.equals(HI_IN))
            lang.add(LangValueMap.get(HI_IN));

        if(( getSession().isDownloaded(SessionManager.BN_IN) &&
                !current.equals(SessionManager.BN_IN)))
            lang.add(LangValueMap.get(SessionManager.BN_IN));

        if( getSession().isDownloaded(SessionManager.MR_IN) &&
                !current.equals(SessionManager.MR_IN))
            lang.add(LangValueMap.get(SessionManager.MR_IN));

        lang.add(LangValueMap.get(current));

        return lang.toArray(new String[lang.size()]);
    }

    private String[] getOnlineLanguages()
    {
        List<String> lang = new ArrayList<>();
        if( !getSession().isDownloaded(ENG_IN))
            lang.add(LangValueMap.get(ENG_IN));
        if( !getSession().isDownloaded(SessionManager.ENG_US))
            lang.add(LangValueMap.get(SessionManager.ENG_US));
        if( !getSession().isDownloaded(SessionManager.ENG_UK))
            lang.add(LangValueMap.get(SessionManager.ENG_UK));
        if( !getSession().isDownloaded(SessionManager.ENG_AU))
            lang.add(LangValueMap.get(SessionManager.ENG_AU));
        if( !getSession().isDownloaded(HI_IN))
            lang.add(LangValueMap.get(HI_IN));
        if( !getSession().isDownloaded(SessionManager.BN_IN))
            lang.add(LangValueMap.get(SessionManager.BN_IN));
        if( !getSession().isDownloaded(SessionManager.MR_IN))
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
        onlineLanguages = getOnlineLanguages();
        offlineLanguages = getOfflineLanguages();
        adapter_lan = new ArrayAdapter<String>(this,
                R.layout.simple_spinner_item, populateCountryNameByUserType(offlineLanguages));
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
        getSession().setLanguage(LangMap.get(selectedLanguage));
        Bundle bundle = new Bundle();
        bundle.putString("LanguageSet", "Switched to "+ LangMap.get(selectedLanguage));
        bundleEvent("Language",bundle);
        setUserProperty("UserLanguage", LangMap.get(selectedLanguage));
        setCrashlyticsCustomKey("UserLanguage",  LangMap.get(selectedLanguage));
        Toast.makeText(this, mLangChanged, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        getSession().setLanguageChange(1);
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
            if (currentList[i].equals(LangValueMap.get(getSession().getLanguage()))) {
            }else
                filteredList[++j] = currentList[i];
        }
        return filteredList;
    }
}