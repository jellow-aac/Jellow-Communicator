package com.dsource.idc.jellowintl;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListViewCompat;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.models.GlobalConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangValueMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.TA_IN;

public class LanguageSelectActivity extends SpeechEngineBaseActivity {
    String selectedLanguage, mLangChanged;
    Button save, languageSelect;
    CustomArrayAdapter adapter_lan;
    // Variable hold strings from regional string.xml file.
    private String mStep1, mStep2, mRawStrStep2, mStep3, mStep4;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);
        enableNavigationBack();
        setActivityTitle(getString(R.string.Language));
        LanguageFactory.deleteOldLanguagePackagesInBackground(this);
        mStep1 = getString(R.string.change_language_line2);
        mStep2 = getString(R.string.change_language_tts_wifi);
        mRawStrStep2 = getString(R.string.txtStep2);
        mStep3 = getString(R.string.change_language_line5);
        mStep4 = getString(R.string.txtApplyChanges);
        mLangChanged = getString(R.string.languageChanged);

        languageSelect = findViewById(R.id.btn_lang_select);
        ArrayList<String> langList = new ArrayList<>();
        langList.addAll(Arrays.asList(LanguageFactory.getAvailableLanguages()));
        langList.remove(LangValueMap.get(getSession().getLanguage()));
        langList.add(0, LangValueMap.get(getSession().getLanguage()));
        adapter_lan = new CustomArrayAdapter(this, R.layout.simple_spinner_dropdown_item,
                /*populateCountryNameByUserType(*/langList/*)*/);
        languageSelect.setText(langList.get(0));
        selectedLanguage = langList.get(0);

        setImageUsingGlide(R.drawable.tts_wifi_1, ((ImageView) findViewById(R.id.ivAddLang1)));
        setImageUsingGlide(R.drawable.tts_wifi_2, ((ImageView) findViewById(R.id.ivAddLang2)));
        setImageUsingGlide(R.drawable.tts_wifi_3, ((ImageView) findViewById(R.id.ivAddLang3)));
        setImageUsingGlide(R.drawable.gtts3, ((ImageView) findViewById(R.id.ivTtsVoiceDat)));
        setImageUsingGlide(R.drawable.arrow, ((ImageView) findViewById(R.id.ivArrow1)));
        setImageUsingGlide(R.drawable.arrow, ((ImageView) findViewById(R.id.ivArrow2)));

        save = findViewById(R.id.saveBut);
        /* **
         * The variables below are defined because android os fall back to default locale
         * after activity restart. These variable will hold the value for variables initialized using
         * user preferred locale.
         ***/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVoiceAvailableForLanguage(LangMap.get(selectedLanguage));
                Crashlytics.log("LanguageSelect Apply");
                if(selectedLanguage.equals(LangValueMap.get(MR_IN)) && !LanguageFactory.isMarathiPackageAvailable
                        (LanguageSelectActivity.this)){
                    startActivity(new Intent(LanguageSelectActivity.this,
                            LanguageDownloadActivity.class)
                            .putExtra(LCODE, MR_IN).putExtra(CLOSE, true));
                } else {
                    saveLanguage();
                }
            }
        });
    }

    private void hideViewsForNonTtsLang(boolean disableViews) {
        //Hide views to when user selected non tts language.
        if(disableViews) {
            findViewById(R.id.tv_step2_info).setVisibility(View.GONE);
            findViewById(R.id.ll_wifi_only_setting).setVisibility(View.GONE);
            findViewById(R.id.btnTTsSetting).setVisibility(View.GONE);
            findViewById(R.id.tv_step3_info).setVisibility(View.GONE);
            findViewById(R.id.ivTtsVoiceDat).setVisibility(View.GONE);
            findViewById(R.id.btnDownloadVoiceData).setVisibility(View.GONE);
        }else{
            findViewById(R.id.tv_step2_info).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_wifi_only_setting).setVisibility(View.VISIBLE);
            findViewById(R.id.btnTTsSetting).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_step3_info).setVisibility(View.VISIBLE);
            findViewById(R.id.ivTtsVoiceDat).setVisibility(View.VISIBLE);
            findViewById(R.id.btnDownloadVoiceData).setVisibility(View.VISIBLE);
        }
    }

    private void updateViewsForNewLangSelect() {
        /* step 1*/
        SpannableString spannedStr = new SpannableString(mStep1);
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0, getDelimitedStringLength(mStep1),0);
        spannedStr.setSpan(new UnderlineSpan(), 0, getDelimitedStringLength(mStep1), 0);
        ((TextView)findViewById(R.id.tv_step1_info)).setText(spannedStr);

        /*step 2*/
        spannedStr = new SpannableString(mStep2);
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0, getDelimitedStringLength(mStep2),0);
        spannedStr.setSpan(new UnderlineSpan(), 0, getDelimitedStringLength(mStep2), 0);
        ((TextView)findViewById(R.id.tv_step2_info)).setText(spannedStr);

        /*step 3*/
        spannedStr = new SpannableString(mStep3.replace("_", getTTsLanguage()));
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD),0, getDelimitedStringLength(mStep3),0);
        spannedStr.setSpan(new UnderlineSpan(), 0, getDelimitedStringLength(mStep3), 0);
        int start = spannedStr.toString().indexOf(getTTsLanguage()),
            end = start + getTTsLanguage().length();
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD), start, end,0);
        ((TextView)findViewById(R.id.tv_step3_info)).setText(spannedStr);

        /*step 4*/
        spannedStr = new SpannableString(mStep4);
        spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, getDelimitedStringLength(mStep4), 0);
        spannedStr.setSpan(new UnderlineSpan(), 0, getDelimitedStringLength(mStep4), 0);
        ((TextView) findViewById(R.id.tv_step4_info)).setText(spannedStr);
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
        getSession().setLanguageChange(GlobalConstants.LANGUAGE_STATE_CHANGED);
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        finishAffinity();
    }

    private String getTTsLanguage() {
        String language = selectedLanguage;
        if(language.equals(LangValueMap.get(HI_IN)))
            return  "Hindi (India)";
        else if(language.equals(LangValueMap.get(BN_IN)))
            return "Bengali (India)";
        else if(language.equals(LangValueMap.get(TA_IN)))
            return "English (India)";
        return selectedLanguage;
    }

    private int getDelimitedStringLength(String text){
        return getSession().getLanguage().equals(BN_IN) ?
                text.indexOf("ঃ")+1 : text.indexOf(":")+1;
    }

    public void showAvailableLanguageDialog(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.language_select_list_parent, null);
        ((ListViewCompat)dialogView.findViewById(R.id.list_language_list)).setAdapter(adapter_lan);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private class CustomArrayAdapter extends ArrayAdapter{
        Context context;
        int layout;
        List arrayList;

        public CustomArrayAdapter(Context context, int layout, List<String> arrayList) {
            super(context, layout, arrayList);
            this.context = context;
            this.layout = layout;
            this.arrayList = arrayList;
        }

        @Override
        public View getDropDownView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return vi.inflate(R.layout.simple_spinner_dropdown_item, null);
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.simple_spinner_dropdown_item, null);
            final RadioButton rb = convertView.findViewById(R.id.rb_select_lang);
            ((TextView)convertView.findViewById(R.id.tv_lang_name)).setText(arrayList.get(position).toString());
            convertView.findViewById(R.id.ll_parent_language_list_item).
                    setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rb.setChecked(true);
                            performLanguageSelection(arrayList.get(position).toString());
                        }
                    });
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    performLanguageSelection(arrayList.get(position).toString());
                }
            });
            return convertView;
        }
    }

    private void performLanguageSelection(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
        if (selectedLanguage.equals(LangValueMap.get(MR_IN))) {
            hideViewsForNonTtsLang(true);
            String stepStr = mRawStrStep2 +" "+ mStep4.substring(getDelimitedStringLength(mStep4));
            SpannableString spannedStr = new SpannableString(stepStr);
            spannedStr.setSpan(new StyleSpan(Typeface.BOLD), 0, getDelimitedStringLength(stepStr), 0);
            spannedStr.setSpan(new UnderlineSpan(), 0, getDelimitedStringLength(stepStr), 0);
            ((TextView) findViewById(R.id.tv_step4_info)).setText(spannedStr);
        } else {
            hideViewsForNonTtsLang(false);
            updateViewsForNewLangSelect();
        }
        languageSelect.setText(selectedLanguage);
        dialog.dismiss();
    }

    private List<String> populateCountryNameByUserType(ArrayList<String> langNameToBeShorten) {
        String[] shortenLanguageNames = new String[langNameToBeShorten.size()];
        if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            for (int i = 0; i < langNameToBeShorten.size(); i++) {
                switch (langNameToBeShorten.get(i)) {
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
                        shortenLanguageNames[i] = langNameToBeShorten.get(i);
                        break;
                }
            }
        }else {
            for (int i = 0; i < langNameToBeShorten.size(); i++) {
                switch (langNameToBeShorten.get(i)) {
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
                        shortenLanguageNames[i] = langNameToBeShorten.get(i);
                        break;
                }
            }
        }
        return Arrays.asList(shortenLanguageNames);
    }
}