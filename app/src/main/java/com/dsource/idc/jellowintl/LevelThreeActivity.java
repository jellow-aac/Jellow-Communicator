package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dsource.idc.jellowintl.Models.LevelThreeVerbiageModel;
import com.dsource.idc.jellowintl.Utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.Utility.IndexSorter;
import com.dsource.idc.jellowintl.Utility.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.dsource.idc.jellowintl.Utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.Utility.Analytics.reportException;
import static com.dsource.idc.jellowintl.Utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.Utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.Utility.Analytics.stopMeasuring;

public class LevelThreeActivity extends AppCompatActivity {
    private final boolean DISABLE_EXPR_BTNS = true;

    /* This flags are used to identify respective expressive button is pressed either
      once or twice. eg. mFlgLike used to identify Like expressive button pressed once or twice.*/
    private int mFlgLike = 0, mFlgYes = 0, mFlgMore = 0, mFlgDntLike = 0, mFlgNo = 0,
            mFlgLess = 0;
    /* This flag identifies which expressive button is pressed.*/
    private int mFlgImage = -1;
    /* This flag indicates keyboard is open or not, 0 indicates is not open.*/
    private int mFlgKeyboard = 0;
    /* This flag identifies that user is pressed a category icon and which border should appear
      on pressed category icon. If flag value = 0, then brown (initial border) will appear.*/
    private int mActionBtnClickCount;
    /*Image views which are visible on the layout such as six expressive buttons, mNavigationBtnTxt navigation
      buttons and speak button when keyboard is open.*/
    private ImageView mIvLike, mIvDontLike, mIvYes, mIvNo, mIvMore, mIvLess,
            mIvHome, mIvKeyboard, mIvBack, mIvTTs;
    /*Input text view to speak custom text.*/
    private EditText mEtTTs;
    private KeyListener originalKeyListener;
    /*This variable hold the views populated in recycler view (category icon) list.*/
    private RecyclerView mRecyclerView;
    /*This variable indicates index of category icon selected in level one, two and three respectively*/
    private int mLevelOneItemPos, mLevelTwoItemPos, mLevelThreeItemPos = -1;
    /*This variable indicates index of category icon in adapter in level 1. This variable is
     different than mLevelOneItemPos. */
    private int mSelectedItemAdapterPos = -1;
    /*This flag is sets to true, when expressive button is pressed in conjunction with
     category icon. It is useful to produce speech output either expression of a button or
      full sentence verbiage.*/
    private boolean mShouldReadFullSpeech = false;
    /*This variable hold the views populated in recycler view (category icon) list.*/
    private ArrayList<View> mRecyclerItemsViewList;
    /*This variable stores current tap mArrIconTapCount to every category icon populated in recycler view.*/
    private Integer[] mArrIconTapCount = new Integer[100];
    /*This variable stores array of index of category icons sorted by tap count*/
    private int[] mArrSort = new int[100];
    private int count_flag = 0;
    private DataBaseHelper myDbHelper;
    /*Below array stores the speech text, below text, expressive button speech text,
     navigation button speech text respectively.*/
    private String[] mSpeechTxt, mExprBtnTxt, mNavigationBtnTxt;
    /*Below list stores the verbiage that are spoken when category icon + expression buttons
    pressed in conjunction*/
	ArrayList <ArrayList <String>> mNewVerbTxt;
    private String actionBarTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(getIntent().getExtras().getString("selectedMenuItemPath"));
        if(findViewById(R.id.parent).getTag().toString().equals("large"))
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                        WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // set app locale which is set in settings by user.
        new ChangeAppLocale(this).setLocale();
        myDbHelper = new DataBaseHelper(this);
        mLevelOneItemPos = getIntent().getExtras().getInt("mLevelOneItemPos");
        mLevelTwoItemPos = getIntent().getExtras().getInt("mLevelTwoItemPos");
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        loadArraysFromResources();

        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            reportException(e);
        }

        mRecyclerItemsViewList = new ArrayList<>(100);
        while(mRecyclerItemsViewList.size() <= 100) mRecyclerItemsViewList.add(null);
        initializeLayoutViews();
        initializeRecyclerViewAdapter();
        initializeViewListeners();
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
        stopMeasuring("LevelThreeActivity");
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    protected void onDestroy() {
        mRecyclerView = null;
        mRecyclerItemsViewList = null;
        mSpeechTxt = mExprBtnTxt = mNavigationBtnTxt = null;
        mArrSort = null; mArrIconTapCount = null;
        myDbHelper = null;
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
        switch(item.getItemId()) {
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
        mIvKeyboard = findViewById(R.id.keyboard);
        mIvKeyboard.setContentDescription(mNavigationBtnTxt[2]);
        mEtTTs = findViewById(R.id.et);
        mEtTTs.setContentDescription(getString(R.string.string_to_speak));
        mEtTTs.setVisibility(View.INVISIBLE);
        mIvTTs = findViewById(R.id.ttsbutton);
        mIvTTs.setContentDescription(getString(R.string.speak_written_text));
        mIvTTs.setVisibility(View.INVISIBLE);
        originalKeyListener = mEtTTs.getKeyListener();
        // Set it to null - this will make to the field non-editable
        mEtTTs.setKeyListener(null);

        mRecyclerView = findViewById(R.id.recycler_view);
        // Initiate 3 columns in Recycler View.
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
    }

    /**
     * <p>This function will initialize the adapter for recycler view.
     * As per the category icon selected in the level one {@link MainActivity} and
     * level two {@link LevelTwoActivity}, category icons are populated in this level.
     * Also, if a category uses preferences then in that category icons are arranged
     * using preferences.</p>
     * */
    private void initializeRecyclerViewAdapter() {
        // get tap mArrIconTapCount string store in SQlite db for given category.
        String savedString = myDbHelper.getlevel(mLevelOneItemPos, mLevelTwoItemPos);
        // saved string is != "false" then load level three category icons with sort
        if (!savedString.equals("false")) {
            count_flag = 1;
            StringTokenizer st = new StringTokenizer(savedString, ",");

            // Temporary array of user taps on category icons
            mArrIconTapCount = new Integer[mNewVerbTxt.size()];
            for (int j = 0; j < mNewVerbTxt.size(); j++) {
                mArrIconTapCount[j] = Integer.parseInt(st.nextToken());
            }
            IndexSorter<Integer> is = new IndexSorter<Integer>(mArrIconTapCount);
            is.sort();
            Integer[] indexes = new Integer[mNewVerbTxt.size()];

            int g = 0;
            for (Integer ij : is.getIndexes()) {
                indexes[g] = ij;
                g++;
            }

            for (int j = 0; j < mArrIconTapCount.length; j++)
                mArrSort[j] = indexes[j];
            retrieveSpeechArrays(mLevelOneItemPos, mLevelTwoItemPos);
            mRecyclerView.setAdapter(new LevelThreeAdapter(this, mLevelOneItemPos,
                    mLevelTwoItemPos, mArrSort));
        // saved string is == "false" then load level three category icons without sort
        // Categories:
        // Fun -> TV, fun -> Music,
        // Time & weather -> (Time, Day, month, Weather, Season) and
        // Learning -> Money, should load always without sorting
        } else if ((mLevelOneItemPos == 3 && (mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) ||
                (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 ||
                        mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4))
                || (mLevelOneItemPos == 4 && mLevelTwoItemPos == 9)) {
            retrieveSpeechArrays(mLevelOneItemPos, mLevelTwoItemPos);
            for (int i=0; i< mNewVerbTxt.size();i++)
                mArrSort[i] = i;
            mRecyclerView.setAdapter(new LevelThreeAdapter(this, mLevelOneItemPos,
                    mLevelTwoItemPos, mArrSort));
        } else {
            count_flag = 0;
            retrieveSpeechArrays(mLevelOneItemPos, mLevelTwoItemPos);
        }
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
     * <p>This function initializes {@link RecyclerTouchListener} and
     * {@link RecyclerView.OnChildAttachStateChangeListener} for recycler view.
     * {@link RecyclerTouchListener} is a custom defined Touch event listener class.
     * {@link RecyclerView.OnChildAttachStateChangeListener} is defined to efficiently handle
     * item state of recycler child, when attached to or detached from recycler view. </p>
     * */
    private void initRecyclerViewListeners() {
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
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
            @Override public void onLongClick(View view, int position) {}
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
     * When pressed navigation back button:
     *  a) If keyboard is open, keyboard is closed.
     *  b) If keyboard is not open, current level is closed returning successful
     *  closure (RESULT_OK) of a screen. User will returned back to {@link MainActivity}.
     * </p>
     * */
    private void initBackBtnListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(mNavigationBtnTxt[1]);
                mIvTTs.setImageResource(R.drawable.speaker_button);
                mIvBack.setImageResource(R.drawable.backpressed);
                if (mFlgKeyboard == 1) {
                    // When keyboard is open, close it and retain expressive button,
                    // category icon states as they are before keyboard opened.
                    mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                    mIvBack.setImageResource(R.drawable.backpressed);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 0;
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    if(mLevelThreeItemPos != -1) retainExpressiveButtonStates();
                } else {
                    // When keyboard is not open simply set result and close the activity.
                    mIvBack.setImageResource(R.drawable.backpressed);
                    setResult(RESULT_OK);
                    finish();
                }
                singleEvent("Navigation","Back");
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
                singleEvent("Navigation","Home");
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
     * {@link LevelThreeActivity} navigation keyboard button either enable or disable
     * the keyboard layout.
     * When keyboard layout is enabled using keyboard button, it is visible to user and
     * action bar title set to "keyboard".
     * When keyboard layout is disabled using keyboard button, the state of the
     * {@link LevelThreeActivity} retrieved as it was before opening keyboard layout.
     * */
    private void initKeyboardBtnListener() {
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mNavigationBtnTxt[2]);
                mIvTTs.setImageResource(R.drawable.speaker_button);
                if (mFlgKeyboard == 1) {
                    // When keyboard is open, close it and retain expressive button,
                    // category icon states as they are before keyboard opened.
                    mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 0;
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    if(mLevelThreeItemPos != -1) retainExpressiveButtonStates();
                    showActionBarTitle(true);
                } else {
                    // When keyboard is not open, open the keyboard. Hide category icons, show
                    // custom speech input text, speak button and disable expressive buttons.
                    mIvKeyboard.setImageResource(R.drawable.keyboardpressed);
                    mEtTTs.setVisibility(View.VISIBLE);
                    mEtTTs.setKeyListener(originalKeyListener);
                    // Focus to the field.
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
                    mEtTTs.requestFocus();
                    mIvTTs.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
                // All expressive button speech flags (except like) are set to reset.
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
                        singleEvent("ExpressiveIcon","ReallyLike");
                    // if value of mFlgLike is 0, then should speak "like".
                    } else {
                        speakSpeech(mExprBtnTxt[0]);
                        mFlgLike = 1;
                        singleEvent("ExpressiveIcon","mIvLike");
                    }
                } else {
                    // if value of mShouldReadFullSpeech is true, then it should speak associated like
                    // expression verbiage to selected category icon.
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgLike is 1, then should speak "really like" expression
                    // verbiage associated to selected category icon.
                    if (mFlgLike == 1) {
                        singleEvent("ExpressiveIcon","ReallyLike");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(1));
                        else {
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(1));
                        }
                        //reset mFlgLike to speak "like" expression
                        mFlgLike = 0;
                    // if value of mFlgLike is 0, then should speak "like" expression
                    // verbiage associated to selected category icon.
                    } else {
                        singleEvent("ExpressiveIcon","mIvLike");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(0));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(0));
                        //reset mFlgLike to speak "really like" expression
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
                // All expressive button speech flags (except dont like) are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgNo = mFlgLess = 0;

                //Value of mFlgImage = 1, indicates dont like expressive button is pressed
                mFlgImage = 1;

                //Set expressive button
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgDntLike is 1, then should speak "really dont like".
                    if (mFlgDntLike == 1) {
                        speakSpeech(mExprBtnTxt[7]);
                        mFlgDntLike = 0;
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                    // if value of mFlgDntLike is 0, then should speak " dont like".
                    } else {
                        speakSpeech(mExprBtnTxt[6]);
                        mFlgDntLike = 1;
                        singleEvent("ExpressiveIcon","Don'tLike");
                    }
                } else {
                    // if value of mShouldReadFullSpeech is true, then it should speak associated
                    // dont like expression verbiage to selected category icon.
                    ++mActionBtnClickCount;

                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);

                    // if value of mFlgDntLike is 1, then should speak "really don't like" expression
                    // verbiage associated to selected category icon.
                    if (mFlgDntLike == 1) {
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(7));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(7));
                        //reset mFlgDntLike to speak "dont like" expression
                        mFlgDntLike = 0;
                    // if value of mFlgDntLike is 0, then should speak "dont like" expression
                    //  verbiage associated to selected category icon.
                    } else {
                        singleEvent("ExpressiveIcon","Don'tLike");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(6));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(6));
                        //reset mFlgDntLike to speak "really don't like" expression
                        mFlgDntLike = 1;
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
                // All expressive button speech flags (except yes) are set to reset.
                mFlgLike = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;

                //Value of mFlgImage = 2, indicates yes expressive button is pressed
                mFlgImage = 2;

                // sets expressive button
                resetExpressiveButtons(mFlgImage);

                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgYes is 1, then should speak "really yes".
                    if (mFlgYes == 1) {
                        speakSpeech(mExprBtnTxt[3]);
                        mFlgYes = 0;
                        singleEvent("ExpressiveIcon","ReallyYes");
                    // if value of mFlgYes is 0, then should speak "yes".
                    } else {
                        speakSpeech(mExprBtnTxt[2]);
                        mFlgYes = 1;
                        singleEvent("ExpressiveIcon","Yes");
                    }
                // if value of mShouldReadFullSpeech is true, then app should speak associated yes
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgYes is 1, then should speak "really yes" expression
                    // verbiage associated to selected category icon.
                    if (mFlgYes == 1) {
                        singleEvent("ExpressiveIcon","ReallyYes");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(3));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(3));
                        //reset mFlgYes to speak "yes" expression
                        mFlgYes = 0;
                    // if value of mFlgYes is 0, then should speak "yes" expression
                    // verbiage associated to selected category icon.
                    } else {
                        singleEvent("ExpressiveIcon","Yes");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(2));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(2));
                        //reset mFlgYes to speak "really yes" expression
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
                // All expressive button speech flags (except no) are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgLess = 0;

                //Value of mFlgImage = 3, indicates no expressive button is pressed
                mFlgImage = 3;

                // Sets expressive button
                resetExpressiveButtons(mFlgImage);

                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgNo is 1, then should speak "really no".
                    if (mFlgNo == 1) {
                        speakSpeech(mExprBtnTxt[9]);
                        mFlgNo = 0;
                        singleEvent("ExpressiveIcon","ReallyNo");
                    // if value of mFlgNo is 0, then should speak "no".
                    } else {
                        speakSpeech(mExprBtnTxt[8]);
                        mFlgNo = 1;
                        singleEvent("ExpressiveIcon","No");
                    }
                // if value of mShouldReadFullSpeech is true, then it should speak associated no
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgNo is 1, then should speak "really no" expression
                    // verbiage associated to selected category icon.
                    if (mFlgNo == 1) {
                        singleEvent("ExpressiveIcon","ReallyNo");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(9));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(9));
                        //reset mFlgNo to speak "no" expression
                        mFlgNo = 0;
                    // if value of mFlgNo is 0, then should speak "no" expression
                    // verbiage associated to selected category icon.
                    } else {
                        singleEvent("ExpressiveIcon","No");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(8));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(8));
                        //reset mFlgLike to speak "really no" expression
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
                // All expressive button speech flags (except more) are set to reset.
                mFlgLike = mFlgYes = mFlgDntLike = mFlgNo = mFlgLess = 0;

                //Value of mFlgImage = 4, indicates more expressive button is pressed
                mFlgImage = 4;

                // Sets expressive button
                resetExpressiveButtons(mFlgImage);

                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgMore is 1, then should speak "really more".
                    if (mFlgMore == 1) {
                        speakSpeech(mExprBtnTxt[5]);
                        mFlgMore = 0;
                        singleEvent("ExpressiveIcon","ReallyMore");
                    // if value of mFlgMore is 0, then should speak "more".
                    } else {
                        speakSpeech(mExprBtnTxt[4]);
                        mFlgMore = 1;
                        singleEvent("ExpressiveIcon","More");
                    }
                // if value of mShouldReadFullSpeech is true, then it should speak associated like
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;

                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgMore is 1, then should speak "really more" expression
                    // verbiage associated to selected category icon.
                    if (mFlgMore == 1) {
                        singleEvent("ExpressiveIcon","ReallyMore");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(5));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(5));
                        //reset mFlgMore to speak "more" expression
                        mFlgMore = 0;
                    // if value of mFlgMore is 0, then should speak "more" expression
                    // verbiage associated to selected category icon.
                    } else {
                        singleEvent("ExpressiveIcon","More");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(4));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(4));
                        //reset mFlgMore to speak "really more" expression
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
                // All expressive button speech flags (except less) are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = 0;
                //Value of mFlgImage = 5, indicates less expressive button is pressed

                mFlgImage = 5;

                // Sets expressive button
                resetExpressiveButtons(mFlgImage);

                // if value of mShouldReadFullSpeech is false, then app should speak only expressive button name.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgLess is 1, then should speak "really less".
                    if (mFlgLess == 1) {
                        speakSpeech(mExprBtnTxt[11]);
                        mFlgLess = 0;
                        singleEvent("ExpressiveIcon","ReallyLess");
                    // if value of mFlgLess is 0, then should speak "less".
                    } else {
                        speakSpeech(mExprBtnTxt[10]);
                        mFlgLess = 1;
                        singleEvent("ExpressiveIcon","Less");
                    }
                } else {
                    // if value of mShouldReadFullSpeech is true, then it should speak associated like
                    // expression verbiage to selected category icon.
                    ++mActionBtnClickCount;
                    // Set border to category icon, border color is applied using
                    // mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgLess is 1, then should speak "really less" expression
                    // verbiage associated to selected category icon.
                    if (mFlgLess == 1) {
                        singleEvent("ExpressiveIcon","ReallyLess");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(11));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(11));
                        //reset mFlgLess to speak "less" expression
                        mFlgLess = 0;
                    // if value of mFlgLess is 0, then should speak "less" expression
                    // verbiage associated to selected category icon.
                    } else {
                        singleEvent("ExpressiveIcon","Less");

                        if (count_flag == 1)
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(10));
                        else
                            speakSpeech(mNewVerbTxt.get(mArrSort[mLevelThreeItemPos]).get(10));
                        //reset mFlgLess to speak "really less" expression
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
                singleEvent("Keyboard", mEtTTs.getText().toString());
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
                    // Hit the soft mIvKeyboard.
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
     *             e) Increment preference mArrIconTapCount of tapped category icon.</p>
     * */
    public void tappedCategoryItemEvent(View view, View v, int position) {
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
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
        mLevelThreeItemPos = mRecyclerView.getChildLayoutPosition(view);
        // categories which does not have preferences enabled, their speech text directly
        // retained from speech array otherwise speech text retained using sort array
        // then text is sent to synthesis.
        if ((mLevelOneItemPos == 3 && (mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) ||
                (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 ||
                        mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) ||
                (mLevelOneItemPos == 4 && mLevelTwoItemPos == 9))
            speakSpeech(mSpeechTxt[mLevelThreeItemPos]);
        else
            speakSpeech(mSpeechTxt[mArrSort[mLevelThreeItemPos]]);

        // increment category item touch count
        incrementTouchCountOfItem(mLevelThreeItemPos);
        // retain state of expressive button when particular type category icon pressed
        retainExpressiveButtonStates();
        Bundle bundle = new Bundle();
        bundle.putString("Icon", mSpeechTxt[position]);
        bundle.putString("Level","LevelThree");
        bundleEvent("Grid",bundle);

        mIvBack.setImageResource(R.drawable.back_button);
    }

    /**
     * <p>This function will display/ hide action bar title.
     * If {@param showTitle} is set then title is displayed otherwise not.</p>
     * */
    private void showActionBarTitle(boolean showTitle){
        if (showTitle)
            getSupportActionBar().setTitle(actionBarTitleTxt);
        else{
            actionBarTitleTxt = getSupportActionBar().getTitle().toString();
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
     *     c) Read verbiage lines into {@link LevelThreeVerbiageModel} model.</p>
     * */
    private void loadArraysFromResources() {
        mExprBtnTxt = getResources().getStringArray(R.array.arrActionSpeech);
        mNavigationBtnTxt = getResources().getStringArray(R.array.arrNavigationSpeech);
        String str = getResources().getString(R.string.levelThreeVerbiage);
        LevelThreeVerbiageModel mLevelThreeVerbiageModel = new Gson().
                fromJson(str, LevelThreeVerbiageModel.class);
        mNewVerbTxt = mLevelThreeVerbiageModel.getVerbiageModel()
                                         .get(mLevelOneItemPos).get(mLevelTwoItemPos);
    }

    /**
     * <p>In level three, in various category icons have multiple expressive buttons are disabled.
     * This function will retain states of expressive buttons back to original using category icons
     * selected in {@link MainActivity} and in {@link LevelTwoActivity}.</p>
     * */
    private void retainExpressiveButtonStates() {
        //if category Greet and feel -> feelings is selected
        if(mLevelOneItemPos == 0 && mLevelTwoItemPos == 1){
            int tmp = mArrSort[mLevelThreeItemPos];
            // if in feeling category icon having index "tmp" = 0 then disable no expressive button
            // and keep enable yes and other button
            if(tmp == 0){
                mIvNo.setAlpha(0.5f);
                mIvNo.setEnabled(false);
                mIvYes.setAlpha(1f);
                mIvYes.setEnabled(true);
            // if in feeling category icon having index "tmp" = 1/2/3/5/6/7/8/9/10/11/12/15 & 16
            // then disable yes expressive button and keep enable no and other button
            } else if (tmp == 1 || tmp == 2 || tmp == 3 || tmp == 5 || tmp == 6 || tmp == 7 ||
                    tmp == 8 || tmp == 9 || tmp == 10 || tmp == 11 || tmp == 12 || tmp == 15 ||
                    tmp == 16) {
                mIvYes.setAlpha(0.5f);
                mIvYes.setEnabled(false);
                mIvNo.setAlpha(1f);
                mIvNo.setEnabled(true);
            // if in feeling category icon having index other than "tmp"  enable each expressive button
            } else
                changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
        // if in feeling -> greetings, feeling -> requests and  feeling -> questions
        // disable all expressive buttons
        }else if(mLevelOneItemPos == 0 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 2 ||
                mLevelTwoItemPos == 3))
            changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
        else if(mLevelOneItemPos == 1 && mLevelTwoItemPos == 3){
            int tmp = mArrSort[mLevelThreeItemPos];
            String lang = new SessionManager(this).getLanguage();
            // if in daily activities -> clothes and accessories
            // and app language is  Hindi, English (India) and temp = 34/35/36/37 or
            // app language is other than English (US, UK) and temp = 39/40/41/42 then
            //disable all expressive buttons
            if ((!lang.equals("en-rUS") && (tmp == 34 || tmp == 35 || tmp == 36 || tmp == 37))||
                    ((lang.equals("en-rUS") || lang.equals("en-rGB")) && (tmp == 39 || tmp == 40 ||
                            tmp == 41 || tmp == 42) ))
                changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
            else
                changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
        //if in time and weather -> time
        //  time and weather -> day
        //  time and weather -> month,
        //  time and weather -> weather
        // in each above category first category icon is will have expressive button disables.
        } else if (mLevelOneItemPos == 7 && (mLevelTwoItemPos == 0 || mLevelTwoItemPos == 1 ||
                mLevelTwoItemPos == 2 || mLevelTwoItemPos == 3 || mLevelTwoItemPos == 4)) {
            if (mLevelThreeItemPos == 0)
                changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
            else
                changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
        }else
            changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
    }

    /**
     * <p>This function enable or disable the expressive buttons using {@param setDisable}.
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
     * using image flag.
     * @param image_flag, is a index of expressive button.
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
     * <p>This function reset the border for all category icons that are populated
     * in recycler view.</p>
     * */
    private void resetRecyclerAllItems() {
        for(int i = 0; i< mRecyclerView.getChildCount(); ++i){
            setMenuImageBorder(mRecyclerView.getChildAt(i), false);
        }
    }

    /**
     * <p>This function increment the touch mArrIconTapCount for the selected category icon.
     * In {@link LevelThreeActivity}, several categories has enabled preferences.
     * This function will store these preferences into local SQLite database.</p>
     * */
    private void incrementTouchCountOfItem(int levelThreeItemPos) {
        if (count_flag == 1) {
            mArrIconTapCount[mArrSort[levelThreeItemPos]] =
                    mArrIconTapCount[mArrSort[levelThreeItemPos]] + 1;
            StringBuilder str = new StringBuilder();
            // increment touch count of tapped category icon
            for(int i = 0; i< mArrIconTapCount.length; ++i)
                str.append(mArrIconTapCount[i]).append(",");
            // append 0 to end of string till total number of token reach to 100
            for(int i = mArrIconTapCount.length; i < 100; ++i)
                str.append("0,");
            // store count string into database
            myDbHelper.setlevel(mLevelOneItemPos, mLevelTwoItemPos, str.toString());
        }
    }

    /**
     * <p>This function retrieve speech text array from arrays
     * resource using category icon index selected in {@link MainActivity} and
     * {@link LevelTwoActivity}.
     * @param levelOneItemPos is a index of category icon selected in
     * {@link MainActivity}.
    * @param levelTwoItemPos is a index of category icon selected in
     * {@link LevelTwoActivity}.</p>
     * */
    private void retrieveSpeechArrays(int levelOneItemPos, int levelTwoItemPos) {
        if (levelOneItemPos == 0) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingsSpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsSpeechText);
                    break;
            }
        } else if (levelOneItemPos == 1) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActBrushingSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActToiletSpeechText);
                    break;
                case 2:
                    mSpeechTxt =  getResources().getStringArray(R.array.arrLevelThreeDailyActBathingSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActClothesAccSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadySpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActSleepSpeechText);
                    break;
                case 6:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActTherapySpeechText);
                    break;
                case 7:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActMorningScheSpeechText);
                    break;
                case 8:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeDailyActBedTimeScheSpeechText);
                    break;
            }
        } else if (levelOneItemPos == 2) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinnerSpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsSpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksSpeechText);
                    break;
                case 6:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutlerySpeechText);
                    break;
                case 7:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonSpeechText);
                    break;
            }
        } else if (levelOneItemPos == 3){
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunInDGamesSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesSpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunSportsSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunTvSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunMusicSpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeFunActivitiesSpeechText);
                    break;
            }
        } else if (levelOneItemPos == 4) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsSpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningBooksSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningColorsSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningShapesSpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningStationarySpeechText);
                    break;
                case 6:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningSchoolObjSpeechText);
                    break;
                case 7:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningHomeObjSpeechText);
                    break;
                case 8:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningTransportSpeechText);
                    break;
                case 9:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeLearningMoneySpeechText);
                    break;
            }
        } else if (levelOneItemPos == 7) {
            switch(levelTwoItemPos){
                case 0:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeSpeechText);
                    break;
                case 1:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaDaySpeechText);
                    break;
                case 2:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthSpeechText);
                    break;
                case 3:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherSpeechText);
                    break;
                case 4:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsSpeechText);
                    break;
                case 5:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestSpeechText);
                    break;
                case 6:
                    mSpeechTxt = getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysSpeechText);
                    break;
            }
        }
    }
}