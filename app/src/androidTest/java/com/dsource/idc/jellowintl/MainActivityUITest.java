package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ir.mahdi.mzip.zip.ZipArchive;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.EspressoTestMatchers.withDrawable;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityUITest {
    private Context mContext;
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void setup(){
        mContext = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(mContext);
        manager.setCaregiverNumber("9653238072");
        manager.setLanguage(ENG_IN);
        manager.setGridSize(4);
        copyAssetsToInternalStorage(mContext);
        extractLanguagePackageZipFile(mContext);
        activityRule.launchActivity(null);
    }

    @Test
    public void _01validateTappedCategoryItemEvent(){
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(ContextCompat.getColor(mContext, R.color.colorSelect));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
    }

    @Test
    public void _02validateExpressiveLikeButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivlike)).perform(click());
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like_pressed)));
    }

    @Test
    public void _03validateExpressiveDontLikeButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivdislike)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike_pressed)));
    }

    @Test
    public void _04validateExpressiveYesButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivyes)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes_pressed)));
    }

    @Test
    public void _05validateExpressiveNoButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivno)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no_pressed)));
    }

    @Test
    public void _06validateExpressiveMoreButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivadd)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more_pressed)));
    }

    @Test
    public void _07validateExpressiveLessButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivminus)).perform(click());
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less_pressed)));
    }

    @Test
    public void _08validateVisibleActivity(){
        activityRule.getActivity().setVisibleAct(MainActivity.class.getSimpleName());
        assert activityRule.getActivity().getVisibleAct().
                equals(MainActivity.class.getSimpleName());
    }

    @Test
    public void _09authenticationTest(){
        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            activityRule.getActivity().finish();
            activityRule.launchActivity(null);
            Thread.sleep(1500);
            if(activityRule.getActivity().isConnectedToNetwork(
                    (ConnectivityManager) activityRule.getActivity().
                            getSystemService(Context.CONNECTIVITY_SERVICE))){
                assert mAuth.getCurrentUser() != null;
            }else {
                onView(withId(com.google.android.material.R.id.snackbar_text))
                        .check(matches(withText(R.string.checkConnectivity)));
                assert true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert false;
    }

    @Test
    public void _010firstBreadCrumb(){
        assert activityRule.getActivity().getActionBar().getTitle().
                equals(activityRule.getActivity().getString(R.string.action_bar_title));
    }

    @Test
    public void _011sessionManagerTest() {
        assert activityRule.getActivity().getSession() != null;
    }

    private void copyAssetsToInternalStorage(Context appContext) {
        Context testContext = getInstrumentation().getContext();
        AssetManager assets = testContext.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assets.open(ENG_IN+".zip", Context.MODE_PRIVATE);
            File outFile = appContext.getDir(ENG_IN, Context.MODE_PRIVATE);
            outFile.mkdir();
            out = new FileOutputStream(outFile+"/"+ENG_IN+".zip");
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out.flush();
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFile(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void extractLanguagePackageZipFile(Context context) {
        File en_dir = context.getDir(ENG_IN, Context.MODE_PRIVATE);
        ZipArchive.unzip(en_dir.getPath()+"/"+ENG_IN+".zip",en_dir.getPath(),"");
        File zip = new File(en_dir.getPath(),ENG_IN+".zip");
        if(zip.exists()) zip.delete();
    }
}
