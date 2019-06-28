package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.dsource.idc.jellowintl.utility.DataBaseHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;

//@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _14_AccessibilitySettingsActivityTest {
    private Context mContext;

    ActivityTestRule<AccessibilitySettingsActivity> activityRule =
            new ActivityTestRule<>(AccessibilitySettingsActivity.class, false, false);

    @Before
    public void setup(){
        mContext = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(mContext);
        manager.setCaregiverNumber("9653238072");
        manager.setLanguage(ENG_IN);
        manager.setGridSize(4);
        DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        dbHelper.createDataBase();
        dbHelper.openDataBase();
        copyAssetsToInternalStorage(mContext, ENG_IN);
        extractLanguagePackageZipFile(mContext, ENG_IN);
        activityRule.launchActivity(null);
        closeSoftKeyboard();
    }

    /*@Test
    public void _01_validateVideoIntentTest(){
        onView(withId(R.id.thumbnailVisualAccess)).perform(click());
        Intent intentOne = YouTubeStandalonePlayer.createVideoIntent(
                activityRule.getActivity(), DeveloperKey.DEVELOPER_KEY,
                VISUAL_ACCESS_VIDEO_ID, 0, false, false);

        onView(withId(R.id.thumbnailSwitchAccess)).perform(click());
        Intent intentTwo = YouTubeStandalonePlayer.createVideoIntent(
                activityRule.getActivity(), DeveloperKey.DEVELOPER_KEY,
                SWITCH_ACCESS_VIDEO_ID, 0, false, false);
    }*/


}
