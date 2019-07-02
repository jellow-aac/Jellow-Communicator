package com.dsource.idc.jellowintl;


import android.content.Intent;
import android.widget.TextView;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.EspressoTestMatchers.withDrawable;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getContext;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _22_SequenceActivityAccessibilityTest {
    private final String l1Title = "Daily Activities";
    private final String l2Title = "Toilet";

    @Rule
    public ActivityTestRule<SequenceActivity> activityRule =
            new ActivityTestRule<>(SequenceActivity.class, false, false);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
        getSession().setGridSize(4);
        getSession().setChangeLanguageNeverAsk(true);
        copyAssetsToInternalStorage(getContext(), ENG_IN);
        extractLanguagePackageZipFile(getContext(), ENG_IN);
    }

    @Before
    public void createIntent(){
        int levelOneItemPos = 1;
        int levelTwoItemPos = 1;
        launchActivityWithCustomIntent(levelOneItemPos, levelTwoItemPos,
                l1Title.concat("/ " + l2Title + "/ "));
        closeSoftKeyboard();
    }

    @Test
    public void _01_validateAccessibilityDialog(){
        onView(withId(R.id.image1)).perform(doubleClick());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.enterCategory)).check(matches(isDisplayed()));
        onView(withId(R.id.btnClose)).check(matches(isDisplayed()));
        onView(withId(R.id.ivlike)).check(matches(isDisplayed()));
        onView(withId(R.id.ivyes)).check(matches(isDisplayed()));
        onView(withId(R.id.ivadd)).check(matches(isDisplayed()));
        onView(withId(R.id.ivdislike)).check(matches(isDisplayed()));
        onView(withId(R.id.ivno)).check(matches(isDisplayed()));
        onView(withId(R.id.ivminus)).check(matches(isDisplayed()));
        onView(withId(R.id.home)).check(matches(isDisplayed()));
        onView(withId(R.id.keyboard)).check(matches(isDisplayed()));
    }

    @Test
    public void _02_validateExpressiveStateOnTap(){
        onView(withId(R.id.image1)).perform(doubleClick());
        onView(withId(R.id.ivlike)).perform(doubleClick());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like_pressed)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivyes)).perform(doubleClick());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes_pressed)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivadd)).perform(doubleClick());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more_pressed)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivdislike)).perform(doubleClick());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike_pressed)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivno)).perform(doubleClick());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no_pressed)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivminus)).perform(doubleClick());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less_pressed)));
    }

    @Test
    public void _03_validateEnterButtonTap(){
        onView(withId(R.id.image1)).perform(doubleClick());
        onView(withId(R.id.enterCategory)).perform(doubleClick());
        onView(withId(R.id.enterCategory)).check(matches(withText("SPEAK")));
    }

    @Test
    public void _04_validateKeyboardButtonTap(){
        onView(withId(R.id.image1)).perform(doubleClick());
        onView(withId(R.id.keyboard)).perform(doubleClick());
        onView(withId(R.id.et_keyboard_utterances)).check(matches(isDisplayed()));
    }

    @Test
    public void _04_validateHomeButtonTap(){
        onView(withId(R.id.image1)).perform(doubleClick());
        onView(withId(R.id.home)).perform(doubleClick());
        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home_pressed)));
    }

    @Test
    public void _05_validateCloseButtonTap(){
        onView(withId(R.id.image1)).perform(doubleClick());
        onView(withId(R.id.btnClose)).perform(doubleClick());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Daily Activities/ Toilet/ ")));
    }

    private void launchActivityWithCustomIntent(int levelOneItemPos, int levelTwoItemPos, String title) {
        Intent intent = new Intent();
        intent.putExtra(getContext().getString(R.string.level_one_intent_pos_tag), levelOneItemPos);
        intent.putExtra(getContext().getString(R.string.level_2_item_pos_tag), levelTwoItemPos);
        intent.putExtra(getContext().getString(R.string.intent_menu_path_tag), title);
        activityRule.launchActivity(intent);
    }

}
