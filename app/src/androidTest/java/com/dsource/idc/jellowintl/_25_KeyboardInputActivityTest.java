package com.dsource.idc.jellowintl;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _25_KeyboardInputActivityTest {
    @Rule
    public ActivityTestRule<KeyboardInputActivity> activityRule =
            new ActivityTestRule<>(KeyboardInputActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
    }

    /*@Test
    public void _01_validateSerialKeyboardBtnTest(){
        onView(withId(R.id.abc)).perform(click());
        onView(withText("Jellow Keyboard")).check(matches(isDisplayed()));
    }

    @Test
    public void _02_validateQwertyKeyboardBtnTest(){
        onView(withId(R.id.qwerty)).perform(click());
        onView(withText("Jellow Keyboard")).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void _03_validateDefaultBtnTest(){
        onView(withId(R.id.qwerty)).perform(click());
        onView(withText("Change keyboard")).inRoot(isDialog()).check(matches(isDisplayed()));
    }*/

    @Test
    public void _01_validateScreenText(){
        onView(withId(R.id.t2)).check(matches(withText(R.string.step1)));
        onView(withId(R.id.t3)).check(matches(withText(R.string.step2)));
    }
}
