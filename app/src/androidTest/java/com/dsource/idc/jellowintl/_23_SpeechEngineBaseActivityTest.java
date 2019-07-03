package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;

import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getContext;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _23_SpeechEngineBaseActivityTest {
    @Rule
    public ActivityTestRule<SpeechEngineBaseActivity> activityRule =
            new ActivityTestRule<>(SpeechEngineBaseActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setEmailId("jellowcommunicator@gmail.com");
        getSession().setCaregiverNumber("9653238072");
        getSession().setName("Akash");
        getSession().setCaregiverName("Anjali");
        getSession().setAddress("Mumbai");
        getSession().setBlood(1);
        getSession().setLanguage(ENG_IN);
        getSession().setPitch(100);
        getSession().setSpeed(100);
        getSession().setChangeLanguageNeverAsk(true);
    }

    @Test
    public void _01_validateSpeechInitTest(){
        assert activityRule.getActivity().getSpeechEngineLanguage().equals(ENG_IN);
    }

    @Test
    public void _02_validateTtsSynthesisForNonTTsLanguageTest(){
        activityRule.finishActivity();
        getSession().setLanguage(MR_IN);
        final String path = getContext().getDir(MR_IN, Context.MODE_PRIVATE).getAbsolutePath() + "/audio";
        File f = new File(path);
        f.mkdir();
        activityRule.launchActivity(null);

        assert  (new File(path+ "/name.mp3")).exists();
        assert  (new File(path+ "/email.mp3")).exists();
        assert  (new File(path+ "/contact.mp3")).exists();
        assert  (new File(path+ "/caregiverName.mp3")).exists();
        assert  (new File(path+ "/address.mp3")).exists();
        assert  (new File(path+ "/bloodGroup.mp3")).exists();
    }

    @Test
    public void _03_validateNonTtsLanguage(){
        assert MR_IN.equals(activityRule.getActivity().isNoTTSLanguage());
    }
}
