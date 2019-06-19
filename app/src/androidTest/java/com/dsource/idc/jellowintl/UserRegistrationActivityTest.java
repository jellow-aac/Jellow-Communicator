package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@LargeTest
public class UserRegistrationActivityTest {
    private SessionManager manager;
    @Rule
    public ActivityTestRule<UserRegistrationActivity> activityRule =
            new ActivityTestRule<>(UserRegistrationActivity.class, false, false);

    @Before
    public void setup(){
        Context context = getInstrumentation().getTargetContext();
        manager = new SessionManager(context);
        manager.setUserLoggedIn(false);
        activityRule.launchActivity(null);
    }

    @Test
    public void validateUserName(){
        try {
            onView(withId(R.id.etName)).perform(clearText());
            onView(withId(R.id.etEmergencyContact)).perform(clearText(), typeText(generateRandomStringOf
                    ("numbers")), closeSoftKeyboard(), swipeUp(), swipeUp(), swipeUp());
            onView(withId(R.id.etEmailId)).perform(click(), clearText(), typeText
                    ("jellowcommunicator@gmail.com"), closeSoftKeyboard(), swipeUp(),
                    swipeUp(), swipeUp());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.radioParent)).perform(click());
            Thread.sleep(500);
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.enterTheName))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            manager.setUserLoggedIn(false);
            activityRule.finishActivity();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateCaregiverNumber(){
        try {
            //Check if mobile number is empty
            onView(withId(R.id.etName)).perform(clearText(), typeText("Akash"),
                    closeSoftKeyboard());
            onView(withId(R.id.etEmergencyContact)).perform(clearText(),
                    swipeUp(), swipeUp(), swipeUp());
            onView(withId(R.id.etEmailId)).perform(click(), clearText(),
                    typeText("jellowcommunicator@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.radioParent)).perform(click());
            Thread.sleep(500);
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.enternonemptycontact))
                    .inRoot(withDecorView(not(
                            is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));

            //Wait for Toast to disappear
            Thread.sleep(1500);
            //Check if mobile number do not contain special symbols
            onView(withId(R.id.parentScroll)).perform(swipeDown(), swipeDown());
            onView(withId(R.id.etEmergencyContact)).perform(clearText(),
                    typeText(generateRandomStringOf("text")), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp(), swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.enternonemptycontact))
                    .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            manager.setUserLoggedIn(false);
            activityRule.finishActivity();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateEmail(){
        try {
            //Check if email id is not empty.
            onView(withId(R.id.etEmergencyContact)).perform(swipeUp(), swipeUp(), swipeUp());
            onView(withId(R.id.etEmailId)).perform(click(), clearText(),closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeDown());
            onView(withId(R.id.etName)).perform(clearText(), typeText("Akash"), closeSoftKeyboard());
            onView(withId(R.id.etEmergencyContact)).perform(clearText(), typeText(
                    generateRandomStringOf("numbers")), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.radioParent)).perform(click());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(
                            is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));

            //Wait for Toast to disappear
            Thread.sleep(1500);
            //Check if email id do not have @ symbol
            onView(withId(R.id.parentScroll)).perform(swipeDown(), swipeDown());
            onView(withId(R.id.etEmergencyContact)).perform(swipeUp(), swipeUp(), swipeUp());
            onView(withId(R.id.etEmailId)).perform(click(), clearText(),
                    typeText("jellowcommunicatorgmail.com"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp(), swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(
                            is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));

            Thread.sleep(1500);
            //Check if email id do not have domain name
            onView(withId(R.id.parentScroll)).perform(swipeDown(), swipeDown());
            onView(withId(R.id.etEmergencyContact)).perform(swipeUp(), swipeUp(), swipeUp());
            onView(withId(R.id.etEmailId)).perform(click(), clearText(),
                    typeText("jellowcommunicator@gmailcom"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp(), swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(
                            is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));

            Thread.sleep(1500);
            //Check if email id do not have user name
            onView(withId(R.id.parentScroll)).perform(swipeDown(), swipeDown());
            onView(withId(R.id.etEmergencyContact)).perform(swipeUp(), swipeUp(), swipeUp());
            onView(withId(R.id.etEmailId)).perform(click(), clearText(),
                    typeText("@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp(), swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(
                            is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));
            manager.setUserLoggedIn(false);
            activityRule.finishActivity();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateUserGroup(){
        //Check if no user group Selected is not empty.
        onView(withId(R.id.etName)).perform(clearText(), typeText("Akash"), closeSoftKeyboard());
        onView(withId(R.id.etEmergencyContact)).perform(clearText(), typeText(
                generateRandomStringOf("numbers")), closeSoftKeyboard(),
                swipeUp(), swipeUp(), swipeUp());
        onView(withId(R.id.etEmailId)).perform(clearText(), click(), typeText(
                "jellowcommunicator@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.parentScroll)).perform(swipeUp(), swipeUp());
        onView(withId(R.id.bRegister)).perform(click());
        onView(withText(R.string.invalid_usergroup))
                .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        manager.setUserLoggedIn(false);
        activityRule.finishActivity();
    }

    @Test
    public void validateAppRegistrationProcess(){
        //Fill form data
        try {
            onView(withId(R.id.etName)).perform(clearText(), typeText("Akash"), closeSoftKeyboard());
            onView(withId(R.id.etEmergencyContact)).perform(clearText(), typeText(
                    generateRandomStringOf("numbers")), closeSoftKeyboard(), swipeUp(),
                    swipeUp(), swipeUp());
            onView(withId(R.id.etEmailId)).perform(click(), clearText(), typeText(
                    "jellowcommunicator@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            Thread.sleep(500);
            onView(withId(R.id.radioTherapist)).perform(click());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            Thread.sleep(700);
            try {
                onView(withText(R.string.checkConnectivity))
                        .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                        .check(matches(isDisplayed()));
                manager.setUserLoggedIn(false);
                activityRule.finishActivity();
            }catch (Exception e){
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String generateRandomStringOf(String pattern){
        StringBuilder sb;
        final Random random = new Random();
        String specialSymbols = "~!@$#$%^&*()_+=-/|,.;':*+?>< N";
        String numbers = "0123456789";
        switch (pattern){
            case "text":
                sb = new StringBuilder(specialSymbols.length());
                for(int i = 0; i< specialSymbols.length(); i++)
                    sb.append(specialSymbols.charAt(random.nextInt(specialSymbols.length())));
                return sb.toString();
            case "numbers":
                int length = random.nextInt(30)+20;
                sb = new StringBuilder(length);
                for(int i=0; i<length; i++)
                    sb.append(numbers.charAt(random.nextInt(numbers.length())));
                return sb.toString();
            default:
                return "";
        }
    }
}
