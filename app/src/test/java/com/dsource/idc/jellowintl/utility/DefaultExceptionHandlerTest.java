package com.dsource.idc.jellowintl.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class DefaultExceptionHandlerTest {

    @Mock
    Activity activity;

    @Mock
    Thread thread;

    @Mock
    Throwable throwable;

    @Test
    public void legacyTests(){
        DefaultExceptionHandler exceptionHandler = new DefaultExceptionHandler(activity);
        try{
            exceptionHandler.uncaughtException(thread,throwable);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
