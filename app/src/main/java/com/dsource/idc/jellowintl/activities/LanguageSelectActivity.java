package com.dsource.idc.jellowintl.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.package_updater_module.UpdatePackageCheckUtils;
import com.dsource.idc.jellowintl.utility.GlideApp;

import java.util.ArrayList;
import java.util.Arrays;

import static com.dsource.idc.jellowintl.activities.LanguageDownloadActivity.CLOSE;
import static com.dsource.idc.jellowintl.activities.UserRegistrationActivity.LCODE;
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
    // Variable hold strings from regional string.xml file.
    private String mStep2, mStep3;
    String[] langList = new String[LangValueMap.size()];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);
        enableNavigationBack();
        setupActionBarTitle(View.VISIBLE, getString(R.string.home)+"/ "+getString(R.string.Language));
        setNavigationUiConditionally();
        LanguageFactory.deleteOldLanguagePackagesInBackground(this);
        new UpdatePackageCheckUtils().checkLanguagePackageUpdateAvailable(this);
        mStep2 = getString(R.string.change_language_tts_wifi);
        mStep3 = getString(R.string.change_language_line5);
        mLangChanged = getString(R.string.languageChanged);

        languageSelect = findViewById(R.id.btn_lang_select);
        {
            ArrayList<String> rawLangList = new ArrayList<>();
            rawLangList.addAll(Arrays.asList(LanguageFactory.getAvailableLanguages()));
            rawLangList.remove(LangValueMap.get(getSession().getLanguage()));
            rawLangList.add(0, LangValueMap.get(getSession().getLanguage()));
            for (int i = 0; i < LangValueMap.size(); i++) {
                langList[i] = rawLangList.get(i);
            }
        }
        languageSelect.setText(langList[0]);
        selectedLanguage = langList[0];

        setImageUsingGlide(R.drawable.tts_wifi_1, ((ImageView) findViewById(R.id.ivAddLang1)));
        setImageUsingGlide(R.drawable.tts_wifi_2, ((ImageView) findViewById(R.id.ivAddLang2)));
        setImageUsingGlide(R.drawable.tts_wifi_3, ((ImageView) findViewById(R.id.ivAddLang3)));
        setImageUsingGlide(R.drawable.gtts3, ((ImageView) findViewById(R.id.ivTtsVoiceDat)));
        setImageUsingGlide(R.drawable.arrow, ((ImageView) findViewById(R.id.ivArrow1)));
        setImageUsingGlide(R.drawable.arrow, ((ImageView) findViewById(R.id.ivArrow2)));

        save = findViewById(R.id.saveBut);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        updateViewsForNewLangSelect();
    }

    private void updateViewsForNewLangSelect() {
        if (selectedLanguage.equals(LangValueMap.get(MR_IN))) {
            findViewById(R.id.ll_hidden_view).setVisibility(View.GONE);
            findViewById(R.id.tv_language_not_working_info).setVisibility(View.GONE);
            return;
        }

        findViewById(R.id.ll_hidden_view).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_language_not_working_info).setVisibility(View.VISIBLE);

        /*step 2*/
        SpannableString spannedStr = new SpannableString(mStep2);
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
    }

    private void setImageUsingGlide(int image, ImageView imgView) {
        GlideApp.with(this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .dontAnimate()
                .into(imgView);
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
            resetAnalytics(this, getSession().getUserId());
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
        CacheManager.clearCache();
        TextFactory.clearJson();
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
        builder.setSingleChoiceItems(langList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedLanguage = langList[which];
                languageSelect.setText(selectedLanguage);
                updateViewsForNewLangSelect();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
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
}