package com.dsource.idc.jellowintl;

import android.content.Context;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.utils.ToastMatcher;

import org.junit.Before;
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
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.deletePackageZipFile;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _05_LanguageSelectActivityTest {
    private Context mContext;
    private SessionManager mSession;
    @Rule
    public ActivityTestRule<LanguageSelectActivity> activityRule =
            new ActivityTestRule<>(LanguageSelectActivity.class, false, false);

    @Before
    public void setup(){
        mContext = getInstrumentation().getTargetContext();
        mSession = new SessionManager(mContext);
        mSession.setCaregiverNumber("9653238072");
        mSession.setLanguage(ENG_IN);
        mSession.setGridSize(4);
        mSession.setWifiOnlyBtnPressedOnce(false);
        copyAssetsToInternalStorage(mContext, ENG_IN);
        extractLanguagePackageZipFile(mContext, ENG_IN);
        mSession.setDownloaded(ENG_IN);
        activityRule.launchActivity(null);
    }

    @Test
    public void _01_validateSaveLanguageTest(){
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.saveBut)).perform(click());
        onView(withText(R.string.txt_save_same_lang_def)).inRoot(new ToastMatcher()).
                check(matches(isDisplayed()));
        try{
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        copyAssetsToInternalStorage(mContext, ENG_AU);
        extractLanguagePackageZipFile(mContext, ENG_AU);
        mSession.setDownloaded(ENG_AU);
        activityRule.finishActivity();
        activityRule.launchActivity(null);
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.saveBut)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deletePackageZipFile(mContext, ENG_AU);
        mSession.setRemoved(ENG_AU);
        assertTrue(activityRule.getActivity().isDestroyed());
    }

    @Test
    public void _02_validateDeleteExistingLanguageTest(){
        onView(withId(R.id.delBut)).perform(click());
        onView(withText(R.string.no_more_lang_2_del)).inRoot(new ToastMatcher()).
                check(matches(isDisplayed()));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mSession.setDownloaded(ENG_AU);
        activityRule.finishActivity();
        activityRule.launchActivity(null);
        onView(withId(R.id.delBut)).perform(click());
        onView(withText("English (AU)")).perform(click());
        onView(withText(R.string.remove)).perform(click());
        onView(withText(R.string.languageRemoved)).inRoot(new ToastMatcher()).
                check(matches(isDisplayed()));
        onView(withId(R.id.addBut)).check(matches(isEnabled()));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mSession.setRemoved(ENG_AU);
    }

    @Test
    public void _03_validateAddNewLanguageTest(){
        mSession.setWifiOnlyBtnPressedOnce(false);
        onView(withId(R.id.addBut)).perform(click());
        onView(withText(R.string.disable_wifi_only)).inRoot(new ToastMatcher()).
                check(matches(isDisplayed()));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mSession.setWifiOnlyBtnPressedOnce(true);
        onView(withId(R.id.addBut)).perform(click());
        onView(withText(R.string.cancel)).perform(click());
        onView(withId(R.id.delBut)).check(matches(isEnabled()));
        onView(withId(R.id.addBut)).perform(click());
        onView(withText(R.string.downloadableLang)).check(matches(isDisplayed()));
        onView(withText("English (UK)")).perform(click());
        onView(withText(R.string.download)).perform(click());
        String str = mContext.getString(R.string.language_downloading)
                .replace("_","English (UK)");
        onView(withText(str)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deletePackageZipFile(mContext, ENG_UK);
        mSession.setRemoved(ENG_UK);
    }

    @Test
    public void _04_validateUiForNonTtsLanguage(){
        mSession.setDownloaded(MR_IN);
        activityRule.finishActivity();
        activityRule.launchActivity(null);
        onView(withId(R.id.parentScroll)).perform(swipeUp());
        onView(withId(R.id.tv4)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivTtsVoiceDat)).check(matches(not(isDisplayed())));
        onView(withId(R.id.btnDownloadVoiceData)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tv5)).check(matches(not(isDisplayed())));
        onView(withId(R.id.llImg)).check(matches(not(isDisplayed())));
        onView(withId(R.id.changeTtsLangBut)).check(matches(not(isDisplayed())));
        mSession.setRemoved(MR_IN);
    }
}