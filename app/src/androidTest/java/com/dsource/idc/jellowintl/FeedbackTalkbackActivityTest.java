package com.dsource.idc.jellowintl;


import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.utils.ToastMatcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FeedbackTalkbackActivityTest {
    @Rule
    public ActivityTestRule<FeedbackActivityTalkBack> activityRule =
            new ActivityTestRule<>(FeedbackActivityTalkBack.class, false, false);

    @Before
    public void setup(){
        Context context = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(context);
        manager.setCaregiverNumber("9653238072");
        activityRule.launchActivity(null);
    }

    @Test
    public void validateAddedComments(){
        onView(withId(R.id.comments)).perform(closeSoftKeyboard());
        onView(withId(R.id.easytouse)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2"))).perform(click());
        onView(withId(R.id.easytouse)).check(matches(withSpinnerText(containsString("2"))));
        onView(withId(R.id.clearpictures)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2"))).perform(click());
        onView(withId(R.id.clearpictures)).check(matches(withSpinnerText(containsString("2"))));
        onView(withId(R.id.clearvoice)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2"))).perform(click());
        onView(withId(R.id.clearvoice)).check(matches(withSpinnerText(containsString("2"))));
        onView(withId(R.id.navigate)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2"))).perform(click());
        onView(withId(R.id.navigate)).check(matches(withSpinnerText(containsString("2"))));
        onView(withId(R.id.bSubmit)).perform(click());
        onView(withText(R.string.rate_jellow)).inRoot(new ToastMatcher()).
                check(matches(isDisplayed()));
    }
}
