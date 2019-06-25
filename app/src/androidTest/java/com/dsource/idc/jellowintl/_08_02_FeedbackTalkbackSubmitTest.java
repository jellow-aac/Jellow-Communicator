package com.dsource.idc.jellowintl;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class _08_02_FeedbackTalkbackSubmitTest {
    @Rule
    public IntentsTestRule<FeedbackActivityTalkBack> intentRule =
            new IntentsTestRule<>(FeedbackActivityTalkBack.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).
                respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void validateSubmittedFeedback(){
        try {
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
            onView(withId(R.id.comments)).perform(typeText("Awesome app"),
                    closeSoftKeyboard());
            Thread.sleep(500);
            onView(withId(R.id.bSubmit)).perform(click());
            intended(allOf(hasAction(Intent.ACTION_CHOOSER)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
