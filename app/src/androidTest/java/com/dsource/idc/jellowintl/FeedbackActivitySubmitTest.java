package com.dsource.idc.jellowintl;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.dsource.idc.jellowintl.UserRegistrationActivityTest.customSwipeUp;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

public class FeedbackActivitySubmitTest {
    @Rule
    public IntentsTestRule<FeedbackActivity> intentRule =
            new IntentsTestRule<>(FeedbackActivity.class, false, false);

    @Before
    public void stubAllExternalIntents() {
        Context context = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(context);
        manager.setCaregiverNumber("9653238072");
        intentRule.launchActivity(null);
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).
                respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void validateSubmittedFeedback(){
        try {
            onView(withId(R.id.easy_to_use)).perform(click());
            onView(withId(R.id.pictures)).perform(click());
            onView(withId(R.id.scrollView2)).perform(customSwipeUp());
            Thread.sleep(1000);
            onView(withId(R.id.voice)).perform(click());
            onView(withId(R.id.navigate)).perform(click());
            onView(withId(R.id.comments)).perform(typeText("Awesome app"), closeSoftKeyboard());
            onView(withId(R.id.bSubmit)).perform(click());
            intended(allOf(hasAction(Intent.ACTION_CHOOSER)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
