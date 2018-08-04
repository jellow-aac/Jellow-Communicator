package com.dsource.idc.jellowintl;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_DoubleClick;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.models.SeqActivityVerbiageModel;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;


/**
 * Created by ekalpa on 6/22/2016.
 */
public class SequenceActivity extends AppCompatActivity {
    private final boolean DISABLE_EXPR_BTNS = true;
    private final int MODE_PICTURE_ONLY = 1;

    /* This flags are used to identify respective expressive button is pressed either
      once or twice. eg. mFlgLike used to identify Like expressive button pressed once or twice.*/
    private int mFlgLike = 0, mFlgYes = 0, mFlgMore = 0, mFlgDontLike = 0, mFlgNo = 0,
            mFlgLess = 0;
    /* This flag identifies either hide/show expressive buttons.*/
    private int mFlgHideExpBtn = -1;
    /* This flag indicates keyboard is open or not, 0 indicates is not open.*/
    private int mFlgKeyboard = 0;
    /*This variable indicates index of category icon selected in level one*/
    private int mLevelTwoItemPos, count = 0;
    /*Image views which are visible on the layout such as six expressive buttons, below navigation
      buttons, speak button when keyboard is open and 3 category icons from left to right.*/
    private ImageView mIvLike, mIvDontLike, mIvMore, mIvLess, mIvYes, mIvNo,
            mIvHome, mIvKeyboard, mIvBack, mIvTTs, mIvCategoryIcon1,
            mIvCategoryIcon2, mIvCategoryIcon3;
    /*Input text view to speak custom text.*/
    private EditText mEtTTs;
    private KeyListener originalKeyListener;
    /* Heading, caption text for category icon respectively .*/
    private TextView mTvHeading, mTvCategory1Caption, mTvCategory2Caption, mTvCategory3Caption;
    private ImageView mIvArrowLeft, mIvArrowRight;
    /*Category icon parent view*/
    private RelativeLayout mRelativeLayCategory;
    /*navigation next, back button in category*/
    private Button mBtnNext, mBtnBack;
    private String mStrPath, mStrNext, mStrBack, mActionBarTitleTxt;
    String[] mCategoryIconText;
    /*Below array stores the speech text, below text, expressive button speech text, heading,
     navigation button speech text, category navigation text respectively.*/
    private String[] mCategoryIconSpeechText, mCategoryIconBelowText, mHeading, mExprBtnTxt,
            mNavigationBtnTxt, mCategoryNav;
    /*Below list stores the verbiage that are spoken when category icon + expression buttons
    pressed in conjunction*/
    private ArrayList<ArrayList<ArrayList<String>>> mSeqActSpeech;
    private SessionManager mSession;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.yellow_bg));
        // set bread crumb title from extra data from level two
        getSupportActionBar().setTitle(getIntent().getExtras()
                .getString(getString(R.string.intent_menu_path_tag)));

        mSession = new SessionManager(this);
        /*get position of category icon selected in level two*/
        mLevelTwoItemPos = getIntent().getExtras().getInt(getString(R.string.level_2_item_pos_tag));

        /* Sequence activity Morning Routine (original index is 7 in level 2) has new index 3, and
        * Bed time Routine (original index is 8 in level 2) has new index 4 */
        if(mLevelTwoItemPos == 7)
            mLevelTwoItemPos = 3;
        else if(mLevelTwoItemPos == 8)
            mLevelTwoItemPos = 4;

        // Get icon set directory path
        File en_dir = this.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
        mStrPath = en_dir.getAbsolutePath()+"/drawables";

        loadArraysFromResources();
        initializeLayoutViews();
        initializeViewListeners();
        setValueToViews();
        resetCategoryIconBorders();
        /*In sequence, expressive buttons are invisible initially*/
        hideExpressiveBtn(true);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            resetAnalytics(this, mSession.getCaregiverNumber().substring(1));
        }
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        // Start measuring user app screen timer .
        startMeasuring();
        if(!mSession.getToastMessage().isEmpty()) {
            Toast.makeText(this, mSession.getToastMessage(), Toast.LENGTH_SHORT).show();
            mSession.setToastMessage("");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(mSession.getSessionCreatedAt());
        mSession.setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer .
        stopMeasuring("SequenceActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSeqActSpeech = null;
        mCategoryIconText = null;
        mRelativeLayCategory = null;
        mCategoryIconSpeechText = null;
        mCategoryIconBelowText = null;
        mHeading = null;
        mExprBtnTxt = null;
        mNavigationBtnTxt = null;
        mCategoryNav = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main_with_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.search: startActivity(new Intent(this, SearchActivity.class));break;
            case R.id.languageSelect:
                startActivity(new Intent(this, LanguageSelectActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class)); break;
            case R.id.info:
                startActivity(new Intent(this, AboutJellowActivity.class));
                break;
            case R.id.usage:
                startActivity(new Intent(this, TutorialActivity.class));
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                break;
            case R.id.feedback:
                AccessibilityManager am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
                boolean isAccessibilityEnabled = am.isEnabled();
                boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
                if(isAccessibilityEnabled && isExploreByTouchEnabled) {
                    startActivity(new Intent(this, FeedbackActivityTalkback.class));
                }
                else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.reset:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * <p>This function will initialize the views that are populated on the activity layout.
     * It also assigns content description to the views to enable speech in
     * Talk-back feature. The Talk-back feature is not available in this version.</p>
     * */
    private void initializeLayoutViews(){
        mIvLike = findViewById(R.id.ivlike);
        mIvDontLike = findViewById(R.id.ivdislike);
        mIvMore = findViewById(R.id.ivadd);
        mIvLess = findViewById(R.id.ivminus);
        mIvYes = findViewById(R.id.ivyes);
        mIvNo = findViewById(R.id.ivno);
        mIvBack = findViewById(R.id.ivback);
        mIvHome = findViewById(R.id.ivhome);
        mIvKeyboard = findViewById(R.id.keyboard);
        mIvTTs = findViewById(R.id.ttsbutton);
        //Initially custom input text speak button is invisible
        mIvTTs.setVisibility(View.INVISIBLE);
        mEtTTs = findViewById(R.id.et);
        originalKeyListener = mEtTTs.getKeyListener();
        // Initially make this field non-editable
        mEtTTs.setKeyListener(null);
        //Initially custom input text is invisible
        mEtTTs.setVisibility(View.INVISIBLE);
        mRelativeLayCategory = findViewById(R.id.relativeLayout);
        mBtnNext = findViewById(R.id.forward);
        mBtnBack = findViewById(R.id.backward);
        mTvHeading = findViewById(R.id.tt1);
        mTvCategory1Caption = findViewById(R.id.bt1);
        mTvCategory2Caption = findViewById(R.id.bt2);
        mTvCategory3Caption = findViewById(R.id.bt3);
        mIvCategoryIcon1 = findViewById(R.id.image1);
        mIvCategoryIcon2 = findViewById(R.id.image2);
        mIvCategoryIcon3 = findViewById(R.id.image3);
        mIvArrowLeft = findViewById(R.id.arrow1);
        mIvArrowRight = findViewById(R.id.arrow2);

        mIvCategoryIcon1.setContentDescription(mCategoryIconBelowText[count]);
        mIvCategoryIcon2.setContentDescription(mCategoryIconBelowText[count + 1]);
        mIvCategoryIcon3.setContentDescription(mCategoryIconBelowText[count + 2]);

        ViewCompat.setAccessibilityDelegate(mIvLike, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvYes, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvMore, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvDontLike, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvNo, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvLess, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvKeyboard, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvHome, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvBack, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvTTs, new TalkbackHints_SingleClick());

        ViewCompat.setAccessibilityDelegate(mIvCategoryIcon1, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvCategoryIcon2, new TalkbackHints_DoubleClick());
        ViewCompat.setAccessibilityDelegate(mIvCategoryIcon3, new TalkbackHints_DoubleClick());

    }

    /**
     * <p>This function will initialize the action listeners to the views which are populated on
     * on this activity.</p>
     * */
    private void initializeViewListeners(){
        initCategoryNavNextListener();
        initCategoryNavBackListener();
        initCategoryIcon1Listener();
        initCategoryIcon2Listener();
        initCategoryIcon3Listener();
        initBackBtnListener();
        initHomeBtnListener();
        initKeyboardBtnListener();
        initLikeBtnListener();
        initDontLikeBtnListener();
        initYesBtnListener();
        initNoBtnListener();
        initMoreBtnListener();
        initLessBtnListener();
        initTTsBtnListener();
        initTTsEditTxtListener();
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation "next" button for
     * category icons.
     * When this button is pressed, the next sequence of images are loaded into all three category icons.
     * If the number of available images in the next sequence of images is less than 3 then available
     * images in sequence are loaded and remaining category icons are changed to invisible.
     * When next button is pressed following task are performed:
     * a) enable back button
     * b) hide expressive buttons
     * c) reset expressive buttons
     * d) next sequence of category icons is loaded
     * e) if sequence is reached to end it disables the next button
     * f) reset category icons</p>
     * */
    private void initCategoryNavNextListener() {
        Crashlytics.log("Sequence category next");
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mStrNext);
                mBtnBack.setEnabled(true);
                mBtnBack.setAlpha(1f);
                count = count + 3;
                // On every next sequence load, all expressive buttons are
                // reset and hidden.
                hideExpressiveBtn(true);
                resetExpressiveButton();
                mFlgHideExpBtn = 0;
                //if reached to end of sequence disable next button
                if (mCategoryIconText.length == count + 3) {
                    mBtnNext.setAlpha(.5f);
                    mBtnNext.setEnabled(false);
                }
                //if the next set of category items to be loaded are less than 3
                if (mCategoryIconText.length < count + 3) {
                    // If activity sequence is "Brushing" or "Bathing", then in its last sequences
                    // only 2 items needed to load
                    if (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 2) {
                        setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count]+".png",
                                mIvCategoryIcon1);
                        setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count+1]+".png",
                                mIvCategoryIcon2);

                        mIvCategoryIcon1.setContentDescription(mCategoryIconBelowText[count]);
                        mIvCategoryIcon2.setContentDescription(mCategoryIconBelowText[count + 1]);

                        // only first two category icons are populated so third icon and its
                        // caption set to invisible and therefore second arrow also set to invisible.
                        mIvArrowRight.setVisibility(View.INVISIBLE);
                        mIvCategoryIcon3.setVisibility(View.INVISIBLE);
                        mTvCategory3Caption.setVisibility(View.INVISIBLE);
                        mTvCategory1Caption.setText(mCategoryIconBelowText[count]);
                        mTvCategory2Caption.setText(mCategoryIconBelowText[count + 1]);

                    // If activity sequence is "Toilet", "Morning routine" or "Bedtime routine"
                    // then in its last sequences only 1 category item needs to be loaded
                    } else if (mLevelTwoItemPos == 1 || mLevelTwoItemPos == 4 || mLevelTwoItemPos == 3) {
                        setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count]+".png",
                                mIvCategoryIcon1);

                        mIvCategoryIcon1.setContentDescription(mCategoryIconBelowText[count]);

                        // only one category icon is populated so second & third icons and their
                        // captions are set to invisible and hence all arrows are set to invisible.
                        mTvCategory1Caption.setText(mCategoryIconBelowText[count]);
                        mIvCategoryIcon2.setVisibility(View.INVISIBLE);
                        mIvCategoryIcon3.setVisibility(View.INVISIBLE);
                        mIvArrowLeft.setVisibility(View.INVISIBLE);
                        mIvArrowRight.setVisibility(View.INVISIBLE);
                        mTvCategory2Caption.setVisibility(View.INVISIBLE);
                        mTvCategory3Caption.setVisibility(View.INVISIBLE);
                    }
                    mBtnNext.setAlpha(.5f);
                    mBtnNext.setEnabled(false);
                // if next items to be loaded are 3 or more then load next 3 icons in the sequence.
                } else {
                    mTvCategory1Caption.setText(mCategoryIconBelowText[count]);
                    mTvCategory2Caption.setText(mCategoryIconBelowText[count + 1]);
                    mTvCategory3Caption.setText(mCategoryIconBelowText[count + 2]);

                    setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count]+".png",
                            mIvCategoryIcon1);

                    setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count+1]+".png",
                            mIvCategoryIcon2);

                    setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count+2]+".png",
                            mIvCategoryIcon3);

                    mIvCategoryIcon1.setContentDescription(mCategoryIconBelowText[count]);
                    mIvCategoryIcon2.setContentDescription(mCategoryIconBelowText[count + 1]);
                    mIvCategoryIcon3.setContentDescription(mCategoryIconBelowText[count + 2]);
                    Log.d("CONTENT", "SET");
                }
                // once sequence is loaded, initially all category icons have no border/ no category
                // icon in sequence have initial border
                resetCategoryIconBorders();
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation "previous" (back) button for
     * category icons.
     * When this button is pressed, previous sequence of images are loaded into all three category icons.
     * If previous sequence of images is unavailable then previous button is unavailable
     * (enabled false).
     * When previous button is pressed following task are performed:
     * a) enable next button
     * b) hide expressive buttons
     * c) reset expressive buttons
     * d) previous sequence of category icons is loaded
     * e) if sequence is reached to the beginning it disables the back button
     * f) reset category icons</p>
     * */
    private void initCategoryNavBackListener() {
        Crashlytics.log("Sequence category back");
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mStrBack);
                mBtnNext.setEnabled(true);
                mBtnNext.setAlpha(1f);
                count = count - 3;
                // On every previous sequence load, all expressive buttons are
                // reset and hidden.
                hideExpressiveBtn(true);
                resetExpressiveButton();
                mFlgHideExpBtn = 0;
                // loading previous sequence will always have all category icons populated
                // therefore all category icons, their caption, and arrows set to visible.
                mIvArrowLeft.setVisibility(View.VISIBLE);
                mIvArrowRight.setVisibility(View.VISIBLE);
                mIvCategoryIcon3.setVisibility(View.VISIBLE);
                mIvCategoryIcon2.setVisibility(View.VISIBLE);
                mIvCategoryIcon1.setVisibility(View.VISIBLE);
                if(mSession.getPictureViewMode() != MODE_PICTURE_ONLY) {
                    mTvCategory1Caption.setVisibility(View.VISIBLE);
                    mTvCategory2Caption.setVisibility(View.VISIBLE);
                    mTvCategory3Caption.setVisibility(View.VISIBLE);
                }
                //set caption to category icons
                mTvCategory1Caption.setText(mCategoryIconBelowText[count]);
                mTvCategory2Caption.setText(mCategoryIconBelowText[count + 1]);
                mTvCategory3Caption.setText(mCategoryIconBelowText[count + 2]);
                //load images to category icons
                setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count]+".png",
                        mIvCategoryIcon1);

                setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count+1]+".png",
                        mIvCategoryIcon2);

                setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count+2]+".png",
                        mIvCategoryIcon3);

                mIvCategoryIcon1.setContentDescription(mCategoryIconBelowText[count]);
                mIvCategoryIcon2.setContentDescription(mCategoryIconBelowText[count + 1]);
                mIvCategoryIcon3.setContentDescription(mCategoryIconBelowText[count + 2]);

                // previous items to be loaded are less = 0 then disable back button
                if (count == 0) {
                    mBtnBack.setEnabled(false);
                    mBtnBack.setAlpha(.5f);
                }
                // once sequence is loaded, initially all category icons have no border/no category
                // icon in sequence have initial border
                resetCategoryIconBorders();
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to category Icon 1 button.
     * a. If category 1 icon button is pressed once, expressive buttons appear.
     * b. If category 1 icon pressed twice, expressive buttons disappear and category
     *    icon 1 border disappears.
     * c. If category icon 2/3 is already in a pressed state and then category 1 icon
     *    is pressed, border on category 2/3 icon disappears (expressive buttons are
     *    visible/enabled at this time).
     * d. If category icon 2/3 is already pressed along with any expressive button
     *    and then category 1 icon is pressed, flags on the expressive buttons are reset
     *    and the border on category 2/3 icon disappears (expressive buttons are
     *    visible/enabled at this time).</p>
     * */
    private void initCategoryIcon1Listener() {
        mIvCategoryIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = 0;
                // If expressive buttons are visible or category icon 1 is in pressed state then
                // reset the border of category icon 1 and hide expressive buttons
                if (mFlgHideExpBtn == 1) {
                    hideExpressiveBtn(true);
                    setBorderToView(findViewById(R.id.borderView1),-1);
                    mFlgHideExpBtn = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("Icon", mCategoryIconSpeechText[count]);
                    bundle.putString("Category", mHeading[mLevelTwoItemPos].toLowerCase());
                    bundleEvent("Grid",bundle);
                // If expressive buttons are hidden or category icon 1 is in unpressed state then
                // set the border of category icon 1 and show expressive buttons
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("Icon", "VisibleExpr " + mCategoryIconSpeechText[count]);
                    bundle.putString("Category", mHeading[mLevelTwoItemPos].toLowerCase());
                    bundleEvent("Grid",bundle);
                    mFlgHideExpBtn = 1;
                    // If new current sequence is the last sequence and category icon 1 is
                    // last item in sequence then hide expressive buttons.
                    // The last item in sequence do not have any expression (so all expressive
                    // buttons are hidden).
                    if (count + mFlgHideExpBtn == mCategoryIconText.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    speakSpeech(mCategoryIconSpeechText[count]);
                    resetExpressiveButton();
                    setBorderToView(findViewById(R.id.borderView1), 6);
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to category Icon 2 button.
     * a. If category 2 icon button is pressed once, expressive buttons appear.
     * b. If category 2 icon pressed twice, expressive buttons disappear and category
     *    icon 2 border disappears.
     * c. If category icon 1/3 is already in a pressed state and then category 2 icon
     *    is pressed, border on category 1/3 icon disappears (expressive buttons are
     *    visible/enabled at this time).
     * d. If category icon 1/3 is already pressed along with any expressive button
     *    and then category 2 icon is pressed, flags on the expressive buttons are reset
     *    and the border on category 1/3 icon disappears (expressive buttons are
     *    visible/enabled at this time).</p>
     * */
    private void initCategoryIcon2Listener() {
        mIvCategoryIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = 0;
                // If expressive buttons are visible or category icon 2 is in pressed state then
                // reset the border of category icon 2 and hide expressive buttons
                if (mFlgHideExpBtn == 2) {
                    hideExpressiveBtn(true);
                    setBorderToView(findViewById(R.id.borderView2),-1);
                    mFlgHideExpBtn = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("Icon", mCategoryIconSpeechText[count+1]);
                    bundle.putString("Category", mHeading[mLevelTwoItemPos].toLowerCase());
                    bundleEvent("Grid",bundle);
                    // If expressive buttons are hidden or category icon 2 is in unpressed state then
                    // set the border of category icon 2 and show expressive buttons
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("Icon", "VisibleExpr " + mCategoryIconSpeechText[count+1]);
                    bundle.putString("Category", mHeading[mLevelTwoItemPos].toLowerCase());
                    bundleEvent("Grid",bundle);
                    mFlgHideExpBtn = 2;
                    // If new current sequence is the last sequence and category icon 2 is
                    // last item in sequence then hide expressive buttons.
                    // The last item in sequence do not have any expression (so all expressive
                    // buttons are hidden).
                    if (count + mFlgHideExpBtn == mCategoryIconText.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    speakSpeech(mCategoryIconSpeechText[count + 1]);
                    resetExpressiveButton();
                    setBorderToView(findViewById(R.id.borderView2),6);
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to category Icon 3 button.
     * a. If category 3 icon button is pressed once, expressive buttons appear.
     * b. If category 3 icon pressed twice, expressive buttons disappear and category
     *    icon 3 border disappears.
     * c. If category icon 1/2 is already in a pressed state and then category 3 icon
     *    is pressed, border on category 1/2 icon disappears (expressive buttons are
     *    visible/enabled at this time).
     * d. If category icon 1/2 is already pressed along with any expressive button
     *    and then category 3 icon is pressed, flags on the expressive buttons are reset
     *    and the border on category 1/2 icon disappears (expressive buttons are
     *    visible/enabled at this time).</p>
     * */
    private void initCategoryIcon3Listener() {
        mIvCategoryIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = 0;
                // If expressive buttons are visible or category icon 3 is in pressed state then
                // reset the border of category icon 3 and hide expressive buttons
                if (mFlgHideExpBtn == 3) {
                    hideExpressiveBtn(true);
                    setBorderToView(findViewById(R.id.borderView3), -1);
                    mFlgHideExpBtn = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("Icon", mCategoryIconSpeechText[count+2]);
                    bundle.putString("Category", mHeading[mLevelTwoItemPos].toLowerCase());
                    bundleEvent("Grid",bundle);
                    // If expressive buttons are hidden or category icon 3 is in unpressed state then
                    // set the border of category icon 3 and show expressive buttons
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("Icon", "VisibleExpr " + mCategoryIconSpeechText[count+2]);
                    bundle.putString("Category", mHeading[mLevelTwoItemPos].toLowerCase());
                    bundleEvent("Grid",bundle);
                    mFlgHideExpBtn = 3;
                    // If new current sequence is the last sequence and category icon 3 is
                    // last item in sequence then hide expressive buttons.
                    // The last item in sequence do not have any expression (so all expressive
                    // buttons are hidden).
                    if (count + mFlgHideExpBtn == mCategoryIconText.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    speakSpeech(mCategoryIconSpeechText[count + 2]);
                    resetExpressiveButton();
                    setBorderToView(findViewById(R.id.borderView3), 6);
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation back button.
     * When user pressed navigation back button and :
     *  a) Custom keyboard input text is open, user intends to close custom keyboard input
     *  text. Hence custom keyboard input text is set to close.
     *  b) custom keyboard input text is not open, user intends to close the current level.
     *   Hence current level is closed with successful closure result (RESULT_OK).
     *   The user will returned back to {@link LevelTwoActivity}.</p>
     * */
    private void initBackBtnListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(mNavigationBtnTxt[1]);
                //Firebase event
                singleEvent("Navigation","Back");
                mIvTTs.setImageResource(R.drawable.ic_search_list_speaker);
                if (mFlgKeyboard == 1) {
                    // When keyboard is open, close it and retain expressive button,
                    // category icon states as they are before keyboard opened.
                    mIvKeyboard.setImageResource(R.drawable.keyboard);
                    mIvBack.setImageResource(R.drawable.back_pressed);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRelativeLayCategory.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 0;
                    // after closing keyboard, then enable all expressive buttons and set visible
                    // category icon navigation icons
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    mBtnNext.setVisibility(View.VISIBLE);
                    mBtnBack.setVisibility(View.VISIBLE);
                } else {
                    // When keyboard is not open simply set result and close the activity.
                    mIvBack.setImageResource(R.drawable.back_pressed);
                    setResult(RESULT_OK);
                    finish();
                }
                showActionBarTitle(true);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation home button.
     * When user press this button user navigated to {@link MainActivity} with
     *  every state of views is like app launched as fresh. Action bar title is set
     *  to 'home'</p>
     * */
    private void initHomeBtnListener() {
        mIvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        speakSpeech(mNavigationBtnTxt[0]);
                    }
                }).start();
                //When home is tapped in this activity it will close all other activities and
                // user is redirected/navigated to MainActivity and gotoHome() method is called.
                // As Firebase home event is defined in gotoHome() function of mainActivity.
                mIvHome.setImageResource(R.drawable.home_pressed);
                mIvKeyboard.setImageResource(R.drawable.keyboard);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(getString(R.string.goto_home), true);
                startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishAffinity();
                    }
                },100);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation keyboard button.
     * {@link SequenceActivity} navigation keyboard button either enable or disable
     * the keyboard layout.
     * When keyboard layout is enabled using keyboard button, it is visible to user and
     * action bar title set to "keyboard".
     * When keyboard layout is disabled using keyboard button, the state of the
     * {@link SequenceActivity} retrieved as it was before opening keyboard layout.
     * */
    private void initKeyboardBtnListener() {
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        final String strKeyboard = getString(R.string.keyboard);
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mNavigationBtnTxt[2]);
                //Firebase event
                singleEvent("Navigation","Keyboard");
                mIvTTs.setImageResource(R.drawable.ic_search_list_speaker);
                //when mFlgKeyboard is set to 1, it means user is using custom keyboard input
                // text and system keyboard is visible.
                if (mFlgKeyboard == 1) {
                    // As user is using custom keyboard input text and then press the keyboard button,
                    // user intent to close custom keyboard input text so below steps will follow:
                    // a) set keyboard button to unpressed state.
                    // b) hide custom input text and speak button views
                    // c) show category icons
                    // d) enable expressive button
                    // e) set category icon next and back button visible
                    mIvKeyboard.setImageResource(R.drawable.keyboard);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRelativeLayCategory.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 0;
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    mBtnBack.setVisibility(View.VISIBLE);
                    mBtnNext.setVisibility(View.VISIBLE);
                    showActionBarTitle(true);
                //when mFlgKeyboard is set to 0, it means user intent to use custom
                //keyboard input text so below steps will follow:
                } else {
                    // a) keyboard button to press
                    // b) show custom keyboard input text and speak button view
                    // c) hide category icons
                    // d) disable expressive buttons
                    // e) hide category icon next and back buttons
                    mIvKeyboard.setImageResource(R.drawable.keyboard_pressed);
                    mEtTTs.setVisibility(View.VISIBLE);
                    mEtTTs.setKeyListener(originalKeyListener);
                    // Focus the field.
                    mRelativeLayCategory.setVisibility(View.INVISIBLE);
                    changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
                    mEtTTs.requestFocus();
                    mIvTTs.setVisibility(View.VISIBLE);
                    // when user intend to use custom keyboard input text system keyboard should
                    // only appear when user taps on custom keyboard input view. Setting
                    // InputMethodManager to InputMethodManager.HIDE_NOT_ALWAYS does this task.
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    mBtnBack.setVisibility(View.INVISIBLE);
                    mBtnNext.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 1;
                    showActionBarTitle(false);
                    getSupportActionBar().setTitle(strKeyboard);
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "like" button.
     * Expressive like button is works in four ways:
     *  a) press expressive like button once
     *  b) press expressive like button twice
     *  c) press category icon first then press expressive like button once
     *  d) press category icon first then press expressive like button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except like button
     *  b) reset all expressive buttons
     *  c) produce speech using output for like
     *  d) set border to category icon of color associated with like button</p>
     * */
    private void initLikeBtnListener() {
        mIvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag (except like) are set to reset.
                mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = 0;
                resetExpressiveButton();
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgLike == 1) {
                        speakSpeech(mExprBtnTxt[1]);
                        mFlgLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLike");
                    } else {
                        speakSpeech(mExprBtnTxt[0]);
                        mFlgLike = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Like");
                    }
                //if expressive buttons are visible then speak category icon verbiage + like expression
                } else {
                    setBorderToIcon(0);
                    if (mFlgLike == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLike");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(1));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(1));
                        mFlgLike = 0;
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","Like");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(0));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(0));
                        mFlgLike = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "don't like" button.
     * Expressive don't like button is works in four ways:
     *  a) press expressive don't like button once
     *  b) press expressive don't like button twice
     *  c) press category icon first then press expressive don't like button once
     *  d) press category icon first then press expressive don't like button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except don't like button
     *  b) reset all expressive buttons
     *  c) produce speech using output for like
     *  d) set border to category icon of color associated with don't like button</p>
     * */
    private void initDontLikeBtnListener() {
        mIvDontLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag (except don't like) are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgNo = mFlgLess = 0;
                resetExpressiveButton();
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgDontLike == 1) {
                        speakSpeech(mExprBtnTxt[7]);
                        mFlgDontLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                    } else {
                        speakSpeech(mExprBtnTxt[6]);
                        mFlgDontLike = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Don'tLike");
                    }
                //if expressive buttons are visible then speak category icon verbiage + don't like expression
                } else {
                    setBorderToIcon(1);
                    if (mFlgDontLike == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(7));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(7));
                        mFlgDontLike = 0;
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","Don'tLike");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(6));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(6));
                        mFlgDontLike = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });

    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "yes" button.
     * Expressive yes button is works in four ways:
     *  a) press expressive yes button once
     *  b) press expressive yes button twice
     *  c) press category icon first then press expressive yes button once
     *  d) press category icon first then press expressive yes button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except yes button
     *  b) reset all expressive buttons
     *  c) produce speech using output for yes
     *  d) set border to category icon of color associated with yes button</p>
     * */
    private void initYesBtnListener() {
        mIvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag (except yes) are set to reset.
                mFlgLike = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = 0;
                resetExpressiveButton();
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgYes == 1) {
                        speakSpeech(mExprBtnTxt[3]);
                        mFlgYes = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyYes");
                    } else {
                        speakSpeech(mExprBtnTxt[2]);
                        mFlgYes = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Yes");
                    }
                //if expressive buttons are visible then speak category icon verbiage + yes expression
                } else {
                    setBorderToIcon(2);
                    if (mFlgYes == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyYes");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(3));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(3));
                        mFlgYes = 0;
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","Yes");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(2));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(2));
                        mFlgYes = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "no" button.
     * Expressive no button is works in four ways:
     *  a) press expressive no button once
     *  b) press expressive no button twice
     *  c) press category icon first then press expressive no button once
     *  d) press category icon first then press expressive no button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except no button
     *  b) reset all expressive buttons
     *  c) produce speech using output for no
     *  d) set border to category icon of color associated with no button</p>
     * */
    private void initNoBtnListener() {
        mIvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag (except no) are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgLess = 0;
                resetExpressiveButton();
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgNo == 1) {
                        speakSpeech(mExprBtnTxt[9]);
                        mFlgNo = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyNo");
                    } else {
                        speakSpeech(mExprBtnTxt[8]);
                        mFlgNo = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","No");
                    }
                //if expressive buttons are visible then speak category icon verbiage + no expression
                } else {
                    setBorderToIcon(3);
                    if (mFlgNo == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyNo");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(9));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(9));
                        mFlgNo = 0;
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","No");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(8));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(8));
                        mFlgNo = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "more" button.
     * Expressive more button is works in four ways:
     *  a) press expressive more button once
     *  b) press expressive more button twice
     *  c) press category icon first then press expressive more button once
     *  d) press category icon first then press expressive more button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except more button
     *  b) reset all expressive buttons
     *  c) produce speech using output for more
     *  d) set border to category icon of color associated with more button</p>
     * */
    private void initMoreBtnListener() {
        mIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag (except more) are set to reset.
                mFlgLike = mFlgYes = mFlgDontLike = mFlgNo = mFlgLess = 0;
                resetExpressiveButton();
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgMore == 1) {
                        speakSpeech(mExprBtnTxt[5]);
                        mFlgMore = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyMore");
                    } else {
                        speakSpeech(mExprBtnTxt[4]);
                        mFlgMore = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","More");
                    }
                    //if expressive buttons are visible then speak category icon verbiage + more expression
                } else {
                    setBorderToIcon(4);
                    if (mFlgMore == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyMore");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(5));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(5));
                        mFlgMore = 0;
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","More");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(4));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(4));
                        mFlgMore = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener for expressive "less" button.
     * Expressive less button is works in four ways:
     *  a) press expressive less button once
     *  b) press expressive less button twice
     *  c) press category icon first then press expressive less button once
     *  d) press category icon first then press expressive less button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except less button
     *  b) reset all expressive buttons
     *  c) produce speech using output for less
     *  d) set border to category icon of color associated with less button</p>
     * */
    private void initLessBtnListener() {
        mIvLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag (except less) are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = 0;
                resetExpressiveButton();
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgLess == 1) {
                        speakSpeech(mExprBtnTxt[11]);
                        mFlgLess = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLess");
                    } else {
                        speakSpeech(mExprBtnTxt[10]);
                        mFlgLess = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Less");
                    }
                //if expressive buttons are visible then speak category icon verbiage + less expression
                } else {
                    setBorderToIcon(5);
                    if (mFlgLess == 1) {
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLess");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(11));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(11));
                        mFlgLess = 0;
                    } else {
                        //Firebase event
                        singleEvent("ExpressiveIcon","Less");
                        singleEvent("ExpressiveGridIcon",
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(10));
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(10));
                        mFlgLess = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Tts Speak button.
     * When Tts speak button is pressed, broadcast speak request is sent to Text-to-speech service.
     * Message typed in Text-to-speech input view, is synthesized by the service.</p>
     * */
    private void initTTsBtnListener() {
        mIvTTs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(mEtTTs.getText().toString());
                //Firebase event
                Bundle bundle = new Bundle();
                bundle.putString("InputName", Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.DEFAULT_INPUT_METHOD));
                bundle.putString("utterence", mEtTTs.getText().toString());
                bundleEvent("Keyboard", bundle);
                //singleEvent("Keyboard", mEtTTs.getText().toString());
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to EditText which is used by user to
     * input custom strings.</p>
     * */
    private void initTTsEditTxtListener() {
        mEtTTs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If it loses focus...
                if (!hasFocus) {
                    // Hide soft mIvKeyboard.
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtTTs.getWindowToken(), 0);
                    // Make it non-editable again.
                    mEtTTs.setKeyListener(null);
                }
            }
        });

    }

    /**
     * <p>This function will show/hide action bar title.
     * If {@param showTitle} is set then title is displayed otherwise not.</p>
     * */
    private void showActionBarTitle(boolean showTitle){
        if (showTitle)
            getSupportActionBar().setTitle(mActionBarTitleTxt);
        else{
            mActionBarTitleTxt = getSupportActionBar().getTitle().toString();
            getSupportActionBar().setTitle("");
        }
    }

    /**
     * <p>This function will send speech output request to
     * {@link com.dsource.idc.jellowintl.utility.JellowTTSService} Text-to-speech Engine.
     * The string in {@param speechText} is speech output request string.</p>
     * */
    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText.toLowerCase());
        sendBroadcast(intent);
    }

    /**
     * <p>This function will:
     *     a) Read speech text from arrays for expressive buttons.
     *     b) Read speech text from arrays for navigation buttons.
     *     c) Read speech text from arrays for category  navigation buttons.
     *     d) Read verbiage lines into {@link SeqActivityVerbiageModel} model.</p>
     * */
    private void loadArraysFromResources() {
        mExprBtnTxt = getResources().getStringArray(R.array.arrActionSpeech);
        mNavigationBtnTxt = getResources().getStringArray(R.array.arrNavigationSpeech);
        mCategoryNav = getResources().getStringArray(R.array.arrSeqActivityNavigationText);
        mHeading = getResources().getStringArray(R.array.arrSeqActivityHeadingText);
        mStrBack = mCategoryNav[0].substring(2, mCategoryNav[0].length());
        mStrNext = mCategoryNav[1].substring(0, mCategoryNav[1].length()-2);
        String verbString = getString(R.string.sequenceActVerbiage1) +
                getString(R.string.sequenceActVerbiage2);
        SeqActivityVerbiageModel verbiageModel = new Gson()
                .fromJson(verbString, SeqActivityVerbiageModel.class);
        mSeqActSpeech = verbiageModel.getVerbiageModel();

        switch(mLevelTwoItemPos){
            case 0:
                mCategoryIconBelowText = getResources().getStringArray
                        (R.array.arrSeqActivityBrushingBelowText);
                mCategoryIconSpeechText = getResources().getStringArray
                        (R.array.arrSeqActivityBrushingSpeechText);
                mCategoryIconText = getResources().getStringArray
                        (R.array.arrSeqActivityBrushingIcon);
                break;
            case 1:
                mCategoryIconBelowText = getResources().getStringArray
                        (R.array.arrSeqActivityToiletBelowText);
                mCategoryIconSpeechText = getResources().getStringArray
                        (R.array.arrSeqActivityToiletSpeechText);
                mCategoryIconText = getResources().getStringArray
                        (R.array.arrSeqActivityToiletIcon);
                break;
            case 2:
                mCategoryIconBelowText = getResources().getStringArray
                        (R.array.arrSeqActivityBathingBelowText);
                mCategoryIconSpeechText = getResources().getStringArray
                        (R.array.arrSeqActivityBathingSpeechText);
                mCategoryIconText = getResources().getStringArray
                        (R.array.arrSeqActivityBathingIcon);
                break;
            case 3:
                mCategoryIconBelowText = getResources().getStringArray
                        (R.array.arrSeqActivityMorningRoutineBelowText);
                mCategoryIconSpeechText = getResources().getStringArray
                        (R.array.arrSeqActivityMorningRoutineSpeechText);
                mCategoryIconText = getResources().getStringArray
                        (R.array.arrSeqActivityMorningRoutineIcon);
                break;
            case 4:
                mCategoryIconBelowText = getResources().getStringArray
                        (R.array.arrSeqActivityBedtimeRoutineBelowText);
                mCategoryIconSpeechText = getResources().getStringArray
                        (R.array.arrSeqActivityBedtimeRoutineSpeechText);
                mCategoryIconText = getResources().getStringArray
                        (R.array.arrSeqActivityBedtimeRoutineIcon);
                break;
        }
    }

    /**
     * <p>This function will:
     *     a) Set "Mukta-Regular", "Mukta-Bold" font to category icon 1,2,3 captions
     *     and category heading respectively.
     *     b) set 1st image in sequence to category icon.
     *     c) Hides category icon captions if picture only mode is enabled.</p>
     * */
    private void setValueToViews() {
        mBtnNext.setText(mCategoryNav[1]);
        mBtnBack.setText(mCategoryNav[0]);
        mBtnBack.setEnabled(false);
        mBtnBack.setAlpha(.5f);

        mTvHeading.setAllCaps(true);
        mTvHeading.setTextColor(Color.rgb(64, 64, 64));
        mTvHeading.setText(mHeading[mLevelTwoItemPos].toLowerCase());

        mTvCategory1Caption.setTextColor(Color.rgb(64, 64, 64));

        mTvCategory2Caption.setTextColor(Color.rgb(64, 64, 64));

        mTvCategory3Caption.setTextColor(Color.rgb(64, 64, 64));

        mTvCategory1Caption.setText(mCategoryIconBelowText[0]);
        mTvCategory2Caption.setText(mCategoryIconBelowText[1]);
        mTvCategory3Caption.setText(mCategoryIconBelowText[2]);

        setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[0]+".png",mIvCategoryIcon1);
        setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[1]+".png", mIvCategoryIcon2);
        setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[2]+".png", mIvCategoryIcon3);

        final int MODE_PICTURE_ONLY = 1;
        if(mSession.getPictureViewMode() == MODE_PICTURE_ONLY){
            mTvCategory1Caption.setVisibility(View.INVISIBLE);
            mTvCategory2Caption.setVisibility(View.INVISIBLE);
            mTvCategory3Caption.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * <p>This function uses {@param path} image path to set image into {@param imgView}
     * using Glide image loading library.
     * @param path, is a storage path of an image.
     * @param  imgView, is a view on which image is going to be displayed.</p>
     * */
    private void setImageUsingGlide(String path, ImageView imgView) {
        GlideApp.with(SequenceActivity.this)
                .load(path)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .centerCrop()
                .dontAnimate()
                .into(imgView);
    }

    /**
     * <p>This function will show/hide expressive buttons.
     * @param  hideBtn, is set then expressive buttons visibility is changed to invisible
     * otherwise the buttons are visible.</p>
     * */
    private void hideExpressiveBtn(boolean hideBtn) {
        if(hideBtn) {
            mIvLike.setVisibility(View.INVISIBLE);
            mIvDontLike.setVisibility(View.INVISIBLE);
            mIvMore.setVisibility(View.INVISIBLE);
            mIvLess.setVisibility(View.INVISIBLE);
            mIvYes.setVisibility(View.INVISIBLE);
            mIvNo.setVisibility(View.INVISIBLE);
        }else{
            mIvLike.setVisibility(View.VISIBLE);
            mIvDontLike.setVisibility(View.VISIBLE);
            mIvMore.setVisibility(View.VISIBLE);
            mIvLess.setVisibility(View.VISIBLE);
            mIvYes.setVisibility(View.VISIBLE);
            mIvNo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * <p>This function will enable/disable expressive buttons.
     * @param  setDisable, is set then expressive buttons are disabled
     * otherwise they remain enabled.</p>
     * */
    private void changeTheExpressiveButtons(boolean setDisable) {
        if(setDisable) {
            mIvLike.setAlpha(0.5f);
            mIvDontLike.setAlpha(0.5f);
            mIvYes.setAlpha(0.5f);
            mIvNo.setAlpha(0.5f);
            mIvMore.setAlpha(0.5f);
            mIvLess.setAlpha(0.5f);
            mIvLike.setEnabled(false);
            mIvDontLike.setEnabled(false);
            mIvYes.setEnabled(false);
            mIvNo.setEnabled(false);
            mIvMore.setEnabled(false);
            mIvLess.setEnabled(false);
        }else{
            mIvLike.setAlpha(1f);
            mIvDontLike.setAlpha(1f);
            mIvYes.setAlpha(1f);
            mIvNo.setAlpha(1f);
            mIvMore.setAlpha(1f);
            mIvLess.setAlpha(1f);
            mIvLike.setEnabled(true);
            mIvDontLike.setEnabled(true);
            mIvYes.setEnabled(true);
            mIvNo.setEnabled(true);
            mIvMore.setEnabled(true);
            mIvLess.setEnabled(true);
        }
    }

    /**
     * <p>This function will reset expressive buttons to original state. Also, it clears
     * any border to category icon 1,2 and 3.</p>
     * */
    private void resetExpressiveButton() {
        resetCategoryIconBorders();
        mIvLike.setImageResource(R.drawable.like);
        mIvDontLike.setImageResource(R.drawable.dontlike);
        mIvYes.setImageResource(R.drawable.yes);
        mIvNo.setImageResource(R.drawable.no);
        mIvMore.setImageResource(R.drawable.more);
        mIvLess.setImageResource(R.drawable.less);
    }

    /**
     * <p>This function selects the category icon for which the border needs to be set. Also, it will
     * apply selected 'pressed' image to the selected expressive button using {@param actionBtnIdx}
     * {@param actionBtnIdx} is a index of expressive button.
     *  e.g. From top to bottom 0 - like button, 1 - don't like button likewise.</p>
     * */
    private void setBorderToIcon(int actionBtnIdx) {
        if (mFlgHideExpBtn == 1) {
            setBorderToView(findViewById(R.id.borderView1), actionBtnIdx);
        } else if (mFlgHideExpBtn == 2) {
            setBorderToView(findViewById(R.id.borderView2), actionBtnIdx);
        } else if (mFlgHideExpBtn == 3) {
            setBorderToView(findViewById(R.id.borderView3), actionBtnIdx);
        }
        switch (actionBtnIdx){
            case 0: mIvLike.setImageResource(R.drawable.like_pressed); break;
            case 1: mIvDontLike.setImageResource(R.drawable.dontlike_pressed); break;
            case 2: mIvYes.setImageResource(R.drawable.yes_pressed); break;
            case 3: mIvNo.setImageResource(R.drawable.no_pressed); break;
            case 4: mIvMore.setImageResource(R.drawable.more_pressed); break;
            case 5: mIvLess.setImageResource(R.drawable.less_pressed); break;
            case 6: mIvHome.setImageResource(R.drawable.home_pressed); break;
            default: break;
        }
    }

    /**
     * <p>This function sets the border color to the selected category icon view
     * {@param borderView}.
     * {@param borderView} is category icon to which border should be applied.
     * {@param actionBtnIdx} is a index of expressive button.
     *  e.g. From top to bottom 0 - like button, 1 - don't like button likewise.</p>
     * */
    private void setBorderToView(View borderView, int actionBtnIdx) {
        resetCategoryIconBorders();
        GradientDrawable gd = (GradientDrawable) borderView.getBackground();
        //actionBtnIdx expressive button index from left top to right bottom
        // eg like = 0, don't like = 1.
        // actionBtnIdx = 6 indicates brown color border.
        switch (actionBtnIdx) {
            case 0: gd.setColor(ContextCompat.getColor(this, R.color.colorLike)); break;
            case 1: gd.setColor(ContextCompat.getColor(this, R.color.colorDontLike)); break;
            case 2: gd.setColor(ContextCompat.getColor(this, R.color.colorYes)); break;
            case 3: gd.setColor(ContextCompat.getColor(this, R.color.colorNo)); break;
            case 4: gd.setColor(ContextCompat.getColor(this, R.color.colorMore)); break;
            case 5: gd.setColor(ContextCompat.getColor(this, R.color.colorLess)); break;
            case 6: gd.setColor(ContextCompat.getColor(this, R.color.colorSelect)); break;
            default: gd.setColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    /**
     * <p>This function will reset category icon 1,2 and 3 border.
     * Transparent border is applied to the view which indicates no border to
     * a view.</p>
     * */
    private void resetCategoryIconBorders() {
        GradientDrawable gd;
        gd = (GradientDrawable) (findViewById(R.id.borderView1)).getBackground();
        gd.setColor(ContextCompat.getColor(this, android.R.color.transparent));
        gd = (GradientDrawable) (findViewById(R.id.borderView2)).getBackground();
        gd.setColor(ContextCompat.getColor(this, android.R.color.transparent));
        gd = (GradientDrawable) (findViewById(R.id.borderView3)).getBackground();
        gd.setColor(ContextCompat.getColor(this, android.R.color.transparent));
    }
}
