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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _20_LevelTwoActivityAccessibilityTest {
    private final String l1Title = "Fun";
    private final int levelOneItemPos = 3;

    @Rule
    public ActivityTestRule<LevelTwoActivity> activityRule =
            new ActivityTestRule<>(LevelTwoActivity.class, false, false);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
        getSession().setGridSize(4);
        getSession().setChangeLanguageNeverAsk(true);
        copyAssetsToInternalStorage(getContext(), ENG_IN);
        extractLanguagePackageZipFile(getContext(), ENG_IN);
        DataBaseHelper dbHelper = new DataBaseHelper(getContext());
        dbHelper.delete();
        dbHelper = new DataBaseHelper(getContext());
        dbHelper.createDataBase();
        dbHelper.openDataBase();
        dbHelper.addLanguageDataToDatabase();
        dbHelper.setLevel(3,3,"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
    }

    @Before
    public void createIntent(){
        Intent intent = new Intent();
        intent.putExtra(getContext().getString(R.string.level_one_intent_pos_tag),
                levelOneItemPos);
        intent.putExtra(getContext().getString(R.string.intent_menu_path_tag),
                l1Title + "/");
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
        onView(withId(R.id.btnClose)).perform(doubleClick());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Fun/ TV/")));
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
                .check(matches(withText("Fun/ TV")));
    }

    @Test
    public void _07_01_validateHelpAboutMeIcon(){
        createHelpIntentLaunchActivity();
        final int aboutMePos = 1;
        //Tapped on Help -> About me
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(aboutMePos, doubleClick()));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.mynameis)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.email)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.contact)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.caregiver)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.address)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.bloodgroup)));

        //Tapped on Help -> About me -> like
        onView(withId(R.id.ivlike)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like_pressed)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));

        //Tapped on Help -> About me -> yes
        onView(withId(R.id.ivyes)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes_pressed)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));

        //Tapped on Help -> About me -> more
        onView(withId(R.id.ivadd)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more_pressed)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));

        //Tapped on Help -> About me -> don't like
        onView(withId(R.id.ivdislike)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike_pressed)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));

        //Tapped on Help -> About me -> no
        onView(withId(R.id.ivno)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no_pressed)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));

        //Tapped on Help -> About me -> less
        onView(withId(R.id.ivminus)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less_pressed)));
    }


    /*@Test
    public void _07_02_validateExpressiveIconStateForHelpSpecialIcons() {
        int[] helpCategoryItems = {
                *//*Emergency*//*           0,
                *//*iFeelSick*//*           2,
                *//*iamHurt*//*             3,
                *//*iFeelTired*//*          4,
                *//*helpMeDoThis*//*        5,
                *//*Allergy*//*             12,
                *//*Danger*//*              13,
                *//*Hazards*//*             14,
                *//*IAmInPain*//*           16,
                *//*IWasPinched*//*         17,
                *//*IWasPushed*//*          18,
                *//*IWasScolded*//*         19,
                *//*IWasHit*//*             20,
                *//*IWasTouchedIn*//*       21,
                *//*IWasMadeFunOf*//*       22
        };
        createHelpIntentLaunchActivity();

        for (int itemPos : helpCategoryItems) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, doubleClick()));
            expressiveIconStateForHelpSpecialIcons();
            onView(withId(R.id.btnClose)).perform(doubleClick());
        }
    }*/

    @Test
    public void _07_03_validateExpressiveIconStateForHelpRestIcons() {
        int[] helpCategoryItems = {
                /*Medicine*/            6,
                /*Bandage*/             7,
                /*Water*/               8,
                /*Toilet*/              9,
                /*SanitaryNapkins*/     11
        };
        createHelpIntentLaunchActivity();
        for (int itemPos : helpCategoryItems) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, click()));
            expressiveIconStateForHelpRestIcons();
            onView(withId(R.id.btnClose)).perform(doubleClick());
        }
    }

    /*@Test
    public void _07_04_validateExpressiveIconStateForHelpUnsafeTouch() {
        createHelpIntentLaunchActivity();
        final int itemPos = 10;
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, doubleClick()));
        expressiveIconStateForHelpUnsafeTouchIcon();
    }

    @Test
    public void _07_05_validateExpressiveIconStateForHelpSafety() {
        createHelpIntentLaunchActivity();
        final int  itemPos = 15;
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        expressiveIconStateForHelpSafetyIcon();
    }*/


    private void createHelpIntentLaunchActivity(){
        activityRule.getActivity().finish();
        final int helpPos = 8;
        final String cat = "Help";
        Intent intent = new Intent();
        intent.putExtra(getContext().getString(R.string.level_one_intent_pos_tag),
                helpPos);
        intent.putExtra(getContext().getString(R.string.intent_menu_path_tag),
                cat + "/");
        activityRule.launchActivity(intent);
    }

    private void expressiveIconStateForHelpSpecialIcons(){
        onView(withId(R.id.ivlike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivyes)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivadd)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivdislike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivno)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivminus)).check(matches(not(isEnabled())));

    }

    private void expressiveIconStateForHelpRestIcons(){
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }

    private void expressiveIconStateForHelpUnsafeTouchIcon(){
        onView(withId(R.id.ivlike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivyes)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivadd)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }

    private void expressiveIconStateForHelpSafetyIcon(){
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }
}
