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
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

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
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.EspressoTestMatchers.withDrawable;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _06_LevelTwoActivityActivityUITest {
    private Context mContext;
    private final String l1Title = "Fun";
    private final int levelOneItemPos = 3;
    @Rule
    public ActivityTestRule<LevelTwoActivity> activityRule =
            new ActivityTestRule<>(LevelTwoActivity.class, false, false);

    @Before
    public void setup(){
        mContext = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(mContext);
        manager.setCaregiverNumber("9653238072");
        manager.setLanguage(ENG_IN);
        manager.setGridSize(4);
        copyAssetsToInternalStorage(mContext);
        extractLanguagePackageZipFile(mContext);
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),
                levelOneItemPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag),
                l1Title + "/");
        activityRule.launchActivity(intent);
    }

    @Test
    public void _01_01validateActionBarTitleEvent(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(levelOneItemPos, click()));
        View view = activityRule.getActivity().mRecyclerView.getChildAt(levelOneItemPos);
        String title = ((TextView)view.findViewById(R.id.te1)).getText().toString();
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(l1Title.concat("/ "+title.replace("…","")))));
        onView(withId(R.id.ivhome)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.home)));
        onView(withId(R.id.keyboard)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.keyboard)));
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
    }

    @Test
    public void _01_02validateTappedCategoryItemEvent(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(levelOneItemPos, click()));
        View view = activityRule.getActivity().mRecyclerView.getChildAt(levelOneItemPos);
        GradientDrawable gd = (GradientDrawable) view.findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(ContextCompat.getColor(mContext, R.color.colorSelect));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home)));
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(l1Title.concat("/ "+"TV"))));
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

    @Test
    public void _02_07validateExpressiveIconsForHelpAboutMe() {
        activityRule.getActivity().finish();
        final int helpPos = 8, aboutMePos = 1;
        final String cat = "Help";
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),
                helpPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag),
                cat + "/");
        activityRule.launchActivity(intent);
        //Tapped on Help -> About me
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(aboutMePos, click()));
        View view = activityRule.getActivity().mRecyclerView.getChildAt(aboutMePos);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorSelect);

        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.mynameis)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.email)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.contact)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.caregiver)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.address)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.bloodgroup)));

        //Tapped on Help -> About me -> like
        onView(withId(R.id.ivlike)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.mynameis_pressed)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.email)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.contact)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.caregiver)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.address)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.bloodgroup)));
        assert gd.getColor().equals(R.color.colorLike);

        //Tapped on Help -> About me -> yes
        onView(withId(R.id.ivyes)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.mynameis)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.email_pressed)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.contact)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.caregiver)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.address)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.bloodgroup)));
        assert gd.getColor().equals(R.color.colorYes);

        //Tapped on Help -> About me -> more
        onView(withId(R.id.ivadd)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.mynameis)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.email)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.contact_pressed)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.caregiver)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.address)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.bloodgroup)));
        assert gd.getColor().equals(R.color.colorMore);

        //Tapped on Help -> About me -> don't like
        onView(withId(R.id.ivdislike)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.mynameis)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.email)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.contact)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.caregiver_pressed)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.address)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.bloodgroup)));
        assert gd.getColor().equals(R.color.colorDontLike);

        //Tapped on Help -> About me -> like
        onView(withId(R.id.ivno)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.mynameis)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.email)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.contact)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.caregiver)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.address_pressed)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.bloodgroup)));
        assert gd.getColor().equals(R.color.colorNo);

        //Tapped on Help -> About me -> yes
        onView(withId(R.id.ivminus)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.mynameis)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.email)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.contact)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.caregiver)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.address)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.blooedgroup_pressed)));
        assert gd.getColor().equals(R.color.colorLess);
    }


    @Test
    public void _03_01validateExpressiveIconStateForHelpSpecialIcons() {
        int[] helpCategoryItems = {
                /*Emergency*/           0,
                /*iFeelSick*/           2,
                /*iamHurt*/             3,
                /*iFeelTired*/          4,
                /*helpMeDoThis*/        5,
                /*Allergy*/             12,
                /*Danger*/              13,
                /*Hazards*/             14,
                /*IAmInPain*/           16,
                /*IWasPinched*/         17,
                /*IWasPushed*/          18,
                /*IWasScolded*/         19,
                /*IWasHit*/             20,
                /*IWasTouchedIn*/       21,
                /*IWasMadeFunOf*/       22
        };
        createHelpIntentLaunchActivity();

        for (int itemPos : helpCategoryItems) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, click()));
            expressiveIconStateForHelpSpecialIcons();
        }
    }

    @Test
    public void _03_02validateExpressiveIconStateForHelpRestIcons() {
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
        }
    }

    @Test
    public void _03_03validateExpressiveIconStateForHelpUnsafeTouch() {
        activityRule.getActivity().finish();
        final int helpPos = 8, itemPos = 10;
        final String cat = "Help";
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),
                helpPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag),
                cat + "/");
        activityRule.launchActivity(intent);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        expressiveIconStateForHelpUnsafeTouchIcon();
    }

    @Test
    public void _03_04validateExpressiveIconStateForHelpSafety() {
        activityRule.getActivity().finish();
        final int helpPos = 8, itemPos = 15;
        final String cat = "Help";
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),
                helpPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag),
                cat + "/");
        activityRule.launchActivity(intent);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        expressiveIconStateForHelpSafetyIcon();
    }


    @Test
    public void _04_00validatePeopleIconLoadingPreferences() {
        activityRule.getActivity().finish();
        final int helpPos = 5;
        final String category = "Help";
        String preferencesString = "1,2,3,4,5,6,7,8,9,0,11,23,45,12,3,4,7,2";
        activityRule.getActivity().getSession().setPeoplePreferences(preferencesString);
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag), helpPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag),category + "/");
        activityRule.launchActivity(intent);
        View v = activityRule.getActivity().mRecyclerView.getChildAt(0);
        String itemOneBelowText = ((TextView)v.findViewById(R.id.te1)).getText().toString();
        assert "Doctor".equals(itemOneBelowText);

    }


    /*@Test
    public void _05_00validateHomeButtonTapEvent() {
        onView(withId(R.id.ivhome)).perform(click());
        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home_pressed)));
        Intents.init();
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }*/


    @Test
    public void _06_01validateKeyboardButtonTapEvent(){
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard_pressed)));
        onView(allOf(instanceOf(TextView.class),
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
        onView(allOf(instanceOf(TextView.class),
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
    public void _06_02validateKeyboardButtonTapExpressiveIconRetainHelpSpecialIcons(){
        int[] helpCategoryItems = {
                /*Emergency*/           0,
                /*iFeelSick*/           2,
                /*iamHurt*/             3,
                /*iFeelTired*/          4,
                /*helpMeDoThis*/        5,
                /*Allergy*/             12,
                /*Danger*/              13,
                /*Hazards*/             14,
                /*IAmInPain*/           16,
                /*IWasPinched*/         17,
                /*IWasPushed*/          18,
                /*IWasScolded*/         19,
                /*IWasHit*/             20,
                /*IWasTouchedIn*/       21,
                /*IWasMadeFunOf*/       22
        };
        createHelpIntentLaunchActivity();
        for (int itemPos : helpCategoryItems) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, click()));
            onView(withId(R.id.keyboard)).perform(click());
            onView(withId(R.id.et)).perform(closeSoftKeyboard());
            onView(withId(R.id.keyboard)).perform(click());
            onView(allOf(instanceOf(TextView.class),
                    withParent(withResourceName("action_bar"))))
                    .check(matches(not(withText(R.string.keyboard))));
            expressiveIconStateForHelpSpecialIcons();
        }
    }

    @Test
    public void _06_03validateKeyboardButtonTapExpressiveIconRetainHelpRestIcons(){
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
            onView(withId(R.id.keyboard)).perform(click());
            onView(withId(R.id.et)).perform(closeSoftKeyboard());
            onView(withId(R.id.keyboard)).perform(click());
            onView(allOf(instanceOf(TextView.class),
                    withParent(withResourceName("action_bar"))))
                    .check(matches(not(withText(R.string.keyboard))));
            expressiveIconStateForHelpRestIcons();
        }
    }

    @Test
    public void _06_04validateKeyboardButtonTapExpressiveIconRetainHelpUnsafeTouchIcon(){
        final int itemPos = 10;
        createHelpIntentLaunchActivity();
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.keyboard)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(not(withText(R.string.keyboard))));
        expressiveIconStateForHelpUnsafeTouchIcon();
    }

    @Test
    public void _06_05validateKeyboardButtonTapExpressiveIconRetainHelpSafetyIcon(){
        final int itemPos = 15;
        createHelpIntentLaunchActivity();
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.keyboard)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(not(withText(R.string.keyboard))));
        expressiveIconStateForHelpSafetyIcon();
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
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(not(withText(R.string.keyboard))));
        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home)));
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
    public void _07_03validateBackButtonTapExpressiveIconRetainHelpSpecialIcons(){
        int[] helpCategoryItems = {
                /*Emergency*/           0,
                /*iFeelSick*/           2,
                /*iamHurt*/             3,
                /*iFeelTired*/          4,
                /*helpMeDoThis*/        5,
                /*Allergy*/             12,
                /*Danger*/              13,
                /*Hazards*/             14,
                /*IAmInPain*/           16,
                /*IWasPinched*/         17,
                /*IWasPushed*/          18,
                /*IWasScolded*/         19,
                /*IWasHit*/             20,
                /*IWasTouchedIn*/       21,
                /*IWasMadeFunOf*/       22
        };
        createHelpIntentLaunchActivity();

        for (int itemPos : helpCategoryItems) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                    actionOnItemAtPosition(itemPos, click()));
            onView(withId(R.id.keyboard)).perform(click());
            onView(withId(R.id.et)).perform(closeSoftKeyboard());
            onView(withId(R.id.ivback)).perform(click());
            onView(allOf(instanceOf(TextView.class),
                    withParent(withResourceName("action_bar"))))
                    .check(matches(not(withText(R.string.keyboard))));
            expressiveIconStateForHelpSpecialIcons();
        }
    }

    @Test
    public void _07_04validateBackButtonTapExpressiveIconRetainHelpUnsafeTouchIcon(){
        final int itemPos = 10;
        createHelpIntentLaunchActivity();
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.ivback)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(not(withText(R.string.keyboard))));
        expressiveIconStateForHelpUnsafeTouchIcon();
    }

    @Test
    public void _07_05validateBackButtonTapExpressiveIconRetainHelpRestIcons(){
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
            onView(withId(R.id.keyboard)).perform(click());
            onView(withId(R.id.et)).perform(closeSoftKeyboard());
            onView(withId(R.id.ivback)).perform(click());
            onView(allOf(instanceOf(TextView.class),
                    withParent(withResourceName("action_bar"))))
                    .check(matches(not(withText(R.string.keyboard))));
            expressiveIconStateForHelpRestIcons();
        }
    }

    @Test
    public void _07_06validateBackButtonTapExpressiveIconRetainHelpSafetyIcon(){
        final int itemPos = 15;
        createHelpIntentLaunchActivity();
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.ivback)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(not(withText(R.string.keyboard))));
        expressiveIconStateForHelpSafetyIcon();
    }


    @Test
    public void _08_00validateAdapterClass() {
        activityRule.getActivity().finish();
        int helpPos = 5;
        String category = "People";
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag), helpPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag),category + "/");
        activityRule.launchActivity(intent);
        assert activityRule.getActivity().mRecyclerView.getAdapter() instanceof PeopleAdapter;
        activityRule.getActivity().finish();

        helpPos = 7;
        category = "Time And Weather";
        intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag), helpPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag),category + "/");
        activityRule.launchActivity(intent);
        assert activityRule.getActivity().mRecyclerView.getAdapter() instanceof LevelTwoAdapter;
    }


    private void createHelpIntentLaunchActivity(){
        activityRule.getActivity().finish();
        final int helpPos = 8;
        final String cat = "Help";
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),
                helpPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag),
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