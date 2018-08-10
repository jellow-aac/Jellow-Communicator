package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dsource.idc.jellowintl.models.LevelOneVerbiageModel;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.dsource.idc.jellowintl.verbiage_model.ConverterActivity;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.singleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

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
    /* This flag is set to true, when user press the category icon and reset when user press the home
     button. When user presses expressive button and mShouldReadFullSpeech = true, it means that user
     is already selected a category icon and user intend to speak full sentence verbiage for a
     selected icon.*/
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
    private String[] mSpeechTxt, mExprBtnTxt, mNavigationBtnTxt, mActionBarTitle;
    private String mActionBarTitleTxt;
    private String mCheckVoiceData, mHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelx_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(getString(R.string.action_bar_title));

        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        loadArraysFromResources();
        // Set the capacity of mRecyclerItemsViewList list to total number of category icons to be
        // populated on the screen.
        mRecyclerItemsViewList = new ArrayList<>(mSpeechTxt.length);
        while (mRecyclerItemsViewList.size() < mSpeechTxt.length)
            mRecyclerItemsViewList.add(null);
        //The variables below are defined because android os fall back to default locale
        // after activity restart. These variable will hold the value for variables initialized using
        // user preferred locale.
        mHome = getString(R.string.action_bar_title);
        mCheckVoiceData = getString(R.string.txt_actLangSel_completestep2);

        initializeLayoutViews();
        initializeViewListeners();

        // If device has android version below Lollipop get Text-to-speech language
        if(Build.VERSION.SDK_INT < 21)
            getSpeechLanguage("");

        //This method is invoked when the activity is launched from the SearchActivity
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
    /**
     * This function is responsible for the Highlighting of the searched item from the
     * search activity
     * @Author Ayaz Alam
     * **/
    RecyclerView.OnScrollListener scrollListener;
    private void highlightSearchedItem() {
        //Referring to the Intent that invoked the activity
        final int iconIndex = getIntent().getExtras().getInt(getString(R.string.search_parent_0));
        int gridCode=new SessionManager(this).getGridSize();
        int gridSize;
        if(gridCode==1)
            gridSize=8;
        else gridSize=2;
        //Scroll to the position if the icon is not present in first grid
        if(iconIndex>gridSize)
        {
            scrollListener =null;
            scrollListener = getListener(iconIndex);
            mRecyclerView.addOnScrollListener(scrollListener);
            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,iconIndex);

        }
        else
            {
            setSearchHighlight(iconIndex);
            }

    }
    /**
     * This functions returns a scroll scrollListener which triggers the setHighlight function
     * when the scrolling is done
     * @Author Ayaz Alam
     * */

    private RecyclerView.OnScrollListener getListener(final int index) {
        scrollListener =new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    //Wait untill the scrolling is complete
                }
                else if(newState==RecyclerView.SCROLL_STATE_IDLE) //Try highlighting if scrolling is done
                    setSearchHighlight(index);
            }
        };
        return scrollListener;
    }
    /**
     * This function is responsible for highlighting the view
     * @param pos
     *
     *
     * */
    ViewTreeObserver.OnGlobalLayoutListener populationDoneListener;
    public void setSearchHighlight(final int pos)
    {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        populationDoneListener=new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //At this point the layout is complete and the
                //dimensions of recyclerView and any child views are known.
                View searchedView = mRecyclerItemsViewList.get(pos);
                if(searchedView==null) {
                    //Try re-scrolling if the view is not present in the current recycler view child
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,pos );
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
                    return;
                }
                //When view is found remove the scrollListener and highlight the background
                GradientDrawable gd = (GradientDrawable) searchedView.findViewById(R.id.borderView).getBackground();
                gd.setColor(ContextCompat.getColor(getApplicationContext(), R.color.search_highlight));
                mRecyclerView.removeOnScrollListener(scrollListener);
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(populationDoneListener);
            }
        };
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(populationDoneListener);




    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            throw new Error("unableToResume");
        }
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
        }
        // broadcast receiver to get response messages from JellowTTsService.
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_TTS_ERROR");
        registerReceiver(receiver, filter);
       // Start measuring user app screen timer.
        startMeasuring();
        SessionManager session = new SessionManager(this);
        if(!session.getToastMessage().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    session.getToastMessage(), Toast.LENGTH_SHORT).show();
            session.setToastMessage("");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        SessionManager session = new SessionManager(this);
        long sessionTime = validatePushId(session.getSessionCreatedAt());
        session.setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer.
        stopMeasuring("LevelOneActivity");
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        // Stop Jellow Test-to-speech service.
        sendBroadcast(new Intent("com.dsource.idc.jellowintl.STOP_SERVICE"));
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main_with_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)     {
        switch (item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.my_boards:
                startActivity(new Intent(this, SetupMMB.class));
                break;
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
                startActivity(new Intent(this, ConverterActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * <p>This function will initialize the views that are populated on the activity layout.
     * It also assigns content description to the views to enable speech in
     * Talk-back feature. The Talk-back feature is not available int this version.</p>
    * */
    private void initializeLayoutViews() {
        mIvLike = findViewById(R.id.ivlike);
        mIvDontLike = findViewById(R.id.ivdislike);
        mIvMore = findViewById(R.id.ivadd);
        mIvLess = findViewById(R.id.ivminus);
        mIvYes = findViewById(R.id.ivyes);
        mIvNo = findViewById(R.id.ivno);
        mIvHome = findViewById(R.id.ivhome);
        mIvBack = findViewById(R.id.ivback);
        //on this screen initially back button is disabled.
        mIvBack.setAlpha(.5f);
        mIvBack.setEnabled(false);
        mIvKeyboard = findViewById(R.id.keyboard);

        mEtTTs = findViewById(R.id.et);
        //Initially custom input text is invisible
        mEtTTs.setVisibility(View.INVISIBLE);

        mIvTTs = findViewById(R.id.ttsbutton);
        //Initially custom input text speak button is invisible
        mIvTTs.setVisibility(View.INVISIBLE);

        // Set it to null - this will make the field non-editable
        originalKeyListener = mEtTTs.getKeyListener();
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
     * {@link RecyclerTouchListener} is a custom defined Touch event scrollListener class.
     * {@link RecyclerView.OnChildAttachStateChangeListener} is defined to manage view state of
     * recycler child view. It is useful to retain current state of child, when recycler view is scrolled
     * and recycler child views are recycled for memory usage optimization.</p>
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

        // When user scrolls into category, the child views are attached and detached from
        // recycler view. Also, the child views in recycler view have scrolled
        // off-screen are kept for reuse. Many times the reused view is assigned to
        // on-screen view. In our app this behaviour be can seen as follows:
        // When user scrolls into category, the child views are attached and detached from
        // recycler view. Also, the child views in recycler view have scrolled
        // off-screen are kept for reuse. Many times the reused view is assigned to
        // on-screen view. In our app this behaviour be can seen as follows:
        // Select "Dog" category icon in "Learning -> Animals & birds" then scrolled it off-screen
        // you will see selection border is appeared to another on-screen child of recycler view.
        // To overcome this situation a global list of state to every child of recycler view
        // is maintained.
        // When view is detached from recycler view it is removed from global list (mRecyclerItemsViewList)
        // and when child is attached to recycler view it is added to global list.
        // If a category icon is selected and it is scrolled off-screen, other on-screen view reusing
        // same view will get selection border so its border is removed first before removing from
        // global list.
        // When recycler view is scrolled, every newly attached child is checked if it is selected
        // previously or not. If the child is selected and it is reattached to recycler view then
        // set its border.
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
     * <p>This function will initialize the click scrollListener to Navigation "back" button.
     * {@link MainActivity} navigation back button enabled when user using custom keyboard input
     * and keyboard is opened.</p>
     * */
    private void initBackBtnListener() {
        mIvBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //when mFlgKeyboardOpened is set to true, it means user is using custom keyboard input
                // text and system keyboard is visible.
                if (mFlgKeyboardOpened){
                    // As user is using custom keyboard input text and then back button is pressed,
                    // user intent to close custom keyboard input text so below steps will follow:
                    // a) hide custom input text and speak button views
                    // b) show category icons
                    // c) set back button to pressed state
                    // d) set flag mFlgKeyboardOpened = false, as user not using custom keyboard input
                    // anymore.
                    // e) disable back button as no more back process available in this level.
                    mIvKeyboard.setImageResource(R.drawable.keyboard);
                    mIvBack.setImageResource(R.drawable.back);
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
     * <p>This function will initialize the click scrollListener to Navigation "home" button.
     * {@link MainActivity} navigation home button when clicked it clears, every state of view as
     * like app is launched as fresh.</p>
     * */
    private void initHomeBtnListener() {
        mIvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoHome(false);
            }
        });
    }

    /**
     * <p>This function will initialize the click scrollListener to Navigation "keyboard" button.
     * {@link MainActivity} navigation keyboard button either enable or disable the keyboard layout.
     * This button enable the back button when keyboard is open.</p>
     * */
    private void initKeyboardBtnListener() {
        final String strKeyboard = getString(R.string.keyboard);
        mIvKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakSpeech(mNavigationBtnTxt[2]);
                //Firebase event
                singleEvent("Navigation","Keyboard");
                mIvTTs.setImageResource(R.drawable.speaker_button);
                //when mFlgKeyboardOpened is set to true, it means user is using custom keyboard input
                // text and system keyboard is visible.
                if (mFlgKeyboardOpened){
                    // As user is using custom keyboard input text and then press the keyboard button,
                    // user intent to close custom keyboard input text so below steps will follow:
                    // a) set keyboard button to unpressed state.
                    // b) disable back button
                    // c) show category icons
                    // d) hide custom keyboard input text and speak button views
                    // e) set flag mFlgKeyboardOpened = false, as user not using custom keyboard input
                    //    anymore.
                    // f) disable back button as no more back process available in this level.
                    mIvKeyboard.setImageResource(R.drawable.keyboard);
                    mIvBack.setImageResource(R.drawable.back);
                    mEtTTs.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mIvTTs.setVisibility(View.INVISIBLE);
                    mFlgKeyboardOpened = false;
                    changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
                    mIvBack.setAlpha(.5f);
                    mIvBack.setEnabled(false);
                    showActionBarTitle(true);
                //when mFlgKeyboardOpened is set to false, it means user intent to use custom
                //keyboard input text so below steps will follow:
                }else {
                    // a) keyboard button to press
                    // b) set back button unpressed state
                    // c) show custom keyboard input text and speak button view
                    // d) hide category icons
                    mIvKeyboard.setImageResource(R.drawable.keyboard_pressed);
                    mIvBack.setImageResource(R.drawable.back);
                    mIvHome.setImageResource(R.drawable.home);
                    mEtTTs.setVisibility(View.VISIBLE);

                    mEtTTs.setKeyListener(originalKeyListener);
                    // Focus the field.
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    changeTheExpressiveButtons(DISABLE_EXPR_BTNS);
                    mEtTTs.requestFocus();
                    mIvTTs.setVisibility(View.VISIBLE);
                    // when user intend to use custom keyboard input text system keyboard should
                    // only appear when user taps on custom keyboard input view. Setting
                    // InputMethodManager to InputMethodManager.HIDE_NOT_ALWAYS does this task.
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
                    // when user is typing in custom keyboard input text it is necessary
                    // for user to see input text. The function setSoftInputMode() does this task.
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    mIvBack.setAlpha(1f);
                    mIvBack.setEnabled(true);
                    mFlgKeyboardOpened = true;
                    showActionBarTitle(false);
                    getSupportActionBar().setTitle(strKeyboard);
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
     *  b) set pressed icon to like button
     *  c) produce speech using output for like
     *  d) set border to category icon of color associated with like button</p>
     * */
    private void initLikeBtnListener() {
        mIvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the like button all expressive button speech flag (except like)
                // are set to reset.
                mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 0, indicates like expressive button is pressed
                mFlgImage = 0;
                // Set like button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                    // if value of mFlgLike is 1 then should speak "really like".
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
                // if value of mShouldReadFullSpeech is true, then speak associated like
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set like button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null){
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    }
                    // if value of mFlgLike is 1 then speak associated really like expression
                    // verbiage for selected category icon.
                    if (mFlgLike == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(1));
                        mFlgLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLike");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(1));
                    // if value of mFlgLike is 0 then Speak associated like expression
                    // verbiage to selected category icon.
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
     * <p>This function will initialize the click scrollListener for expressive "don't like" button.
     * Expressive don't like button is works in four ways:
     *  a) press expressive don't like button once
     *  b) press expressive don't like button twice
     *  c) press category icon first then press expressive don't like button once
     *  d) press category icon first then press expressive don't like button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except don't like button
     *  b) set pressed icon to don't like button
     *  c) produce speech using output for don't like
     *  d) set border to category icon of color associated with don't like button</p>
     * */
    private void initDontLikeBtnListener() {
        mIvDontLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the don't like button all expressive button speech flag (except don't like)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 1, indicates don't like expressive button is pressed
                mFlgImage = 1;
                // Set don't like button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
                if (!mShouldReadFullSpeech) {
                // if value of mFlgDntLike is 1 then should speak "really don't like".
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
                // if value of mShouldReadFullSpeech is true, then speak associated don't like
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set don't like button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgDntLike is 1 then speak associated really don't like expression
                    // verbiage for selected category icon.
                    if (mFlgDntLike == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(7));
                        mFlgDntLike = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyDon'tLike");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(7));
                    // if value of mFlgDntLike is 0 then Speak associated don't like expression
                    // verbiage to selected category icon.
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
     * <p>This function will initialize the click scrollListener for expressive "yes" button.
     * Expressive yes button is works in four ways:
     *  a) press expressive yes button once
     *  b) press expressive yes button twice
     *  c) press category icon first then press expressive yes button once
     *  d) press category icon first then press expressive yes button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except yes button
     *  b) set pressed icon to yes button
     *  c) produce speech using output for yes
     *  d) set border to category icon of color associated with yes button</p>
     * */
    private void initYesBtnListener() {
        mIvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the yes button all expressive button speech flag (except yes)
                // are set to reset.
                mFlgLike = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 2, indicates yes expressive button is pressed
                mFlgImage = 2;
                // Set yes button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
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
                // if value of mShouldReadFullSpeech is true, then speak associated yes
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set yes button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgYes is 1 then speak associated really yes expression
                    // verbiage for selected category icon.
                    if (mFlgYes == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(3));
                        mFlgYes = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyYes");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(3));
                    // if value of mFlgYes is 0 then speak associated yes expression
                    // verbiage for selected category icon.
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
     * <p>This function will initialize the click scrollListener for expressive "no" button.
     * Expressive no button is works in four ways:
     *  a) press expressive no button once
     *  b) press expressive no button twice
     *  c) press category icon first then press expressive no button once
     *  d) press category icon first then press expressive no button twice
     * This function execute task as follows:
     *  a) reset the all expressive button speech flag except no button
     *  b) set pressed icon to no button
     *  c) produce speech using output for no
     *  d) set border to category icon of color associated with no button</p>
     * */
    private void initNoBtnListener() {
        mIvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user press the no button all expressive button speech flag (except no)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgLess = 0;
                //Value of mFlgImage = 3, indicates no expressive button is pressed
                mFlgImage = 3;
                // Sets no button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
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
                // if value of mShouldReadFullSpeech is true, then it should speak associated no
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set no button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgNo is 1 then speak associated really no expression
                    // verbiage for selected category icon.
                    if (mFlgNo == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(9));
                        mFlgNo = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyNo");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(9));
                    // if value of mFlgNo is 0 then Speak associated no expression
                    // verbiage to selected category icon.
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
                // When user press the more button all expressive button speech flag (except more)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgDntLike = mFlgNo = mFlgLess = 0;
                //Value of mFlgImage = 4, indicates more expressive button is pressed
                mFlgImage = 4;
                // Sets more button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
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
                // if value of mShouldReadFullSpeech is true, then it should speak associated more
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
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(5));
                        mFlgMore = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyMore");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(5));
                    // if value of mFlgMore is 0, then should speak "more" expression
                    // verbiage associated to selected category icon.
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
                // When user press the less button all expressive button speech flag (except less)
                // are set to reset.
                mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = 0;
                //Value of mFlgImage = 5, indicates less expressive button is pressed
                mFlgImage = 5;
                // Sets less button icon to pressed using mFlgImage.
                resetExpressiveButtons(mFlgImage);
                // if value of mShouldReadFullSpeech is false then do not speak full sentence verbiage.
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
                // if value of mShouldReadFullSpeech is true, then speak associated less
                // expression verbiage to selected category icon.
                } else {
                    ++mActionBtnClickCount;
                    // Set less button color border to selected category icon.
                    // border color is applied using mActionBtnClickCount and mFlgImage.
                    if(mRecyclerItemsViewList.get(mSelectedItemAdapterPos) != null)
                        setMenuImageBorder(mRecyclerItemsViewList.
                                get(mSelectedItemAdapterPos), true);
                    // if value of mFlgLess is 1 then speak associated really less expression
                    // verbiage for selected category icon.
                    if (mFlgLess == 1) {
                        speakSpeech(mLayerOneSpeech.get(mLevelOneItemPos).get(11));
                        mFlgLess = 0;
                        //Firebase event
                        singleEvent("ExpressiveIcon","ReallyLess");
                        singleEvent("ExpressiveGridIcon",mLayerOneSpeech.
                                get(mLevelOneItemPos).get(11));
                    // if value of mFlgLess is 0 then Speak associated less expression
                    // verbiage to selected category icon.
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
     * <p>This function will initialize the click scrollListener to Tts Speak button.
     * When Tts speak button is pressed, broadcast speak request is sent to Text-to-speech service.
     * Message typed in Text-to-speech input view is speech out by service.</p>
     * */
    private void initTTsBtnListener() {
        mIvTTs.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                speakSpeech(mEtTTs.getText().toString());
                if(!mEtTTs.getText().toString().equals(""))
                    mIvTTs.setImageResource(R.drawable.speaker_pressed);
                //Firebase event
                Bundle bundle = new Bundle();
                bundle.putString("InputName", Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.DEFAULT_INPUT_METHOD));
                bundle.putString("utterence", mEtTTs.getText().toString());
                bundleEvent("Keyboard", bundle);

                //singleEvent("Keyboard", mEtTTs.getText().toString());
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
     * <p>This function will initialize the click scrollListener to EditText which is used by user to
     * input custom strings.</p>
     * */
    private void initTTsEditTxtListener() {
        mEtTTs.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // If custom keyboard input text loses focus.
                if (!hasFocus) {
                    // Hide system keyboard.
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEtTTs.getWindowToken(), 0);
                    // Make it non-editable.
                    mEtTTs.setKeyListener(null);
                }
            }
        });
    }

    /**
     * <p>This function is called when user taps a category icon. It will change the state of
     * category icon pressed. Also, it set the flag for app speak full verbiage sentence.
     * @param view is parent view in selected RecyclerView.
     * @param v is parent relative layout of category icon tapped.
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
        String title = mActionBarTitle[position];
        getSupportActionBar().setTitle(title);
        // below condition is true when user tap same category icon twice.
        // i.e. user intends to open a sub-category of selected category icon.
        if (mLevelOneItemPos == position){
            SessionManager session = new SessionManager(this);
            // Get icon set directory path
            File langDir = new File(getApplicationInfo().dataDir + "/app_"+
                    session.getLanguage()+"/drawables");
            // If icon sets are available for level two then open selected category in level two
            if(langDir.exists() && langDir.isDirectory()) {
                // create event bundle for firebase
                Bundle bundle = new Bundle();
                bundle.putString("Icon", "Opened " + mSpeechTxt[position]);
                bundleEvent("Grid", bundle);
                // send current position in recycler view of selected category icon and bread
                // crumb path as extra intent data to LevelTwoActivity.
                Intent intent = new Intent(MainActivity.this, LevelTwoActivity.class);
                intent.putExtra(getString(R.string.level_one_intent_pos_tag), position);
                intent.putExtra(getString(R.string.intent_menu_path_tag), title + "/");
                startActivityForResult(intent, REQ_HOME);
            }
            langDir = null;
        }else {
            speakSpeech(mSpeechTxt[position]);
            // create event bundle for firebase
            Bundle bundle = new Bundle();
            bundle.putString("Icon", mSpeechTxt[position]);
            bundleEvent("Grid", bundle);
        }
        mLevelOneItemPos = mRecyclerView.getChildLayoutPosition(view);
        mSelectedItemAdapterPos = mRecyclerView.getChildAdapterPosition(view);
    }

    /**
     * <p>When home button is pressed it is needed to make app state as it is started fresh.
     * This function will reset every category icons, expressive button tapped.
     * @param isUserRedirected is set when user presses home from {@link LevelTwoActivity},
     * {@link LevelThreeActivity}, {@link SequenceActivity} and resets when user presses home
     * {@link MainActivity}.</p>
     * */
    private void gotoHome(boolean isUserRedirected) {
        getSupportActionBar().setTitle(mHome);
        // reset all expressive button flags.
        mFlgLike = mFlgYes = mFlgMore = mFlgDntLike = mFlgNo = mFlgLess = 0;
        // reset expressive buttons
        mIvLike.setImageResource(R.drawable.like);
        mIvDontLike.setImageResource(R.drawable.dontlike);
        mIvYes.setImageResource(R.drawable.yes);
        mIvNo.setImageResource(R.drawable.no);
        mIvMore.setImageResource(R.drawable.more);
        mIvLess.setImageResource(R.drawable.less);
        // reset category items
        resetRecyclerMenuItemsAndFlags();
        // clear verbiage speak (mShouldReadFullSpeech), border color flag (mFlgImage)
        mShouldReadFullSpeech = false;
        mFlgImage = -1;
        //when mFlgKeyboardOpened is set to true, it means user is using custom keyboard input
        // text and system keyboard is visible.
        if (mFlgKeyboardOpened){
            // As user is using custom keyboard input text and then press the home button,
            // user is either intent to close custom keyboard input text or
            // want to go home so below steps will follow:
            // a) set keyboard button to unpressed state.
            // b) disable back button
            // c) hide custom input text and speak button views
            // d) show category icons
            // e) set flag mFlgKeyboardOpened = false, as user not using custom keyboard input
            //    anymore.
            // f) disable back button as no more back process available in this level.
            mIvKeyboard.setImageResource(R.drawable.keyboard);
            mIvBack.setImageResource(R.drawable.back);
            mEtTTs.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mIvTTs.setVisibility(View.INVISIBLE);
            mFlgKeyboardOpened = false;
            changeTheExpressiveButtons(!DISABLE_EXPR_BTNS);
            mIvBack.setAlpha(.5f);
            mIvBack.setEnabled(false);
        }
        mIvHome.setImageResource(R.drawable.home_pressed);
        //Firebase event
        singleEvent("Navigation","Home");
        if(!isUserRedirected)
            speakSpeech(mNavigationBtnTxt[0]);
    }

    /**
     * <p>This function will set/reset action bar title depending on {@param showTitle} flag.
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
     *     a) Read verbiage lines into {@link LevelOneVerbiageModel} model.
     *     b) Read speech text from arrays for category icons.
     *     c) Read speech text from arrays for expressive buttons.
     *     d) Read speech text from arrays for navigation buttons.</p>
     * */
    private void loadArraysFromResources() {
        LevelOneVerbiageModel verbiageModel = new Gson()
                .fromJson(getString(R.string.levelOneVerbiage), LevelOneVerbiageModel.class);
        mLayerOneSpeech = verbiageModel.getVerbiageModel();
        mSpeechTxt = getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
        mExprBtnTxt = getResources().getStringArray(R.array.arrActionSpeech);
        mNavigationBtnTxt = getResources().getStringArray(R.array.arrNavigationSpeech);
        mActionBarTitle = getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
    }

    /**
     * <p>This function will reset :
     *     a) Reset expressive button pressed if any. To set home button to pressed
     *        6 value is sent to resetExpressiveButtons(6).
     *     b) Reset category icons pressed if any.
     *     c) Setting mLevelOneItemPos = -1 means, no category icon is selected.</p>
     * */
    private void resetRecyclerMenuItemsAndFlags() {
        resetExpressiveButtons(6);
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
     * which border is applied then apply the border.
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
     * <p>This function first reset if any expressive button is pressed. Then set an
     * expressive button to pressed. {@param image_flag} identifies which expressive button
     * is pressed.
     * If user had previously pressed any expressive button (i.e. state of any expressive
     * button is pressed) and then user presses some other expressive button, it is needed to
     * clear previously pressed expressive button state and home button state (if pressed).
     * {@param image_flag} is a index of expressive button.
     *  e.g. From top to bottom 0 - like button, 1 - don't like button likewise.
     *  To set home button to pressed state image_flag value must be 6</p>
     * */
    private void resetExpressiveButtons(int image_flag) {
        // clear previously selected any expressive button or home button
        mIvLike.setImageResource(R.drawable.like);
        mIvDontLike.setImageResource(R.drawable.dontlike);
        mIvYes.setImageResource(R.drawable.yes);
        mIvNo.setImageResource(R.drawable.no);
        mIvMore.setImageResource(R.drawable.more);
        mIvLess.setImageResource(R.drawable.less);
        mIvHome.setImageResource(R.drawable.home);
        // set expressive button or home button to pressed state
        switch (image_flag){
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
                        Toast.makeText(context, mCheckVoiceData, Toast.LENGTH_LONG).show();
                    break;
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES":
                    SessionManager session = new SessionManager(context);
                    String userLang = session.getLanguage();
                    session.setLangSettingIsCorrect(true);
                    String mSysTtsReg = intent.getStringExtra("systemTtsRegion");
                    //Below if is true when
                    //      1) app language is english India and tts language is hindi India
                    // or   2) app language is not english India and
                    //         app language and Text-to-speech language are different then
                    //         show error toast.
                    if(((userLang.equals("en-rIN") && !mSysTtsReg.equals("hi-rIN"))
                            || (!userLang.equals("en-rIN") && !userLang.equals(mSysTtsReg)))) {
                        Toast.makeText(context, getString(R.string.speech_engin_lang_sam),
                                Toast.LENGTH_LONG).show();
                        session.setLangSettingIsCorrect(false);
                    }
                    break;
            }
        }
    };

    /**
     * <p>This function check whether Text-to-speech service is running? It will
     * return true if service is running else false is service is closed.</p>
     * */
    public static boolean isTTSServiceRunning(ActivityManager manager) {
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            // JellowTTSService is name of TTS service class.
            if (JellowTTSService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * <p>This function check whether user device has sim card insterted into SIM slot
     * and user can make call.
     * @return true if device can able to make phone calls.</p>
     * */
    public static boolean isDeviceReadyToCall(TelephonyManager tm){
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }
}