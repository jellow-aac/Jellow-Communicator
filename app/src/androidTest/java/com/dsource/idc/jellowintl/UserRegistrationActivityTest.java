package com.dsource.idc.jellowintl;

import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@SmallTest
public class UserRegistrationActivityTest {
    @Rule
    public ActivityTestRule<UserRegistrationActivity> activityRule =
            new ActivityTestRule<>(UserRegistrationActivity.class);

    @Test
    public void validateUserName(){
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.bRegister)).perform(click());
        onView(withText(R.string.enterTheName))
                .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void validateContact(){
        try {
            onView(withId(R.id.etName)).perform(typeText("Akash"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.enternonemptycontact))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmergencyContact)).perform(typeText("("), closeSoftKeyboard());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.enternonemptycontact))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmergencyContact)).perform(
                    clearText(),
                    typeText("number in text"),
                    closeSoftKeyboard());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.enternonemptycontact))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmergencyContact)).perform(
                    clearText(),
                    typeText("5464+4563"),
                    closeSoftKeyboard());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.enternonemptycontact))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateEmailId(){
        try {
            onView(withId(R.id.etName)).perform(typeText("Akash"), closeSoftKeyboard());
            onView(withId(R.id.etEmergencyContact)).perform(typeText("9653238072"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmailId)).perform(typeText("jellowcommunicatorgmail.com"), closeSoftKeyboard());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmailId)).perform(clearText(), typeText("jellowcommunicator@gmailcom"), closeSoftKeyboard());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmailId)).perform(clearText(), typeText("jellowcommunicator@gmail"), closeSoftKeyboard());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
            onView(withId(R.id.etEmailId)).perform(clearText(), typeText("@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateUserGroup(){
        onView(withId(R.id.etName)).perform(typeText("Akash"), closeSoftKeyboard());
        onView(withId(R.id.etEmergencyContact)).perform(typeText("9653238072"), closeSoftKeyboard());
        onView(withId(R.id.etEmailId)).perform(typeText("jellowcommunicator@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.bRegister)).perform(click());
        onView(withText(R.string.invalid_usergroup))
                .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }
}