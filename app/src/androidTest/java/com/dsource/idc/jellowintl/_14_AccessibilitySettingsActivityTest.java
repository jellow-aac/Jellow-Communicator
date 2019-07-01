package com.dsource.idc.jellowintl;

import android.app.Activity;
import android.app.Instrumentation;
import android.provider.MediaStore;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;

import com.dsource.idc.jellowintl.utility.DeveloperKey;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static com.dsource.idc.jellowintl.ThumbnailListener.VISUAL_ACCESS_VIDEO_ID;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getContext;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;

//@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _14_AccessibilitySettingsActivityTest {
    //@Rule
    IntentsTestRule<AccessibilitySettingsActivity> activityRule =
            new IntentsTestRule<>(AccessibilitySettingsActivity.class);
    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
    }

    @Before
    public void stubCameraIntent() {
        Instrumentation.ActivityResult result = createYoutTubeActivityResultStub();
        // Stub the Intent.
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);
    }


    /*@Test
    public void _01_validateVideoIntentTest(){
        onView(withId(R.id.thumbnailVisualAccess)).perform(click());
        *//*Intent intentOne = YouTubeStandalonePlayer.createVideoIntent(
                activityRule.getActivity(), DeveloperKey.DEVELOPER_KEY,
                VISUAL_ACCESS_VIDEO_ID, 0, false, false);*//*

        *//*onView(withId(R.id.thumbnailSwitchAccess)).perform(click());
        Intent intentTwo = YouTubeStandalonePlayer.createVideoIntent(
                activityRule.getActivity(), DeveloperKey.DEVELOPER_KEY,
                SWITCH_ACCESS_VIDEO_ID, 0, false, false);*//*
    }*/

    private Instrumentation.ActivityResult createYoutTubeActivityResultStub() {
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, YouTubeStandalonePlayer
            .createVideoIntent((Activity) getContext(), DeveloperKey.DEVELOPER_KEY,
                VISUAL_ACCESS_VIDEO_ID, 0, false, false));
    }
}
