package com.dsource.idc.jellowintl;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class FeedbackActivityTest {
    @Rule
    public ActivityTestRule<FeedbackActivity> activityRule =
            new ActivityTestRule<>(FeedbackActivity.class);

    @Test
    public void validateRatedEasyToUse(){
        onView(withId(R.id.comments)).perform(closeSoftKeyboard());
        onView(withId(R.id.pictures)).perform(click(), swipeUp());
        onView(withId(R.id.voice)).perform(click(), swipeUp());
        onView(withId(R.id.navigate)).perform(click(), swipeUp());
        onView(withId(R.id.comments)).perform(typeText("Awesome app"), closeSoftKeyboard());
        onView(withId(R.id.bSubmit)).perform(click());
        onView(withText(R.string.rate_jellow))
            .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void validateRatedClearPictures(){
        onView(withId(R.id.comments)).perform(closeSoftKeyboard());
        onView(withId(R.id.easy_to_use)).perform(click(), swipeUp());
        onView(withId(R.id.voice)).perform(click(), swipeUp());
        onView(withId(R.id.navigate)).perform(click(), swipeUp());
        onView(withId(R.id.comments)).perform(typeText("Awesome app"), closeSoftKeyboard());
        onView(withId(R.id.bSubmit)).perform(click());
        onView(withText(R.string.rate_jellow))
            .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void validateRatedClearVoice(){
        onView(withId(R.id.comments)).perform(closeSoftKeyboard());
        onView(withId(R.id.easy_to_use)).perform(click(), swipeUp());
        onView(withId(R.id.pictures)).perform(click(), swipeUp());
        onView(withId(R.id.navigate)).perform(click(), swipeUp());
        onView(withId(R.id.comments)).perform(typeText("Awesome app"), closeSoftKeyboard());
        onView(withId(R.id.bSubmit)).perform(click());
        onView(withText(R.string.rate_jellow))
            .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void validateRatedEasyToNavigate(){
        onView(withId(R.id.comments)).perform(closeSoftKeyboard());
        onView(withId(R.id.easy_to_use)).perform(click(), swipeUp());
        onView(withId(R.id.pictures)).perform(click(), swipeUp());
        onView(withId(R.id.voice)).perform(click(), swipeUp());
        onView(withId(R.id.comments)).perform(typeText("Awesome app"), closeSoftKeyboard());
        onView(withId(R.id.bSubmit)).perform(click());
        onView(withText(R.string.rate_jellow))
            .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void validateAddedComments(){
        onView(withId(R.id.comments)).perform(closeSoftKeyboard());
        onView(withId(R.id.easy_to_use)).perform(click(), swipeUp());
        onView(withId(R.id.pictures)).perform(click(), swipeUp());
        onView(withId(R.id.voice)).perform(click(), swipeUp());
        onView(withId(R.id.navigate)).perform(click(), swipeUp());
        onView(withId(R.id.bSubmit)).perform(click());
        onView(withText(R.string.rate_jellow))
            .inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }
}