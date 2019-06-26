package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.dsource.idc.jellowintl.utility.DataBaseHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utils.EspressoTestMatchers.withDrawable;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _07_LevelThreeActivityUITest {
    private Context mContext;
    private final String l1Title = "Fun";
    private final String l2Title = "Outdoor Games";
    private final int levelOneItemPos = 3;
    private final int levelTwoItemPos = 1;

    @Rule
    public ActivityTestRule<LevelThreeActivity> activityRule =
            new ActivityTestRule<>(LevelThreeActivity.class, false, false);

    @Before
    public void setup(){
        mContext = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(mContext);
        manager.setCaregiverNumber("9653238072");
        manager.setLanguage(ENG_IN);
        manager.setGridSize(4);
        copyAssetsToInternalStorage(mContext);
        extractLanguagePackageZipFile(mContext);
        DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        dbHelper.delete();
        dbHelper = new DataBaseHelper(mContext);
        dbHelper.createDataBase();
        dbHelper.openDataBase();
        dbHelper.addLanguageDataToDatabase();
        dbHelper.setLevel(levelOneItemPos,levelTwoItemPos,"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),
                levelOneItemPos);
        intent.putExtra(mContext.getString(R.string.level_2_item_pos_tag),
                levelTwoItemPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag),
                l1Title.concat("/ " + l2Title + "/ "));
        activityRule.launchActivity(intent);
    }

    @Test
    public void _01_01validateActionBarTitleEvent(){
        Random randomGenerator = new Random();
        int itemPos = randomGenerator.nextInt(9);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(l1Title.concat("/ " + l2Title + "/ "))));
        onView(withId(R.id.keyboard)).perform(click(), closeSoftKeyboard());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.keyboard)));
        onView(withId(R.id.keyboard)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(l1Title.concat("/ " + l2Title + "/ "))));
        onView(withId(R.id.ivhome)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.home)));
    }

    @Test
    public void _01_02validateTappedCategoryItemEvent(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(levelTwoItemPos, click()));
        View view = activityRule.getActivity().mRecyclerView.getChildAt(levelTwoItemPos);
        GradientDrawable gd = (GradientDrawable) view.findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(ContextCompat.getColor(mContext, R.color.colorSelect));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home)));
    }

    @Test
    public void _02_01validateExpressiveLikeButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivlike)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorLike);
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like_pressed)));
    }

    @Test
    public void _02_02validateExpressiveDontLikeButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivdislike)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorDontLike);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike_pressed)));
    }

    @Test
    public void _02_03validateExpressiveYesButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivyes)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorYes);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes_pressed)));
    }

    @Test
    public void _02_04validateExpressiveNoButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivno)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorNo);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no_pressed)));
    }

    @Test
    public void _02_05validateExpressiveMoreButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivadd)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorMore);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more_pressed)));
    }

    @Test
    public void _02_06validateExpressiveLessButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivminus)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorLess);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less_pressed)));
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
    public void _03_01validateExpressiveIconState() {
        {
            closeSoftKeyboard();
            final int greetingCategorySize = 14;
            final int requestsCategorySize = 19;
            final int questionsCategorySize = 9;
            final int habitsCategorySize = 24;
            createIntentLaunchActivity(0, 0);
            for (int itemPos = 0; itemPos < greetingCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, click()));
                expressiveIconState();
            }
            createIntentLaunchActivity(0, 2);
            for (int itemPos = 0; itemPos < requestsCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, click()));
                expressiveIconState();
            }
            createIntentLaunchActivity(0, 3);
            for (int itemPos = 0; itemPos < questionsCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, click()));
                expressiveIconState();
            }
            createIntentLaunchActivity(1, 9);
            for (int itemPos = 0; itemPos < habitsCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, click()));
                expressiveIconState();
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
                        actionOnItemAtPosition(itemPos, click()));
                expressiveIconState();
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
                        actionOnItemAtPosition(iconPos, click()));
                expressiveIconState();
            }
        }
    }

    @Test
    public void _03_02validateExpressiveIconStateForGreetFeelFeeling() {
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
            _03_02expressiveIconStateForGreetFeelFeelings();
        }
    }

    @Test
    public void _03_03validateExpressiveIconStateForGreetFeelFeeling() {
        int[] catIcons = {
                /*Amazed*/      4,
                /*Hot*/         13,
                /*Cold*/        14
        };
        createIntentLaunchActivity(0, 1);
        for (int itemPos : catIcons) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, click()));
            _03_03expressiveIconStateForGreetFeelFeelings();
        }
    }

    @Test
    public void _03_04validateExpressiveIconStateForGreetFeelFeelingHappyIcon() {
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


    @Test
    public void _04_01validateCategoryLoadWithoutPreferences() {
        String[] categoriesWithoutSort = {"3,3", "3,4", "4,9", "7,0", "7,1", "7,2", "7,3", "7,4" };
        String[] firstIconInCategory= {
                "Switch on TV", "Change music", "INR 10", "Current time?", "What day is it?",
                "Current month?", "Today's weather?", "Current season?"
        };
        for (int i = 0; i < categoriesWithoutSort.length; i++) {
                createIntentLaunchActivity(Integer.valueOf(categoriesWithoutSort[i].split(",")[0]),
                    Integer.valueOf(categoriesWithoutSort[i].split(",")[1]));
            View v = activityRule.getActivity().mRecyclerView.getChildAt(0);
            String itemOneBelowText = ((TextView)v.findViewById(R.id.te1)).getText().toString();
            assert firstIconInCategory[i].equals(itemOneBelowText);
        }

    }

    @Test
    public void _04_02validateCategoryPreferencesIncrement() {
        View v = activityRule.getActivity().mRecyclerView.getChildAt(1);
        String itemOneBelowText = ((TextView)v.findViewById(R.id.te1)).getText().toString();
        DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        dbHelper.openDataBase();
        String prefString = dbHelper.getLevel(levelOneItemPos, levelTwoItemPos);
        assert "Park".equals(itemOneBelowText);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(1, click()));
        assert !prefString.equals(dbHelper.getLevel(levelOneItemPos, levelTwoItemPos));
    }


    /*@Test
    public void _05_00validateHomeButtonTapEvent() {
        Intents.init();
        onView(withId(R.id.ivhome)).perform(click());
        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home_pressed)));
        intended(hasComponent(MainActivity.class.getName()));
    }*/


    @Test
    public void _06_01validateKeyboardButtonTapEvent(){
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard_pressed)));
        onView(allOf(IsInstanceOf.instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.keyboard)));
        onView(withId(R.id.ttsbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.et)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivlike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivyes)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivadd)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivdislike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivno)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivminus)).check(matches(not(isEnabled())));

        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard)));
        onView(allOf(IsInstanceOf.instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(not(withText(R.string.keyboard))));
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }

    @Test
    public void _06_02validateKeyboardButtonTapExpressiveIconState(){
        {
            closeSoftKeyboard();
            final int greetingCategorySize = 14;
            final int requestsCategorySize = 19;
            final int questionsCategorySize = 9;
            final int habitsCategorySize = 24;
            createIntentLaunchActivity(0, 0);
            for (int itemPos = 0; itemPos < greetingCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, click()));
                performKeyboardClick();
                expressiveIconState();
            }
            createIntentLaunchActivity(0, 2);
            for (int itemPos = 0; itemPos < requestsCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, click()));
                performKeyboardClick();
                expressiveIconState();
            }
            createIntentLaunchActivity(0, 3);
            for (int itemPos = 0; itemPos < questionsCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, click()));
                performKeyboardClick();
                expressiveIconState();
            }
            createIntentLaunchActivity(1, 9);
            for (int itemPos = 0; itemPos < habitsCategorySize; itemPos++) {
                onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                        actionOnItemAtPosition(itemPos, click()));
                performKeyboardClick();
                expressiveIconState();
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
                        actionOnItemAtPosition(itemPos, click()));
                performKeyboardClick();
                expressiveIconState();
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
                        actionOnItemAtPosition(iconPos, click()));
                performKeyboardClick();
                expressiveIconState();
            }
        }
    }

    @Test
    public void _06_03validateKeyboardButtonTapExpressiveIconStateForGreetFeelFeeling(){
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
            performKeyboardClick();
            _03_02expressiveIconStateForGreetFeelFeelings();
        }}

    @Test
    public void _06_04validateKeyboardButtonTapExpressiveIconStateForGreetFeelFeeling(){
        int[] catIcons = {
                /*Amazed*/      4,
                /*Hot*/         13,
                /*Cold*/        14
        };
        createIntentLaunchActivity(0, 1);
        for (int itemPos : catIcons) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, click()));
            performKeyboardClick();
            _03_03expressiveIconStateForGreetFeelFeelings();
        }
    }

    @Test
    public void _06_05validateKeyboardButtonTapExpressiveIconStateForGreetFeelFeelingHappyIcon(){
        int catIcon = 0 /*Happy*/;
        createIntentLaunchActivity(0, 1);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(catIcon, click()));
        performKeyboardClick();
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }


    @Test
    public void _07_01validateBackButtonSimpleTapEvent(){
        String intentString = activityRule.getActivity().getIntent().getStringExtra(
                activityRule.getActivity().getString(R.string.from_search));
        if(intentString == null)
            return;
        if(intentString.isEmpty())
            return;
        try{
            Intents.init();
            assert intentString.equals(activityRule.getActivity().getString(R.string.search_tag));
            intended(hasComponent(MainActivity.class.getName()));
        }catch (Exception e){
            return;
        }
        Intents.release();
    }

    @Test
    public void _07_02validateBackButtonTapUiTestEvent(){
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.ivback)).perform(click());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard)));
        onView(withId(R.id.ivback)).check(matches(withDrawable(R.drawable.back_pressed)));
        onView(allOf(IsInstanceOf.instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(not(withText(R.string.keyboard))));
        onView(withId(R.id.ttsbutton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.et)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }

    @Test
    public void _07_03validateBackButtonTapExpressiveIconStateForGreetFeelFeeling(){
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
            onView(withId(R.id.keyboard)).perform(click());
            onView(withId(R.id.et)).perform(closeSoftKeyboard());
            onView(withId(R.id.ivback)).perform(click());
            _03_02expressiveIconStateForGreetFeelFeelings();
        }}

    @Test
    public void _07_04validateBackButtonTapExpressiveIconStateForGreetFeelFeeling(){
        int[] catIcons = {
                /*Amazed*/      4,
                /*Hot*/         13,
                /*Cold*/        14
        };
        createIntentLaunchActivity(0, 1);
        for (int itemPos : catIcons) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, click()));
            onView(withId(R.id.keyboard)).perform(click());
            onView(withId(R.id.et)).perform(closeSoftKeyboard());
            onView(withId(R.id.ivback)).perform(click());
            _03_03expressiveIconStateForGreetFeelFeelings();
        }
    }

    @Test
    public void _07_05validateKeyboardButtonTapExpressiveIconStateForGreetFeelFeelingHappyIcon(){
        int catIcon = 0 /*Happy*/;
        createIntentLaunchActivity(0, 1);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(catIcon, click()));
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.ivback)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }


    private void _03_02expressiveIconStateForGreetFeelFeelings() {
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }

    private void _03_03expressiveIconStateForGreetFeelFeelings() {
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }

    private void createIntentLaunchActivity(int l1Pos, int l2Pos){
        activityRule.getActivity().finish();
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag), l1Pos);
        intent.putExtra(mContext.getString(R.string.level_2_item_pos_tag), l2Pos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag), "dummyForTest");
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

    private void performKeyboardClick() {
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard_pressed)));
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard)));
    }
}
