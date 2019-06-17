package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.DialogKeyboardUtterance;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DialogKeyboardUtteranceTest {
    private Context mContext;

    @Before
    public void setup(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void showTest(){
        DialogKeyboardUtterance dKy = new DialogKeyboardUtterance(mContext);
        dKy.show();
        assert dKy.getDialog().isShowing();
    }
}
