package com.dsource.idc.jellowintl;

import androidx.test.filters.LargeTest;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _02_01_FeedbackActivitySubmitTest {
    /*@Rule
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
        try {
            intending(not(isInternal())).
                    respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
            Intents.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void _01validateSubmittedFeedback(){
        try {
            onView(ViewMatchers.withId(R.id.easy_to_use)).perform(click());
            onView(withId(R.id.pictures)).perform(click());
            onView(withId(R.id.voice)).perform(click());
            onView(withId(R.id.navigate)).perform(click());
            onView(withId(R.id.scrollView2)).perform(swipeUp());
            onView(withId(R.id.comments)).perform(click(), typeText("Awesome app"),
                    closeSoftKeyboard());
            onView(withId(R.id.bSubmit)).perform(click());
            Thread.sleep(700);
            onView(withText(R.string.checkConnectivity)).inRoot(withDecorView(not(is(
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
