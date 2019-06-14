package com.dsource.idc.jellowintl;


import android.content.Context;

import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.LanguageHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LanguageHelperTest {
    private Context mContext;

    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void checkIfCorrectLocaleContext(){
        Context context = LanguageHelper.setLanguage(mContext, Locale.US);
        assert context.getResources().getConfiguration().locale.equals(Locale.US);
    }
}
