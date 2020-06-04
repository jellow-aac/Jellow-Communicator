package com.dsource.idc.jellowintl;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseActivityTest {
    @Rule
    public ActivityTestRule<BaseActivity> activityRule =
            new ActivityTestRule<>(BaseActivity.class);

    @BeforeClass
    public static void setup(){
        /*getSession().setUserCountryCode("91");
        getSession().setCaregiverNumber("9653238072");*/
    }

    @AfterClass
    public static void cleanUp(){
        /*getSession().setCaregiverNumber("");
        getSession().setUserCountryCode("");
        getSession().setToastMessage("");*/
    }

    @Test
    public void _01validateVisibleActivity(){
        activityRule.getActivity().setVisibleAct(MainActivity.class.getName());
        assert activityRule.getActivity().getVisibleAct().equals(MainActivity.class.getName());
    }
}