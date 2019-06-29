package com.dsource.idc.jellowintl;

import androidx.test.espresso.Espresso;
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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.deletePackageZipFile;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getContext;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _05_LanguageSelectActivityTest {
    @Rule
    public ActivityTestRule<LanguageSelectActivity> activityRule =
            new ActivityTestRule<>(LanguageSelectActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
        getSession().setGridSize(4);
        getSession().setWifiOnlyBtnPressedOnce(false);
        copyAssetsToInternalStorage(getContext(), ENG_IN);
        extractLanguagePackageZipFile(getContext(), ENG_IN);
        getSession().setDownloaded(ENG_IN);
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
        deletePackageZipFile(getContext(), ENG_IN);
        deletePackageZipFile(getContext(), ENG_UK);
        getSession().setRemoved(ENG_IN);
        getSession().setRemoved(ENG_UK);
        getSession().setRemoved(MR_IN);
        getSession().setWifiOnlyBtnPressedOnce(false);
    }

    @Test
    public void _01_validateSaveLanguageTest(){
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.saveBut)).perform(click());
        onView(withText(R.string.txt_save_same_lang_def)).inRoot(new ToastMatcher()).
                check(matches(isDisplayed()));
        try{
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        copyAssetsToInternalStorage(getContext(), ENG_AU);
        extractLanguagePackageZipFile(getContext(), ENG_AU);
        getSession().setDownloaded(ENG_AU);
        activityRule.finishActivity();
        activityRule.launchActivity(null);
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.saveBut)).perform(click());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(activityRule.getActivity().isDestroyed());
    }

    @Test
    public void _02_validateDeleteExistingLanguageTest(){
        onView(withId(R.id.delBut)).perform(click());
        onView(withText("English (IN)")).perform(click());
        onView(withText(R.string.remove)).perform(click());
        onView(withText(R.string.languageRemoved)).inRoot(new ToastMatcher()).
                check(matches(isDisplayed()));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.addBut)).check(matches(isEnabled()));
        activityRule.finishActivity();
        activityRule.launchActivity(null);
        onView(withId(R.id.delBut)).perform(click());
        onView(withText(R.string.no_more_lang_2_del)).inRoot(new ToastMatcher()).
                check(matches(isDisplayed()));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void _03_validateAddNewLanguageTest(){
        getSession().setWifiOnlyBtnPressedOnce(false);
        onView(withId(R.id.addBut)).perform(click());
        onView(withText(R.string.disable_wifi_only)).inRoot(new ToastMatcher()).
                check(matches(isDisplayed()));
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getSession().setWifiOnlyBtnPressedOnce(true);
        activityRule.finishActivity();
        activityRule.launchActivity(null);
        onView(withId(R.id.addBut)).perform(click());
        onView(withText(R.string.cancel)).perform(click());
        onView(withId(R.id.delBut)).check(matches(isEnabled()));
        onView(withId(R.id.addBut)).perform(click());
        onView(withText(R.string.downloadableLang)).check(matches(isDisplayed()));
        onView(withText("English (IN)")).perform(click());
        onView(withText(R.string.download)).perform(click());
        String str = getContext().getString(R.string.language_downloading)
                .replace("_","English (IN)");
        try{
            onView(withText(str)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        deletePackageZipFile(getContext(), ENG_IN);
        getSession().setRemoved(ENG_IN);
    }

    @Test
    public void _04_validateUiForNonTtsLanguage(){
        getSession().setRemoved(ENG_IN);
        getSession().setRemoved(ENG_UK);
        getSession().setDownloaded(MR_IN);
        activityRule.finishActivity();
        activityRule.launchActivity(null);
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.tv4)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivTtsVoiceDat)).check(matches(not(isDisplayed())));
        onView(withId(R.id.btnDownloadVoiceData)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tv5)).check(matches(not(isDisplayed())));
        onView(withId(R.id.llImg)).check(matches(not(isDisplayed())));
        onView(withId(R.id.changeTtsLangBut)).check(matches(not(isDisplayed())));
    }
}