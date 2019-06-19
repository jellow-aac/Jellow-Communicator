package com.dsource.idc.jellowintl.utility;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class AnalyticsTest {
    private Context mContext;

    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(mContext);
        Analytics.getAnalytics(mContext, "9653238072");
    }

    @Test
    public void analyticsTest(){
        assert Analytics.isAnalyticsActive();
    }

    @Test
    public void validatePushIdTest(){
        //Check if current timestamp is expired then returning new timestamp
        long timeStamp = new Date().getTime() - 86400000L;
        assert timeStamp != Analytics.validatePushId(timeStamp);
        //Check if current timestamp is not expired then existing timestamp
        timeStamp = new Date().getTime();
        assert timeStamp == Analytics.validatePushId(timeStamp);
    }

    @Test
    public void maskNumberTest(){
       String contact = Analytics.maskNumber("9653238072");
        assert contact.equals("107730134470");
    }

    @Test
    public void updateSessionRefTest(){
        String oldRef = Analytics.getSessionRef();
        Analytics.updateSessionRef("9653238071");
        assert !oldRef.equals(Analytics.getSessionRef());
    }
}
