package com.dsource.idc.jellowintl;


import android.os.Build;

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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _17_SettingsActivityTest {
    @Rule
    public ActivityTestRule<SettingActivity> activityRule =
            new ActivityTestRule<>(SettingActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setGridSize(4);
        getSession().setPitch(12);
        getSession().setSpeed(12);
        getSession().setPictureViewMode(1);
        getSession().setGridSize(4);
        getSession().setToastMessage("");
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
    }

   @Test
    public void _01_validateCallingEnable(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onView(withId(R.id.parent_scroll)).perform(swipeUp());
            onView(withId(R.id.switchEnableCall)).check(matches(not(isSelected()))).perform(click());
            assertTrue(getSession().getEnableCalling());
            onView(withId(R.id.switchEnableCall)).perform(click()).check(matches(not(isSelected())));
            assertFalse(getSession().getEnableCalling());
        }
    }

    @Test
    public void _02_validateUiForNonTtsLanguage(){
        activityRule.finishActivity();
        getSession().setLanguage(MR_IN);
        activityRule.launchActivity(null);
        onView(withId(R.id.speed)).check(matches(not(isDisplayed())));
        onView(withId(R.id.pitch)).check(matches(not(isDisplayed())));
        onView(withId(R.id.speechspeed)).check(matches(not(isDisplayed())));
        onView(withId(R.id.voicepitch)).check(matches(not(isDisplayed())));
    }

    /*@Test
    public void _03_validateSaveSettings(){
        activityRule.finishActivity();
        getSession().setLanguage(ENG_IN);
        getSession().setPitch(22);
        getSession().setSpeed(22);
        activityRule.launchActivity(null);
        onView(withId(R.id.spinner3)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Picture only"))).perform(click());
        onView(withId(R.id.spinner4)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("3"))).perform(click());
        onView(withId(R.id.speed)).perform(click());
        onView(withId(R.id.pitch)).perform(click());
        //onView(withId(R.id.button4)).perform(click());
        assert getSession().getPictureViewMode() == 1;
        assert getSession().getGridSize() == 2;
    }*/
}