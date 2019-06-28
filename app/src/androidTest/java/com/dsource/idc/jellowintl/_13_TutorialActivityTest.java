package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;

//@RunWith(AndroidJUnit4.class)
@LargeTest
public class _13_TutorialActivityTest {

    ActivityTestRule<TutorialActivity> activityRule =
            new ActivityTestRule<>(TutorialActivity.class, false, false);

    @Before
    public void setup(){
        Context mContext = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(mContext);
        manager.setCaregiverNumber("9653238072");
        copyAssetsToInternalStorage(mContext, ENG_IN);
        extractLanguagePackageZipFile(mContext, ENG_IN);
        closeSoftKeyboard();
        activityRule.launchActivity(null);
    }

    /*@Test
    public void validateTutorialImagesLoadedCorrectly(){
        int[] imageViewId = { R.id.pic1, R.id.pic2, R.id.pic4, R.id.pic5, R.id.pic6, R.id.pic7,
                R.id.pic8, R.id.pic9, R.id.pic10, R.id.gtts1, R.id.gtts2, R.id.gtts3};
        int[] drawable = { R.drawable.categorybuttons,  R.drawable.expressivebuttons,
                R.drawable.speakingwithjellowimage2, R.drawable.eatingcategory1,
                R.drawable.eatingcategory2, R.drawable.eatingcategory3, R.drawable.settings,
                R.drawable.sequencewithoutexpressivebuttons, R.drawable.sequencewithexpressivebuttons,
                R.drawable.gtts1, R.drawable.gtts2, R.drawable.gtts3};
        for (int i = 0; i < imageViewId.length; i++) {
            onView(withId(imageViewId[i])).check(matches(withDrawable(drawable[i])));
        }
    }*/
}