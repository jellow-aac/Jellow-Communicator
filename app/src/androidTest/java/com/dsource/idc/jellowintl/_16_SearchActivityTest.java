package com.dsource.idc.jellowintl;


import android.content.Intent;
import android.widget.TextView;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.core.IsInstanceOf;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.deletePackageZipFile;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getContext;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _16_SearchActivityTest {
    @Rule
    public ActivityTestRule<SplashActivity> activityRule =
            new ActivityTestRule<>(SplashActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
        getSession().setGridSize(4);
        copyAssetsToInternalStorage(getContext(), ENG_IN);
        extractLanguagePackageZipFile(getContext(), ENG_IN);
        getSession().setDownloaded(ENG_IN);
        getSession().setToastMessage("");
        getSession().setLanguageChange(0);
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
        deletePackageZipFile(getContext(), ENG_IN);
        getSession().setRemoved(ENG_IN);
        getSession().setLanguageChange(0);
    }

    @Test
    public void _01_searchInvalidInputTest(){
        String visibleAct = "";
        while (!visibleAct.equals(MainActivity.class.getSimpleName())){
            visibleAct = activityRule.getActivity().getVisibleAct();
            if (visibleAct == null)
                visibleAct = "";
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        activityRule.getActivity().startActivity(
                new Intent(activityRule.getActivity(),SearchActivity.class));
        onView(withId(R.id.search_auto_complete)).perform(typeText("asdfg"));
        onView(withId(R.id.search_icon_title)).check(matches(withText(R.string.icon_not_found)));
    }

    @Test
    public void _02_searchKeywordTest(){
        String visibleAct = "";
        while (!visibleAct.equals(MainActivity.class.getSimpleName())){
            visibleAct = activityRule.getActivity().getVisibleAct();
            if (visibleAct == null)
                visibleAct = "";
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        activityRule.getActivity().startActivity(
                new Intent(activityRule.getActivity(),SearchActivity.class));
        onView(withId(R.id.search_auto_complete)).perform(typeText("apple"));
        onData(allOf(is(instanceOf(String.class)), containsString("Apple")));
    }

    @Test
    public void _03_searchTapLevelOneTest(){
        String visibleAct = "";
        while (!visibleAct.equals(MainActivity.class.getSimpleName())){
            visibleAct = activityRule.getActivity().getVisibleAct();
            if (visibleAct == null)
                visibleAct = "";
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        activityRule.getActivity().startActivity(
                new Intent(activityRule.getActivity(),SearchActivity.class));
        onView(withId(R.id.search_auto_complete)).perform(typeText("help"));
        onView(withId(R.id.icon_search_recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, click()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(IsInstanceOf.instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Home")));
    }

    @Test
    public void _04_searchTapLevelTwoTest(){
        String visibleAct = "";
        while (!visibleAct.equals(MainActivity.class.getSimpleName())){
            visibleAct = activityRule.getActivity().getVisibleAct();
            if (visibleAct == null)
                visibleAct = "";
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        activityRule.getActivity().startActivity(
                new Intent(activityRule.getActivity(),SearchActivity.class));
        onView(withId(R.id.search_auto_complete)).perform(typeText("medi"));
        onView(withId(R.id.icon_search_recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, click()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(IsInstanceOf.instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Help…/")));
    }

    @Test
    public void _05_searchTapLevelThreeTest(){
        String visibleAct = "";
        while (!visibleAct.equals(MainActivity.class.getSimpleName())){
            visibleAct = activityRule.getActivity().getVisibleAct();
            if (visibleAct == null)
                visibleAct = "";
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        activityRule.getActivity().startActivity(
                new Intent(activityRule.getActivity(),SearchActivity.class));
        onView(withId(R.id.search_auto_complete)).perform(typeText("pear"));
        onView(withId(R.id.icon_search_recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(0, click()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(IsInstanceOf.instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText("Eating…/Fruits/")));
    }
}
