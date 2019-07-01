package com.dsource.idc.jellowintl;

import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.utility.DataBaseHelper;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getContext;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _07_ResetPreferencesActivityTest {
    @Rule
    public ActivityTestRule<ResetPreferencesActivity> activityRule =
            new ActivityTestRule<>(ResetPreferencesActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
        getSession().setGridSize(4);
        copyAssetsToInternalStorage(getContext(), ENG_IN);
        extractLanguagePackageZipFile(getContext(), ENG_IN);
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        dbHelper.createDataBase();
        dbHelper.openDataBase();
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
        CacheManager.clearCache();
        TextFactory.clearJson();
    }

    @Before
    public void closeKeyboard(){
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void _01_preferencesResetTest(){
        onView(withId(R.id.yes)).perform(click());
        try {
            assert getSession().isLanguageChanged() == 0;
            assert !getSession().isRequiredToPerformDbOperations();
            assert getSession().getPeoplePreferences().isEmpty();
            assertTrue(activityRule.getActivity().isDestroyed());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*@Test
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
    }*/
}
