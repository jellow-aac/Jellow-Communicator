package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.dsource.idc.jellowintl.Models.LevelTwoVerbiageModel;
import com.dsource.idc.jellowintl.Utility.ChangeAppLocale;
import com.dsource.idc.jellowintl.Utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.Utility.IndexSorter;
import com.dsource.idc.jellowintl.Utility.SessionManager;
import com.dsource.idc.jellowintl.Utility.ToastWithCustomTime;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import static com.dsource.idc.jellowintl.Utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.Utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.Utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.Utility.Analytics.stopMeasuring;

public class LevelTwoActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private final int REQ_HOME = 0;
    private final int CATEGORY_ICON_PEOPLE = 5;
    private final int CATEGORY_ICON_PLACES = 6;
    private final int CATEGORY_ICON_HELP = 8;
    private final boolean DISABLE_EXPR_BTNS = true;

    private int mFlgLike = 0, mFlgYes = 0, mFlgMore = 0, mFlgDntLike = 0, mFlgNo = 0,
            mFlgLess = 0;
    private int mFlgImage = -1, mFlgKeyboard = 0, mActionBtnClickCount;
    private ImageView mIvLike, mIvDontLike, mIvYes, mIvNo, mIvMore, mIvLess,
            mIvHome, mIvKeyboard, mIvBack, mIvTts;
    private EditText mEtTTs;
    private KeyListener originalKeyListener;
    private RecyclerView mRecyclerView;
    private LinearLayout mMenuItemLinearLayout;
    private int mLevelOneItemPos, mLevelTwoItemPos = -1, mSelectedItemAdapterPos = -1;
    private boolean mShouldReadFullSpeech = false;
    private ArrayList<View> mRecyclerItemsViewList;
    private String mActionBarTitle;
    private SessionManager mSession;

    private String[] mLevelTwoSpeechText, mLevelTwoAdapterText,side, below;
    private ArrayList<ArrayList<ArrayList<String>>> mLayerTwoSpeech;
    private String[] mArrSpeechText, mArrAdapterTxt;

    private String[] mAdapterTextArray = new String[100];

    private String[] mIconArray = new String[100];
    private Integer[] mIconTapCount = new Integer[100];
    private String[] mIconArrayNew = new String[100];
    private Integer[] mSortArray = new Integer[100];

    /*private ArrayList<String> mIconList, mIconListNew;
    private ArrayList<Integer> mIconTapCountList,mSortList;*/

    private String end, actionBarTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        mLevelOneItemPos = getIntent().getExtras().getInt("mLevelOneItemPos");
        getSupportActionBar().setTitle(getIntent().getExtras().getString("selectedMenuItemPath"));
        if(findViewById(R.id.parent).getTag().toString().equals("large"))
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        new ChangeAppLocale(this).setLocale();
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        loadArraysFromResources();

        mSession = new SessionManager(this);
        initializeArrayListOfRecycler();
        initializeLayoutViews();
        initializeRecyclerViewAdapter();
        initializeViewListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMeasuring();
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMeasuring("LevelTwoActivity");
        new ChangeAppLocale(this).setLocale();
    }

    @Override
    protected void onDestroy() {
        mRecyclerView = null;
        mLayerTwoSpeech = null;
        mRecyclerItemsViewList = null;
        mMenuItemLinearLayout = null;
        mLevelTwoSpeechText = null;
        mLevelTwoAdapterText = null;
        side = null; below = null;
        mAdapterTextArray = null;
        mIconArray = null;
        mIconTapCount = null;
        mIconArrayNew = null;
        mSortArray = null;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_HOME && resultCode == RESULT_CANCELED)
            finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String Permissions[], int[] grantResults){
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //startCall("tel:" + mSession.getFather_no());
            } else {
                Toast.makeText(this, "Call permission was denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * <p>This function will initialize the views that are populated on the activity layout.
     * It also assigns content description to the views to enable speech in
     * Talk-back feature. The Talk-back feature is not available in this version.</p>
     * */
    private void initializeLayoutViews() {
        mIvLike = findViewById(R.id.ivlike);
        mIvLike.setContentDescription(side[0]);
        mIvDontLike = findViewById(R.id.ivdislike);
        mIvDontLike.setContentDescription(side[6]);
        mIvMore = findViewById(R.id.ivadd);
        mIvMore.setContentDescription(side[4]);
        mIvLess = findViewById(R.id.ivminus);
        mIvLess.setContentDescription(side[10]);
        mIvYes = findViewById(R.id.ivyes);
        mIvYes.setContentDescription(side[2]);
        mIvNo = findViewById(R.id.ivno);
        mIvNo.setContentDescription(side[8]);
        mIvHome = findViewById(R.id.ivhome);
        mIvHome.setContentDescription(below[0]);
        mIvBack = findViewById(R.id.ivback);
        mIvBack.setContentDescription(below[1]);
        mIvBack.setAlpha(1f);
        mIvKeyboard = findViewById(R.id.keyboard);
        mIvKeyboard.setContentDescription(below[2]);
        mEtTTs = findViewById(R.id.et);
        mEtTTs.setContentDescription(getString(R.string.string_to_speak));
        mEtTTs.setVisibility(View.INVISIBLE);

        mIvTts = findViewById(R.id.ttsbutton);
        mIvTts.setContentDescription(getString(R.string.speak_written_text));
        mIvTts.setVisibility(View.INVISIBLE);

        originalKeyListener = mEtTTs.getKeyListener();
        // Set it to null - this will make the field non-editable
        mEtTTs.setKeyListener(null);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager( new GridLayoutManager(this, 3));
        mRecyclerView.setVerticalScrollBarEnabled(true);
        mRecyclerView.setScrollbarFadingEnabled(false);
    }

    /**
     * <p>This function will initialize the adapter for recycler view. In this level two
     * types of adapter are used:
     *      a) {@link PeoplePlacesAdapter}
     *      b) {@link LevelTwoAdapter}
     * As per the category icon selected in the {@link MainActivity}, an adapter is selected and hence
     * category icons are populated in this level.</p>
     * */
    private void initializeRecyclerViewAdapter() {
        if(mLevelOneItemPos != CATEGORY_ICON_PEOPLE && mLevelOneItemPos != CATEGORY_ICON_PLACES) {
            mRecyclerView.setAdapter(new LevelTwoAdapter(this, mLevelOneItemPos));
            return;
        }

        end = getString(R.string.endString);
        String savedString = "";
        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
            savedString = mSession.getPeoplePreferences();
            mIconArray = getResources().getStringArray(R.array.arrLevelTwoPeopleIcon);
        }else if (mLevelOneItemPos == CATEGORY_ICON_PLACES) {
            savedString = mSession.getPlacesPreferences();
            mIconArray = getResources().getStringArray(R.array.arrLevelTwoPlacesIcon);
        }

        mIconTapCount = new Integer[mIconArray.length];     //mIconTapCountList
        mIconArrayNew = new String[mIconArray.length];      //mIconListNew
        mSortArray = new Integer[mIconArray.length];        //mSortList

        /*mIconTapCountList = new ArrayList<>(Collections.nCopies(mIconArray.length, 0));
        mIconListNew = new ArrayList<>(mIconArray.length);
        mSortList = new ArrayList<>(mIconArray.length);*/

        for (int j = 0; j < mIconArray.length; ++j) {
            mIconTapCount[j] = 0;
            mSortArray[j] = j;
        }

        if(!savedString.equals("")){
            StringTokenizer st = new StringTokenizer(savedString, ",");
            for (int j = 0; j < mIconArray.length; ++j)
                mIconTapCount[j] = Integer.parseInt(st.nextToken());
        }

        IndexSorter<Integer> is = new IndexSorter<Integer>(mIconTapCount);
        is.sort();
        mSortArray =  new Integer[mLayerTwoSpeech.get(mLevelOneItemPos).size()];
        int g = -1;
        for (Integer ij : is.getIndexes()) {
            mSortArray[++g] = ij;
        }

        for (int j = 0; j < mIconTapCount.length; ++j) {
            mIconArrayNew[j] = mIconArray[mSortArray[j]];
            mAdapterTextArray[j] = mLevelTwoAdapterText[mSortArray[j]];
        }

        mRecyclerView.setAdapter(new PeoplePlacesAdapter(this,
                Arrays.copyOfRange(mAdapterTextArray, 0, mIconTapCount.length),
                Arrays.copyOfRange(mIconArrayNew, 0, mIconTapCount.length)));
        mArrSpeechText = new String[mIconTapCount.length];
        for (int i = 0; i < mLevelTwoSpeechText.length; ++i)
            mArrSpeechText[i] = mLevelTwoSpeechText[mSortArray[i]];
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
                mMenuItemLinearLayout = view.findViewById(R.id.linearlayout_icon1);
                mMenuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tappedCategoryItemEvent(view, v, position);
                    }
                });
            }
            @Override   public void onLongClick(View view, int position) {}
        }));

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
     *  b) If keyboard is not open, current level is closed returning with successful
     *  closure (RESULT_OK) of a screen. User will returned back to {@link MainActivity}.
     * </p>
     * */
    private void initBackBtnListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                singleEvent("Navigation","Back");
                speakSpeech(below[1]);
                if (mFlgKeyboard == 1) {
                    mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                    mIvBack.setImageResource(R.drawable.backpressed);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mIvTts.setVisibility(View.INVISIBLE);
                    mFlgKeyboard = 0;
                    if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 1) {
                        setExpressiveButtonToAboutMe(mFlgImage);
                        changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    }else if(mLevelOneItemPos == CATEGORY_ICON_HELP &&
                            ((mLevelTwoItemPos == 0) ||(mLevelTwoItemPos == 2) || (mLevelTwoItemPos == 3) ||
                                    (mLevelTwoItemPos == 4) ||(mLevelTwoItemPos == 5) ||
                                    (mLevelTwoItemPos == 12) ||(mLevelTwoItemPos == 13) ||
                                    (mLevelTwoItemPos == 14)))
                        changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
                    else if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 10)
                        unsafeTouchDisableExpressiveButtons();
                    else if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 15)
                        safetyDisableExpressiveButtons();
                    else
                        changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                } else {
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
                singleEvent("Navigation","Home");
                mIvHome.setImageResource(R.drawable.homepressed);
                mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                speakSpeech(below[0]);
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /**
     * <p>This function will initialize the click listener to Navigation keyboard button.
     * {@link LevelTwoActivity} navigation keyboard button either enable or disable
     * the keyboard layout.
     * When keyboard layout is enabled using keyboard button, it is visible to user and
     * action bar title set to "keyboard".
     * When keyboard layout is disabled using keyboard button, the state of the
     * {@link LevelTwoActivity} retrieved as it was before opening keyboard layout.
     * */
    private void initKeyboardBtnListener() {
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(below[2]);
                mIvTts.setImageResource(R.drawable.speaker_button);
                if (mFlgKeyboard == 1) {
                    mIvKeyboard.setImageResource(R.drawable.keyboard_button);
                    mIvBack.setImageResource(R.drawable.back_button);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mIvTts.setVisibility(View.INVISIBLE);
                    if (mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 1){
                        setExpressiveButtonToAboutMe(mFlgImage);
                        changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    }else if(mLevelOneItemPos == CATEGORY_ICON_HELP &&
                            ((mLevelTwoItemPos == 0) ||(mLevelTwoItemPos == 2) || (mLevelTwoItemPos == 3) ||
                                    (mLevelTwoItemPos == 4) ||(mLevelTwoItemPos == 5) ||
                                    (mLevelTwoItemPos == 12) ||(mLevelTwoItemPos == 13) ||
                                    (mLevelTwoItemPos == 14)))
                        changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
                    else if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 10)
                        unsafeTouchDisableExpressiveButtons();
                    else if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 15)
                        safetyDisableExpressiveButtons();
                    else
                        changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    mFlgKeyboard = 0;
                    showActionBarTitle(true);
                } else {
                    mIvKeyboard.setImageResource(R.drawable.keyboardpressed);
                    mEtTTs.setVisibility(View.VISIBLE);
                    mEtTTs.setKeyListener(originalKeyListener);
                    // Focus the field.
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
                    mEtTTs.requestFocus();
                    mIvTts.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    mIvBack.setImageResource(R.drawable.back_button);
                    mIvBack.setAlpha(1f);
                    mIvBack.setEnabled(true);
                    mFlgKeyboard = 1;
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
                mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
                mFlgImage = 0;
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
                    resetExpressiveButtons(mFlgImage);
                if (!mShouldReadFullSpeech) {
                    if (mFlgLike == 1) {
                        speakSpeech(side[1]);                               //if pressing mIvLike for second time then says really like
                        mFlgLike = 0;
                        singleEvent("ExpressiveIcon","ReallyLike");
                    } else {
                        speakSpeech(side[0]);                               //if pressing mIvLike for first time says like
                        mFlgLike = 1;
                        singleEvent("ExpressiveIcon","Like");
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mFlgLike == 1) {
                        singleEvent("ExpressiveIcon","ReallyLike");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(1));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(1) + mSession.getName()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(1));
                        mFlgLike = 0;
                    } else {
                        singleEvent("ExpressiveIcon","Like");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(0));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(0)+ mSession.getName()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(0));
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
                mFlgLike = 0; mFlgYes = 0; mFlgMore = 0; mFlgNo = 0; mFlgLess = 0;
                mFlgImage = 1;
                resetExpressiveButtons(mFlgImage);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
                    resetExpressiveButtons(mFlgImage);
                if (!mShouldReadFullSpeech) {
                    if (mFlgDntLike == 1) {
                        speakSpeech(side[7]);
                        mFlgDntLike = 0;
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                    } else {
                        speakSpeech(side[6]);
                        mFlgDntLike = 1;
                        singleEvent("ExpressiveIcon","Don'tLike");
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mFlgDntLike == 1) {
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(7));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(7)+ mSession.getFather_name()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(7));
                        mFlgDntLike = 0;
                    } else {
                        singleEvent("ExpressiveIcon","Don'tLike");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(6));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(6)+ mSession.getFather_name()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(6));
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
                mFlgLike = 0; mFlgMore = 0; mFlgDntLike = 0; mFlgNo = 0; mFlgLess = 0;
                mFlgImage = 2;
                resetExpressiveButtons(mFlgImage);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
                    resetExpressiveButtons(mFlgImage);
                if (!mShouldReadFullSpeech) {
                    if (mFlgYes == 1) {
                        speakSpeech(side[3]);
                        mFlgYes = 0;
                        singleEvent("ExpressiveIcon","ReallyYes");
                    } else {
                        speakSpeech(side[2]);
                        mFlgYes = 1;
                        singleEvent("ExpressiveIcon","Yes");
                    }
                } else  {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mFlgYes == 1) {
                        singleEvent("ExpressiveIcon","ReallyYes");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(3));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(3)+ mSession.getEmailId()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(3));
                        mFlgYes = 0;
                    } else {
                        singleEvent("ExpressiveIcon","Yes");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(2));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(2)+ mSession.getEmailId()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(2));
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
                mFlgLike = 0; mFlgYes = 0; mFlgMore = 0; mFlgDntLike = 0; mFlgLess = 0;
                mFlgImage = 3;
                resetExpressiveButtons(mFlgImage);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
                    resetExpressiveButtons(mFlgImage);
                if (!mShouldReadFullSpeech) {
                    if (mFlgNo == 1) {
                        speakSpeech(side[9]);
                        mFlgNo = 0;
                        singleEvent("ExpressiveIcon","ReallyNo");
                    } else {
                        speakSpeech(side[8]);
                        mFlgNo = 1;
                        singleEvent("ExpressiveIcon","No");
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mFlgNo == 1) {
                        singleEvent("ExpressiveIcon","ReallyNo");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(9));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(9)+ mSession.getAddress()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(9));
                        mFlgNo = 0;
                    } else {
                        singleEvent("ExpressiveIcon","No");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(8));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(8)+ mSession.getAddress()+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(8));
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
                mFlgLike = 0; mFlgYes = 0; mFlgDntLike = 0; mFlgNo = 0; mFlgLess = 0;
                mFlgImage = 4;
                resetExpressiveButtons(mFlgImage);
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
                    resetExpressiveButtons(mFlgImage);
                if (!mShouldReadFullSpeech) {
                    if (mFlgMore == 1) {
                        speakSpeech(side[5]);
                        mFlgMore = 0;
                        singleEvent("ExpressiveIcon","ReallyMore");
                    } else {
                        speakSpeech(side[4]);
                        mFlgMore = 1;
                        singleEvent("ExpressiveIcon","More");
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mFlgMore == 1) {
                        singleEvent("ExpressiveIcon","ReallyMore");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(5));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(5)+ mSession.getFather_no().replaceAll("\\B", " ")+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(5));
                        mFlgMore = 0;
                    } else {
                        singleEvent("ExpressiveIcon","More");
                        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(4));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(5) + mSession.getFather_no().replaceAll("\\B", " ")+end);
                        else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(4));
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
                mFlgLike = 0; mFlgYes = 0; mFlgMore = 0; mFlgDntLike = 0; mFlgNo = 0;
                mFlgImage = 5;
                if (mLevelOneItemPos == 8 && mLevelTwoItemPos == 1)
                    setExpressiveButtonToAboutMe(mFlgImage);
                else
                    resetExpressiveButtons(mFlgImage);
                if (!mShouldReadFullSpeech) {
                    if (mFlgLess == 1) {
                        speakSpeech(side[11]);
                        mFlgLess = 0;
                        singleEvent("ExpressiveIcon","ReallyLess");
                    } else {
                        speakSpeech(side[10]);
                        mFlgLess = 1;
                        singleEvent("ExpressiveIcon","Less");
                    }
                } else {
                    ++mActionBtnClickCount;
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.get(mSelectedItemAdapterPos), true);
                    if (mFlgLess == 1) {
                        singleEvent("ExpressiveIcon","ReallyLess");
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(11));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(11));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1){
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(10) + getBloodGroup() +end);
                        } else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(11));
                        mFlgLess = 0;
                    } else {
                        singleEvent("ExpressiveIcon","Less");
                        if (mLevelOneItemPos == 5)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(10));
                        else if (mLevelOneItemPos == 6)
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mSortArray[mLevelTwoItemPos]).get(10));
                        else if(mLevelOneItemPos == 8 && mLevelTwoItemPos == 1){
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(10)+ getBloodGroup()+end);
                        } else
                            speakSpeech(mLayerTwoSpeech.get(mLevelOneItemPos).get(mLevelTwoItemPos).get(10));
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
        mIvTts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                speakSpeech(mEtTTs.getText().toString());
                if(!mEtTTs.getText().toString().equals(""))
                    mIvTts.setImageResource(R.drawable.speaker_pressed);
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
     *             f) Set action bar title.
     *             g) Increment preference count of tapped category icon.</p>
     * */
    public void tappedCategoryItemEvent(View view, View v, int position) {
        mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
        resetExpressiveButtons(-1);
        resetRecyclerAllItems();
        mActionBtnClickCount = 0;
        setMenuImageBorder(v, true);
        mShouldReadFullSpeech = true;
        String title = getIntent().getExtras().getString("selectedMenuItemPath")+ " ";
        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES) {
            speakSpeech(mArrSpeechText[position]);
            Bundle bundle = new Bundle();
            bundle.putString("Icon", mArrSpeechText[position]);
            bundle.putString("Level","Two");
            bundleEvent("Grid",bundle);
        }else if(mLevelTwoItemPos == position && mLevelOneItemPos != CATEGORY_ICON_HELP){
            Intent intent = new Intent(LevelTwoActivity.this, LevelThreeActivity.class);
            int MENU_ITEM_DAILY_ACT = 1;
            if(mLevelOneItemPos == MENU_ITEM_DAILY_ACT &&
                    ( mLevelTwoItemPos == 0 ||  mLevelTwoItemPos == 1 || mLevelTwoItemPos == 2 ||
                            mLevelTwoItemPos == 7 || mLevelTwoItemPos == 8 ))
                intent = new Intent(LevelTwoActivity.this, SequenceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("Icon", mArrSpeechText[position]);
            bundle.putString("Level","LevelTwo");
            bundleEvent("Grid",bundle);
            intent.putExtra("mLevelOneItemPos", mLevelOneItemPos);
            intent.putExtra("mLevelTwoItemPos", mLevelTwoItemPos);
            intent.putExtra("selectedMenuItemPath", mActionBarTitle+ "/");
            startActivityForResult(intent, REQ_HOME);
        }else {
            speakSpeech(mArrSpeechText[position]);
        }
        mLevelTwoItemPos = mRecyclerView.getChildLayoutPosition(view);
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);

        if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE)
            title += mArrAdapterTxt[mSortArray[mLevelTwoItemPos]];
        else if( mLevelOneItemPos == CATEGORY_ICON_PLACES)
            title += mArrAdapterTxt[mSortArray[mLevelTwoItemPos]];
        else if( mLevelOneItemPos == CATEGORY_ICON_HELP)
            title += mArrAdapterTxt[mLevelTwoItemPos];
        else
            title += mArrAdapterTxt[mLevelTwoItemPos].substring
                    (0, mArrAdapterTxt[mLevelTwoItemPos].length()-1);
        mActionBarTitle = title;
        getSupportActionBar().setTitle(mActionBarTitle);

        if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE || mLevelOneItemPos == CATEGORY_ICON_PLACES)
            incrementTouchCountOfItem(mLevelTwoItemPos);

        if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 1)
            setExpressiveButtonToAboutMe(-1);
        if(mLevelOneItemPos == CATEGORY_ICON_HELP &&
                ((mLevelTwoItemPos == 2) || (mLevelTwoItemPos == 3) || (mLevelTwoItemPos == 4) ||
                        (mLevelTwoItemPos == 5) ||(mLevelTwoItemPos == 12) ||
                        (mLevelTwoItemPos == 13) ||(mLevelTwoItemPos == 14)))
            changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
        else if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 0) {
            changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
            //showCallPreview();
        }else if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 10)
            unsafeTouchDisableExpressiveButtons();
        else if(mLevelOneItemPos == CATEGORY_ICON_HELP && mLevelTwoItemPos == 15)
            safetyDisableExpressiveButtons();
        else
            changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
        mIvBack.setImageResource(R.drawable.back_button);
    }

    /**
     * <p>This function will set capacity of ArrayList holding RecyclerView items to the array size.
     * Array is selected based on category icon opened in {@link MainActivity}</p>
     * */
    private void initializeArrayListOfRecycler() {
        int size = -1;
        switch (mLevelOneItemPos) {
            case 0:
                size = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechText).length;
                break;
            case 1:
                size = getResources().getStringArray(R.array.arrLevelTwoEatSpeechText).length;
                break;
            case 2:
                size = getResources().getStringArray(R.array.arrLevelTwoEatSpeechText).length;
                break;
            case 3:
                size = getResources().getStringArray(R.array.arrLevelTwoFunSpeechText).length;
                break;
            case 4:
                size = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechText).length;
                break;
            case 5:
                size = getResources().getStringArray(R.array.arrLevelTwoPeopleSpeechText).length;
                break;
            case 6:
                size = getResources().getStringArray(R.array.arrLevelTwoPlacesSpeechText).length;
                break;
            case 7:
                size = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechText).length;
                break;
            case 8:
                size = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechText).length;
                break;
        }
        mRecyclerItemsViewList = new ArrayList<>(size);
        while(mRecyclerItemsViewList.size() <= size) mRecyclerItemsViewList.add(null);
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
     *     a) Read speech text from arrays for navigation buttons.
     *     b) Read speech text from arrays for expressive buttons.
     *     c) Read verbiage lines into {@link LevelTwoVerbiageModel} model.
     *     d) Read speech and adapter text from arrays for category icons
     *        for People/Places if in {@link MainActivity } mIconArray/places
     *        category is selected.
     *     e) Read speech and adapter text from arrays for category icons
     *        other than People/Places.</p>
     * */
    private void loadArraysFromResources() {
        side = getResources().getStringArray(R.array.arrActionSpeech);
        below = getResources().getStringArray(R.array.arrNavigationSpeech);

        LevelTwoVerbiageModel verbiageModel = new Gson()
                .fromJson(getString(R.string.levelTwoVerbiage), LevelTwoVerbiageModel.class);
        mLayerTwoSpeech = verbiageModel.getVerbiageModel();

        if (mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
            mLevelTwoSpeechText = getResources().getStringArray(R.array.arrLevelTwoPeopleSpeechText);
            mLevelTwoAdapterText = getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterText);
        }else if (mLevelOneItemPos == CATEGORY_ICON_PLACES) {
            mLevelTwoSpeechText = getResources().getStringArray(R.array.arrLevelTwoPlacesSpeechText);
            mLevelTwoAdapterText = getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterText);
        }
        retrieveSpeechAndAdapterArrays(mLevelOneItemPos);
    }

    /**
     * <p>This function will find and return the blood group of user
     * @return blood group of user.</p>
     * */
    private String getBloodGroup() {
        switch(mSession.getBlood()){
            case  1: return getString(R.string.aPos);
            case  2: return getString(R.string.aNeg);
            case  3: return getString(R.string.bPos);
            case  4: return getString(R.string.bNeg);
            case  5: return getString(R.string.abPos);
            case  6: return getString(R.string.abNeg);
            case  7: return getString(R.string.oPos);
            case  8: return getString(R.string.oNeg);
            default: return "";
        }
    }

    /**
     * <p>Category icon "Help" -> "Safety" when selected, it needed to disable expressive
     * no button. This function disables the No expressive button and keep
     * enabled other buttons</p>
     * */
    private void safetyDisableExpressiveButtons() {
        mIvLike.setAlpha(1f);
        mIvDontLike.setAlpha(1f);
        mIvYes.setAlpha(1f);
        mIvNo.setAlpha(0.5f);
        mIvMore.setAlpha(1f);
        mIvLess.setAlpha(1f);
        mIvLike.setEnabled(true);
        mIvDontLike.setEnabled(true);
        mIvYes.setEnabled(true);
        mIvNo.setEnabled(false);
        mIvMore.setEnabled(true);
        mIvLess.setEnabled(true);
    }

    /**
     * <p>Category icon "Help" -> "Unsafe touch" when selected, it needed to disable expressive
     * buttons Like, Yes, More. This function disables the Like, Yes, More expressive buttons
     * and keep enabled other buttons</p>
     * */
    private void unsafeTouchDisableExpressiveButtons() {
        mIvDontLike.setAlpha(1f);
        mIvNo.setAlpha(1f);
        mIvLess.setAlpha(1f);
        mIvDontLike.setEnabled(true);
        mIvNo.setEnabled(true);
        mIvLess.setEnabled(true);
        mIvLike.setAlpha(0.5f);
        mIvYes.setAlpha(0.5f);
        mIvMore.setAlpha(0.5f);
        mIvLike.setEnabled(false);
        mIvYes.setEnabled(false);
        mIvMore.setEnabled(false);
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
            if (mActionBtnClickCount > 0) {
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
     * {@param mFlgImage} is a index of expressive button.
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
        for(int i = 0; i< mRecyclerView.getChildCount(); ++i)
            setMenuImageBorder(mRecyclerView.getChildAt(i), false);
    }

    /**
     * <p>This function increment the touch count for the selected category icon.
     * In {@link LevelTwoActivity}, only People and Places has preferences which are stored
     * directly into preferences using this function.</p>
     * */
    private void incrementTouchCountOfItem(int levelTwoItemPosition) {
        StringBuilder stringBuilder = new StringBuilder();
        mIconTapCount[mSortArray[levelTwoItemPosition]] += 1;
        for (Integer countPeople : mIconTapCount) stringBuilder.append(countPeople).append(",");
        if(mLevelOneItemPos == CATEGORY_ICON_PEOPLE) {
            mSession.setPeoplePreferences(stringBuilder.toString());
        }else {
            mSession.setPlacesPreferences(stringBuilder.toString());
        }
    }

    /**
     * <p>This function first reset the all expressive buttons. Then set expressive button
     * to About me category icon ("Help" -> "About Me"). Also, set expressive button to
     * selected if {@param mFlgImage} is greater than -1.
     * {@param mFlgImage} is a index of expressive button.
     *  e.g. From top to bottom 0 - like button, 1 - don't like button likewise.</p>
     * */
    private void setExpressiveButtonToAboutMe(int image_flag){
        mIvLike.setImageResource(R.drawable.mynameis);
        mIvDontLike.setImageResource(R.drawable.caregiver);
        mIvYes.setImageResource(R.drawable.email);
        mIvNo.setImageResource(R.drawable.address);
        mIvMore.setImageResource(R.drawable.contact);
        mIvLess.setImageResource(R.drawable.bloodgroup);
        switch (image_flag){
            case 0: mIvLike.setImageResource(R.drawable.mynameis_pressed); break;
            case 1: mIvDontLike.setImageResource(R.drawable.caregiver_pressed); break;
            case 2: mIvYes.setImageResource(R.drawable.email_pressed); break;
            case 3: mIvNo.setImageResource(R.drawable.address_pressed); break;
            case 4: mIvMore.setImageResource(R.drawable.contact_pressed); break;
            case 5: mIvLess.setImageResource(R.drawable.blooedgroup_pressed); break;
            case 6: mIvHome.setImageResource(R.drawable.homepressed); break;
            default: break;
        }
    }

    /**
     * <p>This function retrieve speech text array and adapter text array from arrays
     * resource using category icon index selected in {@link MainActivity}.
     * @param levelOneItemPos is a index of category icon selected in
     * {@link MainActivity}.</p>
     * */
    public void retrieveSpeechAndAdapterArrays(int levelOneItemPos){
        switch (levelOneItemPos){
            case 0:
                mArrSpeechText = getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechText);
                mArrAdapterTxt = getResources().getStringArray(R.array.arrLevelTwoGreetFeelAdapterText);
                break;
            case 1:
                mArrSpeechText = getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechText);
                mArrAdapterTxt = getResources().getStringArray(R.array.arrLevelTwoDailyActAdapterText);
                break;
            case 2:
                mArrSpeechText = getResources().getStringArray(R.array.arrLevelTwoEatSpeechText);
                mArrAdapterTxt = getResources().getStringArray(R.array.arrLevelTwoEatAdapterText);
                break;
            case 3:
                mArrSpeechText = getResources().getStringArray(R.array.arrLevelTwoFunSpeechText);
                mArrAdapterTxt = getResources().getStringArray(R.array.arrLevelTwoFunAdapterText);
                break;
            case 4:
                mArrSpeechText = getResources().getStringArray(R.array.arrLevelTwoLearningSpeechText);
                mArrAdapterTxt = getResources().getStringArray(R.array.arrLevelTwoLearningAdapterText);
                break;
            case 5:
                mArrAdapterTxt = getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterText);
                break;
            case 6:
                mArrAdapterTxt = getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterText);
                break;
            case 7:
                mArrSpeechText = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechText);
                mArrAdapterTxt = getResources().getStringArray(R.array.arrLevelTwoTimeWeatherAdapterText);
                break;
            case 8:
                mArrSpeechText = getResources().getStringArray(R.array.arrLevelTwoHelpSpeechText);
                mArrAdapterTxt = getResources().getStringArray(R.array.arrLevelTwoHelpAdapterText);
                break;
        }
    }

    /**
     * Unused code
     * **/
    private void showCallPreview(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            //startCall("tel:" + mSession.getFather_no());
        } else {
            //requestCallPermission();
        }
    }

    /**
     * Unused code**/
    private void requestCallPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)){
            new ToastWithCustomTime(this,"Call permission is not available. Requesting permission for making a call in case of an emergency.", 10000);
        } else {
            new ToastWithCustomTime(this,"Call access is required to make an emergency call. Please enable call permission from app settings.", 10000);
        }
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
    }

    /**
     * Unused code
     * **/
    public void startCall(String contact){
        Intent callintent = new Intent(Intent.ACTION_CALL);
        callintent.setData(Uri.parse(contact));
        startActivity(callintent);
    }
}