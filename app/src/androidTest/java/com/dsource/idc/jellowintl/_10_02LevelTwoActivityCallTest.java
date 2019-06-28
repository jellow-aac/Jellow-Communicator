package com.dsource.idc.jellowintl;

import androidx.test.filters.LargeTest;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _10_02LevelTwoActivityCallTest {
    /*@Rule
    public IntentsTestRule<LevelTwoActivity> intentRule =
            new IntentsTestRule<>(LevelTwoActivity.class, false, false);

    @Before
    public void stubAllExternalIntents() {
        Context context = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(context);
        manager.setCaregiverNumber("9653238072");
        manager.setEnableCalling(true);
        final int helpPos = 8;
        final String cat = "Help";
        Intent intent = new Intent();
        intent.putExtra(context.getString(R.string.level_one_intent_pos_tag),
                helpPos);
        intent.putExtra(context.getString(R.string.intent_menu_path_tag),
                cat + "/");
        intentRule.launchActivity(intent);
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).
                respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void _09_00validateCallingIntent() {
        final int itemPos = 0;
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        intended(allOf(hasAction(Intent.ACTION_CALL)));
    }*/
}
