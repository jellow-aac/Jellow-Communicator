package com.dsource.idc.jellowintl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    boolean isOpenedTtsSett = false;
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
                    if (mSession.getLanguage().equals(LangMap.get(selectedLanguage))) return;
                    getSpeechLanguage(LangMap.get(selectedLanguage));
                    /*if(mSession.getLanguage().equals(LangMap.get(selectedLanguage))) return;
                    mSession.setLanguage(LangMap.get(selectedLanguage));
                    ChangeAppLocale changeAppLocale = new ChangeAppLocale(getBaseContext());
                    changeAppLocale.setLocale();
                    Toast.makeText(LanguageSelectActivity.this,getString(R.string.languageChanged),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finishAffinity();*/

                } else
                    Toast.makeText(getBaseContext(),"Please Select a Language",Toast.LENGTH_SHORT).show();
            }
        });

        add = findViewById(R.id.addBut);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new MaterialDialog.Builder(LanguageSelectActivity.this)
                            .title("Downloadable Languages")
                            .items(onlineLanguages)
                            .itemsCallbackSingleChoice(
                                    0, new MaterialDialog.ListCallbackSingleChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
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
                    new MaterialDialog.Builder(LanguageSelectActivity.this)
                            .title("Downloaded Languages")
                            .items(offlineLanguages)
                            .itemsCallbackSingleChoice(
                                    0, new MaterialDialog.ListCallbackSingleChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
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
                    systemTtsLang = intent.getStringExtra("systemTtsRegion");
                    if(intent.getBooleanExtra("saveUserLanguage",false)){
                        mSession.setLanguage(LangMap.get(selectedLanguage));
                        ChangeAppLocale changeAppLocale = new ChangeAppLocale(getBaseContext());
                        changeAppLocale.setLocale();
                        Toast.makeText(LanguageSelectActivity.this, getString(R.string.languageChanged), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                        finishAffinity();
                    }else if(intent.getBooleanExtra("showError",false))
                        Toast.makeText(context, getString(R.string.set_engine_language), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


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

    private void getSpeechLanguage(String saveLanguage){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_REQ");
        intent.putExtra("saveSelectedLanguage", saveLanguage);
        sendBroadcast(intent);
    }
}
