package com.dsource.idc.jellowintl;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utils.ToastMatcher;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _04_ProfileFormActivityTest {
    @Rule
    public ActivityTestRule<ProfileFormActivity> activityRule =
            new ActivityTestRule<>(ProfileFormActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setUserCountryCode("91");
        getSession().setCaregiverNumber("9653238072");
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
        getSession().setUserCountryCode("");
        getSession().setToastMessage("");
    }

    @Test
    public void _01validateUserName(){
        onView(withId(R.id.parentScroll)).perform(swipeDown());
        onView(ViewMatchers.withId(R.id.etName)).perform(clearText());
        onView(withId(R.id.bSave)).perform(click());
        onView(withText(R.string.enterTheName)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void _02validateCaregiverNumber(){
        try {
            onView(withId(R.id.etName)).perform(clearText(), typeText("Akash"),
                    closeSoftKeyboard());
            onView(withId(R.id.etFathercontact)).perform(clearText());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.enternonemptycontact)).inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etFathercontact)).perform(typeText("("), closeSoftKeyboard());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.enternonemptycontact)).inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etFathercontact)).perform(clearText(),
                    typeText("number in text"),closeSoftKeyboard());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.enternonemptycontact))
                    .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etFathercontact)).perform(
                    clearText(),
                    typeText("5464+4563"),
                    closeSoftKeyboard());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.enternonemptycontact))
                    .inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _03validateEmailId(){
        try {
            onView(withId(R.id.etName)).perform(typeText("Akash"), closeSoftKeyboard());
            onView(withId(R.id.etFathercontact)).perform(
                    typeText("9653238072"), closeSoftKeyboard());
            onView(withId(R.id.etEmailId)).perform(clearText());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.invalid_emailId)).inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmailId)).perform(typeText(
                    "jellowcommunicatorgmail.com"), closeSoftKeyboard());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.invalid_emailId)).inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmailId)).perform(clearText(), typeText(
                    "jellowcommunicator@gmailcom"), closeSoftKeyboard());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.invalid_emailId)).inRoot(withDecorView(not(is(
                    activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmailId)).perform(clearText(), typeText(
                    "jellowcommunicator@gmail"), closeSoftKeyboard());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.invalid_emailId)).inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmailId)).perform(clearText(), typeText(
                    "@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.invalid_emailId)).inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _04validateDataSave(){
        //Fill form data
        try {
            onView(withId(R.id.etName)).perform(clearText(), typeText("Akash"),
                    closeSoftKeyboard());
            onView(withId(R.id.etFathercontact)).perform(clearText(),
                    typeText("9653238072"),
                    closeSoftKeyboard());
            onView(withId(R.id.etEmailId)).perform(click(), clearText(), typeText(
                    "jellowcommunicator@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.radioTherapist)).perform(click(), closeSoftKeyboard());
            onView(withId(R.id.bSave)).perform(click());
            onView(withText(R.string.checkConnectivity)).
                    inRoot(new ToastMatcher()).check(matches(not(isDisplayed())));
        }catch (Exception e){
            return;
        }
    }
}