package com.dsource.idc.jellowintl;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utils.ToastMatcher;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _02_01_FeedbackActivityTest {
    @Rule
    public ActivityTestRule<FeedbackActivity> activityRule =
            new ActivityTestRule<>(FeedbackActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
    }

    @Test
    public void _01validateRatedEasyToUse(){
        try {
            onView(withId(R.id.pictures)).perform(click());
            onView(withId(R.id.voice)).perform(click());
            onView(withId(R.id.navigate)).perform(click());
            onView(withId(R.id.comments)).perform(typeText("Awesome app")
                    , closeSoftKeyboard());
            Thread.sleep(500);
            onView(withId(R.id.bSubmit)).perform(click());
            onView(withText(R.string.rate_jellow))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _02validateRatedClearPictures(){
        try {
            onView(withId(R.id.easy_to_use)).perform(click());
            onView(withId(R.id.voice)).perform(click());
            onView(withId(R.id.navigate)).perform(click());
            onView(withId(R.id.comments)).perform(typeText("Awesome app"),
                    closeSoftKeyboard());
            Thread.sleep(500);
            onView(withId(R.id.bSubmit)).perform(click());
            onView(withText(R.string.rate_jellow))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _03validateRatedClearVoice(){
        try {
            onView(withId(R.id.comments)).perform(closeSoftKeyboard());
            onView(withId(R.id.easy_to_use)).perform(click());
            onView(withId(R.id.pictures)).perform(click());
            onView(withId(R.id.navigate)).perform(click());
            onView(withId(R.id.comments)).perform(typeText("Awesome app"),
                    closeSoftKeyboard());
            Thread.sleep(500);
            onView(withId(R.id.bSubmit)).perform(click());
            onView(withText(R.string.rate_jellow))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _04validateRatedEasyToNavigate(){
        try {
            onView(withId(R.id.comments)).perform(closeSoftKeyboard());
            onView(withId(R.id.easy_to_use)).perform(click());
            onView(withId(R.id.pictures)).perform(click());
            onView(withId(R.id.voice)).perform(click());
            onView(withId(R.id.comments)).perform(typeText("Awesome app"),
                    closeSoftKeyboard());
            Thread.sleep(500);
            onView(withId(R.id.bSubmit)).perform(click());
            onView(withText(R.string.rate_jellow))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _05validateAddedComments(){
        onView(withId(R.id.easy_to_use)).perform(click());
        onView(withId(R.id.pictures)).perform(click());
        onView(withId(R.id.voice)).perform(click());
        onView(withId(R.id.navigate)).perform(click());
        onView(withId(R.id.bSubmit)).perform(click());
        onView(withText(R.string.rate_jellow))
            .inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }
}