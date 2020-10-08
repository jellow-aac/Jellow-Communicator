package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;

@RunWith(AndroidJUnit4.class)
public class SessionManagerTest {
    private SessionManager mSession;

    @Before
    public void setup(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mSession = new SessionManager(context);
    }

    @Test
    public void checkSetUserLoggedIn(){
        mSession.setUserLoggedIn(true);
        assert mSession.isUserLoggedIn();
    }

    @Test
    public void checkSetBlood(){
        mSession.setBlood(0);
        assert mSession.getBlood() == 0;
    }

    @Test
    public void checkSetName(){
        mSession.setName("Prakash");
        assert mSession.getName().equals("Prakash");
    }

    @Test
    public void checkSetEmailId(){
        mSession.setEmailId("jellowcommunicator@gmail.com");
        assert mSession.getEmailId().equals("jellowcommunicator@gmail.com");
    }

    @Test
    public void checkSetCaregiverNumber(){
        mSession.setCaregiverNumber("9653238072");
        assert mSession.getCaregiverNumber().equals("9653238072");
    }

    @Test
    public void checkSetCaregiverName(){
        mSession.setCaregiverName("Anjali");
        assert mSession.getCaregiverName().equals("Anjali");
    }

    @Test
    public void checkSetAddress(){
        mSession.setName("IIT Bombay");
        assert mSession.getName().equals("IIT Bombay");
    }

    @Test
    public void checkSetLanguage(){
        mSession.setLanguage(ENG_IN);
        assert mSession.getLanguage().equals(ENG_IN);
    }

    @Test
    public void checkSetPictureViewMode(){
        mSession.setPictureViewMode(0);
        assert mSession.getPictureViewMode() == 0;
    }

    @Test
    public void checkSetGridSize(){
        mSession.setGridSize(0);
        assert mSession.getGridSize() == 0;
    }

    @Test
    public void checkSetSpeed(){
        mSession.setSpeed(10);
        assert mSession.getSpeed() == 10;
        mSession.setSpeed(0);
        assert mSession.getSpeed() == 50;
    }

    @Test
    public void checkSetPitch(){
        mSession.setPitch(20);
        assert mSession.getPitch() == 20;
        mSession.setPitch(0);
        assert mSession.getPitch() == 50;
    }

    @Test
    public void addressTest(){
        String address = "Test";
        mSession.setAddress(address);
        assert mSession.getAddress().equals(address);
    }
}
