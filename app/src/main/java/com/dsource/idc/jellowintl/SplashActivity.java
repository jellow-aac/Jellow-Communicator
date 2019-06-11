package com.dsource.idc.jellowintl;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.utility.CreateDataBase;
import com.dsource.idc.jellowintl.utility.DataBaseHelper;
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

import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.setCrashlyticsCustomKey;
import static com.dsource.idc.jellowintl.utility.Analytics.setUserProperty;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.DE_DE;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.ES_ES;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.TA_IN;

/**
 * Created by ekalpa on 7/12/2016.
 */
public class SplashActivity extends BaseActivity {
    //Field to create IconDatabase
    CreateDataBase iconDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        updateLangPackagesIfUpdateAvail();
        new DataBaseHelper(this).createDataBase();

        PlayGifView pGif = findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);

        if (getSession().isRequiredToPerformDbOperations()) {
            performDatabaseOperations();
            getSession().setCompletedDbOperations(true);
        }
        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
            (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED))
            getSession().setEnableCalling(false);

        if (getSession().isLanguageChanged() == 1) {
            CacheManager.clearCache();
            TextFactory.clearJson();
        }

        if(getSession().isLanguageChanged()!=2) {
            iconDatabase = new CreateDataBase(this, getAppDatabase());
            iconDatabase.execute();
        }else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finishAffinity();
        }
     }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
        setUserParameters();
    }

    private void setUserParameters() {
        final int GRID_3BY3 = 1, PICTURE_TEXT = 0;
        if(getSession().isGridSizeKeyExist()) {
            if(getSession().getGridSize() == GRID_3BY3){
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

        if(getSession().getPictureViewMode() == PICTURE_TEXT) {
            setUserProperty("PictureViewMode", "PictureText");
            setCrashlyticsCustomKey("PictureViewMode", "PictureText");
        }else{
            setUserProperty("PictureViewMode", "PictureOnly");
            setCrashlyticsCustomKey("PictureViewMode", "PictureOnly");
        }
    }

    private void performDatabaseOperations() {
        DataBaseHelper helper = new DataBaseHelper(this);
        helper.openDataBase();
        helper.addLanguageDataToDatabase();
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
        if(getSession().isDownloaded(ENG_IN))
            lang.add(ENG_IN);
        if(getSession().isDownloaded(ENG_US))
            lang.add(ENG_US);
        if(getSession().isDownloaded(ENG_AU))
            lang.add(ENG_AU);
        if(getSession().isDownloaded(ENG_UK))
            lang.add(ENG_UK);
        if(getSession().isDownloaded(HI_IN))
            lang.add(HI_IN);
        if(getSession().isDownloaded(MR_IN))
            lang.add(MR_IN);
        if(getSession().isDownloaded(BN_IN))
            lang.add(BN_IN);
        if(getSession().isDownloaded(ES_ES))
            lang.add(ES_ES);
        if(getSession().isDownloaded(TA_IN))
            lang.add(TA_IN);
        if(getSession().isDownloaded(DE_DE))
            lang.add(DE_DE);

        return lang.toArray(new String[lang.size()]);
    }
}
