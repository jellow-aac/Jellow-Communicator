package com.dsource.idc.jellowintl;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
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
public class _09_MainActivityUITest {
    private Context mContext;
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void setup(){
        mContext = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(mContext);
        manager.setCaregiverNumber("9653238072");
        manager.setLanguage(ENG_IN);
        manager.setGridSize(4);
        copyAssetsToInternalStorage(mContext, ENG_IN);
        extractLanguagePackageZipFile(mContext, ENG_IN);
        activityRule.launchActivity(null);
        Espresso.closeSoftKeyboard();
    }

    @Test
    public void _01_01validateActionBarTitleEvent(){
        onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        String title = ((TextView)view.findViewById(R.id.te1)).getText().toString();
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(title.replace("…",""))));
        onView(withId(R.id.ivhome)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.home)));
        onView(withId(R.id.keyboard)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.keyboard)));
    }

    @Test
    public void _01_02validateTappedCategoryItemEvent(){
        Random randomGenerator = new Random();
        int itemPos = randomGenerator.nextInt(9);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        View view = activityRule.getActivity().mRecyclerView.getChildAt(itemPos);
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

    @Test
    public void _03_01validateHomeButtonTapEvent() {
        onView(withId(R.id.ivhome)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.home)));
        int count = activityRule.getActivity().mRecyclerView.getChildCount();
        for (int i=0;i<count;i++){
            View view = activityRule.getActivity().mRecyclerView.getChildAt(i);
            GradientDrawable gd = (GradientDrawable) view.
                    findViewById(R.id.borderView).getBackground();
            assert gd.equals(activityRule.getActivity().
                    getDrawable(R.drawable.border_drawable_nocolor_one_icon));
        }

        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home_pressed)));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
    }

    @Test
    public void _04_01validateKeyboardButtonTapEvent(){
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard_pressed)));
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.keyboard)));
        onView(withId(R.id.ivback)).check(matches(isEnabled()));
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
        onView(withId(R.id.ivback)).check(matches(not(isEnabled())));
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
    public void _05_01validateBackButtonTapEvent(){
        onView(withId(R.id.ivback)).check(matches(not(isEnabled())));
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.ivback)).check(matches(isEnabled()));
        onView(withId(R.id.ivback)).perform(click());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard)));
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
        onView(withId(R.id.ivback)).check(matches(not(isEnabled())));
    }

    @Test
    public void _08_00validateVisibleActivity(){
        activityRule.getActivity().setVisibleAct(MainActivity.class.getSimpleName());
        assert activityRule.getActivity().getVisibleAct().
                equals(MainActivity.class.getSimpleName());
    }

    @Test
    public void _09_00authenticationTest(){
        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            activityRule.getActivity().finish();
            activityRule.launchActivity(null);
            Thread.sleep(1500);
            if(activityRule.getActivity().isConnectedToNetwork(
                    (ConnectivityManager) activityRule.getActivity().
                            getSystemService(Context.CONNECTIVITY_SERVICE))){
                assert mAuth.getCurrentUser() != null;
            }else {
                onView(withId(com.google.android.material.R.id.snackbar_text))
                        .check(matches(withText(R.string.checkConnectivity)));
                assert true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert false;
    }

    @Test
    public void _10_00firstBreadCrumb(){
        assert activityRule.getActivity().getActionBar().getTitle().
                equals(activityRule.getActivity().getString(R.string.action_bar_title));
    }

    @Test
    public void _11_00sessionManagerTest() {
        assert activityRule.getActivity().getSession() != null;
    }
}
