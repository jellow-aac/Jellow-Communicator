package com.dsource.idc.jellowintl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.text.method.KeyListener;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
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
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.MiscellaneousIcon;
import com.dsource.idc.jellowintl.models.SeqNavigationButton;
import com.dsource.idc.jellowintl.utility.DialogKeyboardUtterance;
import com.dsource.idc.jellowintl.utility.UserEventCollector;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;
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
public class SequenceActivity extends LevelBaseActivity{
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
    private String mActionBarTitleTxt;
    String[] mCategoryIconText;
    /*Below array stores the speech text, below text, expressive button speech text, heading,
     navigation button speech text, category navigation text respectively.*/
    private String[] mCategoryIconSpeechText, mCategoryIconBelowText, mHeading, mExprBtnTxt,
            mNavigationBtnTxt, mVerbCode;

    /*Firebase event Collector class instance.*/
    private UserEventCollector mUec;

    private Icon[] level3SeqIconObjects;
    private SeqNavigationButton[] seqNavigationButtonObjects;
    private String[] l3SeqIcons;

    private String mSpeak;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNotchDevice())
            setContentView(R.layout.activity_sequence_notch);
        else
            setContentView(R.layout.activity_sequence);
        setLevelActionBar(getIntent().getExtras().getString(getString(R.string.intent_menu_path_tag)));

        mUec = new UserEventCollector();
        /*get position of category icon selected in level two*/
        mLevelTwoItemPos = getIntent().getExtras().getInt(getString(R.string.level_2_item_pos_tag));

        mSpeak = getString(R.string.speak);

        loadArraysFromResources();
        initializeLayoutViews();
        initializeViewListeners();
        setValueToViews();
        resetCategoryIconBorders();
        /*In sequence, expressive buttons are invisible initially*/
        hideExpressiveBtn(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(SequenceActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
        // Start measuring user app screen timer .
        startMeasuring();
        if(!getSession().getToastMessage().isEmpty()) {
            Toast.makeText(this, getSession().getToastMessage(), Toast.LENGTH_SHORT).show();
            getSession().setToastMessage("");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer .
        stopMeasuring("SequenceActivity");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCategoryIconText = null;
        mRelativeLayCategory = null;
        mCategoryIconSpeechText = null;
        mCategoryIconBelowText = null;
        mHeading = null;
        mExprBtnTxt = null;
        mNavigationBtnTxt = null;
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
        mEtTTs.setVisibility(View.INVISIBLE);
        mEtTTs.setSingleLine();
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

        if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            Typeface tf = ResourcesCompat.getFont(this, R.font.mukta_semibold);
            mTvCategory1Caption.setTypeface(tf);
            mTvCategory2Caption.setTypeface(tf);
            mTvCategory3Caption.setTypeface(tf);
        }
        mIvCategoryIcon1 = findViewById(R.id.image1);
        mIvCategoryIcon2 = findViewById(R.id.image2);
        mIvCategoryIcon3 = findViewById(R.id.image3);
        mIvArrowLeft = findViewById(R.id.arrow1);
        mIvArrowRight = findViewById(R.id.arrow2);

        mIvCategoryIcon1.setContentDescription(mCategoryIconBelowText[count]);
        mIvCategoryIcon2.setContentDescription(mCategoryIconBelowText[count + 1]);
        mIvCategoryIcon3.setContentDescription(mCategoryIconBelowText[count + 2]);

        ViewCompat.setAccessibilityDelegate(mIvLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvYes, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvMore, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvDontLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvNo, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvLess, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvKeyboard, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvHome, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvBack, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvTTs, new TalkbackHints_SingleClick());

        ViewCompat.setAccessibilityDelegate(mIvCategoryIcon1, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvCategoryIcon2, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvCategoryIcon3, new TalkbackHints_SingleClick());
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
                if(!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    speak(seqNavigationButtonObjects[1].getSpeech_Label());
                }
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
                        setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[count]),
                                mIvCategoryIcon1);
                        setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[count+1]),
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
                    } else if (mLevelTwoItemPos == 1 || mLevelTwoItemPos == 7 || mLevelTwoItemPos == 8) {
                        setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[count]),
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

                    setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[count]),
                            mIvCategoryIcon1);

                    setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[count+1]),
                            mIvCategoryIcon2);

                    setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[count+2]),
                            mIvCategoryIcon3);

                    mIvCategoryIcon1.setContentDescription(mCategoryIconBelowText[count]);
                    mIvCategoryIcon2.setContentDescription(mCategoryIconBelowText[count + 1]);
                    mIvCategoryIcon3.setContentDescription(mCategoryIconBelowText[count + 2]);
                }
                mIvCategoryIcon1.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
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
                speak(seqNavigationButtonObjects[0].getSpeech_Label());
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
                if(getSession().getPictureViewMode() != MODE_PICTURE_ONLY) {
                    mTvCategory1Caption.setVisibility(View.VISIBLE);
                    mTvCategory2Caption.setVisibility(View.VISIBLE);
                    mTvCategory3Caption.setVisibility(View.VISIBLE);
                }
                //set caption to category icons
                mTvCategory1Caption.setText(mCategoryIconBelowText[count]);
                mTvCategory2Caption.setText(mCategoryIconBelowText[count + 1]);
                mTvCategory3Caption.setText(mCategoryIconBelowText[count + 2]);
                //load images to category icons
                setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[count]),
                        mIvCategoryIcon1);

                setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[count+1]),
                        mIvCategoryIcon2);

                setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[count+2]),
                        mIvCategoryIcon3);

                mIvCategoryIcon1.setContentDescription(mCategoryIconBelowText[count]);
                mIvCategoryIcon2.setContentDescription(mCategoryIconBelowText[count + 1]);
                mIvCategoryIcon3.setContentDescription(mCategoryIconBelowText[count + 2]);
                mIvCategoryIcon1.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);

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
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        mUec.createSendFbEventFromTappedView(12, mCategoryIconBelowText[count],
                                mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                // If expressive buttons are hidden or category icon 1 is in unpressed state then
                // set the border of category icon 1 and show expressive buttons
                } else {
                    mFlgHideExpBtn = 1;
                    // If new current sequence is the last sequence and category icon 1 is
                    // last item in sequence then hide expressive buttons.
                    // The last item in sequence do not have any expression (so all expressive
                    // buttons are hidden).
                    if (count + mFlgHideExpBtn == mCategoryIconText.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        speak(mCategoryIconSpeechText[count]);
                        mUec.createSendFbEventFromTappedView(12, "VisibleExpr " +
                                mCategoryIconBelowText[count], mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    resetExpressiveButton();
                    setBorderToView(findViewById(R.id.borderView1), 6);
                }
                mIvBack.setImageResource(R.drawable.back);
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    showAccessibleDialog(count, v);
                    v.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                    mUec.accessibilityPopupOpenedEvent(mCategoryIconSpeechText[count]);
                }
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
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        mUec.createSendFbEventFromTappedView(12,
                                mCategoryIconBelowText[count + 1], mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    // If expressive buttons are hidden or category icon 2 is in unpressed state then
                    // set the border of category icon 2 and show expressive buttons
                } else {
                    mFlgHideExpBtn = 2;
                    // If new current sequence is the last sequence and category icon 2 is
                    // last item in sequence then hide expressive buttons.
                    // The last item in sequence do not have any expression (so all expressive
                    // buttons are hidden).
                    if (count + mFlgHideExpBtn == mCategoryIconText.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        speak(mCategoryIconSpeechText[count + 1]);
                        mUec.createSendFbEventFromTappedView(12, "VisibleExpr " +
                                mCategoryIconBelowText[count+1], mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    resetExpressiveButton();
                    setBorderToView(findViewById(R.id.borderView2),6);
                }
                mIvBack.setImageResource(R.drawable.back);
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    showAccessibleDialog(count + 1, v);
                    v.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                    mUec.accessibilityPopupOpenedEvent(mCategoryIconSpeechText[count+1]);
                }
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
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        mUec.createSendFbEventFromTappedView(12,
                                mCategoryIconBelowText[count+2], mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    // If expressive buttons are hidden or category icon 3 is in unpressed state then
                    // set the border of category icon 3 and show expressive buttons
                } else {
                    mFlgHideExpBtn = 3;
                    // If new current sequence is the last sequence and category icon 3 is
                    // last item in sequence then hide expressive buttons.
                    // The last item in sequence do not have any expression (so all expressive
                    // buttons are hidden).
                    if (count + mFlgHideExpBtn == mCategoryIconText.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        speak(mCategoryIconSpeechText[count + 2]);
                        mUec.createSendFbEventFromTappedView(12, "VisibleExpr " +
                                mCategoryIconBelowText[count+2], mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    resetExpressiveButton();
                    setBorderToView(findViewById(R.id.borderView3), 6);
                }
                mIvBack.setImageResource(R.drawable.back);
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    showAccessibleDialog(count + 2, v);
                    v.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                    mUec.accessibilityPopupOpenedEvent(mCategoryIconSpeechText[count+2]);
                }
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
                speak(mNavigationBtnTxt[1]);
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
                    mUec.createSendFbEventFromTappedView(27, "", "");
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
                mUec.createSendFbEventFromTappedView(26, "", "");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        speak(mNavigationBtnTxt[0]);
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
                        setResult(RESULT_CANCELED);
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
                //Firebase event
                singleEvent("Navigation", "Keyboard");
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    new DialogKeyboardUtterance(SequenceActivity.this).show();
                } else {
                    speak(mNavigationBtnTxt[2]);
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
                        speak(mExprBtnTxt[1]);
                        mFlgLike = 0;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(1, "", "");
                    } else {
                        speak(mExprBtnTxt[0]);
                        mFlgLike = 1;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(0, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + like expression
                } else {
                    setBorderToIcon(0);
                    if (mFlgLike == 1) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(14, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"LL", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getLL());
                        mFlgLike = 0;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(13, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"L0", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getL());
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
                        speak(mExprBtnTxt[7]);
                        mFlgDontLike = 0;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(7, "", "");
                    } else {
                        speak(mExprBtnTxt[6]);
                        mFlgDontLike = 1;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(6, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + don't like expression
                } else {
                    setBorderToIcon(1);
                    if (mFlgDontLike == 1) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(20, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"DD", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getDD());
                        mFlgDontLike = 0;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(19, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"D0", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getD());
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
                        speak(mExprBtnTxt[3]);
                        mFlgYes = 0;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(3, "", "");
                    } else {
                        speak(mExprBtnTxt[2]);
                        mFlgYes = 1;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(2, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + yes expression
                } else {
                    setBorderToIcon(2);
                    if (mFlgYes == 1) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(16, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"YY", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getYY());
                        mFlgYes = 0;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(15, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"Y0", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getY());
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
                        speak(mExprBtnTxt[9]);
                        mFlgNo = 0;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(9, "", "");
                    } else {
                        speak(mExprBtnTxt[8]);
                        mFlgNo = 1;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(8, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + no expression
                } else {
                    setBorderToIcon(3);
                    if (mFlgNo == 1) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(22, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"NN", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getNN());
                        mFlgNo = 0;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(21, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"N0", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getN());
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
                        speak(mExprBtnTxt[5]);
                        mFlgMore = 0;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(5, "", "");
                    } else {
                        speak(mExprBtnTxt[4]);
                        mFlgMore = 1;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(4, "", "");
                    }
                    //if expressive buttons are visible then speak category icon verbiage + more expression
                } else {
                    setBorderToIcon(4);
                    if (mFlgMore == 1) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(18, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"MM", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getMM());
                        mFlgMore = 0;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(17, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"M0", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getM());
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
                        speak(mExprBtnTxt[11]);
                        mFlgLess = 0;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(11, "", "");
                    } else {
                        speak(mExprBtnTxt[10]);
                        mFlgLess = 1;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(10, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + less expression
                } else {
                    setBorderToIcon(5);
                    if (mFlgLess == 1) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(24, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"SS", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getSS());
                        mFlgLess = 0;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(23, getPrefixTag()
                            +"_"+ mVerbCode[count + mFlgHideExpBtn]+"S0", "");
                        speak(level3SeqIconObjects[count + mFlgHideExpBtn - 1].getS());
                        mFlgLess = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    private String getPrefixTag() {
        if (mFlgHideExpBtn == 1)
            return level3SeqIconObjects[count].getEvent_Tag();
        else if (mFlgHideExpBtn == 2)
            return level3SeqIconObjects[count+1].getEvent_Tag();
        else if (mFlgHideExpBtn == 3)
            return level3SeqIconObjects[count+2].getEvent_Tag();
        else return "";
    }

    /**
     * <p>This function will initialize the click scrollListener to Tts Speak button.
     * When Tts speak button is pressed, broadcast speak request is sent to Text-to-speech service.
     * Message typed in Text-to-speech input view, is synthesized by the service.</p>
     * */
    private void initTTsBtnListener() {
        mIvTTs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speak(mEtTTs.getText().toString());
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

    private void showAccessibleDialog(final int position, final View disabledView) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SequenceActivity.this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);

        Button enterCategory = mView.findViewById(R.id.enterCategory);
        final Button closeDialog = mView.findViewById(R.id.btnClose);
        ImageView ivLike = mView.findViewById(R.id.ivlike);
        ImageView ivYes = mView.findViewById(R.id.ivyes);
        ImageView ivAdd = mView.findViewById(R.id.ivadd);
        ImageView ivDisLike = mView.findViewById(R.id.ivdislike);
        ImageView ivNo = mView.findViewById(R.id.ivno);
        ImageView ivMinus = mView.findViewById(R.id.ivminus);
        ImageView ivBack = mView.findViewById(R.id.back);
        ImageView ivHome = mView.findViewById(R.id.home);
        ImageView ivKeyboard = mView.findViewById(R.id.keyboard);
        ViewCompat.setAccessibilityDelegate(ivLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivYes, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivAdd, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivDisLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivNo, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivMinus, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivBack, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivHome, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(ivKeyboard, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(enterCategory, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(closeDialog, new TalkbackHints_SingleClick());
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final ImageView[] expressiveBtns = {ivLike, ivYes, ivAdd, ivDisLike, ivNo, ivMinus};

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvLike.performClick();
                setBorderToExpression(0, expressiveBtns);
            }
        });
        ivYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvYes.performClick();
                setBorderToExpression(1, expressiveBtns);
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvMore.performClick();
                setBorderToExpression(2, expressiveBtns);
            }
        });
        ivDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvDontLike.performClick();
                setBorderToExpression(3, expressiveBtns);
            }
        });
        ivNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvNo.performClick();
                setBorderToExpression(4, expressiveBtns);
            }
        });
        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvLess.performClick();
                setBorderToExpression(5, expressiveBtns);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear pending Firebase events.
                mUec.clearPendingEvent();
                mIvBack.performClick();
                dialog.dismiss();
            }
        });
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear pending Firebase events.
                mUec.clearPendingEvent();
                mIvHome.performClick();
                dialog.dismiss();
            }
        });
        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear pending Firebase events.
                mUec.clearPendingEvent();
                mIvKeyboard.performClick();
                dialog.dismiss();
            }
        });

        if(position == (mCategoryIconSpeechText.length-1)) {
            ImageView[] btns = {ivLike, ivYes, ivAdd, ivDisLike, ivNo, ivMinus};
            for (int i = 0; i < btns.length; i++) {
                btns[i].setEnabled(false);
                btns[i].setAlpha(0.5f);
                btns[i].setOnClickListener(null);
            }
        }
        enterCategory.setText(mSpeak);
        enterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(mCategoryIconSpeechText[position]);
                mUec.createSendFbEventFromTappedView(12, mCategoryIconBelowText[position],
                        mHeading[mLevelTwoItemPos].toLowerCase());
            }
        });

        enterCategory.setAccessibilityDelegate(new View.AccessibilityDelegate(){
            @Override
            public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onPopulateAccessibilityEvent(host, event);
                if(event.getEventType() != AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                    mView.findViewById(R.id.txTitleHidden).
                            setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                }
            }
        });
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss dialog
                dialog.dismiss();
                //Firebase event
                singleEvent("Navigation","Back");
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //clear all selection
                resetExpressiveButton();
                disabledView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2; //style id
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
    }

    private void setBorderToExpression(int btnPos, ImageView[] diagExprBtns) {
        // clear previously selected any expressive button or home button
        diagExprBtns[0].setImageResource(R.drawable.like);
        diagExprBtns[1].setImageResource(R.drawable.yes);
        diagExprBtns[2].setImageResource(R.drawable.more);
        diagExprBtns[3].setImageResource(R.drawable.dontlike);
        diagExprBtns[4].setImageResource(R.drawable.no);
        diagExprBtns[5].setImageResource(R.drawable.less);
        // set expressive button or home button to pressed state
        switch (btnPos){
            case 0: diagExprBtns[0].setImageResource(R.drawable.like_pressed); break;
            case 1: diagExprBtns[1].setImageResource(R.drawable.yes_pressed); break;
            case 2: diagExprBtns[2].setImageResource(R.drawable.more_pressed); break;
            case 3: diagExprBtns[3].setImageResource(R.drawable.dontlike_pressed); break;
            case 4: diagExprBtns[4].setImageResource(R.drawable.no_pressed); break;
            case 5: diagExprBtns[5].setImageResource(R.drawable.less_pressed); break;
            default: break;
        }
    }

    /**
     * <p>This function will:
     *     a) Read speech text from arrays for expressive buttons.
     *     b) Read speech text from arrays for navigation buttons.
     *     c) Read speech text from arrays for category  navigation buttons.</p>
     * */
    private void loadArraysFromResources() {
        // As sequence is in Daily Activities. so it's index is fixed to position 1.
        int levelOne = 1;
        int levelTwo = mLevelTwoItemPos;

        l3SeqIcons = IconFactory.getL3SeqIcons(
                PathFactory.getIconDirectory(this),
                LanguageFactory.getCurrentLanguageCode(this),
                getLevel2_3IconCode(levelOne),
                getLevel2_3IconCode(levelTwo)
        );
        mVerbCode = new String[l3SeqIcons.length];
        for (int i = 0; i < mVerbCode.length; i++) {
            mVerbCode[i] = l3SeqIcons[i].replace(".png","");
        }

        level3SeqIconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(this),
                IconFactory.removeFileExtension(l3SeqIcons)
        );


        mCategoryIconBelowText = TextFactory.getDisplayText(level3SeqIconObjects);
        mCategoryIconSpeechText = TextFactory.getSpeechText(level3SeqIconObjects);
        mCategoryIconText = mCategoryIconSpeechText;

        String[] expressiveIcons = IconFactory.getExpressiveIcons(
                PathFactory.getIconDirectory(this),
                LanguageFactory.getCurrentLanguageCode(this)
        );

        ExpressiveIcon[] expressiveIconObjects = TextFactory.getExpressiveIconObjects(
                PathFactory.getJSONFile(this),
                IconFactory.removeFileExtension(expressiveIcons)
        );

        mExprBtnTxt = TextFactory.getExpressiveSpeechText(expressiveIconObjects);


        String[] miscellaneousIcons = IconFactory.getMiscellaneousIcons(
                PathFactory.getIconDirectory(this),
                LanguageFactory.getCurrentLanguageCode(this)
        );

        MiscellaneousIcon[] miscellaneousIconObjects = TextFactory.getMiscellaneousIconObjects(
                PathFactory.getJSONFile(this),
                IconFactory.removeFileExtension(miscellaneousIcons)
        );

        mNavigationBtnTxt = TextFactory.getTitle(miscellaneousIconObjects);
        String[] categoryNavButton;
            try {
                // "categoryNavButton" variable initialized from files available in
                // /audio folder.Only non tts language will load this variable from
                // folder.
                categoryNavButton = IconFactory.getCategoryNavigationButtons(
                        new File(PathFactory.getAudioPath(this)),
                        LanguageFactory.getCurrentLanguageCode(this)
                );
            } catch (Exception e) {
                categoryNavButton = new String[] {
                        LanguageFactory.getCurrentLanguageCode(this) + "04MS.mp3",
                        LanguageFactory.getCurrentLanguageCode(this) + "05MS.mp3"
                };
            }
            seqNavigationButtonObjects = TextFactory.getSeqNavigationButtonObjects(
                    PathFactory.getJSONFile(this),
                    IconFactory.removeFileExtension(categoryNavButton)
            );
        mHeading = getResources().getStringArray(R.array.arrSequenceActivityHeadingText);
    }

    /**
     * <p>This function will:
     *     a) Set "Mukta-Regular", "Mukta-Bold" font to category icon 1,2,3 captions
     *     and category heading respectively.
     *     b) set 1st image in sequence to category icon.
     *     c) Hides category icon captions if picture only mode is enabled.</p>
     * */
    private void setValueToViews() {
        mBtnBack.setText(Html.fromHtml("&lt;&lt;").toString().concat("  "+seqNavigationButtonObjects[0].getDisplay_Label()));
        mBtnNext.setText(seqNavigationButtonObjects[1].getDisplay_Label().concat("  "+Html.fromHtml("&gt;&gt;").toString()));
        mBtnBack.setEnabled(false);
        mBtnBack.setAlpha(.5f);
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            mTvHeading.setAllCaps(true);
        }else{
            mTvHeading.setAllCaps(false);
        }
        mTvHeading.setTextColor(Color.rgb(64, 64, 64));
        mTvHeading.setText(mHeading[mLevelTwoItemPos].toLowerCase());

        mTvCategory1Caption.setTextColor(Color.rgb(64, 64, 64));

        mTvCategory2Caption.setTextColor(Color.rgb(64, 64, 64));

        mTvCategory3Caption.setTextColor(Color.rgb(64, 64, 64));

        mTvCategory1Caption.setText(mCategoryIconBelowText[0]);
        mTvCategory2Caption.setText(mCategoryIconBelowText[1]);
        mTvCategory3Caption.setText(mCategoryIconBelowText[2]);

        setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[0]),mIvCategoryIcon1);
        setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[1]), mIvCategoryIcon2);
        setImageUsingGlide(getIconPath(SequenceActivity.this,l3SeqIcons[2]), mIvCategoryIcon3);

        final int MODE_PICTURE_ONLY = 1;
        if(getSession().getPictureViewMode() == MODE_PICTURE_ONLY){
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
            mIvLike.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            mIvDontLike.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            mIvYes.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            mIvNo.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            mIvMore.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
            mIvLess.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
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
            mIvLike.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
            mIvDontLike.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
            mIvMore.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
            mIvYes.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
            mIvNo.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
            mIvLess.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
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

    private String getLevel2_3IconCode(int level2_3Position){
        if(level2_3Position+1 <= 9){
            return "0" + Integer.toString(level2_3Position+1);
        } else {
            return Integer.toString(level2_3Position+1);
        }
    }
}
