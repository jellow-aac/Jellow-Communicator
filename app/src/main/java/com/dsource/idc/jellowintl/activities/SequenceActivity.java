package com.dsource.idc.jellowintl.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.MiscellaneousIcon;
import com.dsource.idc.jellowintl.models.SeqNavigationButton;
import com.dsource.idc.jellowintl.utility.DialogKeyboardUtterance;
import com.dsource.idc.jellowintl.utility.GlideApp;
import com.dsource.idc.jellowintl.utility.LevelUiUtils;
import com.dsource.idc.jellowintl.utility.UserEventCollector;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;
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
    /* This flags are used to identify respective expressive button is pressed either
      once or twice. eg. mFlgLike used to identify Like expressive button pressed once or twice.*/
    private int mFlgLike = GlobalConstants.SHORT_SPEECH, mFlgYes = GlobalConstants.SHORT_SPEECH,
            mFlgMore = GlobalConstants.SHORT_SPEECH, mFlgDontLike = GlobalConstants.SHORT_SPEECH,
            mFlgNo = GlobalConstants.SHORT_SPEECH, mFlgLess = GlobalConstants.SHORT_SPEECH;
    /* This flag identifies either hide/showDialog expressive buttons.*/
    private int mFlgHideExpBtn = -1;
    /*This variable indicates index of category icon selected in level one*/
    private int mLevelTwoItemPos, count = 0;
    /*Image views which are visible on the layout such as six expressive buttons, below navigation
      buttons, speak button when keyboard is open and 3 category icons from left to right.*/
    private ImageView mIvLike, mIvDontLike, mIvMore, mIvLess, mIvYes, mIvNo,
            mIvHome, mIvKeyboard, mIvBack, mIvCategoryIcon1,
            mIvCategoryIcon2, mIvCategoryIcon3;
    /* Heading, caption text for category icon respectively .*/
    private TextView mTvHeading, mTvCategory1Caption, mTvCategory2Caption, mTvCategory3Caption;
    private ImageView mIvArrowLeft, mIvArrowRight;
    /*Category icon parent view*/
    private RelativeLayout mRelativeLayCategory;
    /*navigation next, back button in category*/
    private Button mBtnNext, mBtnBack;
    private String txtActionBarTitle, txtKeyboard;
    /*Below array stores the expressive button speech text, heading,
     navigation button speech text, category navigation text respectively.*/
    private String[] mHeading, mExprBtnTxt, mNavigationBtnTxt, mIconCode;

    /*Firebase event Collector class instance.*/
    private UserEventCollector mUec;

    private Icon[] seqIconObjects;
    private SeqNavigationButton[] seqNavigationButtonObjects;

    private String mSpeak;

    private ImageView[] expressiveBtn;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence);
        txtActionBarTitle = getIntent().getExtras().getString(getString(R.string.intent_menu_path_tag));
        txtKeyboard = getString(R.string.keyboard);
        setupActionBarTitle(View.GONE, txtActionBarTitle);
        setNavigationUiConditionally();
        adjustTopMarginForNavigationUi();

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
        try {
        String s = getIntent().getExtras().getString(getString(R.string.from_search));
            if (s != null)
                if (s.equals(getString(R.string.search_tag))) {
                    highlightSearchedItem();
                }
        }
        catch (NullPointerException e)
        {
            //Not from Search Activity
        }
    }

    private void highlightSearchedItem() {
        int iconIndex = getIntent().getExtras().getInt(getString(R.string.search_parent_2));
        for (int i=0;i<(iconIndex / 3);i++)
            mBtnNext.callOnClick();

        if (iconIndex % 3 == 0)
            mIvCategoryIcon1.callOnClick();
        else if (iconIndex % 3 == 1)
            mIvCategoryIcon2.callOnClick();
        else
            mIvCategoryIcon3.callOnClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(SequenceActivity.class.getSimpleName());
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
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
        mRelativeLayCategory = null;
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

        mIvCategoryIcon1.setContentDescription(seqIconObjects[count].getDisplay_Label());
        mIvCategoryIcon2.setContentDescription(seqIconObjects[count+1].getDisplay_Label());
        mIvCategoryIcon3.setContentDescription(seqIconObjects[count+2].getDisplay_Label());

        ViewCompat.setAccessibilityDelegate(mIvLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvYes, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvMore, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvDontLike, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvNo, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvLess, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvKeyboard, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvHome, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvBack, new TalkbackHints_SingleClick());

        ViewCompat.setAccessibilityDelegate(mIvCategoryIcon1, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvCategoryIcon2, new TalkbackHints_SingleClick());
        ViewCompat.setAccessibilityDelegate(mIvCategoryIcon3, new TalkbackHints_SingleClick());
        expressiveBtn = new ImageView[]{ mIvLike, mIvYes, mIvMore, mIvDontLike, mIvNo, mIvLess };
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
                mBtnBack.setAlpha(GlobalConstants.ENABLE_ALPHA);
                count = count + 3;
                // On every next sequence load, all expressive buttons are
                // reset and hidden.
                hideExpressiveBtn(true);
                resetCategoryIconBorders();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                mFlgHideExpBtn = 0;
                //if reached to end of sequence disable next button
                if (seqIconObjects.length == count + 3) {
                    mBtnNext.setAlpha(GlobalConstants.DISABLE_ALPHA);
                    mBtnNext.setEnabled(false);
                }
                //if the next set of category items to be loaded are less than 3
                if (seqIconObjects.length < count + 3) {
                    // If activity sequence is "Brushing" or "Bathing", then in its last sequences
                    // only 2 items needed to load
                    if (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 2) {
                        setImageUsingGlide(getIconPath(SequenceActivity.this,
                                seqIconObjects[count].getEvent_Tag()),
                                mIvCategoryIcon1);
                        setImageUsingGlide(getIconPath(SequenceActivity.this,
                                seqIconObjects[count+1].getEvent_Tag()),
                                mIvCategoryIcon2);

                        mIvCategoryIcon1.setContentDescription(seqIconObjects[count].
                                getDisplay_Label());
                        mIvCategoryIcon2.setContentDescription(seqIconObjects[count+1].
                                getDisplay_Label());

                        // only first two category icons are populated so third icon and its
                        // caption set to invisible and therefore second arrow also set to invisible.
                        mIvArrowRight.setVisibility(View.INVISIBLE);
                        mIvCategoryIcon3.setVisibility(View.INVISIBLE);
                        mTvCategory3Caption.setVisibility(View.INVISIBLE);
                        mTvCategory1Caption.setText(seqIconObjects[count].getDisplay_Label());
                        mTvCategory2Caption.setText(seqIconObjects[count+1].getDisplay_Label());

                    // If activity sequence is "Toilet", "Morning routine" or "Bedtime routine"
                    // then in its last sequences only 1 category item needs to be loaded
                    } else if (mLevelTwoItemPos == 1 || mLevelTwoItemPos == 7 || mLevelTwoItemPos == 8) {
                        setImageUsingGlide(getIconPath(SequenceActivity.this,
                                seqIconObjects[count].getEvent_Tag()), mIvCategoryIcon1);

                        mIvCategoryIcon1.setContentDescription(seqIconObjects[count].getDisplay_Label());

                        // only one category icon is populated so second & third icons and their
                        // captions are set to invisible and hence all arrows are set to invisible.
                        mTvCategory1Caption.setText(seqIconObjects[count].getDisplay_Label());
                        mIvCategoryIcon2.setVisibility(View.INVISIBLE);
                        mIvCategoryIcon3.setVisibility(View.INVISIBLE);
                        mIvArrowLeft.setVisibility(View.INVISIBLE);
                        mIvArrowRight.setVisibility(View.INVISIBLE);
                        mTvCategory2Caption.setVisibility(View.INVISIBLE);
                        mTvCategory3Caption.setVisibility(View.INVISIBLE);
                    }
                    mBtnNext.setAlpha(GlobalConstants.DISABLE_ALPHA);
                    mBtnNext.setEnabled(false);
                // if next items to be loaded are 3 or more then load next 3 icons in the sequence.
                } else {
                    mTvCategory1Caption.setText(seqIconObjects[count].getDisplay_Label());
                    mTvCategory2Caption.setText(seqIconObjects[count+1].getDisplay_Label());
                    mTvCategory3Caption.setText(seqIconObjects[count+2].getDisplay_Label());

                    setImageUsingGlide(getIconPath(SequenceActivity.this,
                            seqIconObjects[count].getEvent_Tag()), mIvCategoryIcon1);

                    setImageUsingGlide(getIconPath(SequenceActivity.this,
                            seqIconObjects[count+1].getEvent_Tag()), mIvCategoryIcon2);

                    setImageUsingGlide(getIconPath(SequenceActivity.this,
                            seqIconObjects[count+2].getEvent_Tag()), mIvCategoryIcon3);

                    mIvCategoryIcon1.setContentDescription(seqIconObjects[count].getDisplay_Label());
                    mIvCategoryIcon2.setContentDescription(seqIconObjects[count+1].getDisplay_Label());
                    mIvCategoryIcon3.setContentDescription(seqIconObjects[count+2].getDisplay_Label());
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
                mBtnNext.setAlpha(GlobalConstants.ENABLE_ALPHA);
                count = count - 3;
                // On every previous sequence load, all expressive buttons are
                // reset and hidden.
                hideExpressiveBtn(true);
                resetCategoryIconBorders();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                mFlgHideExpBtn = 0;
                // loading previous sequence will always have all category icons populated
                // therefore all category icons, their caption, and arrows set to visible.
                mIvArrowLeft.setVisibility(View.VISIBLE);
                mIvArrowRight.setVisibility(View.VISIBLE);
                mIvCategoryIcon3.setVisibility(View.VISIBLE);
                mIvCategoryIcon2.setVisibility(View.VISIBLE);
                mIvCategoryIcon1.setVisibility(View.VISIBLE);
                if(getSession().getPictureViewMode() != GlobalConstants.DISPLAY_PICTURE_ONLY) {
                    mTvCategory1Caption.setVisibility(View.VISIBLE);
                    mTvCategory2Caption.setVisibility(View.VISIBLE);
                    mTvCategory3Caption.setVisibility(View.VISIBLE);
                }
                //set caption to category icons
                mTvCategory1Caption.setText(seqIconObjects[count].getDisplay_Label());
                mTvCategory2Caption.setText(seqIconObjects[count+1].getDisplay_Label());
                mTvCategory3Caption.setText(seqIconObjects[count+2].getDisplay_Label());
                //load images to category icons
                setImageUsingGlide(getIconPath(SequenceActivity.this,
                        seqIconObjects[count].getEvent_Tag()), mIvCategoryIcon1);

                setImageUsingGlide(getIconPath(SequenceActivity.this,
                        seqIconObjects[count+1].getEvent_Tag()), mIvCategoryIcon2);

                setImageUsingGlide(getIconPath(SequenceActivity.this,
                        seqIconObjects[count+2].getEvent_Tag()), mIvCategoryIcon3);

                mIvCategoryIcon1.setContentDescription(seqIconObjects[count].getDisplay_Label());
                mIvCategoryIcon2.setContentDescription(seqIconObjects[count+1].getDisplay_Label());
                mIvCategoryIcon3.setContentDescription(seqIconObjects[count+2].getDisplay_Label());
                mIvCategoryIcon1.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);

                // previous items to be loaded are less = 0 then disable back button
                if (count == 0) {
                    mBtnBack.setEnabled(false);
                    mBtnBack.setAlpha(GlobalConstants.DISABLE_ALPHA);
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
                    setBorderToView(findViewById(R.id.borderView1),GlobalConstants.NOT_SELECTED);
                    mFlgHideExpBtn = 0;
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        mUec.createSendFbEventFromTappedView(12, seqIconObjects[count].
                                    getDisplay_Label(),mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                // If expressive buttons are hidden or category icon 1 is in unpressed state then
                // set the border of category icon 1 and showDialog expressive buttons
                } else {
                    mFlgHideExpBtn = 1;
                    // If new current sequence is the last sequence and category icon 1 is
                    // last item in sequence then hide expressive buttons.
                    // The last item in sequence do not have any expression (so all expressive
                    // buttons are hidden).
                    if (count + mFlgHideExpBtn == seqIconObjects.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        speak(seqIconObjects[count].getSpeech_Label());
                        mUec.createSendFbEventFromTappedView(12, "VisibleExpr " +
                                seqIconObjects[count].getDisplay_Label()
                                    , mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    resetCategoryIconBorders();
                    LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                    setBorderToView(findViewById(R.id.borderView1), 6);
                }
                mIvBack.setImageResource(R.drawable.back);
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    showAccessibleDialog(count, v);
                    v.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                    mUec.accessibilityPopupOpenedEvent(seqIconObjects[count].getSpeech_Label());
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
                    setBorderToView(findViewById(R.id.borderView2),GlobalConstants.NOT_SELECTED);
                    mFlgHideExpBtn = 0;
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        mUec.createSendFbEventFromTappedView(12,
                                seqIconObjects[count+1].getDisplay_Label(),
                                mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    // If expressive buttons are hidden or category icon 2 is in unpressed state then
                    // set the border of category icon 2 and showDialog expressive buttons
                } else {
                    mFlgHideExpBtn = 2;
                    // If new current sequence is the last sequence and category icon 2 is
                    // last item in sequence then hide expressive buttons.
                    // The last item in sequence do not have any expression (so all expressive
                    // buttons are hidden).
                    if (count + mFlgHideExpBtn == seqIconObjects.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        speak(seqIconObjects[count+1].getSpeech_Label());
                        mUec.createSendFbEventFromTappedView(12, "VisibleExpr " +
                                seqIconObjects[count+1].getDisplay_Label()
                                , mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    resetCategoryIconBorders();
                    LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                    setBorderToView(findViewById(R.id.borderView2),6);
                }
                mIvBack.setImageResource(R.drawable.back);
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    showAccessibleDialog(count + 1, v);
                    v.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                    mUec.accessibilityPopupOpenedEvent(seqIconObjects[count+1].getSpeech_Label());
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
                    setBorderToView(findViewById(R.id.borderView3), GlobalConstants.NOT_SELECTED);
                    mFlgHideExpBtn = 0;
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        mUec.createSendFbEventFromTappedView(12,
                            seqIconObjects[count+2].getDisplay_Label(),
                                mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    // If expressive buttons are hidden or category icon 3 is in unpressed state then
                    // set the border of category icon 3 and showDialog expressive buttons
                } else {
                    mFlgHideExpBtn = 3;
                    // If new current sequence is the last sequence and category icon 3 is
                    // last item in sequence then hide expressive buttons.
                    // The last item in sequence do not have any expression (so all expressive
                    // buttons are hidden).
                    if (count + mFlgHideExpBtn == seqIconObjects.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                        speak(seqIconObjects[count+2].getSpeech_Label());
                        mUec.createSendFbEventFromTappedView(12, "VisibleExpr " +
                                seqIconObjects[count+2].getDisplay_Label(),
                                mHeading[mLevelTwoItemPos].toLowerCase());
                    }
                    resetCategoryIconBorders();
                    LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                    setBorderToView(findViewById(R.id.borderView3), 6);
                }
                mIvBack.setImageResource(R.drawable.back);
                if (isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    showAccessibleDialog(count + 2, v);
                    v.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                    mUec.accessibilityPopupOpenedEvent(seqIconObjects[count+2].getSpeech_Label());
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
                mUec.createSendFbEventFromTappedView(27, "", "");
                mIvBack.setImageResource(R.drawable.back_pressed);
                String str = getIntent().getExtras().getString(getString(R.string.from_search));
                if(str != null && !str.isEmpty() && str.equals(getString(R.string.search_tag))) {
                    Intent intent = new Intent(SequenceActivity.this, LevelTwoActivity.class);
                    intent.putExtra(getString(R.string.level_one_intent_pos_tag), 1/*1 is index for Daily Activities*/);
                    intent.putExtra("search_and_back", true);
                    {
                        String path = getIntent().getExtras().getString(getString(R.string.intent_menu_path_tag));
                        path = path.split("/")[0]+ "/"+path.split("/")[1]+"/";
                        intent.putExtra(getString(R.string.intent_menu_path_tag), path);
                    }
                    startActivity(intent);
                }else{
                    setResult(RESULT_OK);
                }
                finish();
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
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Firebase event
                singleEvent("Navigation", "Keyboard");
                new DialogKeyboardUtterance().show(SequenceActivity.this);
                speak(mNavigationBtnTxt[2]);
                mIvKeyboard.setImageResource(R.drawable.keyboard_pressed);
                mIvBack.setImageResource(R.drawable.back);
                mIvHome.setImageResource(R.drawable.home);
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
                mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                resetCategoryIconBorders();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgLike == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[1]);
                        mFlgLike = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(1, "", "");
                    } else {
                        speak(mExprBtnTxt[0]);
                        mFlgLike = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(0, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + like expression
                } else {
                    setBorderToIcon(0);
                    if (mFlgLike == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(14, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"LL", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getLL());
                        mFlgLike = GlobalConstants.SHORT_SPEECH;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(13, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"L0", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getL());
                        mFlgLike = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgYes = mFlgMore = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                resetCategoryIconBorders();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgDontLike == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[7]);
                        mFlgDontLike = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(7, "", "");
                    } else {
                        speak(mExprBtnTxt[6]);
                        mFlgDontLike = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(6, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + don't like expression
                } else {
                    setBorderToIcon(1);
                    if (mFlgDontLike == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(20, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"DD", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getDD());
                        mFlgDontLike = GlobalConstants.SHORT_SPEECH;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(19, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"D0", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getD());
                        mFlgDontLike = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                resetCategoryIconBorders();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgYes == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[3]);
                        mFlgYes = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(3, "", "");
                    } else {
                        speak(mExprBtnTxt[2]);
                        mFlgYes = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(2, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + yes expression
                } else {
                    setBorderToIcon(2);
                    if (mFlgYes == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(16, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"YY", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getYY());
                        mFlgYes = GlobalConstants.SHORT_SPEECH;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(15, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"Y0", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getY());
                        mFlgYes = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgLess = GlobalConstants.SHORT_SPEECH;
                resetCategoryIconBorders();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgNo == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[9]);
                        mFlgNo = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(9, "", "");
                    } else {
                        speak(mExprBtnTxt[8]);
                        mFlgNo = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(8, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + no expression
                } else {
                    setBorderToIcon(3);
                    if (mFlgNo == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(22, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"NN", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getNN());
                        mFlgNo = GlobalConstants.SHORT_SPEECH;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(21, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"N0", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getN());
                        mFlgNo = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgYes = mFlgDontLike = mFlgNo = mFlgLess = GlobalConstants.SHORT_SPEECH;
                resetCategoryIconBorders();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgMore == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[5]);
                        mFlgMore = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(5, "", "");
                    } else {
                        speak(mExprBtnTxt[4]);
                        mFlgMore = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(4, "", "");
                    }
                    //if expressive buttons are visible then speak category icon verbiage + more expression
                } else {
                    setBorderToIcon(4);
                    if (mFlgMore == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(18, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"MM", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getMM());
                        mFlgMore = GlobalConstants.SHORT_SPEECH;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(17, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"M0", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getM());
                        mFlgMore = GlobalConstants.LONG_SPEECH;
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
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = GlobalConstants.SHORT_SPEECH;
                resetCategoryIconBorders();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
                //if expressive buttons are hidden then speak expressive button name only
                if (mFlgHideExpBtn == 0) {
                    if (mFlgLess == GlobalConstants.LONG_SPEECH) {
                        speak(mExprBtnTxt[11]);
                        mFlgLess = GlobalConstants.SHORT_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(11, "", "");
                    } else {
                        speak(mExprBtnTxt[10]);
                        mFlgLess = GlobalConstants.LONG_SPEECH;
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(10, "", "");
                    }
                //if expressive buttons are visible then speak category icon verbiage + less expression
                } else {
                    setBorderToIcon(5);
                    if (mFlgLess == GlobalConstants.LONG_SPEECH) {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(24, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"SS", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getSS());
                        mFlgLess = GlobalConstants.SHORT_SPEECH;
                    } else {
                        //Firebase event
                        mUec.createSendFbEventFromTappedView(23, getPrefixTag()
                            +"_"+ mIconCode[count + mFlgHideExpBtn]+"S0", "");
                        speak(seqIconObjects[count + mFlgHideExpBtn - 1].getS());
                        mFlgLess = GlobalConstants.LONG_SPEECH;
                    }
                }
                mIvBack.setImageResource(R.drawable.back);
            }
        });
    }

    private String getPrefixTag() {
        if (mFlgHideExpBtn == 1)
            return seqIconObjects[count].getEvent_Tag();
        else if (mFlgHideExpBtn == 2)
            return seqIconObjects[count+1].getEvent_Tag();
        else if (mFlgHideExpBtn == 3)
            return seqIconObjects[count+2].getEvent_Tag();
        else return "";
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
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.LIKE);
            }
        });
        ivYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvYes.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.YES);
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvMore.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.MORE);
            }
        });
        ivDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvDontLike.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.DONT_LIKE);
            }
        });
        ivNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvNo.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.NO);
            }
        });
        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvLess.performClick();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtns, GlobalConstants.LESS);
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

        LevelUiUtils.setExpressiveIconConditionally(expressiveBtns, seqIconObjects[position]);
        enterCategory.setText(mSpeak);
        enterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(seqIconObjects[position].getSpeech_Label());
                mUec.createSendFbEventFromTappedView(12, seqIconObjects[count].
                    getDisplay_Label(), mHeading[mLevelTwoItemPos].toLowerCase());
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
                resetCategoryIconBorders();
                LevelUiUtils.setExpressiveIconPressedState(expressiveBtn, GlobalConstants.NO_EXPR);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && isNotchDevice()) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        //int levelTwo = mLevelTwoItemPos;

        mIconCode = IconFactory.getSequenceIconCodes(
                PathFactory.getJSONFile(this),
                LanguageFactory.getCurrentLanguageCode(this),
                getLevel2_3IconCode(mLevelTwoItemPos)
        );

        seqIconObjects = TextFactory.getIconObjects(mIconCode);


        //Retrieve expressive icons text
        String[] expressiveIcons = IconFactory.getExpressiveIconCodes(
                PathFactory.getJSONFile(this),
                LanguageFactory.getCurrentLanguageCode(this)
        );
        //Extract expressive icon text from expressiveIconObjects
        ExpressiveIcon[] expressiveIconObjects = TextFactory.getExpressiveIconObjects(
                expressiveIcons);
        mExprBtnTxt = TextFactory.getExpressiveSpeechText(expressiveIconObjects);


        //Retrieve Navigation icon text
        String[] miscellaneousIcons = IconFactory.getMiscellaneousIconCodes(
                PathFactory.getJSONFile(this),
                LanguageFactory.getCurrentLanguageCode(this)
        );
        MiscellaneousIcon[] miscellaneousIconObjects = TextFactory.getMiscellaneousIconObjects(
                miscellaneousIcons
        );
        //Extract Navigation icon text from miscellaneousIconObjects
        mNavigationBtnTxt = TextFactory.getTitle(miscellaneousIconObjects);

        String[] categoryNavButton;
            try {
                // "categoryNavButton" variable initialized from files available in
                // /audio folder.Only non tts language will load this variable from
                // folder.
                categoryNavButton = IconFactory.getMiscellaneousNavigationIconCodes(
                        PathFactory.getJSONFile(this),
                        LanguageFactory.getCurrentLanguageCode(this)
                );
            } catch (Exception e) {
                categoryNavButton = new String[] {
                        LanguageFactory.getCurrentLanguageCode(this) + "04MS.mp3",
                        LanguageFactory.getCurrentLanguageCode(this) + "05MS.mp3"
                };
            }
            seqNavigationButtonObjects = TextFactory.getMiscellaneousNavigationIconObjects(
                    categoryNavButton
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
        mBtnBack.setText(Html.fromHtml("&lt;&lt;").toString().
                concat("  "+seqNavigationButtonObjects[0].getDisplay_Label()));
        mBtnNext.setText(seqNavigationButtonObjects[1].getDisplay_Label().
                concat("  "+Html.fromHtml("&gt;&gt;").toString()));
        mBtnBack.setEnabled(false);
        mBtnBack.setAlpha(GlobalConstants.DISABLE_ALPHA);
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

        mTvCategory1Caption.setText(seqIconObjects[0].getDisplay_Label());
        mTvCategory2Caption.setText(seqIconObjects[1].getDisplay_Label());
        mTvCategory3Caption.setText(seqIconObjects[2].getDisplay_Label());

        setImageUsingGlide(getIconPath(SequenceActivity.this,
                seqIconObjects[0].getEvent_Tag()),mIvCategoryIcon1);
        setImageUsingGlide(getIconPath(SequenceActivity.this,
                seqIconObjects[1].getEvent_Tag()), mIvCategoryIcon2);
        setImageUsingGlide(getIconPath(SequenceActivity.this,
                seqIconObjects[2].getEvent_Tag()), mIvCategoryIcon3);

        if(getSession().getPictureViewMode() == GlobalConstants.DISPLAY_PICTURE_ONLY){
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
                .load(path+EXTENSION)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .centerCrop()
                .dontAnimate()
                .into(imgView);
    }

    /**
     * <p>This function will showDialog/hide expressive buttons.
     * @param  hideBtn, is set then expressive buttons visibility is changed to invisible
     * otherwise the buttons are visible.</p>
     * */
    private void hideExpressiveBtn(boolean hideBtn) {
        if(hideBtn) {/*HERE*/
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
     * <p>This function selects the category icon for which the border needs to be set. Also, it will
     * apply selected 'pressed' image to the selected expressive button using {@param actionBtnIdx}
     * {@param actionBtnIdx} is a index of expressive button.
     *  e.g. From top to bottom 0 - like button, 1 - don't like button likewise.</p>
     * */
    private void setBorderToIcon(int actionBtnIdx) {
        if (mFlgHideExpBtn == 1) {/*HERE*/
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
        resetCategoryIconBorders();/*HERE*/
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
            return "0" + (level2_3Position + 1);
        } else {
            return Integer.toString(level2_3Position+1);
        }
    }

    public void hideCustomKeyboardDialog() {
        mIvKeyboard.setImageResource(R.drawable.keyboard);
        mIvBack.setImageResource(R.drawable.back);
    }
}