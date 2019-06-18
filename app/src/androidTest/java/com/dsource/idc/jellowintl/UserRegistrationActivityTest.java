package com.dsource.idc.jellowintl;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.rule.ActivityTestRule;

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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public class UserRegistrationActivityTest {

    @Rule
    public ActivityTestRule<UserRegistrationActivity> activityRule =
            new ActivityTestRule<>(UserRegistrationActivity.class);

    @Test
    public void validateUserName(){
        onView(withId(R.id.etEmergencyContact)).perform(typeText(
                generateRandomStringOf("numbers")), closeSoftKeyboard());
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.etEmailId)).perform(
                typeText("jellowcommunicator@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.radioTherapist)).perform(click());
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.bRegister)).perform(click());
        onView(withText(R.string.enterTheName))
                .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void validateCaregiverNumber(){
        try {
            //Check if mobile number is empty
            onView(withId(R.id.etName)).perform(typeText("Akash"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.etEmailId)).perform(
                    typeText("jellowcommunicator@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.radioTherapist)).perform(click());
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateEmail(){
        try {
            //Check if email id is not empty.
            onView(withId(R.id.etName)).perform(typeText("Akash"), closeSoftKeyboard());
            onView(withId(R.id.etEmergencyContact)).perform(typeText(
                    generateRandomStringOf("numbers")), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.radioTherapist)).perform(click());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(
                            is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));

            //Wait for Toast to disappear
            Thread.sleep(1500);
            //Check if email id do not have @ symbol
            onView(withId(R.id.parentScroll)).perform(swipeDown());
            onView(withId(R.id.etEmailId)).perform(clearText(),
                    typeText("jellowcommunicatorgmail.com"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp(), swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(
                            is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));

            Thread.sleep(1500);
            //Check if email id do not have domain name
            onView(withId(R.id.parentScroll)).perform(swipeDown());
            onView(withId(R.id.etEmailId)).perform(clearText(),
                    typeText("jellowcommunicator@gmailcom"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp(), swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(
                            is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));

            Thread.sleep(1500);
            //Check if email id do not have user name
            onView(withId(R.id.parentScroll)).perform(swipeDown());
            onView(withId(R.id.etEmailId)).perform(clearText(),
                    typeText("@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp(), swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            onView(withText(R.string.invalid_emailId))
                    .inRoot(withDecorView(not(
                            is(activityRule.getActivity().getWindow().getDecorView()))))
                    .check(matches(isDisplayed()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validateUserGroup(){
        //Check if no user group Selected is not empty.
        onView(withId(R.id.etName)).perform(typeText("Akash"), closeSoftKeyboard());
        onView(withId(R.id.etEmergencyContact)).perform(typeText(
                generateRandomStringOf("numbers")), closeSoftKeyboard());
        onView(withId(R.id.parentScroll)).perform(customSwipeUp());
        onView(withId(R.id.etEmailId)).perform(click(), typeText(
                "jellowcommunicator@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.parentScroll)).perform(customSwipeUp());

        onView(withId(R.id.bRegister)).perform(click());
        onView(withText(R.string.invalid_usergroup))
                .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

   /* @Test
    public void validateAppRegistrationProcess(){
        //Fill form data
        /*try {
            onView(withId(R.id.etName)).perform(typeText("Akash"), closeSoftKeyboard());
            onView(withId(R.id.etEmergencyContact)).perform(typeText(
                    generateRandomStringOf("numbers")), closeSoftKeyboard());
            onView(withId(R.id.etEmailId)).perform(
                    typeText("jellowcommunicator@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.radioTherapist)).perform(click(), closeSoftKeyboard());
            onView(withId(R.id.parentScroll)).perform(swipeUp());
            onView(withId(R.id.bRegister)).perform(click());
            Thread.sleep(1000);
            try {
                onView(withText(R.string.checkConnectivity))
                        .inRoot(withDecorView(not(is(activityRule.getActivity().getWindow().getDecorView()))))
                        .check(matches(isDisplayed()));
            }catch (Exception e){
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }}*/


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

    public static ViewAction customSwipeUp() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.BOTTOM_CENTER,
                GeneralLocation.TOP_CENTER, Press.FINGER);
    }
}
