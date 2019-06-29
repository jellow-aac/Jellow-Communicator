package com.dsource.idc.jellowintl.utility;


import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;

@RunWith(AndroidJUnit4.class)
public class LanguageHelperTest {
    private Context mContext;
    SessionManager sessionManager;

    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sessionManager = new SessionManager(mContext);
        sessionManager.setLanguage(ENG_IN);
    }

    @Test
    public void checkIfCorrectLocaleContext(){
        Context context = LanguageHelper.setLanguage(mContext, Locale.US);
        assert context.getResources().getConfiguration().locale.equals(Locale.US);
    }

    @Test
    public void leagcyTests(){
     LanguageHelper.onAttach(mContext);
    }
}
