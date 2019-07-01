package com.dsource.idc.jellowintl;

import android.view.View;
import android.widget.ImageView;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.runners.MethodSorters;

import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;

//@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _18_TutorialActivityTest {
    @Rule
    public ActivityTestRule<TutorialActivity> activityRule =
            new ActivityTestRule<>(TutorialActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
    }

    /*@Test
    public void validateTutorialImagesLoadedCorrectly(){
        int[] imageViewId = { R.id.pic2, R.id.pic4, R.id.pic5, R.id.pic6, R.id.pic7,
                R.id.pic8, R.id.pic9, R.id.pic10, R.id.gtts1, R.id.gtts2, R.id.gtts3};
        onView(withId(R.id.pic1)).check(matches(hasDrawable()));
        for (int id : imageViewId) {
            onView(withId(id)).perform(scrollTo()).check(matches(hasDrawable()));
        }
    }*/

    public static Matcher<View> hasDrawable() {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {}

            @Override
            protected boolean matchesSafely(View item) {
                ImageView imageView = (ImageView) item;
                return imageView.getDrawable() != null;
            }
        };
    }
}