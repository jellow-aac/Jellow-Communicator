package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _07_ResetPreferencesActivityTest {
    private Context mContext;

    ActivityTestRule<ResetPreferencesActivity> activityRule =
            new ActivityTestRule<>(ResetPreferencesActivity.class, false, false);

    @Before
    public void setup(){
        mContext = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(mContext);
        manager.setCaregiverNumber("9653238072");
        manager.setLanguage(ENG_IN);
        manager.setGridSize(4);
        /*DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        dbHelper.createDataBase();
        dbHelper.openDataBase();
        copyAssetsToInternalStorage(mContext, ENG_IN);
        extractLanguagePackageZipFile(mContext, ENG_IN);*/
        activityRule.launchActivity(null);
        closeSoftKeyboard();
    }

    /*@Test
    public void _01_preferencesResetTest(){
        onView(withId(R.id.yes)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SessionManager manager = new SessionManager(mContext);
        manager.setLanguageChange(2);
        //assert manager.isLanguageChanged() == 0;
        assert !manager.isRequiredToPerformDbOperations();
        assert manager.getPeoplePreferences().isEmpty();
        assertTrue(activityRule.getActivity().isDestroyed());
    }*/

    @Test
    public void _02_backPressedTest(){
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityRule.getActivity().onBackPressed();
            }
        });

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(activityRule.getActivity().isDestroyed());
    }
}
