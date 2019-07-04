package com.dsource.idc.jellowintl;

import android.widget.TextView;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _14_AccessibilitySettingsActivityTest {
    @Rule
    public ActivityTestRule<AccessibilitySettingsActivity> activityRule =
            new ActivityTestRule<>(AccessibilitySettingsActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
    }

    /*@Before
    public void stubCameraIntent() {
        Instrumentation.ActivityResult result = createYoutTubeActivityResultStub();
        // Stub the Intent.
        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);
    }*/


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


    @Test
    public void _02_ActionbarTitleTest(){
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.menuAccessibility)));
    }

    /*private Instrumentation.ActivityResult createYoutTubeActivityResultStub() {
        return new Instrumentation.ActivityResult(Activity.RESULT_OK, YouTubeStandalonePlayer
            .createVideoIntent((Activity) getContext(), DeveloperKey.DEVELOPER_KEY,
                VISUAL_ACCESS_VIDEO_ID, 0, false, false));
    }*/
}
