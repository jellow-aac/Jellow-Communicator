package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.utils.ToastMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
public class FeedbackActivityTest {
    @Rule
    public ActivityTestRule<FeedbackActivity> activityRule =
            new ActivityTestRule<>(FeedbackActivity.class, false, false);

    @Before
    public void setup(){
        Context context = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(context);
        manager.setCaregiverNumber("9653238072");
        activityRule.launchActivity(null);
    }

    @Test
    public void validateRatedEasyToUse(){
        try {
            onView(withId(R.id.pictures)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.voice)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.navigate)).perform(click(), swipeUp(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.comments)).perform(typeText("Awesome app"), closeSoftKeyboard());
            onView(withId(R.id.bSubmit)).perform(click());
            onView(withText(R.string.rate_jellow))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            activityRule.finishActivity();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateRatedClearPictures(){
        try {
            onView(withId(R.id.easy_to_use)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.voice)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.navigate)).perform(click(), swipeUp(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.comments)).perform(typeText("Awesome app"), closeSoftKeyboard());
            onView(withId(R.id.bSubmit)).perform(click());
            onView(withText(R.string.rate_jellow))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            activityRule.finishActivity();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateRatedClearVoice(){
        try {
            onView(withId(R.id.comments)).perform(closeSoftKeyboard());
            onView(withId(R.id.easy_to_use)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.pictures)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.navigate)).perform(click(), swipeUp(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.comments)).perform(typeText("Awesome app"), closeSoftKeyboard());
            onView(withId(R.id.bSubmit)).perform(click());
            onView(withText(R.string.rate_jellow))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            activityRule.finishActivity();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateRatedEasyToNavigate(){
        try {
            onView(withId(R.id.comments)).perform(closeSoftKeyboard());
            onView(withId(R.id.easy_to_use)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.pictures)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.voice)).perform(click(), swipeUp(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.comments)).perform(typeText("Awesome app"), closeSoftKeyboard());
            onView(withId(R.id.bSubmit)).perform(click());
            onView(withText(R.string.rate_jellow))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            activityRule.finishActivity();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateAddedComments(){
        try {
            onView(withId(R.id.easy_to_use)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.pictures)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.voice)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.navigate)).perform(click(), swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.bSubmit)).perform(click());
            onView(withText(R.string.rate_jellow))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            activityRule.finishActivity();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}