package com.dsource.idc.jellowintl;

import android.content.Intent;

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
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _24_Intro {
    @Rule
    public ActivityTestRule<UserRegistrationActivity> activityRule =
            new ActivityTestRule<>(UserRegistrationActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setUserLoggedIn(true);
        getSession().setCompletedIntro(false);
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
        getSession().setDownloaded(ENG_IN);
    }

    @Test
    public void _01_validateSlideTitles(){
        activityRule.getActivity().startActivity(new Intent(activityRule.getActivity(),
                Intro.class));
        onView(withId(R.id.tv_intro_title)).check(matches(withText(R.string.txt_intro1_central9btn)));
        onView(withId(R.id.intro_img)).perform(swipeLeft());
        onView(withId(R.id.tv_intro5_title)).check(matches(withText(R.string.txt_intro5_jellowUsageDesc)));
        onView(withId(R.id.intro5_img)).perform(swipeLeft());
        onView(withId(R.id.tv_intro2_title)).check(matches(withText(R.string.txt_intro2_appUsageDesc)));
        onView(withId(R.id.intro2_img)).perform(swipeLeft());
        onView(withId(R.id.tv_intro3_title)).check(matches(withText(R.string.txt_intro3_level2CatDesc)));
        onView(withId(R.id.intro3_img)).perform(swipeLeft());
        onView(withId(R.id.tv_intro4_title)).check(matches(withText(R.string.txt_intro4_customizeAppDesc)));
        onView(withId(R.id.intro4_img)).perform(swipeLeft());
        onView(withId(R.id.tv_intro8_title)).check(matches(withText(R.string.txt_intro8_txtTitle)));
        onView(withId(R.id.img2_intro8)).perform(swipeLeft());
        onView(withId(R.id.intro7_tvtop)).check(matches(withText(R.string.txt_intro7_getStartedDesc)));
    }
}
