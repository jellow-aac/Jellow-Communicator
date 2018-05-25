package com.dsource.idc.jellowintl.app;

import android.app.Application;
import android.content.Context;

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
}