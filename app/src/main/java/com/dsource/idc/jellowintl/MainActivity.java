package com.dsource.idc.jellowintl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dsource.idc.jellowintl.Models.LevelOneVerbiageModel;
import com.dsource.idc.jellowintl.Utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.Utility.SessionManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.Utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.Utility.Analytics.reportLog;
import static com.dsource.idc.jellowintl.Utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.Utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.Utility.Analytics.stopMeasuring;

public class MainActivity extends AppCompatActivity {
    private final int REQ_HOME = 0;
    private final boolean DISABLE_EXPR_BTNS = true;

    /* This flags are used to identify respective expressive button is pressed either
      once or twice. eg. mFlgLike used to identify Like expressive button pressed once or twice.*/
    private int mFlgLike = 0, mFlgYes = 0, mFlgMore = 0, mFlgDntLike = 0, mFlgNo = 0,
            mFlgLess = 0;
    /* This flag identifies which expressive button is pressed.*/
    private int mFlgImage = -1;
    /* This variable counts Text-to-speech engine synthesize process failure.*/
    private int mTTsNotWorkingCount = 0;
    /*Image views which are visible on the layout such as six expressive buttons, below navigation
      buttons and speak button when keyboard is open.*/
    private ImageView mIvLike, mIvDontLike, mIvYes, mIvNo, mIvMore, mIvLess,
            mIvHome, mIvKeyboard, mIvBack, mIvTTs;
    /*Input text view to speak custom text.*/
    private EditText mEtTTs;
    private KeyListener originalKeyListener;
    /*Recycler view which will populate category icons.*/
    public RecyclerView mRecyclerView;
    /*This variable indicates index of category icon selected in level one*/
    private int mLevelOneItemPos = -1;
    /*This variable indicates index of category icon in adapter in level 1. This variable is
     different than mLevelOneItemPos. */
    private int mSelectedItemAdapterPos = -1;
    /* This flag identifies that user is pressed a category icon and which border should appear
      on pressed category icon. If flag value = 0, then brown (initial border) will appear.*/
    private int mActionBtnClickCount = -1;
    /*This flag is sets to true, when expressive button is pressed in conjunction with
     category icon. It is useful to produce speech output either expression of a button or
      full sentence verbiage.*/
    private boolean mShouldReadFullSpeech = false;
    /* This flag indicates keyboard is open or not, true indicates is not open.*/
    private boolean mFlgKeyboardOpened = false;
    /*This variable hold the views populated in recycler view (category icon) list.*/
    private ArrayList<View> mRecyclerItemsViewList;
    /*Below list stores the verbiage that are spoken when category icon + expression buttons
    pressed in conjunction*/
    private ArrayList<ArrayList<String>> mLayerOneSpeech;
    /*Below array stores the speech text, below text, expressive button speech text,
     navigation button speech text respectively.*/
    private String[] mSpeechTxt, mExprBtnTxt, mNavigationBtnTxt;
    private String mActionBarTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(getString(R.string.action_bar_title));

        // Set app locale which is set in settings by user.
        new ChangeAppLocale(this).setLocale();
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        loadArraysFromResources();
        mRecyclerItemsViewList = new ArrayList<>(mSpeechTxt.length);
        while (mRecyclerItemsViewList.size() < mSpeechTxt.length)
            mRecyclerItemsViewList.add(null);
        initializeLayoutViews();
        initializeViewListeners();

