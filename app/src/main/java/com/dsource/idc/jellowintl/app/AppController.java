package com.dsource.idc.jellowintl.app;

import android.app.Application;
import android.content.Context;

import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import androidx.multidex.MultiDex;

/**.
 * Created by ekalpa on 8/19/2016.
 */
public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try{
            FirebaseApp.initializeApp(getApplicationContext());
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.onAttach(base));
        MultiDex.install(this);
    }
}