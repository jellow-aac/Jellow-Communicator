package com.dsource.idc.jellowintl;

import androidx.test.filters.LargeTest;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LevelThreeActivityUITest {
    /*private Context mContext;
    @Rule
    public ActivityTestRule<LevelThreeActivity> activityRule =
            new ActivityTestRule<>(LevelThreeActivity.class, false, false);

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
    public void _01_01validateActionBarTitleEvent(){
        Random randomGenerator = new Random();
        int itemPos = randomGenerator.nextInt(9);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        View view = activityRule.getActivity().mRecyclerView.getChildAt(itemPos);
        String title = ((TextView)view.findViewById(R.id.te1)).getText().toString();
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(title.replace("…",""))));
        onView(withId(R.id.ivhome)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.home)));
        onView(withId(R.id.keyboard)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.keyboard)));
    }

    @Test
    public void _01_02validateTappedCategoryItemEvent(){
        Random randomGenerator = new Random();
        int itemPos = randomGenerator.nextInt(9);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(itemPos, click()));
        View view = activityRule.getActivity().mRecyclerView.getChildAt(itemPos);
        GradientDrawable gd = (GradientDrawable) view.findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(ContextCompat.getColor(mContext, R.color.colorSelect));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home)));
    }

    @Test
    public void _02_1validateExpressiveLikeButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivlike)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorLike);
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like_pressed)));
    }

    @Test
    public void _02_2validateExpressiveDontLikeButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivdislike)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorDontLike);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike_pressed)));
    }

    @Test
    public void _02_3validateExpressiveYesButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivyes)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorYes);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes_pressed)));
    }

    @Test
    public void _02_4validateExpressiveNoButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivno)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorNo);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no_pressed)));
    }

    @Test
    public void _02_5validateExpressiveMoreButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivadd)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorMore);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more_pressed)));
    }

    @Test
    public void _02_6validateExpressiveLessButtonTapEvent() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.
                actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
        onView(withId(R.id.ivminus)).perform(click());
        View view = activityRule.getActivity().mRecyclerView.getChildAt(3);
        GradientDrawable gd = (GradientDrawable) view.
                findViewById(R.id.borderView).getBackground();
        assert gd.getColor().equals(R.color.colorLess);
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less_pressed)));
    }

    @Test
    public void _03_01validateHomeButtonTapEvent() {
        onView(withId(R.id.ivhome)).perform(click());
        onView(allOf(instanceOf(TextView.class),
                withParent(withResourceName("action_bar"))))
                .check(matches(withText(R.string.home)));
        int count = activityRule.getActivity().mRecyclerView.getChildCount();
        for (int i=0;i<count;i++){
            View view = activityRule.getActivity().mRecyclerView.getChildAt(i);
            GradientDrawable gd = (GradientDrawable) view.
                    findViewById(R.id.borderView).getBackground();
            assert gd.equals(activityRule.getActivity().
                    getDrawable(R.drawable.border_drawable_nocolor_one_icon));
        }

        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home_pressed)));
        onView(withId(R.id.ivlike)).check(matches(withDrawable(R.drawable.like)));
        onView(withId(R.id.ivyes)).check(matches(withDrawable(R.drawable.yes)));
        onView(withId(R.id.ivadd)).check(matches(withDrawable(R.drawable.more)));
        onView(withId(R.id.ivdislike)).check(matches(withDrawable(R.drawable.dontlike)));
        onView(withId(R.id.ivno)).check(matches(withDrawable(R.drawable.no)));
        onView(withId(R.id.ivminus)).check(matches(withDrawable(R.drawable.less)));
    }

    @Test
    public void _04_01validateKeyboardButtonTapEvent(){
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard_pressed)));
        onView(withId(R.id.ivback)).check(matches(isEnabled()));
        onView(withId(R.id.ttsbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.et)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.ivlike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivyes)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivadd)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivdislike)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivno)).check(matches(not(isEnabled())));
        onView(withId(R.id.ivminus)).check(matches(not(isEnabled())));

        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard)));
        onView(withId(R.id.ivback)).check(matches(not(isEnabled())));
        onView(withId(R.id.ttsbutton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.et)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
    }

    @Test
    public void _05_01validateBackButtonTapEvent(){
        onView(withId(R.id.ivback)).check(matches(not(isEnabled())));
        onView(withId(R.id.keyboard)).perform(click());
        onView(withId(R.id.et)).perform(closeSoftKeyboard());
        onView(withId(R.id.ivback)).check(matches(isEnabled()));
        onView(withId(R.id.ivback)).perform(click());
        onView(withId(R.id.keyboard)).check(matches(withDrawable(R.drawable.keyboard)));
        onView(withId(R.id.ivhome)).check(matches(withDrawable(R.drawable.home)));
        onView(withId(R.id.ttsbutton)).check(matches(not(isDisplayed())));
        onView(withId(R.id.et)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.ivlike)).check(matches(isEnabled()));
        onView(withId(R.id.ivyes)).check(matches(isEnabled()));
        onView(withId(R.id.ivadd)).check(matches(isEnabled()));
        onView(withId(R.id.ivdislike)).check(matches(isEnabled()));
        onView(withId(R.id.ivno)).check(matches(isEnabled()));
        onView(withId(R.id.ivminus)).check(matches(isEnabled()));
        onView(withId(R.id.ivback)).check(matches(not(isEnabled())));
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
    }*/
}