        // If device has android version below Lollipop get Text-to-speech language
        if(Build.VERSION.SDK_INT < 21)
            getSpeechLanguage("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start measuring user app screen timer .
        startMeasuring();
        // broadcast receiver to get response messages from JellowTTsService.
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_TTS_ERROR");
        registerReceiver(receiver, filter);
        //After resume from other app if the locale is other than
        // app locale, set it back to app locale.
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop measuring user app screen timer .
        stopMeasuring("LevelOneActivity");
        unregisterReceiver(receiver);
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    protected void onDestroy() {
        // Stop Jellow  Test-to-speech service.
        sendBroadcast(new Intent("com.dsource.idc.jellowintl.STOP_SERVICE"));
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.languageSelect:
                startActivity(new Intent(this, LanguageSelectActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                break;
            case R.id.info:
                startActivity(new Intent(this, AboutJellowActivity.class));
                break;
            case R.id.usage:
                startActivity(new Intent(this, TutorialActivity.class));
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(getApplication(), SettingActivity.class));
                break;
            case R.id.reset:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                break;
            case R.id.feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_HOME && resultCode == RESULT_CANCELED)
            gotoHome(false);
    }

    /**
     * <p>This function will initialize the views that are populated on the activity layout.
     * It also assigns content description to the views to enable speech in
     * Talk-back feature. The Talk-back feature is not available int this version.</p>
    * */
    private void initializeLayoutViews() {
        mIvLike = findViewById(R.id.ivlike);
        mIvLike.setContentDescription(mExprBtnTxt[0]);
        mIvDontLike = findViewById(R.id.ivdislike);
        mIvDontLike.setContentDescription(mExprBtnTxt[6]);
        mIvMore = findViewById(R.id.ivadd);
        mIvMore.setContentDescription(mExprBtnTxt[4]);
        mIvLess = findViewById(R.id.ivminus);
        mIvLess.setContentDescription(mExprBtnTxt[10]);
        mIvYes = findViewById(R.id.ivyes);
        mIvYes.setContentDescription(mExprBtnTxt[2]);
        mIvNo = findViewById(R.id.ivno);
        mIvNo.setContentDescription(mExprBtnTxt[8]);
        mIvHome = findViewById(R.id.ivhome);
        mIvHome.setContentDescription(mNavigationBtnTxt[0]);
        mIvBack = findViewById(R.id.ivback);
        mIvBack.setContentDescription(mNavigationBtnTxt[1]);
        mIvBack.setAlpha(.5f);
        mIvBack.setEnabled(false);
        mIvKeyboard = findViewById(R.id.keyboard);
        mIvKeyboard.setContentDescription(mNavigationBtnTxt[2]);
        mEtTTs = findViewById(R.id.et);
        mEtTTs.setContentDescription(getString(R.string.string_to_speak));
        //Initially custom input text is invisible
        mEtTTs.setVisibility(View.INVISIBLE);

        mIvTTs = findViewById(R.id.ttsbutton);
        mIvTTs.setContentDescription(getString(R.string.speak_written_text));
        //Initially custom input text speak button is invisible
        mIvTTs.setVisibility(View.INVISIBLE);

        originalKeyListener = mEtTTs.getKeyListener();
        // Set it to null - this will make the field non-editable
        mEtTTs.setKeyListener(null);
        mRecyclerView = findViewById(R.id.recycler_view);
        // Initiate 3 columns in Recycler View.
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(new MainActivityAdapter(this));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
        mRecyclerView.requestFocus();
    }

    /**
     * <p>This function will initialize the action listeners to the views which are populated on
     * on this activity.</p>
     * */
    private void initializeViewListeners() {
        initRecyclerViewListeners();
        initBackBtnListener();
        initHomeBtnListener();
        initKeyboardBtnListener();
        initTTsBtnListener();
        initTTsEditTxtListener();
        initLikeBtnListener();
        initDontLikeBtnListener();
        initYesBtnListener();
        initNoBtnListener();
        initMoreBtnListener();
        initLessBtnListener();
    }

    /**
     * <p>This function initializes {@link RecyclerTouchListener} and
     * {@link RecyclerView.OnChildAttachStateChangeListener} for recycler view.
     * {@link RecyclerTouchListener} is a custom defined Touch event listener class.
     * {@link RecyclerView.OnChildAttachStateChangeListener} is defined to efficiently handle
     * item state of recycler child, when attached to or detached from recycler view. </p>
     * */
    private void initRecyclerViewListeners() {
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(final View view, final int position) {
                LinearLayout menuItemLinearLayout = view.findViewById(R.id.linearlayout_icon1);
                menuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tappedCategoryItemEvent(view, v, position);
                    }
                });
            }
            @Override   public void onLongClick(View view, int position) {}
        }));

        // When user scrolls the category icons, views in recycler are attached and detached from
        // recycler view. As views are recycled, it may change its state. So, global list
        // (local to activity) of recycler views is maintained which is stored in its correct state.
        // Whenever, child is attached to recycler view if it is selected then its state is retained.
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                mRecyclerItemsViewList.set(mRecyclerView.getChildLayoutPosition(view), view);
                if(mRecyclerItemsViewList.contains(view) && mSelectedItemAdapterPos > -1 &&
                        mRecyclerView.getChildLayoutPosition(view) == mSelectedItemAdapterPos)
                    setMenuImageBorder(view, true);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                setMenuImageBorder(view, false);
                mRecyclerItemsViewList.set(mRecyclerView.getChildLayoutPosition(view), null);
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Navigation back button.
     * {@link MainActivity} navigation back button enabled only if keyboard is opened.</p>
     * */
    private void initBackBtnListener() {
        mIvBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if (mFlgKeyboardOpened){
                    // When keyboard is open, close it and retain expressive button,
                    // category icon states as they are before keyboard opened.
                    mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                    mIvBack.setImageResource(R.drawable.back_button);
                    mIvHome.setImageResource(R.drawable.home);
                    mIvTTs.setImageResource(R.drawable.speaker_button);
                    speakSpeech(mNavigationBtnTxt[1]);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboardOpened = false;
                    // after closing keyboard, then enable all expressive buttons
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    mIvBack.setEnabled(false);
                    mIvBack.setAlpha(.5f);
                    showActionBarTitle(true);
                    //Firebase event
                    singleEvent("Navigation","Back");
                }
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Navigation home button.
     * {@link MainActivity} navigation home button when clicked it clears, every state of view like
     * app launched as fresh.</p>
     * */
    private void initHomeBtnListener() {
        mIvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoHome(true);
                //Firebase event
                singleEvent("Navigation","Home");
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Navigation keyboard button.
     * {@link MainActivity} navigation keyboard button either enable or disable the keyboard layout.
     * This button enable the back button when keyboard is open.</p>
     * */
    private void initKeyboardBtnListener() {
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mNavigationBtnTxt[2]);
                mIvTTs.setImageResource(R.drawable.speaker_button);
                if (mFlgKeyboardOpened){
                    // When keyboard is open, close it and retain expressive button,
                    // category icon states as they are before keyboard opened.
                    mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                    mIvBack.setImageResource(R.drawable.back_button);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboardOpened = false;
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    mIvBack.setAlpha(.5f);
                    mIvBack.setEnabled(false);
                    showActionBarTitle(true);
                }else {
                    // When keyboard is not open, open the keyboard. Hide category icons, show
                    // custom speech input text, speak button and disable expressive buttons.
                    mIvKeyboard.setImageResource(R.drawable.keyboardpressed);
                    mIvBack.setImageResource(R.drawable.back_button);
                    mIvHome.setImageResource(R.drawable.home);
                    mEtTTs.setVisibility(View.VISIBLE);

                    mEtTTs.setKeyListener(originalKeyListener);
                    // Focus the field.
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
                    mEtTTs.requestFocus();
                    mIvTTs.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    mIvBack.setAlpha(1f);
                    mIvBack.setEnabled(true);
                    mFlgKeyboardOpened = true;
                    showActionBarTitle(false);
                    getSupportActionBar().setTitle(getString(R.string.keyboard));
                }
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
                mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 0, indicates like expressive button is pressed
                mFlgImage = 0;
                // Sets expressive button
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgLike is 1, then should speak "really like".
                    if (mFlgLike == 1) {
                        speakSpeech(mExprBtnTxt[1]);
                        mFlgLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLike");
                    // if value of mFlgLike is 0, then should speak "like".
                    } else {
                        speakSpeech(mExprBtnTxt[0]);
                        mFlgLike = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Like");
                    }
                } else {
                    // if value of mShouldReadFullSpeech is true, then it should speak associated like
                    // expression verbiage to selected category icon.
                    reportLog(getLocalClassName()+", mIvLike: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null){
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    }
                    // Speak associated really like expression verbiage to selected category icon.
                    if (mFlgLike == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(1));
                        mFlgLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLike");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(1));
                    // Speak associated like expression verbiage to selected category icon.
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(0));
                        mFlgLike = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Like");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(0));
                    }
                }
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
                //Value of mFlgImage = 1, indicates don't like expressive button is pressed
                mFlgImage = 1;
                // Sets expressive button
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgDntLike is 1, then should speak "really don't like".
                    if (mFlgDntLike == 1) {
                        speakSpeech(mExprBtnTxt[7]);
                        mFlgDntLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                        // if value of mFlgDntLike is 0, then should speak "don't like".
                    } else {
                        speakSpeech(mExprBtnTxt[6]);
                        mFlgDntLike = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Don'tLike");
                    }
                } else {
                    // if value of mShouldReadFullSpeech is true, then it should speak associated
                    // don't like expression verbiage to selected category icon.
                    reportLog(getLocalClassName()+", mIvDontLike: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // speak associated really don't like expression verbiage to selected category icon.
                    if (mFlgDntLike == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(7));
                        mFlgDntLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(7));
                    // speak associated don't like expression verbiage to selected category icon.
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(6));
                        mFlgDntLike = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Don'tLike");
                        singleEvent("ExpressiveGridIcon", mLayerOneSpeech.
                                get(mLevelOneItemPos).get(6));
                    }
                }
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
                mFlgLike = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 2, indicates yes expressive button is pressed
                mFlgImage = 2;
                // Sets expressive button
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgYes is 1, then should speak "really yes".
                    if (mFlgYes == 1) {
                        speakSpeech(mExprBtnTxt[3]);
                        mFlgYes = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyYes");
                    // if value of mFlgYes is 0, then should speak "yes".
                    } else {
                        speakSpeech(mExprBtnTxt[2]);
                        mFlgYes = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Yes");
                    }
                } else {
                    // if value of mShouldReadFullSpeech is true, then it should speak associated like
                    // expression verbiage to selected category icon.
                    reportLog(getLocalClassName()+", mIvYes: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // Speak associated really yes expression verbiage to selected category icon.
                    if (mFlgYes == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(3));
                        mFlgYes = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyYes");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(3));
                    // Speak associated yes expression verbiage to selected category icon.
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(2));
                        mFlgYes = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Yes");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(2));
                    }
                }
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
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgLess = 0;
                //Value of mFlgImage = 3, indicates like expressive button is pressed
                mFlgImage = 3;
                // Sets expressive button
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    if (mFlgNo == 1) {
                        // if value of mFlgNo is 1, then should speak "really no".
                        speakSpeech(mExprBtnTxt[9]);
                        mFlgNo = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyNo");
                    } else {
                        // if value of mFlgNo is 0, then should speak "no".
                        speakSpeech(mExprBtnTxt[8]);
                        mFlgNo = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","No");
                    }
                } else {
                    // if value of mShouldReadFullSpeech is true, then it should speak associated like
                    // expression verbiage to selected category icon.
                    reportLog(getLocalClassName()+", mIvNo: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // Speak associated really no expression verbiage to selected category icon.
                    if (mFlgNo == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(9));
                        mFlgNo = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyNo");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(9));
                        // Speak associated no expression verbiage to selected category icon.
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(8));
                        mFlgNo = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","No");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(8));
                    }
                }
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
                mFlgLike = mFlgYes = mFlgDntLike = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 4, indicates like expressive button is pressed
                mFlgImage = 4;
                // Sets expressive button
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgMore is 1, then should speak "really more".
                    if (mFlgMore == 1) {
                        speakSpeech(mExprBtnTxt[5]);
                        mFlgMore = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyMore");
                    // if value of mFlgMore is 0, then should speak "more".
                    } else {
                        speakSpeech(mExprBtnTxt[4]);
                        mFlgMore = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","More");
                    }
                } else {
                    // if value of mShouldReadFullSpeech is true, then it should speak associated like
                    // expression verbiage to selected category icon.
                    reportLog(getLocalClassName()+", mIvMore: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // Speak associated really more expression verbiage to selected category icon.
                    if (mFlgMore == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(5));
                        mFlgMore = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyMore");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(5));
                        // Speak associated more expression verbiage to selected category icon.
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(4));
                        mFlgMore = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","More");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(4));
                    }
                }
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
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = 0;
                //Value of mFlgImage = 5, indicates  less expressive button is pressed
                mFlgImage = 5;
                // Sets expressive button
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgLess is 1, then should speak "really less".
                    if (mFlgLess == 1) {
                        speakSpeech(mExprBtnTxt[11]);
                        mFlgLess = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLess");
                    // if value of mFlgLess is 0, then should speak "less".
                    } else {
                        speakSpeech(mExprBtnTxt[10]);
                        mFlgLess = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Less");
                    }
                } else {
                    // if value of mShouldReadFullSpeech is true, then it should speak associated like
                    // expression verbiage to selected category icon.
                    reportLog(getLocalClassName()+", mIvLess: "+mLevelOneItemPos, Log.INFO);
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // Speak associated really less expression verbiage to selected category icon.
                    if (mFlgLess == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(11));
                        mFlgLess = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLess");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(11));
                    // Speak associated less expression verbiage to selected category icon.
                    } else {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(10));
                        mFlgLess = 1;
                        //Firebase event
                        singleEvent("ExpressiveIcon","Less");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(10));
                    }
                }
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Tts Speak button.
     * When Tts speak button is pressed, broadcast speak request is sent to Text-to-speech service.
     * Message typed in Text-to-speech input view is speech out by service.</p>
     * */
    private void initTTsBtnListener() {
        mIvTTs.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                speakSpeech(mEtTTs.getText().toString());
                if(!mEtTTs.getText().toString().equals(""))
                    mIvTTs.setImageResource(R.drawable.speaker_pressed);
                //Firebase get log
                reportLog(getLocalClassName()+", TtsSpeak", Log.INFO);
                //Firebase event
                singleEvent("Keyboard", mEtTTs.getText().toString());
                //if expressive buttons always disabled during custom text speech output
                mIvLike.setEnabled(false);
                mIvDontLike.setEnabled(false);
                mIvMore.setEnabled(false);
                mIvLess.setEnabled(false);
                mIvYes.setEnabled(false);
                mIvNo.setEnabled(false);
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
     * <p>This function will changes the category icons state as per user touch.
     * @param view is parent view in selected RecyclerView.
     * @param v is parent relative layout of category icon tapped,
     * @param position is position of a tapped category icon in the RecyclerView.
     * This function
     *             a) Clear every expressive button flags
     *             b) Reset expressive button icons
     *             c) Reset category icon views
     *             d) Set the border to selected category icon
     *             e) If same category icon clicked twice that category will open up.
     *             f) Checks if, level two icons data set is available or not.</p>
     * */
    public void tappedCategoryItemEvent(final View view, View v, int position) {
        mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
        // reset all expressive button.
        resetExpressiveButtons(-1);
        // reset every populated category icon before setting the border to selected icon.
        resetRecyclerAllItems();
        mActionBtnClickCount = 0;
        // set border to selected category icon
        setMenuImageBorder(v, true);
        // set true to speak verbiage associated with category icon
        mShouldReadFullSpeech = true;
        String title = getActionBarTitle(position);
        getSupportActionBar().setTitle(title);
        if (mLevelOneItemPos == position) {
            // If icon sets are available for level two then open selected category in level two
            SessionManager session = new SessionManager(this);
            File langDir = new File("/data/data/com.dsource.idc.jellowintl/app_"+
                    session.getLanguage()+"/drawables");
            if(langDir.exists() && langDir.isDirectory()) {
                Bundle bundle = new Bundle();
                bundle.putString("Icon", mSpeechTxt[position]);
                bundle.putString("Level", "LevelOne");
                bundleEvent("Grid", bundle);
                Intent intent = new Intent(MainActivity.this, LevelTwoActivity.class);
                intent.putExtra("mLevelOneItemPos", position);
                intent.putExtra("selectedMenuItemPath", title + "/");
                startActivityForResult(intent, REQ_HOME);
            }
            langDir = null;
        }else {
            speakSpeech(mSpeechTxt[position]);
        }
        mLevelOneItemPos = mRecyclerView.getChildLayoutPosition(view);
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
        //Firebase get log
        reportLog(getLocalClassName()+" "+mLevelOneItemPos, Log.INFO);
    }

    /**
     * <p>This function will reset every category icons, expressive button tapped.
     * @param isHomePressed is set when user presses home from {@link MainActivity}
     * and resets when user presses home from {@link LevelTwoActivity},
     * {@link LevelThreeActivity}, {@link SequenceActivity}.</p>
     * */
    private void gotoHome(boolean isHomePressed) {
        getSupportActionBar().setTitle(getString(R.string.action_bar_title));
        // reset all expressive button flags.
        mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
        // reset expressive buttons
        mIvLike.setImageResource(R.drawable.ilikewithoutoutline);
        mIvDontLike.setImageResource(R.drawable.idontlikewithout);
        mIvYes.setImageResource(R.drawable.iwantwithout);
        mIvNo.setImageResource(R.drawable.idontwantwithout);
        mIvMore.setImageResource(R.drawable.morewithout);
        mIvLess.setImageResource(R.drawable.lesswithout);
        // reset category items
        resetRecyclerMenuItemsAndFlags(6);
        // clear verbiage speak (mShouldReadFullSpeech), border color flag (mFlgImage)
        mShouldReadFullSpeech = false;
        mFlgImage = -1;
        // close the keyboard is open
        if (mFlgKeyboardOpened){
            // When keyboard is open, close it and retain expressive button,
            // category icon states as they are before keyboard opened.
            mIvKeyboard.setImageResource(R.drawable.keyboard_button);
            mIvBack.setImageResource(R.drawable.back_button);
            mEtTTs.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mIvTTs.setVisibility(View.INVISIBLE);
            mFlgKeyboardOpened = false;
            changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
            mIvBack.setAlpha(.5f);
            mIvBack.setEnabled(false);
        }
        if(isHomePressed) {
            speakSpeech(mNavigationBtnTxt[0]);
            //Firebase event
            singleEvent("Navigation","Home");
            mIvHome.setImageResource(R.drawable.homepressed);
        }else
            mIvHome.setImageResource(R.drawable.home);
    }

    /**
     * <p>This function will set / reset action bar title depending on {@param showTitle} flag.
     * @param showTitle is set action bar title is set other wise empty title</p>
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
     * <p>This function will provide action bar title to be set.
     * @param position, position of the cateogory icon pressed.
     * @return the actionbarTitle string.</p>
     * */
    private String getActionBarTitle(int position) {
        String[] tempTextArr = getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
        return tempTextArr[position];
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
     *     a) Read verbiage lines into {@link LevelOneVerbiageModel} model.
     *     b) Read speech text from arrays for category icons.
     *     c) Read speech text from arrays for expressive buttons.
     *     d) Read speech text from arrays for navigation buttons.</p>
     * */
    private void loadArraysFromResources() {
        LevelOneVerbiageModel verbiageModel = new Gson()
                .fromJson(getString(R.string.levelOneVerbiage), LevelOneVerbiageModel.class);
        mLayerOneSpeech = verbiageModel.getVerbiageModel();
        //Firebase get log
        reportLog("Activity created", Log.INFO);
        mSpeechTxt = getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
        mExprBtnTxt = getResources().getStringArray(R.array.arrActionSpeech);
        mNavigationBtnTxt = getResources().getStringArray(R.array.arrNavigationSpeech);
    }

    /**
     * <p>This function will reset :
     *     a) Set expressive button pressed. {@param setPressedIcon} define the index of
     *     expressive button. e.g from top to bottom 0 - like button, 1 - don't like button
     *     b) Reset category icons pressed.</p>
     * */
    private void resetRecyclerMenuItemsAndFlags(int setPressedIcon) {
        resetExpressiveButtons(setPressedIcon);
        mLevelOneItemPos = -1;
        resetRecyclerAllItems();
        mActionBtnClickCount = 0;
    }

    /**
     * <p>This function reset the border for all category icons that are populated
     * in recycler view.</p>
     * */
    private void resetRecyclerAllItems() {
        for(int i = 0; i< mRecyclerView.getChildCount(); ++i){
            setMenuImageBorder(mRecyclerView.getChildAt(i), false);
        }
    }

    /**
     * <p>This function enable or disable the expressive buttons using {@param setDisable}:
     * {@param setDisable}, if setDisable = true, buttons are disabled otherwise
     * enabled.</p>
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
     * <p>This function set the border to category icons. This function first extracts the view to
     * which border should applied then apply the border.
     * {@param recyclerChildView} is a parent view extracted from recycler view when category icon is tapped.
     * {@param setBorder} is set if category icon tapped and a color border should appear on the view other wise
     *  transparent color is set to border.
     * </p>
     * */
    private void setMenuImageBorder(View recyclerChildView, boolean setBorder) {
        GradientDrawable gd = (GradientDrawable) recyclerChildView.findViewById(R.id.borderView).getBackground();
        if(setBorder){
            // mActionBtnClickCount = 0, brown color border is set.
            if (mActionBtnClickCount > 0) {
                // mFlgImage define color of border.
                switch (mFlgImage) {
                    case 0: gd.setColor(ContextCompat.getColor(this,R.color.colorLike)); break;
                    case 1: gd.setColor(ContextCompat.getColor(this,R.color.colorDontLike)); break;
                    case 2: gd.setColor(ContextCompat.getColor(this,R.color.colorYes)); break;
                    case 3: gd.setColor(ContextCompat.getColor(this,R.color.colorNo)); break;
                    case 4: gd.setColor(ContextCompat.getColor(this,R.color.colorMore)); break;
                    case 5: gd.setColor(ContextCompat.getColor(this,R.color.colorLess)); break;
                }
            } else
                gd.setColor(ContextCompat.getColor(this,R.color.colorSelect));
        } else
            gd.setColor(ContextCompat.getColor(this,android.R.color.transparent));
    }

    /**
     * <p>This function first reset the all expressive button. Then set expressive button
     * using image_flag.
     * {@param image_flag} is a index of expressive button.
     *  e.g. From top to bottom 0 - like button, 1 - don't like button likewise.</p>
     * */
    private void resetExpressiveButtons(int image_flag) {
        mIvLike.setImageResource(R.drawable.ilikewithoutoutline);
        mIvDontLike.setImageResource(R.drawable.idontlikewithout);
        mIvYes.setImageResource(R.drawable.iwantwithout);
        mIvNo.setImageResource(R.drawable.idontwantwithout);
        mIvMore.setImageResource(R.drawable.morewithout);
        mIvLess.setImageResource(R.drawable.lesswithout);
        mIvHome.setImageResource(R.drawable.home);
        switch (image_flag){
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
     * <p>This function send the broadcast message to Text-to-speech service about
     * requesting current Text-to-speech engine language. This function is only used in
     * devices below Lollipop (api less than 21).
     * To get only, which TTs language is as a broadcast response, in {@param saveLanguage}
     * param empty string is set. While to save selected user language, in {@param saveLanguage}
     * param current app language is set.</p>
     * */
    private void getSpeechLanguage(String saveLanguage){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_REQ");
        intent.putExtra("saveSelectedLanguage", saveLanguage);
        sendBroadcast(intent);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellowintl.SPEECH_TTS_ERROR":
                    // Text synthesize process failed third time then show TTs error.
                    if(++mTTsNotWorkingCount > 2)
                        Toast.makeText(context, getString(R.string.txt_actLangSel_completestep2),
                                Toast.LENGTH_LONG).show();
                    break;
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES":
                    SessionManager session = new SessionManager(MainActivity.this);
                    String userLang = session.getLanguage();
                    session.setLangSettingIsCorrect(true);
                    String mSysTtsReg = intent.getStringExtra("systemTtsRegion");
                    // If App language and Text-to-speech language are different then show error toast.
                    if((userLang.equals("en-rIN") && !mSysTtsReg.equals("hi-rIN"))
                            || (!userLang.equals("en-rIN") && !userLang.equals(mSysTtsReg))) {
                        Toast.makeText(context, getString(R.string.speech_engin_lang_sam),
                                Toast.LENGTH_LONG).show();
                        session.setLangSettingIsCorrect(false);
                    }
                    break;
            }
        }
    };
}