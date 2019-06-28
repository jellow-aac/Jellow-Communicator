package com.dsource.idc.jellowintl;

import androidx.test.filters.LargeTest;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _03_02_FeedbackTalkbackSubmitTest {
    /*@Rule
    public IntentsTestRule<FeedbackActivityTalkBack> intentRule =
            new IntentsTestRule<>(FeedbackActivityTalkBack.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        try {
            intending(not(isInternal())).
                    respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
            Intents.release();
        }catch (Exception e){
            e.printStackTrace();
        }
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
            onView(withId(R.id.bSubmit)).perform(click());
            Thread.sleep(700);
            onView(withText(R.string.checkConnectivity)).inRoot(withDecorView(CoreMatchers.not(is(
                    intentRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            intended(allOf(hasAction(Intent.ACTION_CHOOSER)));
            Intents.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }*/
}
