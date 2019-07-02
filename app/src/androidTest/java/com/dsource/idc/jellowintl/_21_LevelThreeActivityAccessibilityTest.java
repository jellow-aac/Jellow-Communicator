package com.dsource.idc.jellowintl;

import android.content.Intent;
import android.widget.TextView;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.DataBaseHelper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utils.EspressoTestMatchers.withDrawable;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getContext;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _21_LevelThreeActivityAccessibilityTest {
    private final String l1Title = "Fun";
    private final String l2Title = "Outdoor Games";
    private final int levelOneItemPos = 3;
    private final int levelTwoItemPos = 1;

    @Rule
    public ActivityTestRule<LevelThreeActivity> activityRule =
            new ActivityTestRule<>(LevelThreeActivity.class, false, false);

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
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        dbHelper.delete();
        dbHelper = new DataBaseHelper(getContext());
        dbHelper.createDataBase();
        dbHelper.openDataBase();
        dbHelper.addLanguageDataToDatabase();
        dbHelper.setLevel(levelOneItemPos,levelTwoItemPos,"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
        Intent intent = new Intent();
        intent.putExtra(getContext().getString(R.string.level_one_intent_pos_tag),
                levelOneItemPos);
        intent.putExtra(getContext().getString(R.string.level_2_item_pos_tag),
                levelTwoItemPos);
        intent.putExtra(getContext().getString(R.string.intent_menu_path_tag),
                l1Title.concat("/ " + l2Title + "/ "));
        activityRule.launchActivity(intent);
    }

    @Test
    public void _01_validateAccessibilityDialog(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(2, doubleClick()));
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
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(2, doubleClick()));
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
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, doubleClick()));
        onView(withId(R.id.enterCategory)).perform(doubleClick());
        onView(withId(R.id.enterCategory)).check(matches(withText("SPEAK")));
    }

    @Test
    public void _04_validateKeyboardButtonTap(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, doubleClick()));
        onView(withId(R.id.keyboard)).perform(doubleClick());
        onView(withId(R.id.et_keyboard_utterances)).check(matches(isDisplayed()));
    }

    @Test
    public void _05_validateHomeButtonTap(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, doubleClick()));
        onView(withId(R.id.home)).perform(doubleClick());
        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home_pressed)));
    }

    @Test
    public void _06_validateCloseButtonTap(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, doubleClick()));
        onView(withId(R.id.btnClose)).perform(doubleClick());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Fun/ Outdoor Games/ ")));
    }

    /**Categories are:
     *   Greet and Feel -> Greetings, Requests, Questions
     *   Daily Activities -> Clothes and more
     *   Daily Activities -> Habits
     *   Time and Weather -> Time
     *   Time and Weather -> Days
     *   Time and Weather -> Month
     *   Time and Weather -> Weather
     *   Time and Weather -> Seasons
     **/
    @Test
    public void _07_01validateExpressiveIconState() {
        {
            closeSoftKeyboard();
            final int greetingCategorySize = 14;
            final int requestsCategorySize = 19;
            final int questionsCategorySize = 9;
            final int habitsCategorySize = 24;
            createIntentLaunchActivity(0, 0);
            for (int itemPos = 0; itemPos < greetingCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, doubleClick()));
                expressiveIconState();
                onView(withId(R.id.btnClose)).perform(doubleClick());
            }
            createIntentLaunchActivity(0, 2);
            for (int itemPos = 0; itemPos < requestsCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, doubleClick()));
                expressiveIconState();
                onView(withId(R.id.btnClose)).perform(doubleClick());
            }
            createIntentLaunchActivity(0, 3);
            for (int itemPos = 0; itemPos < questionsCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, doubleClick()));
                expressiveIconState();
                onView(withId(R.id.btnClose)).perform(doubleClick());
            }
            createIntentLaunchActivity(1, 9);
            for (int itemPos = 0; itemPos < habitsCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, doubleClick()));
                expressiveIconState();
                onView(withId(R.id.btnClose)).perform(doubleClick());
            }
        }
        {
            ArrayList<Integer> catIcons = new ArrayList<>(4);
            if (activityRule.getActivity().getSession().getLanguage().contains(ENG_IN) ||
                    activityRule.getActivity().getSession().getLanguage().contains(HI_IN) ||
                    activityRule.getActivity().getSession().getLanguage().contains(BN_IN) ||
                    activityRule.getActivity().getSession().getLanguage().contains(MR_IN)) {
                catIcons.add(34);       /*MyClothesAreTight*/
                catIcons.add(35);       /*MyClothesAreLoose*/
                catIcons.add(36);       /*HelpMeRemoveClothes*/
                catIcons.add(37);       /*HelpMepUtOnClothes*/
            } else {
                catIcons.add(39);       /*MyClothesAreTight*/
                catIcons.add(40);       /*MyClothesAreLoose*/
                catIcons.add(41);       /*HelpMeRemoveClothes*/
                catIcons.add(42);       /*HelpMepUtOnClothes*/
            }
            createIntentLaunchActivity(1, 3);
            for (int itemPos : catIcons) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, doubleClick()));
                expressiveIconState();
                onView(withId(R.id.btnClose)).perform(doubleClick());
            }
        }
        {
            final int iconPos = 0;
            final int[] levelTwoCategoryPos = {
                    /*Time*/      0,
                    /*Day*/       1,
                    /*Month*/     2,
                    /*Weather*/   3,
                    /*Seasons*/   4
            };
            for (int catPos : levelTwoCategoryPos) {
                createIntentLaunchActivity(7, catPos);
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(iconPos, doubleClick()));
                expressiveIconState();
                onView(withId(R.id.btnClose)).perform(doubleClick());
            }
        }
    }

    @Test
    public void _07_02validateExpressiveIconStateForGreetFeelFeeling() {
        int[] catIcons = {
                /*Sad*/             1,
                /*Angry*/           2,
                /*Afraid*/          3,
                /*Irritated*/       5,
                /*Confused*/        6,
                /*Ashamed*/         7,
                /*Disappointed*/    8,
                /*Bored*/           9,
                /*Worried*/         10,
                /*Stressed*/        11,
                /*tired?*/          12,
                /*Sick*/            15,
                /*Hurt*/            16
        };
        createIntentLaunchActivity(0, 1);
        for (int itemPos : catIcons) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, click()));
            _07_02expressiveIconStateForGreetFeelFeelings();
            onView(withId(R.id.btnClose)).perform(doubleClick());
        }
    }

    @Test
    public void _07_03validateExpressiveIconStateForGreetFeelFeeling() {
        int[] catIcons = {
                /*Amazed*/      4,
                /*Hot*/         13,
                /*Cold*/        14
        };
        createIntentLaunchActivity(0, 1);
        for (int itemPos : catIcons) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, click()));
            _07_03expressiveIconStateForGreetFeelFeelings();
            onView(withId(R.id.btnClose)).perform(doubleClick());
        }
    }

    @Test
    public void _07_04validateExpressiveIconStateForGreetFeelFeelingHappyIcon() {
        int catIcon = 0 /*Happy*/;
        createIntentLaunchActivity(0, 1);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(catIcon, click()));
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }


    private void createIntentLaunchActivity(int l1Pos, int l2Pos){
        activityRule.getActivity().finish();
        Intent intent = new Intent();
        intent.putExtra(getContext().getString(R.string.level_one_intent_pos_tag), l1Pos);
        intent.putExtra(getContext().getString(R.string.level_2_item_pos_tag), l2Pos);
        intent.putExtra(getContext().getString(R.string.intent_menu_path_tag), "dummyForTest");
        activityRule.launchActivity(intent);
    }

    private void expressiveIconState() {
        onView(withId(R.id.ivlike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivyes)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivadd)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivdislike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivno)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivminus)).check(matches(not(isEnabled())));
    }

    private void _07_02expressiveIconStateForGreetFeelFeelings() {
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }

    private void _07_03expressiveIconStateForGreetFeelFeelings() {
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }
}
