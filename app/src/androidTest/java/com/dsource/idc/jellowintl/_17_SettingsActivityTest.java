package com.dsource.idc.jellowintl;


import android.os.Build;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

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
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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
        getSession().setEnableCalling(false);
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
}
