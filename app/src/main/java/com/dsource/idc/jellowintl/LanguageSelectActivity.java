package com.dsource.idc.jellowintl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import com.dsource.idc.jellowintl.Utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.Utility.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dsource.idc.jellowintl.UserRegistrationActivity.LCODE;
import static com.dsource.idc.jellowintl.Utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.Utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.Utility.SessionManager.LangMap;
import static com.dsource.idc.jellowintl.Utility.SessionManager.LangValueMap;

public class LanguageSelectActivity extends AppCompatActivity{

    public static final String FINISH = "finish";
    SessionManager mSession;
    String[] offlineLanguages;
    String[] onlineLanguages;
    Spinner languageSelect;
    String selectedLanguage, systemTtsLang;
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
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        mSession = new SessionManager(this);
        if(Build.VERSION.SDK_INT >= 21){
            findViewById(R.id.llFollwStep).setVisibility(View.GONE);
            findViewById(R.id.llStep2).setVisibility(View.GONE);
            findViewById(R.id.llImg).setVisibility(View.GONE);
            findViewById(R.id.changeTtsLangBut).setVisibility(View.GONE);
            findViewById(R.id.llStep3).setVisibility(View.GONE);
            ((TextView)findViewById(R.id.tv2)).setText(getString(R.string.change_language_line2).substring(7));
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        registerReceiver(receiver, filter);

        getSpeechLanguage("");
        offlineLanguages = getOfflineLanguages();
        onlineLanguages = getOnlineLanguages();
        languageSelect = findViewById(R.id.selectDownloadedLanguageSpinner);
        adapter_lan = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, offlineLanguages);

        adapter_lan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        languageSelect.setAdapter(adapter_lan);
        languageSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLanguage = offlineLanguages[i];
                mSelectedItem = i;
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
                if(selectedLanguage != null)
                {
                    if(Build.VERSION.SDK_INT >= 21 && mSession.getLanguage().equals(LangMap.get(selectedLanguage))) {
                        Toast.makeText(LanguageSelectActivity.this, getString(R.string.txt_save_same_lang_def), Toast.LENGTH_SHORT).show();
                        return;
                    }else if(Build.VERSION.SDK_INT >= 21 || shouldSaveLang) {
                        saveLanguage();
                        mSession.setLangSettingIsCorrect(true);
                        return;
                    }
                    else if (mSession.getLanguage().equals(LangMap.get(selectedLanguage)) && !shouldSaveLang){
                        Toast.makeText(LanguageSelectActivity.this, getString(R.string.txt_save_same_lang_def), Toast.LENGTH_SHORT).show();
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
                try {
                    delete.setEnabled(false);
                     new MaterialDialog.Builder(LanguageSelectActivity.this)
                            .title("Downloadable Languages")
                            .items(onlineLanguages)
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
                                                    Toast.makeText(LanguageSelectActivity.this,getString(R.string.languageLimitExceeded),Toast.LENGTH_SHORT).show();
                                                }

                                            }else {

                                                Toast.makeText(LanguageSelectActivity.this,getString(R.string.checkConnectivity),Toast.LENGTH_SHORT).show();
                                            }

                                            return true;
                                        }
                                    })
                            .positiveText("Download")
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    add.setEnabled(false);
                    new MaterialDialog.Builder(LanguageSelectActivity.this)
                            .title("Downloaded Languages")
                            .items(offlineLanguages)
                            .itemsCallbackSingleChoice(
                                    0, new MaterialDialog.ListCallbackSingleChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                            add.setEnabled(true);
                                            String locale = LangMap.get(offlineLanguages[which]);
                                            if(mSession.getLanguage().equals(locale))
                                            {
                                                Toast.makeText(getBaseContext(),getString(R.string.languageCurrentlyInUse),Toast.LENGTH_SHORT).show();
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
                                                    android.R.layout.simple_spinner_item, offlineLanguages);
                                            languageSelect.setAdapter(adapter_lan);
                                            dialog.dismiss();
                                            Toast.makeText(getBaseContext(),getString(R.string.languageRemoved),Toast.LENGTH_SHORT).show();
                                            return true;
                                        }
                                    })
                            .positiveText("Remove")
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
        stopMeasuring("ChangeLanguageActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMeasuring();
        new ChangeAppLocale(this).setLocale();
        onlineLanguages = getOnlineLanguages();
        offlineLanguages = getOfflineLanguages();
        adapter_lan = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, offlineLanguages);
        languageSelect.setAdapter(adapter_lan);
        if(isOpenedTtsSett) getSpeechLanguage("");
        if(mSelectedItem != -1)
            languageSelect.setSelection(mSelectedItem);
        //isOpenedTtsSett = false;
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

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES":
                    isTtsLangChanged = systemTtsLang != null && !systemTtsLang.equals(intent.getStringExtra("systemTtsRegion"));

                    systemTtsLang = intent.getStringExtra("systemTtsRegion");
                    if(intent.getBooleanExtra("saveUserLanguage",false)/* || shouldSaveLang*/){
                        saveLanguage();
                    }else if(intent.getBooleanExtra("showError",false))
                        Toast.makeText(context, getString(R.string.set_engine_language), Toast.LENGTH_LONG).show();

                    if(isOpenedTtsSett && isTtsLangChanged && !mSession.getLangSettingIsCorrect())
                        if((mSession.getLanguage().equals("en-rIN") && systemTtsLang.equals("hi-rIN")) ||
                                (!mSession.getLanguage().equals("en-rIN") && mSession.getLanguage().equals(systemTtsLang)))
                            shouldSaveLang = true;
                    isOpenedTtsSett = isTtsLangChanged = false;
                    break;
            }
        }
    };

    private void saveLanguage() {
        mSession.setLanguage(LangMap.get(selectedLanguage));
        ChangeAppLocale changeAppLocale = new ChangeAppLocale(getBaseContext());
        changeAppLocale.setLocale();
        Toast.makeText(LanguageSelectActivity.this, getString(R.string.languageChanged), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
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
}