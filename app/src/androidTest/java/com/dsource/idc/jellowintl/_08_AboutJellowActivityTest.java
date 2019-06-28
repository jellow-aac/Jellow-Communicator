package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class _08_AboutJellowActivityTest {
    @Rule
    public ActivityTestRule<AboutJellowActivity> activityRule =
            new ActivityTestRule<>(AboutJellowActivity.class, false, false);

    @Before
    public void stubAllExternalIntents() {
        final Context context = getInstrumentation().getTargetContext();
        //private TextToSpeech ttsEngine;
        SessionManager mSession = new SessionManager(context);
        mSession.setCaregiverNumber("9653238072");
        activityRule.launchActivity(null);
    }

    @Test
    public void _01_validateSoftwareVersionInfo(){
        final Context context = getInstrumentation().getTargetContext();
        String versionInfo = context.getString(R.string.software_info)
                .replace("_", BuildConfig.VERSION_NAME);
        onView(withId(R.id.tv8)).check(matches(withText(versionInfo)));
    }

    @Test
    public void _02_backPressedTest(){
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityRule.getActivity().onBackPressed();
            }
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(activityRule.getActivity().isDestroyed());
    }

    /*@Test
    public void _01_isTtsSynthesizeInProcess(){
        onView(withId(R.id.speak)).perform(click());
        assert ttsEngine.isSpeaking();
    }*/
}
