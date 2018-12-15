package com.dsource.idc.jellowboard.app;

import android.app.Application;
import android.content.Context;

import com.dsource.idc.jellowboard.utility.LanguageHelper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

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
    }
}