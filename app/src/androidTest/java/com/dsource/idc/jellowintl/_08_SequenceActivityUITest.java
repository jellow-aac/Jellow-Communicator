package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.dsource.idc.jellowintl.utility.SessionManager;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utils.EspressoTestMatchers.withDrawable;
import static com.dsource.idc.jellowintl.utils.FileOperations.copyAssetsToInternalStorage;
import static com.dsource.idc.jellowintl.utils.FileOperations.extractLanguagePackageZipFile;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class _08_SequenceActivityUITest {
    private Context mContext;
    private final String l1Title = "Daily Activities";
    private final String l2Title = "Toilet";
    private final int levelOneItemPos = 1;
    private final int levelTwoItemPos = 1;


    ActivityTestRule<SequenceActivity> activityRule =
            new ActivityTestRule<>(SequenceActivity.class, false, false);

    @Before
    public void setup(){
        mContext = getInstrumentation().getTargetContext();
        SessionManager manager = new SessionManager(mContext);
        manager.setCaregiverNumber("9653238072");
        manager.setLanguage(ENG_IN);
        manager.setGridSize(4);
        copyAssetsToInternalStorage(mContext);
        extractLanguagePackageZipFile(mContext);
        launchActivityWithCustomIntent(levelOneItemPos, levelTwoItemPos,
                l1Title.concat("/ " + l2Title + "/ "));
        closeSoftKeyboard();
    }

    @Test
    public void _01_01validateActionBarTitleAndHeading(){
        onView(withId(R.id.tt1)).check(matches(withText(l2Title.toUpperCase())));
        onView(withId(R.id.image1)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(l1Title.concat("/ " + l2Title + "/ "))));
        onView(withId(R.id.tt1)).check(matches(withText(l2Title.toUpperCase())));
        onView(withId(R.id.keyboard)).perform(click(), closeSoftKeyboard());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.keyboard)));
        onView(withId(R.id.tt1)).check(matches(not(isDisplayed())));
        onView(withId(R.id.keyboard)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(l1Title.concat("/ " + l2Title + "/ "))));
        onView(withId(R.id.tt1)).check(matches(isDisplayed()));
        onView(withId(R.id.keyboard)).perform(click(), closeSoftKeyboard());
        onView(withId(R.id.ivback)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(l1Title.concat("/ " + l2Title + "/ "))));
        onView(withId(R.id.tt1)).check(matches(isDisplayed()));
        onView(withId(R.id.tt1)).check(matches(withText(l2Title.toUpperCase())));
    }


    @Test
    public void _02_01validateSequenceCategoryIconsSimpleTap(){
        int[] categoryIcons = {R.id.image1, R.id.image2, R.id.image3};
        int[] parent = {R.id.linearLayoutOne, R.id.linearLayoutTwo, R.id.linearLayoutThree};
        int[] borderView = {R.id.borderView1, R.id.borderView2, R.id.borderView3};

        for (int i = 0; i < 3; i++) {
            onView(withId(categoryIcons[i])).perform(click());
            View view = activityRule.getActivity().findViewById(parent[i]);
            GradientDrawable gd = (GradientDrawable) view.findViewById(borderView[i]).getBackground();
            assert gd.getColor().equals(ContextCompat.getColor(mContext, R.color.colorSelect));
            checkAllExpressiveIconVisible();
            onView(withId(categoryIcons[i])).perform(click());
            checkAllExpressiveIconInvisible();
        }
    }

    @Test
    public void _02_02validateSequenceCategoryIconsContinuesTap(){
        int[] categoryIcons = {R.id.image1, R.id.image2, R.id.image3};
        boolean expressiveHidden = false;
        Random random = new Random();
        int newRand, oldRand = -1;
        for (int i = 0; i < 15; i++) {
            newRand = random.nextInt(3);
            onView(withId(categoryIcons[newRand])).perform(click());
            if (oldRand == newRand && !expressiveHidden)
                expressiveHidden = checkAllExpressiveIconInvisible();
            else
                expressiveHidden = checkAllExpressiveIconVisible();
            oldRand = newRand;
        }
    }


    @Test
    public void _03_01validateExpressiveLikeButtonTapEvent() {
        int[] categoryIcons = {R.id.image1, R.id.image2, R.id.image3};
        int[] parent = {R.id.linearLayoutOne, R.id.linearLayoutTwo, R.id.linearLayoutThree};
        int[] borderView = {R.id.borderView1, R.id.borderView2, R.id.borderView3};

        for (int i = 0; i < 3; i++) {
            onView(withId(categoryIcons[i])).perform(click());
            onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
            onView(withId(R.id.ivlike)).perform(click());
            View view = activityRule.getActivity().findViewById(parent[i]);
            GradientDrawable gd = (GradientDrawable) view.findViewById(borderView[i]).getBackground();
            assert gd.getColor().equals(R.color.colorLike);
            onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
            onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
            onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
            onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
            onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
            onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like_pressed)));
        }
    }

    @Test
    public void _03_02validateExpressiveDontLikeButtonTapEvent() {
        int[] categoryIcons = {R.id.image1, R.id.image2, R.id.image3};
        int[] parent = {R.id.linearLayoutOne, R.id.linearLayoutTwo, R.id.linearLayoutThree};
        int[] borderView = {R.id.borderView1, R.id.borderView2, R.id.borderView3};

        for (int i = 0; i < 3; i++) {
            onView(withId(categoryIcons[i])).perform(click());
            onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
            onView(withId(R.id.ivdislike)).perform(click());
            View view = activityRule.getActivity().findViewById(parent[i]);
            GradientDrawable gd = (GradientDrawable) view.findViewById(borderView[i]).getBackground();
            assert gd.getColor().equals(R.color.colorDontLike);
            onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
            onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
            onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
            onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike_pressed)));
            onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
            onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        }
    }

    @Test
    public void _03_03validateExpressiveYesButtonTapEvent() {
        int[] categoryIcons = {R.id.image1, R.id.image2, R.id.image3};
        int[] parent = {R.id.linearLayoutOne, R.id.linearLayoutTwo, R.id.linearLayoutThree};
        int[] borderView = {R.id.borderView1, R.id.borderView2, R.id.borderView3};

        for (int i = 0; i < 3; i++) {
            onView(withId(categoryIcons[i])).perform(click());
            onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
            onView(withId(R.id.ivyes)).perform(click());
            View view = activityRule.getActivity().findViewById(parent[i]);
            GradientDrawable gd = (GradientDrawable) view.findViewById(borderView[i]).getBackground();
            assert gd.getColor().equals(R.color.colorYes);
            onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
            onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes_pressed)));
            onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
            onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
            onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
            onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        }
    }

    @Test
    public void _03_04validateExpressiveNoButtonTapEvent() {
        int[] categoryIcons = {R.id.image1, R.id.image2, R.id.image3};
        int[] parent = {R.id.linearLayoutOne, R.id.linearLayoutTwo, R.id.linearLayoutThree};
        int[] borderView = {R.id.borderView1, R.id.borderView2, R.id.borderView3};

        for (int i = 0; i < 3; i++) {
            onView(withId(categoryIcons[i])).perform(click());
            onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
            onView(withId(R.id.ivno)).perform(click());
            View view = activityRule.getActivity().findViewById(parent[i]);
            GradientDrawable gd = (GradientDrawable) view.findViewById(borderView[i]).getBackground();
            assert gd.getColor().equals(R.color.colorNo);
            onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
            onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
            onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
            onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
            onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no_pressed)));
            onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        }
    }

    @Test
    public void _03_05validateExpressiveMoreButtonTapEvent() {
        int[] categoryIcons = {R.id.image1, R.id.image2, R.id.image3};
        int[] parent = {R.id.linearLayoutOne, R.id.linearLayoutTwo, R.id.linearLayoutThree};
        int[] borderView = {R.id.borderView1, R.id.borderView2, R.id.borderView3};

        for (int i = 0; i < 3; i++) {
            onView(withId(categoryIcons[i])).perform(click());
            onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
            onView(withId(R.id.ivadd)).perform(click());
            View view = activityRule.getActivity().findViewById(parent[i]);
            GradientDrawable gd = (GradientDrawable) view.findViewById(borderView[i]).getBackground();
            assert gd.getColor().equals(R.color.colorMore);
            onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
            onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
            onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more_pressed)));
            onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
            onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
            onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        }
    }

    @Test
    public void _03_06validateExpressiveLessButtonTapEvent() {
        int[] categoryIcons = {R.id.image1, R.id.image2, R.id.image3};
        int[] parent = {R.id.linearLayoutOne, R.id.linearLayoutTwo, R.id.linearLayoutThree};
        int[] borderView = {R.id.borderView1, R.id.borderView2, R.id.borderView3};

        for (int i = 0; i < 3; i++) {
            onView(withId(categoryIcons[i])).perform(click());
            onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
            onView(withId(R.id.ivminus)).perform(click());
            View view = activityRule.getActivity().findViewById(parent[i]);
            GradientDrawable gd = (GradientDrawable) view.findViewById(borderView[i]).getBackground();
            assert gd.getColor().equals(R.color.colorLess);
            onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
            onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
            onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
            onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
            onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
            onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less_pressed)));
        }
    }


    @Test
    public void _04_01validateSequenceLoading() {
        final int[] textView = {R.id.bt1, R.id.bt2, R.id.bt3};
        String[] belowText = { "1. Close door" , "2. Pull pants down", "3. Sit on toilet",
                "4. Wash bottom", "5. Flush toilet", "6. Pull pants up",
                "7. Wash hands", "8. Open door", "9. All done" };
        String[] iconNames = { "0102020001SS", "0102020002SS", "0102020003SS",
                "0102020004SS", "0102020005SS", "0102020006SS",
                "0102020007SS", "0102020008SS", "0102020009SS"};
        int i = 0;
        do {
            checkIconImageAndBelowText(i, textView, belowText, iconNames);
            onView(withId(R.id.forward)).perform(click());
            i += 3;
        } while (i < 9);
        i-=3;
        do {
            checkIconImageAndBelowText(i, textView, belowText, iconNames);
            onView(withId(R.id.backward)).perform(click());
            i -= 3;
        } while (i > 0);
    }


    @Test
    public void _04_02validateSequenceCategoryLoading() {
        int[] post = {  0,  //Brushing
                        1,  //Toilet
                        2,  //Bathing
                        7,  //Morning routine
                        8}; //Bed Time routine
        String[] heading = {"Brushing", "Toilet"," Bathing", "Morning routine", " Bed Time routine" };

        for (int i = 0; i < 5; i++) {
            activityRule.finishActivity();
            launchActivityWithCustomIntent(1, post[i], "dummyTitle");
            assert ((TextView)activityRule.getActivity().
                    findViewById(R.id.tt1)).
                    getText().toString().toUpperCase().equals(heading[i].toUpperCase());
        }
    }


    @Test
    public void _05_00validateKeyboardButtonTapEvent(){
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard_pressed)));
        onView(allOf(IsInstanceOf.instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.keyboard)));
        onView(withId(R.id.ttsbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.et)).check(matches(isDisplayed()));
        onView(withId(R.id.image1)).check(matches(not(isDisplayed())));
        onView(withId(R.id.image2)).check(matches(not(isDisplayed())));
        onView(withId(R.id.image3)).check(matches(not(isDisplayed())));
        checkAllExpressiveIconDisabled();
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard)));
        onView(allOf(IsInstanceOf.instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(not(withText(R.string.keyboard))));
        onView(withId(R.id.image1)).check(matches(isDisplayed()));
        onView(withId(R.id.image2)).check(matches(isDisplayed()));
        onView(withId(R.id.image3)).check(matches(isDisplayed()));
        checkAllExpressiveIconEnabled();
    }


    @Test
    public void _06_00validateBackButtonTapUiTestEvent(){
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.ivback)).perform(click());
        onView(withId(R.id.ivback)).check(matches(withDrawable(R.drawable.back_pressed)));
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard)));
        onView(allOf(IsInstanceOf.instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(not(withText(R.string.keyboard))));
        onView(withId(R.id.image1)).check(matches(isDisplayed()));
        onView(withId(R.id.image2)).check(matches(isDisplayed()));
        onView(withId(R.id.image3)).check(matches(isDisplayed()));
        checkAllExpressiveIconEnabled();
    }


    private void checkIconImageAndBelowText(int pos, int[] textView, String[] belowText, String[] iconNames) {
        onView(withId(textView[0])).check(matches(withText(belowText[pos])));
        onView(withId(textView[1])).check(matches(withText(belowText[pos + 1])));
        onView(withId(textView[2])).check(matches(withText(belowText[pos + 2])));
        ImageView icon1 = activityRule.getActivity().findViewById(R.id.image1);
        Bitmap bmIcon1 = Bitmap.createBitmap(icon1.getDrawable().getIntrinsicWidth(),
                icon1.getDrawable().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        assert bmIcon1.sameAs(BitmapFactory.decodeFile(
                getIconPath(mContext, iconNames[pos] + ".png")));
        ImageView icon2 = activityRule.getActivity().findViewById(R.id.image2);
        Bitmap bmIcon2 = Bitmap.createBitmap(icon2.getDrawable().getIntrinsicWidth(),
                icon1.getDrawable().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        assert bmIcon2.sameAs(BitmapFactory.decodeFile(
                getIconPath(mContext, iconNames[pos+1] + ".png")));
        ImageView icon3 = activityRule.getActivity().findViewById(R.id.image3);
        Bitmap bmIcon3 = Bitmap.createBitmap(icon1.getDrawable().getIntrinsicWidth(),
                icon3.getDrawable().getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        assert bmIcon3.sameAs(BitmapFactory.decodeFile(
                getIconPath(mContext, iconNames[pos+2] + ".png")));
    }

    private boolean checkAllExpressiveIconVisible(){
        onView(withId(R.id.ivlike)).check(matches(isDisplayed()));
        onView(withId(R.id.ivyes)).check(matches(isDisplayed()));
        onView(withId(R.id.ivadd)).check(matches(isDisplayed()));
        onView(withId(R.id.ivdislike)).check(matches(isDisplayed()));
        onView(withId(R.id.ivno)).check(matches(isDisplayed()));
        onView(withId(R.id.ivminus)).check(matches(isDisplayed()));

        return false;
    }

    private boolean checkAllExpressiveIconInvisible(){
        onView(withId(R.id.ivlike)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivyes)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivadd)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivdislike)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivno)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivminus)).check(matches(not(isDisplayed())));
        return true;
    }

    private void checkAllExpressiveIconDisabled(){
        onView(withId(R.id.ivlike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivyes)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivadd)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivdislike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivno)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivminus)).check(matches(not(isEnabled())));
    }

    private void checkAllExpressiveIconEnabled() {
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }

    private void launchActivityWithCustomIntent(int levelOneItemPos, int levelTwoItemPos, String title) {
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag), levelOneItemPos);
        intent.putExtra(mContext.getString(R.string.level_2_item_pos_tag), levelTwoItemPos);
        intent.putExtra(mContext.getString(R.string.intent_menu_path_tag), title);
        activityRule.launchActivity(intent);
    }
}
