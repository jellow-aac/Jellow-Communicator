package com.dsource.idc.jellowintl;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.DataBaseHelper;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.deletePackageZipFile;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getContext;
import static com.dsource.idc.jellowintl.utils.TestClassUtils.getSession;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class _04_SplashActivityTest {

    @Rule
    public ActivityTestRule<SplashActivity> activityRule =
            new ActivityTestRule<>(SplashActivity.class);

    @BeforeClass
    public static void setup(){
        getSession().setCaregiverNumber("9653238072");
        getSession().setLanguage(ENG_IN);
        getSession().setGridSize(4);
        getSession().setWifiOnlyBtnPressedOnce(false);
        copyAssetsToInternalStorage(getContext(), ENG_IN);
        extractLanguagePackageZipFile(getContext(), ENG_IN);
        getSession().setDownloaded(ENG_IN);
        getSession().setToastMessage("");
    }

    @AfterClass
    public static void cleanUp(){
        getSession().setCaregiverNumber("");
        deletePackageZipFile(getContext(), ENG_IN);
        deletePackageZipFile(getContext(), ENG_AU);
        getSession().setRemoved(ENG_IN);
        getSession().setRemoved(ENG_AU);
        getSession().setRemoved(MR_IN);
        getSession().setWifiOnlyBtnPressedOnce(false);
    }

    @Test
    public void _01_validateDb() {
        DataBaseHelper dBHelper = new DataBaseHelper(getContext());
        assert dBHelper.checkDataBase();
    }
}