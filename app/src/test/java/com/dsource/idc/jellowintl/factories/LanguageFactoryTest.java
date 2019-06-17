package com.dsource.idc.jellowintl.factories;


import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Objects;

import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

@RunWith(AndroidJUnit4.class)
public class LanguageFactoryTest {
    private final HashMap<String,String> langCodeMap = new HashMap<String,String>(){
        {
            put(ENG_IN,"01");
            put(ENG_US,"02");
            put(ENG_UK,"03");
            put(HI_IN,"04");
            put(MR_IN,"05");
            put(BN_IN,"06");
            put(ENG_AU,"07");
            put("empty", "error");
        }
    };

    @Test
    public void getLanguageCodeTest(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SessionManager session = new SessionManager(context);session.setLanguage(ENG_UK);
        session.setLanguage(ENG_IN);
        assert Objects.equals(LanguageFactory.getCurrentLanguageCode(context), langCodeMap.get(ENG_IN));
        session.setLanguage(ENG_US);
        assert Objects.equals(LanguageFactory.getCurrentLanguageCode(context), langCodeMap.get(ENG_US));
        session.setLanguage(ENG_UK);
        assert Objects.equals(LanguageFactory.getCurrentLanguageCode(context), langCodeMap.get(ENG_UK));
        session.setLanguage(HI_IN);
        assert Objects.equals(LanguageFactory.getCurrentLanguageCode(context), langCodeMap.get(HI_IN));
        session.setLanguage(MR_IN);
        assert Objects.equals(LanguageFactory.getCurrentLanguageCode(context), langCodeMap.get(MR_IN));
        session.setLanguage(BN_IN);
        assert Objects.equals(LanguageFactory.getCurrentLanguageCode(context), langCodeMap.get(BN_IN));
        session.setLanguage(ENG_AU);
        assert Objects.equals(LanguageFactory.getCurrentLanguageCode(context), langCodeMap.get(ENG_AU));
        session.setLanguage("empty");
        assert LanguageFactory.getCurrentLanguageCode(context) == null;
    }
}