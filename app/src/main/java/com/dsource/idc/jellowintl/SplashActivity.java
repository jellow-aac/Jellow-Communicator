package com.dsource.idc.jellowintl;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.utility.CreateDataBase;
import com.dsource.idc.jellowintl.utility.DataBaseHelper;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.utility.SpeechUtils;
import com.dsource.idc.jellowintl.utility.TextToSpeechErrorUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;

/**
 * Created by ekalpa on 7/12/2016.
 */
public class SplashActivity extends AppCompatActivity {
    //Field to create IconDatabase
    CreateDataBase iconDatabase;
    SessionManager mSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mSession = new SessionManager(this);
        updateLangPackagesIfUpdateAvail();
        new DataBaseHelper(this).createDataBase();

        if(isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)))
            stopTTsService();
        startTTsService();
        PlayGifView pGif = findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);

        if (mSession.isRequiredToPerformDbOperations()) {
            performDatabaseOperations();
            mSession.setCompletedDbOperations(true);
        }
        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
            (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED))
            mSession.setEnableCalling(false);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.INIT_SERVICE");
        filter.addAction("com.dsource.idc.jellowintl.INIT_SERVICE_ERR");
        if (mSession.isLanguageChanged() == 1) {
            CacheManager.clearCache();
            TextFactory.clearJson();
            SpeechUtils.updateSpeechParam(this);
        }
        registerReceiver(receiver, filter);
        iconDatabase=new CreateDataBase(this);
        iconDatabase.execute();
     }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            resetAnalytics(this, mSession.getCaregiverNumber().substring(1));
        }
        setUserParameters();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellowintl.INIT_SERVICE":
                    checkIfDatabaseCreated();
                    break;
                case "com.dsource.idc.jellowintl.INIT_SERVICE_ERR":
                    new TextToSpeechErrorUtils(SplashActivity.this)
                            .showErrorDialog();
                    break;
            }
        }
    };

    private void setUserParameters() {
        final int GRID_3BY3 = 1, PICTURE_TEXT = 0;
        if(mSession.isGridSizeKeyExist()) {
            if(mSession.getGridSize() == GRID_3BY3){
                setUserProperty("GridSize", "9");
                setCrashlyticsCustomKey("GridSize", "9");
            }else{
                setUserProperty("GridSize", "3");
                setCrashlyticsCustomKey("GridSize", "3");
            }
        }else{
            setUserProperty("GridSize", "9");
            setCrashlyticsCustomKey("GridSize", "9");
        }

        if(mSession.getPictureViewMode() == PICTURE_TEXT) {
            setUserProperty("PictureViewMode", "PictureText");
            setCrashlyticsCustomKey("PictureViewMode", "PictureText");
        }else{
            setUserProperty("PictureViewMode", "PictureOnly");
            setCrashlyticsCustomKey("PictureViewMode", "PictureOnly");
        }
    }

    private void checkIfDatabaseCreated()
    {
        if(!(mSession.isLanguageChanged()==2))
            startApp();//if changes are their in the app then check whether the data base is created or not
        else
            startJellow();//If no change in language then simply start the app.
    }

    private void startApp() {
        final Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(iconDatabase.getStatus()== AsyncTask.Status.FINISHED)
                {
                    startJellow();
                    timer.cancel();
                }
            }
        },0,100);
    }

    private void startJellow() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        try{
            unregisterReceiver(receiver);
        } catch(IllegalArgumentException | NullPointerException | IllegalStateException e) {
            e.printStackTrace();
        }
        finishAffinity();
    }

    private void performDatabaseOperations() {
        DataBaseHelper helper = new DataBaseHelper(this);
        helper.openDataBase();
        helper.addLanguageDataToDatabase();
    }

    private void startTTsService() {
        startService(new Intent(getApplication(), JellowTTSService.class));
    }

    private void stopTTsService() {
        Intent intent = new Intent("com.dsource.idc.jellowintl.STOP_SERVICE");
        sendBroadcast(intent);
    }

    private void updateLangPackagesIfUpdateAvail() {
        //This function will check if any language package is updated at Firebase. Then
        // user required to download that package.
        final FirebaseRemoteConfig frc = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        frc.setConfigSettings(configSettings);
        frc.setDefaults(R.xml.remote_config_default);
        frc.fetch(1)
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // After config data is successfully fetched, it must be activated before
                        // newly fetched values are returned.
                        frc.activateFetched();
                        String updateLangPackageData = frc.getString("vcTwentyUpdateLanguagePackages");
                        if(updateLangPackageData.isEmpty())
                            return;
                        StringBuilder lang = new StringBuilder();
                        //1)Get local packages list.
                        //2)Compare which package from local list has updates.
                        //3)Add language name to update list.
                        try {
                            JSONObject jObj = new JSONObject(updateLangPackageData);
                            for (String langName: getOfflineLanguages()) {
                                try {
                                    JSONArray jArray = jObj.getJSONArray(langName);
                                    String path = getBaseContext().getDir(langName, Context.MODE_PRIVATE).getPath();
                                    for (int i = 0; i < jArray.length(); i++) {
                                        if (!(new File(path + jArray.get(i))).exists()) {
                                            lang.append(langName+",");
                                            break;
                                        }
                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (lang.toString().isEmpty())
                            return;
                        startActivity(new Intent(SplashActivity.this,
                                LanguagePackageUpdateActivity.class).putExtra("packageList", lang.toString()));
                        finish();
                    } else {
                        Crashlytics.log("RemoteConfigFetchFailed");
                    }
                }
            });
    }

    private String[] getOfflineLanguages(){
        List<String> lang = new ArrayList<>();
        if(mSession.isDownloaded(ENG_IN))
            lang.add(ENG_IN);
        if(mSession.isDownloaded(ENG_US))
            lang.add(ENG_US);
        if(mSession.isDownloaded(ENG_AU))
            lang.add(ENG_AU);
        if(mSession.isDownloaded(ENG_UK))
            lang.add(ENG_UK);
        if(mSession.isDownloaded(HI_IN))
            lang.add(HI_IN);
        return lang.toArray(new String[lang.size()]);
    }
}