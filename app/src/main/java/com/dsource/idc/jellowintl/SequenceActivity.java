package com.dsource.idc.jellowintl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.Models.SeqActivityVerbiageModel;
import com.dsource.idc.jellowintl.Utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.Utility.SessionManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.Utility.Analytics.reportLog;
import static com.dsource.idc.jellowintl.Utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.Utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.Utility.Analytics.stopMeasuring;


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
    /* This flag identifies either hide / show expressive buttons.*/
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
    /*navigation next, nack button in category*/
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
        getSupportActionBar().setTitle(getIntent().getExtras()
                .getString("selectedMenuItemPath"));

        mSession = new SessionManager(this);
        /*get position of category icon selected in level two*/
        mLevelTwoItemPos = getIntent().getExtras().getInt("mLevelTwoItemPos");
        // Set app locale which is set in settings by user.
        new ChangeAppLocale(this).setLocale();
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

        /* Sequence activity Morning Routine (original index is 7 in level 2) has new index 3, and
        * Bed time Routine (original index is 8 in level 2) has new index 4 */
        if(mLevelTwoItemPos == 7)
            mLevelTwoItemPos = 3;
        else if(mLevelTwoItemPos == 8)
            mLevelTwoItemPos = 4;

        File en_dir = this.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
        mStrPath = en_dir.getAbsolutePath()+"/drawables";
        loadArraysFromResources();
        initializeLayoutViews();
        initializeViewListeners();
        setValueToViews();
        resetCategoryIconBorders();
        /*In sequence, expressive buttons are invisible at initial*/
        hideExpressiveBtn(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start measuring user app screen timer .
        startMeasuring();
        //After resume from other app if the locale is other than
        // app locale, set it back to app locale.
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop measuring user app screen timer .
        stopMeasuring("SequenceActivity");
        new ChangeAppLocale(this).setLocale();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
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
                startActivity(new Intent(this, FeedbackActivity.class));
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
        mIvTTs.setVisibility(View.INVISIBLE);
        mEtTTs = findViewById(R.id.et);
        originalKeyListener = mEtTTs.getKeyListener();
        // Set it to null - this will make the field non-editable
        mEtTTs.setKeyListener(null);
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
     * <p>This function will initialize the click listener to Navigation next button for
     * category icons.
     * When pressed the button, next sequence of images are loaded into all three category icons.
     * If next sequence has available images less than 3 to load then available images are
     * loaded into category icons and category icons in which images are not loaded are changed to
     * invisible.</p>
     * */
    private void initCategoryNavNextListener() {
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mStrNext);
                mBtnBack.setEnabled(true);
                mBtnBack.setAlpha(1f);
                count = count + 3;
                //hide expressive button
                hideExpressiveBtn(true);
                //reset expressive button
                resetExpressiveButton();
                mFlgHideExpBtn = 0;
                //if reached to end of sequence disable next button
                if (mCategoryIconText.length == count + 3) {
                    mBtnNext.setAlpha(.5f);
                    mBtnNext.setEnabled(false);
                }
                //if next items to be loaded int category are less than 3
                if (mCategoryIconText.length < count + 3) {
                    // If activity sequence is "Brushing", then only 2 items needed to load
                    if (mLevelTwoItemPos == 0) {
                        setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count]+".png",
                                mIvCategoryIcon1);
                        setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count+1]+".png",
                                mIvCategoryIcon2);

                        mIvArrowRight.setVisibility(View.INVISIBLE);
                        mIvCategoryIcon3.setVisibility(View.INVISIBLE);
                        mTvCategory3Caption.setVisibility(View.INVISIBLE);
                        mTvCategory1Caption.setText(mCategoryIconBelowText[count]);
                        mTvCategory2Caption.setText(mCategoryIconBelowText[count + 1]);

                    // If activity sequence is "Toilet", "Morning routine" or "Betime routine"
                    // then only 1 category item needed to load
                    } else if (mLevelTwoItemPos == 1 || mLevelTwoItemPos == 4 || mLevelTwoItemPos == 3) {
                        setImageUsingGlide(mStrPath +"/"+ mCategoryIconText[count]+".png",
                                mIvCategoryIcon1);
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
                // if next items to be loaded are 3 or more then load next three items directly.
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
                }
                resetCategoryIconBorders();
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Navigation previous (back) button for
     * category icons.
     * When pressed the button, previous sequence of images are loaded into all three category icons.
     * If previous sequence of images is unavailable then button is unavailable
     * (enabled false).</p>
     * */
    private void initCategoryNavBackListener() {
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mStrBack);
                mBtnNext.setEnabled(true);
                mBtnNext.setAlpha(1f);
                count = count - 3;
                //hide expressive button
                hideExpressiveBtn(true);
                //reset expressive button
                resetExpressiveButton();
                mFlgHideExpBtn = 0;
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
                // previous items to be loaded are less = 0 then disable back button
                if (count == 0) {
                    mBtnBack.setEnabled(false);
                    mBtnBack.setAlpha(.5f);
                }
                resetCategoryIconBorders();
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to category Icon 1 button.
     * When expressive button is pressed it:
     *  a. Set/remove border for category icon 1.
     *  b. Clears every expressive button flags
     *  c. Produces speech output with respect to Icon.
     *  d. Show/hide expressive buttons</p>
     * */
    private void initCategoryIcon1Listener() {
        mIvCategoryIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag (except like) are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = 0;
                // hide expressive icon and reset the border of category icon 1
                if (mFlgHideExpBtn == 1) {
                    hideExpressiveBtn(true);
                    setBorderToView(findViewById(R.id.borderView1),-1);
                    mFlgHideExpBtn = 0;
                // show expressive icon and set brown border to category icon 1
                } else {
                    singleEvent("SequenceActivity", mCategoryIconSpeechText[count]);
                    reportLog(getLocalClassName()+", firstIcon: "+
                            mCategoryIconSpeechText[count], Log.INFO);
                    mFlgHideExpBtn = 1;
                    if (count + mFlgHideExpBtn == mCategoryIconText.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    speakSpeech(mCategoryIconSpeechText[count]);
                    resetExpressiveButton();
                    setBorderToView(findViewById(R.id.borderView1), 6);
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to category Icon 2 button.
     * When expressive button is pressed it:
     *  a. Set/remove border for category icon 2.
     *  b. Clears every expressive button flags
     *  c. Produces speech output with respect to Icon.
     *  d. Show/hide expressive buttons</p>
     * */
    private void initCategoryIcon2Listener() {
        mIvCategoryIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag (except like) are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = 0;
                // hide expressive icon and reset the border of category icon 2
                if (mFlgHideExpBtn == 2) {
                    hideExpressiveBtn(true);setBorderToView(findViewById(R.id.borderView2),
                            -1);
                    mFlgHideExpBtn = 0;
                // show expressive icon and set brown border to category icon 2
                } else {
                    singleEvent("SequenceActivity", mCategoryIconSpeechText[count+1]);
                    reportLog(getLocalClassName()+", secondIcon: "+
                            mCategoryIconSpeechText[count + 1], Log.INFO);
                    mFlgHideExpBtn = 2;
                    if (count + mFlgHideExpBtn == mCategoryIconText.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    speakSpeech(mCategoryIconSpeechText[count + 1]);
                    resetExpressiveButton();
                    setBorderToView(findViewById(R.id.borderView2),6);
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to category Icon 3 button.
     * When expressive button is pressed it:
     *  a. Set/remove border for category icon 3.
     *  b. Clears every expressive button flags
     *  c. Produces speech output with respect to Icon.
     *  d. Show/hide expressive buttons</p>
     * */
    private void initCategoryIcon3Listener() {
        mIvCategoryIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All expressive button speech flag (except like) are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDontLike = mFlgNo = mFlgLess = 0;
                // hide expressive icon and reset the border of category icon 2
                if (mFlgHideExpBtn == 3) {
                    hideExpressiveBtn(true);
                    setBorderToView(findViewById(R.id.borderView3), -1);
                    mFlgHideExpBtn = 0;
                // show expressive icon and set brown border to category icon 2
                } else {
                    singleEvent("SequenceActivity", mCategoryIconSpeechText[count+2]);
                    reportLog(getLocalClassName()+", thirdIcon: "+
                            mCategoryIconSpeechText[count + 2], Log.INFO);
                    mFlgHideExpBtn = 3;
                    if (count + mFlgHideExpBtn == mCategoryIconText.length)
                        hideExpressiveBtn(true);
                    else
                        hideExpressiveBtn(false);
                    speakSpeech(mCategoryIconSpeechText[count + 2]);
                    resetExpressiveButton();
                    setBorderToView(findViewById(R.id.borderView3), 6);
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Navigation back button.
     * When pressed navigation back button:
     *  a) If keyboard is open, keyboard is closed.
     *  b) If keyboard is not open, current level is closed returning with successful
     *  closure (RESULT_OK) of a screen. User will returned back
     *  to {@link LevelTwoActivity}.</p>
     * */
    private void initBackBtnListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(mNavigationBtnTxt[1]);
                mIvTTs.setImageResource(R.drawable.speaker_button);
                if (mFlgKeyboard == 1) {
                    // When keyboard is open, close it and retain expressive button,
                    // category icon states as they are before keyboard opened.
                    mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                    mIvBack.setImageResource(R.drawable.backpressed);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRelativeLayCategory.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 0;
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    mBtnNext.setVisibility(View.VISIBLE);
                    mBtnBack.setVisibility(View.VISIBLE);
                } else {
                    // When keyboard is not open simply set result and close the activity.
                    mIvBack.setImageResource(R.drawable.backpressed);
                    setResult(RESULT_OK);
                    finish();
                }
                showActionBarTitle(true);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Navigation home button.
     * When user press this button user navigated to {@link MainActivity} with
     *  every state of views is like app launched as fresh. Action bar title is set
     *  to 'home'</p>
     * */
    private void initHomeBtnListener() {
        mIvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIvHome.setImageResource(R.drawable.homepressed);
                mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                speakSpeech(mNavigationBtnTxt[0]);
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Navigation keyboard button.
     * {@link SequenceActivity} navigation keyboard button either enable or disable
     * the keyboard layout.
     * When keyboard layout is enabled using keyboard button, it is visible to user and
     * action bar title set to "keyboard".
     * When keyboard layout is disabled using keyboard button, the state of the
     * {@link SequenceActivity} retrieved as it was before opening keyboard layout.
     * */
    private void initKeyboardBtnListener() {
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mNavigationBtnTxt[2]);
                mIvTTs.setImageResource(R.drawable.speaker_button);
                // When keyboard is open, close it and retain expressive button,
                // category icon states as they are before keyboard opened.
                if (mFlgKeyboard == 1) {
                    mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRelativeLayCategory.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 0;
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    mBtnBack.setVisibility(View.VISIBLE);
                    mBtnNext.setVisibility(View.VISIBLE);
                    showActionBarTitle(true);
                } else {
                    // When keyboard is not open, open the keyboard. Hide category icons, show
                    // custom speech input text, speak button and disable expressive buttons.
                    mIvKeyboard.setImageResource(R.drawable.keyboardpressed);
                    mEtTTs.setVisibility(View.VISIBLE);
                    mEtTTs.setKeyListener(originalKeyListener);
                    // Focus the field.
                    mRelativeLayCategory.setVisibility(View.INVISIBLE);
                    changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
                    mEtTTs.requestFocus();
                    mIvTTs.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    mBtnBack.setVisibility(View.INVISIBLE);
                    mBtnNext.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 1;
                    showActionBarTitle(false);
                    getSupportActionBar().setTitle(getString(R.string.keyboard));
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to expressive like button.
     * When expressive like button is pressed:
     *  a. Single time, speech output is 'like'
     *  b. twice, speech output is 'really like'
     * When expressive like button in conjunction with category icon is pressed:
     *  a. Single time, speech output is full sentence with 'like' expression.
     *  b. twice, speech output is full sentence with 'really like' expression.
     *  Also, it set image flag to 0. This flag is used when border is applied to
     *  a category icon.</p>
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
                        singleEvent("ExpressiveIcon","ReallyLike");
                    } else {
                        speakSpeech(mExprBtnTxt[0]);
                        mFlgLike = 1;
                        singleEvent("ExpressiveIcon","mIvLike");
                    }
                //if expressive buttons are visible then speak category icon verbiage + like expression
                } else {
                    setBorderToIcon(0);
                    if (mFlgLike == 1) {
                        reportLog(getLocalClassName()+", mIvLike: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(1), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(1));
                        mFlgLike = 0;
                    } else {
                        reportLog(getLocalClassName()+", mIvLike: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(0), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(0));
                        mFlgLike = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to expressive don't like button.
     * When expressive don't like button is pressed:
     *  a. Single time, speech output is 'don't like'
     *  b. twice, speech output is 'really don't like'
     * When expressive don't like button in conjunction with category icon is pressed:
     *  a. Single time, speech output is full sentence with 'don't like' expression.
     *  b. twice, speech output is full sentence with 'really don't like' expression.
     *  Also, it set image flag to 1. This flag is used when border is applied to
     *  a category icon.</p>
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
                        singleEvent("ExpressiveIcon","ReallyDislike");
                    } else {
                        speakSpeech(mExprBtnTxt[6]);
                        mFlgDontLike = 1;
                        singleEvent("ExpressiveIcon","Dislike");
                    }
                //if expressive buttons are visible then speak category icon verbiage + don't like expression
                } else {
                    setBorderToIcon(1);
                    if (mFlgDontLike == 1) {
                        reportLog(getLocalClassName()+", mIvDontLike: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(7), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(7));
                        mFlgDontLike = 0;
                    } else {
                        reportLog(getLocalClassName()+", mIvDontLike: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(6), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(6));
                        mFlgDontLike = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });

    }

    /**
     * <p>This function will initialize the click listener to expressive yes button.
     * When expressive yes button is pressed:
     *  a. Single time, speech output is 'yes'
     *  b. twice, speech output is 'really yes'
     * When expressive yes button in conjunction with category icon is pressed:
     *  a. Single time, speech output is full sentence with 'yes' expression.
     *  b. twice, speech output is full sentence with 'really yes' expression.
     *  Also, it set image flag to 2. This flag is used when border is applied to
     *  a category icon.</p>
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
                        singleEvent("ExpressiveIcon","ReallyYes");
                    } else {
                        speakSpeech(mExprBtnTxt[2]);
                        mFlgYes = 1;
                        singleEvent("ExpressiveIcon","Yes");
                    }
                //if expressive buttons are visible then speak category icon verbiage + yes expression
                } else {
                    setBorderToIcon(2);
                    if (mFlgYes == 1) {
                        reportLog(getLocalClassName()+", mIvYes: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(3), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(3));
                        mFlgYes = 0;
                    } else {
                        reportLog(getLocalClassName()+", mIvYes: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(2), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(2));
                        mFlgYes = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to expressive no button.
     * When expressive no button is pressed:
     *  a. Single time, speech output is 'no'
     *  b. twice, speech output is 'really no'
     * When expressive no button in conjunction with category icon is pressed:
     *  a. Single time, speech output is full sentence with 'no' expression.
     *  b. twice, speech output is full sentence with 'really no' expression.
     *  Also, it set image flag to 3. This flag is used when border is applied to
     *  a category icon.</p>
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
                        singleEvent("ExpressiveIcon","ReallyNo");
                    } else {
                        speakSpeech(mExprBtnTxt[8]);
                        mFlgNo = 1;
                        singleEvent("ExpressiveIcon","No");
                    }
                //if expressive buttons are visible then speak category icon verbiage + no expression
                } else {
                    setBorderToIcon(3);
                    if (mFlgNo == 1) {
                        reportLog(getLocalClassName()+", mIvNo: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(9), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(9));
                        mFlgNo = 0;
                    } else {
                        reportLog(getLocalClassName()+", mIvNo: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(8), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(8));
                        mFlgNo = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to expressive more button.
     * When expressive more button is pressed:
     *  a. Single time, speech output is 'more'
     *  b. twice, speech output is 'some more'
     * When expressive more button in conjunction with category icon is pressed:
     *  a. Single time, speech output is full sentence with 'more' expression.
     *  b. twice, speech output is full sentence with 'some more' expression.
     *  Also, it set image flag to 4. This flag is used when border is applied to
     *  a category icon.</p>
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
                        singleEvent("ExpressiveIcon","ReallyMore");
                    } else {
                        speakSpeech(mExprBtnTxt[4]);
                        mFlgMore = 1;
                        singleEvent("ExpressiveIcon","More");
                    }
                    //if expressive buttons are visible then speak category icon verbiage + more expression
                } else {
                    setBorderToIcon(4);
                    if (mFlgMore == 1) {
                        reportLog(getLocalClassName()+", mIvMore: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(5), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(5));
                        mFlgMore = 0;
                    } else {
                        reportLog(getLocalClassName()+", mIvMore: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(4), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(4));
                        mFlgMore = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to expressive less button.
     * When expressive less button is pressed:
     *  a. Single time, speech output is 'less'
     *  b. twice, speech output is 'really less'
     * When expressive less button in conjunction with category icon is pressed:
     *  a. Single time, speech output is full sentence with 'less' expression.
     *  b. twice, speech output is full sentence with 'really less' expression.
     *  Also, it set image flag to 5. This flag is used when border is applied to
     *  a category icon.</p>
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
                        singleEvent("ExpressiveIcon","ReallyLess");
                    } else {
                        speakSpeech(mExprBtnTxt[10]);
                        mFlgLess = 1;
                        singleEvent("ExpressiveIcon","Less");
                    }
                //if expressive buttons are visible then speak category icon verbiage + less expression
                } else {
                    setBorderToIcon(5);
                    if (mFlgLess == 1) {
                        reportLog(getLocalClassName()+", mIvLess: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(11), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(11));
                        mFlgLess = 0;
                    } else {
                        reportLog(getLocalClassName()+", mIvLess: "+
                                mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                        .get(10), Log.INFO);
                        speakSpeech(mSeqActSpeech.get(mLevelTwoItemPos).get(count + mFlgHideExpBtn - 1)
                                .get(10));
                        mFlgLess = 1;
                    }
                }
                mIvBack.setImageResource(R.drawable.back_button);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Tts Speak button.
     * When Tts speak button is pressed, broadcast speak request is sent to Text-to-speech service.
     * Message typed in Text-to-speech input view, is synthesized by the service.</p>
     * */
    private void initTTsBtnListener() {
        mIvTTs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(mEtTTs.getText().toString());
                if(!mEtTTs.getText().toString().equals(""))
                    mIvTTs.setImageResource(R.drawable.speaker_pressed);
                reportLog(getLocalClassName()+", TtsSpeak: ", Log.INFO);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to EditText which is used by user to
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
     * <p>This function will display/ hide action bar title.
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
     * {@link com.dsource.idc.jellowintl.Utility.JellowTTSService} Text-to-speech Engine.
     * The string in {@param speechText} is speech output request string.</p>
     * */
    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText);
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

        SeqActivityVerbiageModel verbiageModel = new Gson()
                .fromJson(getString(R.string.sequenceActVerbiage),
                        SeqActivityVerbiageModel.class);
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
        Typeface fontMuktaRegular = Typeface.createFromAsset(getApplicationContext()
                .getAssets(), "fonts/Mukta-Regular.ttf");
        Typeface fontMuktaBold = Typeface.createFromAsset(getApplicationContext()
                .getAssets(), "fonts/Mukta-Bold.ttf");
        mBtnNext.setText(mCategoryNav[1]);
        mBtnBack.setText(mCategoryNav[0]);
        mBtnBack.setEnabled(false);
        mBtnBack.setAlpha(.5f);

        mTvHeading.setTypeface(fontMuktaBold);
        mTvHeading.setAllCaps(true);
        mTvHeading.setTextColor(Color.rgb(64, 64, 64));
        mTvHeading.setText(mHeading[mLevelTwoItemPos].toLowerCase());

        mTvCategory1Caption.setTypeface(fontMuktaRegular);
        mTvCategory1Caption.setTextColor(Color.rgb(64, 64, 64));

        mTvCategory2Caption.setTypeface(fontMuktaRegular);
        mTvCategory2Caption.setTextColor(Color.rgb(64, 64, 64));

        mTvCategory3Caption.setTypeface(fontMuktaRegular);
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
     * otherwise to visible.</p>
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
     * @param  setDisable, is set then expressive buttons are changed to disabled
     * otherwise to enabled.</p>
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
        mIvLike.setImageResource(R.drawable.ilikewithoutoutline);
        mIvDontLike.setImageResource(R.drawable.idontlikewithout);
        mIvYes.setImageResource(R.drawable.iwantwithout);
        mIvNo.setImageResource(R.drawable.idontwantwithout);
        mIvMore.setImageResource(R.drawable.morewithout);
        mIvLess.setImageResource(R.drawable.lesswithout);
    }

    /**
     * <p>This function select category icon to which border should apply. Also, it will
     * apply selected image to expressive button using {@param actionBtnIdx}
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
            case 0: mIvLike.setImageResource(R.drawable.ilikewithoutline); break;
            case 1: mIvDontLike.setImageResource(R.drawable.idontlikewithoutline); break;
            case 2: mIvYes.setImageResource(R.drawable.iwantwithoutline); break;
            case 3: mIvNo.setImageResource(R.drawable.idontwantwithoutline); break;
            case 4: mIvMore.setImageResource(R.drawable.morewithoutline); break;
            case 5: mIvLess.setImageResource(R.drawable.lesswithoutline); break;
            case 6: mIvHome.setImageResource(R.drawable.homepressed); break;
            default: break;
        }
    }

    /**
     * <p>This function set the border of a color to selected category icon view
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
     * <p>This function will reset category icon 1,2 and 3 border.</p>
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