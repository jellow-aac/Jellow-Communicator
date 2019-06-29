package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.os.AsyncTask;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CreateDatabaseTest {

    Context mContext;
    SessionManager sessionManager;



    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sessionManager = new SessionManager(mContext);
    }

    @Test
    public void setLanguageChange0(){
        sessionManager.setLanguageChange(0);
        CreateDataBase createDataBase = new CreateDataBase(mContext);
        try{
            createDataBase.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        assert createDataBase.getStatus() == AsyncTask.Status.FINISHED;
    }

    @Test
    public void setLanguageChange1(){
        sessionManager.setLanguageChange(1);
        CreateDataBase createDataBase = new CreateDataBase(mContext);
        try{
            createDataBase.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        assert createDataBase.getStatus() == AsyncTask.Status.FINISHED;
    }

    @Test
    public void setLanguageChange2(){
        sessionManager.setLanguageChange(2);
        CreateDataBase createDataBase = new CreateDataBase(mContext);
        try{
            createDataBase.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        assert createDataBase.getStatus() == AsyncTask.Status.FINISHED;
    }

}
