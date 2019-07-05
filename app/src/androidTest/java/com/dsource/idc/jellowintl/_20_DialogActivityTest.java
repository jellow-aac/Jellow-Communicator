package com.dsource.idc.jellowintl;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _20_DialogActivityTest {
    @Rule
    public ActivityTestRule<DialogActivity> activityRule =
            new ActivityTestRule<>(DialogActivity.class);

    @Test
    public void dialogTest(){
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityRule.getActivity().showCustomDialog(activityRule.getActivity());
            }
        });
        onView(withId(R.id.enterCategory)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void dialogTest2(){
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityRule.getActivity().showCustomDialog(activityRule.getActivity());
            }
        });
        onView(withId(R.id.enterCategory)).check(matches(isDisplayed()));
    }

    @Test
    public void dialogTest3(){
        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityRule.getActivity().showCustomDialog(activityRule.getActivity());
            }
        });
        onView(withId(R.id.enterCategory))
            .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}
